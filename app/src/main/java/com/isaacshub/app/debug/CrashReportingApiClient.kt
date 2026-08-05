package com.isaacshub.app.debug

import android.util.Log
import com.google.gson.Gson
import java.io.OutputStreamWriter
import java.net.HttpURLConnection

/**
 * API client for reporting crashes to the server.
 * Handles local/remote URL fallback without requiring vault connection.
 * Uses non-blocking, fire-and-forget pattern with rate limiting.
 */
class CrashReportingApiClient(
    private val localUrl: String,
    private val remoteUrl: String
) {
    private val gson = Gson()
    private var lastCrashReportTime = 0L
    private val minTimeBetweenReports = 5000L  // 5 seconds minimum between reports

    /**
     * Report a crash to the server (non-blocking, best effort).
     * Implements rate limiting to prevent spam.
     */
    fun reportCrashAsync(crash: CrashLog) {
        val now = System.currentTimeMillis()
        if (now - lastCrashReportTime < minTimeBetweenReports) {
            Log.d("CrashReporting", "Rate limited - crash report deferred")
            return
        }
        lastCrashReportTime = now

        // Fire and forget in background
        Thread {
            try {
                reportCrash(crash)
            } catch (e: Exception) {
                Log.e("CrashReporting", "Failed to report crash: ${e.message}")
                // Silently fail - don't let crash reporting cause additional crashes
            }
        }.start()
    }

    /**
     * Report a crash to the server (blocking call).
     * Tries both local and remote URLs until one succeeds.
     */
    private fun reportCrash(crash: CrashLog) {
        val urls = listOf(localUrl, remoteUrl)
        var lastException: Exception? = null

        for (url in urls) {
            try {
                val fullUrl = "$url/api/debug/crash-logs"
                val conn = java.net.URL(fullUrl).openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.connectTimeout = 6000
                conn.readTimeout = 10000
                conn.setRequestProperty("Content-Type", "application/json")
                conn.doOutput = true

                // Write crash log as JSON
                val jsonData = gson.toJson(crash)
                OutputStreamWriter(conn.outputStream).use { writer ->
                    writer.write(jsonData)
                    writer.flush()
                }

                // Check response
                val responseCode = conn.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                    Log.d("CrashReporting", "Crash reported successfully (code: $responseCode)")
                    return
                } else {
                    Log.w("CrashReporting", "Server returned error code: $responseCode")
                    lastException = Exception("HTTP $responseCode")
                }

                conn.disconnect()
            } catch (e: Exception) {
                lastException = e
                Log.d("CrashReporting", "Failed to report crash to $url: ${e.message}")
                // Continue to next URL
            }
        }

        Log.e("CrashReporting", "Failed to report crash to any server: ${lastException?.message}")
    }
}
