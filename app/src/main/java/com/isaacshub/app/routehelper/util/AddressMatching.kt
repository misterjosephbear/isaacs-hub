package com.isaacshub.app.routehelper.util

import com.isaacshub.app.routehelper.data.CandidateAddressEntity
import com.isaacshub.app.routehelper.data.RoutedStopEntity

/**
 * Shared utilities for address matching and validation across the app.
 * Used by package scanning, Amazon route scanning, and mail scanning.
 */

/**
 * Find the best matching routed stop for an extracted address using fuzzy matching.
 * Handles street name abbreviations and variations.
 */
fun findBestRoutedStopMatch(
    extractedAddress: String,
    routeStops: List<RoutedStopEntity>
): RoutedStopEntity? {
    val normalizedExtracted = normalizeAddress(extractedAddress)

    // First try exact match
    routeStops.find { stop ->
        normalizeAddress(stop.addressLabel) == normalizedExtracted
    }?.let { return it }

    // Try matching just the street number (address numbers must match)
    val extractedNumber = extractAddressNumber(normalizedExtracted)
    if (extractedNumber == null) {
        // No number found, try substring matching
        return routeStops.find { stop ->
            val normalizedStop = normalizeAddress(stop.addressLabel)
            normalizedStop.contains(normalizedExtracted) ||
            normalizedExtracted.contains(normalizedStop)
        }
    }

    // Find stops with matching address number
    val candidatesWithMatchingNumber = routeStops.filter { stop ->
        extractAddressNumber(normalizeAddress(stop.addressLabel)) == extractedNumber
    }

    if (candidatesWithMatchingNumber.isEmpty()) {
        return null  // No stops with matching number
    }

    if (candidatesWithMatchingNumber.size == 1) {
        return candidatesWithMatchingNumber.first()  // Only one match, return it
    }

    // Multiple stops with same number - use fuzzy matching on street name
    val extractedStreet = extractStreetName(normalizedExtracted)
    return candidatesWithMatchingNumber.maxByOrNull { stop ->
        val stopStreet = extractStreetName(normalizeAddress(stop.addressLabel))
        calculateStreetNameSimilarity(extractedStreet, stopStreet)
    }
}

/**
 * Find the best matching candidate address for an extracted address using fuzzy matching.
 * Used when building routes from scanned addresses.
 */
fun findBestCandidateMatch(
    extractedAddress: String,
    candidates: List<CandidateAddressEntity>,
    similarityThreshold: Double = 0.6
): CandidateAddressEntity? {
    val normalizedExtracted = normalizeAddress(extractedAddress)

    // First try exact match
    candidates.find { candidate ->
        normalizeAddress(candidate.label) == normalizedExtracted
    }?.let { return it }

    // Try matching just the street number (address numbers must match)
    val extractedNumber = extractAddressNumber(normalizedExtracted)
    if (extractedNumber == null) {
        // No number found, try substring matching
        return candidates.find { candidate ->
            val normalizedCandidate = normalizeAddress(candidate.label)
            normalizedCandidate.contains(normalizedExtracted) ||
            normalizedExtracted.contains(normalizedCandidate)
        }
    }

    // Find candidates with matching address number
    val candidatesWithMatchingNumber = candidates.filter { candidate ->
        extractAddressNumber(normalizeAddress(candidate.label)) == extractedNumber
    }

    if (candidatesWithMatchingNumber.isEmpty()) {
        return null  // No candidates with matching number
    }

    if (candidatesWithMatchingNumber.size == 1) {
        return candidatesWithMatchingNumber.first()  // Only one match, return it
    }

    // Multiple candidates with same number - use fuzzy matching on street name
    val extractedStreet = extractStreetName(normalizedExtracted)
    return candidatesWithMatchingNumber.maxByOrNull { candidate ->
        val candidateStreet = extractStreetName(normalizeAddress(candidate.label))
        val similarity = calculateStreetNameSimilarity(extractedStreet, candidateStreet)
        // Filter by similarity threshold
        if (similarity >= similarityThreshold) similarity else 0.0
    }?.takeIf {
        // Only return if similarity meets threshold
        val candidateStreet = extractStreetName(normalizeAddress(it.label))
        calculateStreetNameSimilarity(extractedStreet, candidateStreet) >= similarityThreshold
    }
}

/**
 * Normalize an address for comparison:
 * - Lowercase
 * - Remove extra whitespace
 * - Expand common abbreviations
 */
fun normalizeAddress(address: String): String {
    return address
        .lowercase()
        .trim()
        .replace(Regex("\\s+"), " ")  // Collapse multiple spaces
        .replace("saint", "st")
        .replace("avenue", "ave")
        .replace("street", "st")
        .replace("drive", "dr")
        .replace("road", "rd")
        .replace("boulevard", "blvd")
        .replace("lane", "ln")
        .replace("court", "ct")
        .replace("place", "pl")
        .replace("circle", "cir")
        .replace("north", "n")
        .replace("south", "s")
        .replace("east", "e")
        .replace("west", "w")
}

/**
 * Extract the address number from a normalized address.
 * E.g., "1006 jackson st" -> 1006
 */
fun extractAddressNumber(normalizedAddress: String): Int? {
    return normalizedAddress.split(" ").firstOrNull()?.toIntOrNull()
}

/**
 * Extract the street name from a normalized address (everything after the number, before city/zip).
 * E.g., "1006 jackson st" -> "jackson st"
 * E.g., "1006 jackson st, martinsburg, wv 25401" -> "jackson st"
 */
fun extractStreetName(normalizedAddress: String): String {
    // First, split off everything before a comma (removes city, state, zip)
    val beforeComma = normalizedAddress.split(",").firstOrNull() ?: normalizedAddress

    // Then extract everything after the house number
    val parts = beforeComma.trim().split(" ", limit = 2)
    return if (parts.size > 1) parts[1].trim() else beforeComma.trim()
}

/**
 * Calculate similarity between two street names.
 * Returns a score from 0.0 (no match) to 1.0 (perfect match).
 * Handles partial addresses well (e.g., "showers dr" matches "showers dr, martinsburg, wv")
 */
fun calculateStreetNameSimilarity(street1: String, street2: String): Double {
    // Normalize both streets (remove city/zip if present)
    val normalized1 = street1.split(",").firstOrNull()?.trim() ?: street1
    val normalized2 = street2.split(",").firstOrNull()?.trim() ?: street2

    // Exact match
    if (normalized1 == normalized2) return 1.0

    // One contains the other (handles partial addresses)
    if (normalized1.contains(normalized2) || normalized2.contains(normalized1)) return 0.9

    // Extract significant words (longer than 2 characters)
    val words1 = normalized1.split(" ").filter { it.length > 2 }
    val words2 = normalized2.split(" ").filter { it.length > 2 }

    // If one list is empty but we have text, try substring matching
    if (words1.isEmpty() || words2.isEmpty()) {
        return if (normalized1.contains(normalized2) || normalized2.contains(normalized1)) 0.7 else 0.0
    }

    // Count matching words (exact match or prefix match)
    val matchingWords = words1.count { w1 ->
        words2.any { w2 ->
            w1 == w2 ||                           // Exact match
            w1.startsWith(w2) ||                  // Prefix match
            w2.startsWith(w1) ||                  // Reverse prefix match
            levenshteinSimilarity(w1, w2) >= 0.8  // Allow minor typos
        }
    }

    // Calculate similarity score
    // Use the minimum size to be more lenient with partial addresses
    val minSize = minOf(words1.size, words2.size)
    val maxSize = maxOf(words1.size, words2.size)

    return if (minSize > 0) {
        // If all words in the shorter address match, give high score
        val baseScore = matchingWords.toDouble() / maxSize
        if (matchingWords >= minSize) {
            // All words in shorter address matched - very likely the same street
            (baseScore + 0.2).coerceAtMost(1.0)
        } else {
            baseScore
        }
    } else {
        0.0
    }
}

/**
 * Calculate Levenshtein distance-based similarity between two strings.
 * Returns a score from 0.0 (completely different) to 1.0 (identical).
 * Useful for catching minor OCR errors or typos.
 */
private fun levenshteinSimilarity(s1: String, s2: String): Double {
    val maxLength = maxOf(s1.length, s2.length)
    if (maxLength == 0) return 1.0

    val distance = levenshteinDistance(s1, s2)
    return 1.0 - (distance.toDouble() / maxLength)
}

/**
 * Calculate Levenshtein distance (edit distance) between two strings.
 * The minimum number of single-character edits needed to change one string into the other.
 */
private fun levenshteinDistance(s1: String, s2: String): Int {
    val m = s1.length
    val n = s2.length
    val dp = Array(m + 1) { IntArray(n + 1) }

    for (i in 0..m) dp[i][0] = i
    for (j in 0..n) dp[0][j] = j

    for (i in 1..m) {
        for (j in 1..n) {
            dp[i][j] = if (s1[i - 1] == s2[j - 1]) {
                dp[i - 1][j - 1]
            } else {
                1 + minOf(dp[i - 1][j], dp[i][j - 1], dp[i - 1][j - 1])
            }
        }
    }

    return dp[m][n]
}
