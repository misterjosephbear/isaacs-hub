package com.isaacshub.app.banking.data

import com.isaacshub.app.banking.domain.AccountType
import com.isaacshub.app.banking.domain.BankAccount
import com.isaacshub.app.banking.domain.Transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Client for Plaid API (https://plaid.com)
 *
 * Plaid provides banking data aggregation with automatic balance updates.
 * Using the free Trial plan which allows 10 production items (bank connections).
 */
class PlaidClient(
    private val clientId: String = PlaidConfig.CLIENT_ID,
    private val secret: String = PlaidConfig.SECRET
) {

    private val environment = PlaidConfig.ENVIRONMENT
    private val baseUrl = when (environment) {
        "sandbox" -> "https://sandbox.plaid.com"
        "development" -> "https://development.plaid.com"
        "production" -> "https://production.plaid.com"
        else -> "https://sandbox.plaid.com"
    }

    /**
     * Exchange a public token (from Plaid Link) for an access token
     */
    suspend fun exchangePublicToken(publicToken: String): Result<String> = withContext(Dispatchers.IO) {
        runCatching {
            val requestBody = JSONObject().apply {
                put("client_id", clientId)
                put("secret", secret)
                put("public_token", publicToken)
            }

            val response = makeRequest("/item/public_token/exchange", requestBody)
            response.getString("access_token")
        }
    }

    /**
     * Fetch account balances for a given access token
     */
    suspend fun fetchAccounts(accessToken: String): Result<List<BankAccount>> = withContext(Dispatchers.IO) {
        runCatching {
            val requestBody = JSONObject().apply {
                put("client_id", clientId)
                put("secret", secret)
                put("access_token", accessToken)
            }

            val response = makeRequest("/accounts/balance/get", requestBody)
            parseAccountsResponse(response)
        }
    }

    /**
     * Get institution name for an access token
     */
    suspend fun getInstitutionName(accessToken: String): Result<String> = withContext(Dispatchers.IO) {
        runCatching {
            val requestBody = JSONObject().apply {
                put("client_id", clientId)
                put("secret", secret)
                put("access_token", accessToken)
            }

            val response = makeRequest("/item/get", requestBody)
            val institutionId = response.getJSONObject("item").getString("institution_id")

            // Fetch institution details
            val institutionRequest = JSONObject().apply {
                put("client_id", clientId)
                put("secret", secret)
                put("institution_id", institutionId)
                put("country_codes", org.json.JSONArray().put("US"))
            }

            val institutionResponse = makeRequest("/institutions/get_by_id", institutionRequest)
            institutionResponse.getJSONObject("institution").getString("name")
        }
    }

    /**
     * Create a Link token for initializing Plaid Link
     */
    suspend fun createLinkToken(userId: String): Result<String> = withContext(Dispatchers.IO) {
        runCatching {
            val requestBody = JSONObject().apply {
                put("client_id", clientId)
                put("secret", secret)
                put("client_name", "Isaac's Hub")
                put("user", JSONObject().put("client_user_id", userId))
                put("products", org.json.JSONArray().put("auth").put("transactions"))
                put("country_codes", org.json.JSONArray().put("US"))
                put("language", "en")
            }

            val response = makeRequest("/link/token/create", requestBody)
            response.getString("link_token")
        }
    }

    /**
     * Fetch transactions for a given access token
     * Fetches the last 90 days of transactions by default
     */
    suspend fun fetchTransactions(
        accessToken: String,
        startDate: String? = null,
        endDate: String? = null
    ): Result<List<Transaction>> = withContext(Dispatchers.IO) {
        runCatching {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val calendar = Calendar.getInstance()

            val end = endDate ?: dateFormat.format(calendar.time)
            calendar.add(Calendar.DAY_OF_YEAR, -90)
            val start = startDate ?: dateFormat.format(calendar.time)

            val requestBody = JSONObject().apply {
                put("client_id", clientId)
                put("secret", secret)
                put("access_token", accessToken)
                put("start_date", start)
                put("end_date", end)
            }

            val response = makeRequest("/transactions/get", requestBody)
            parseTransactionsResponse(response)
        }
    }

    private fun makeRequest(endpoint: String, body: JSONObject): JSONObject {
        val url = URL(baseUrl + endpoint)
        val conn = url.openConnection() as HttpURLConnection

        try {
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.doOutput = true
            conn.connectTimeout = 30_000
            conn.readTimeout = 30_000

            // Send request
            conn.outputStream.use { os ->
                os.write(body.toString().toByteArray())
            }

            val responseCode = conn.responseCode
            val responseBody = if (responseCode in 200..299) {
                conn.inputStream.bufferedReader().use { it.readText() }
            } else {
                val error = conn.errorStream?.bufferedReader()?.use { it.readText() } ?: "Unknown error"
                throw Exception("Plaid API error: HTTP $responseCode - $error")
            }

            return JSONObject(responseBody)
        } finally {
            conn.disconnect()
        }
    }

    private fun parseAccountsResponse(response: JSONObject): List<BankAccount> {
        val accountsArray = response.getJSONArray("accounts")
        val currentTime = System.currentTimeMillis()

        return (0 until accountsArray.length()).map { i ->
            val accountJson = accountsArray.getJSONObject(i)
            val balances = accountJson.getJSONObject("balances")

            BankAccount(
                id = accountJson.getString("account_id"),
                institutionName = "Unknown", // Will be fetched separately
                accountName = accountJson.optString("name", "Account"),
                accountType = parseAccountType(accountJson.optString("type", "other")),
                balance = balances.optDouble("current", 0.0),
                currency = balances.optString("iso_currency_code", "USD"),
                lastUpdated = currentTime
            )
        }
    }

    private fun parseAccountType(type: String): AccountType {
        return when (type.lowercase()) {
            "depository" -> AccountType.CHECKING // Plaid groups checking/savings as "depository"
            "credit" -> AccountType.CREDIT_CARD
            "loan" -> AccountType.LOAN
            "investment", "brokerage" -> AccountType.INVESTMENT
            else -> AccountType.OTHER
        }
    }

    private fun parseTransactionsResponse(response: JSONObject): List<Transaction> {
        val transactionsArray = response.getJSONArray("transactions")
        val currentTime = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

        return (0 until transactionsArray.length()).map { i ->
            val txnJson = transactionsArray.getJSONObject(i)

            // Parse date string to timestamp
            val dateString = txnJson.getString("date")
            val date = try {
                dateFormat.parse(dateString)?.time ?: currentTime
            } catch (e: Exception) {
                currentTime
            }

            // Parse category array
            val categories = mutableListOf<String>()
            val categoryArray = txnJson.optJSONArray("category")
            if (categoryArray != null) {
                for (j in 0 until categoryArray.length()) {
                    categories.add(categoryArray.getString(j))
                }
            }

            Transaction(
                id = txnJson.getString("transaction_id"),
                accountId = txnJson.getString("account_id"),
                amount = txnJson.getDouble("amount"),
                date = date,
                name = txnJson.optString("name", "Transaction"),
                merchantName = txnJson.optString("merchant_name", null),
                category = categories.takeIf { it.isNotEmpty() },
                pending = txnJson.optBoolean("pending", false),
                paymentChannel = txnJson.optString("payment_channel", null),
                lastUpdated = currentTime
            )
        }
    }
}
