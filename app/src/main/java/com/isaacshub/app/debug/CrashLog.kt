package com.isaacshub.app.debug

import java.util.UUID

/**
 * Represents a single app crash event with full diagnostic information.
 * Designed for remote debugging and crash analysis.
 */
data class CrashLog(
    val id: String = UUID.randomUUID().toString(),
    val timestamp: Long = System.currentTimeMillis(),
    val appVersion: String,
    val buildNumber: Int,
    val threadName: String,
    val threadId: Long,
    val exceptionClassName: String,
    val exceptionMessage: String?,
    val stackTrace: String,
    val cause: String?,
    val totalMemory: Long,
    val freeMemory: Long,
    val maxMemory: Long,
    val databaseErrors: List<DatabaseError> = emptyList()
)

/**
 * Represents a database-specific error detected during crash analysis.
 * Used to identify schema version mismatches and corruption issues.
 */
data class DatabaseError(
    val databaseName: String,
    val errorMessage: String,
    val errorType: String  // e.g., "SchemaVersionMismatch", "CorruptionDetected", "LockTimeout"
)
