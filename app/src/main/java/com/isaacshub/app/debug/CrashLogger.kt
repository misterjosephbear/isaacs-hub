package com.isaacshub.app.debug

import android.app.Application
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Global exception handler for the IsaacsHub app.
 * Captures all unhandled exceptions and logs them locally and to the server.
 * Provides diagnostic information including stack traces, memory state, and database errors.
 */
object CrashLogger {
    private lateinit var crashLogDb: CrashLogDatabase
    private lateinit var crashReportingClient: CrashReportingApiClient
    private lateinit var appScope: CoroutineScope
    private var previousHandler: Thread.UncaughtExceptionHandler? = null
    private lateinit var application: Application

    /**
     * Install the global crash handler.
     * Must be called once during app initialization.
     */
    fun install(
        app: Application,
        scope: CoroutineScope,
        reportingClient: CrashReportingApiClient
    ) {
        application = app
        appScope = scope
        crashReportingClient = reportingClient
        crashLogDb = CrashLogDatabase(app)

        // Save the previous handler to chain if needed
        previousHandler = Thread.getDefaultUncaughtExceptionHandler()

        // Install our handler
        Thread.setDefaultUncaughtExceptionHandler { thread, exception ->
            handleUncaughtException(thread, exception)
        }

        Log.d("CrashLogger", "Global crash handler installed")
    }

    /**
     * Handle an uncaught exception.
     * This method is called when any thread throws an unhandled exception.
     */
    private fun handleUncaughtException(thread: Thread, exception: Throwable) {
        try {
            Log.e("CrashLogger", "Uncaught exception in thread '${thread.name}'", exception)

            // Build crash log with full diagnostics
            val crashLog = buildCrashLog(thread, exception)

            // Store locally (blocking)
            try {
                // Run synchronously since we're about to crash
                Thread {
                    Thread.sleep(100)  // Give some time for disk writes
                    appScope.launch {
                        crashLogDb.addCrash(crashLog)
                        crashReportingClient.reportCrashAsync(crashLog)
                    }
                }.start()
            } catch (e: Exception) {
                Log.e("CrashLogger", "Failed to store crash log: ${e.message}")
            }

            // Give time for async operations
            Thread.sleep(500)

        } catch (e: Exception) {
            Log.e("CrashLogger", "Error in crash handler itself: ${e.message}")
        } finally {
            // Call the previous handler (usually system handler that kills the app)
            previousHandler?.uncaughtException(thread, exception)
        }
    }

    /**
     * Build a comprehensive crash log with diagnostic information.
     */
    private fun buildCrashLog(thread: Thread, exception: Throwable): CrashLog {
        val runtime = Runtime.getRuntime()

        // Get app version and build number
        val appVersion = try {
            application.packageManager.getPackageInfo(application.packageName, 0).versionName ?: "unknown"
        } catch (e: Exception) {
            "unknown"
        }

        val buildNumber = try {
            application.packageManager.getPackageInfo(application.packageName, 0).versionCode
        } catch (e: Exception) {
            -1
        }

        // Extract stack trace
        val stackTrace = exception.stackTraceToString()

        // Detect database errors
        val databaseErrors = detectDatabaseErrors(exception, stackTrace)

        // Get memory info
        val totalMemory = runtime.totalMemory()
        val freeMemory = runtime.freeMemory()
        val maxMemory = runtime.maxMemory()

        return CrashLog(
            timestamp = System.currentTimeMillis(),
            appVersion = appVersion,
            buildNumber = buildNumber,
            threadName = thread.name,
            threadId = thread.id,
            exceptionClassName = exception.javaClass.name,
            exceptionMessage = exception.message,
            stackTrace = stackTrace,
            cause = exception.cause?.toString(),
            totalMemory = totalMemory,
            freeMemory = freeMemory,
            maxMemory = maxMemory,
            databaseErrors = databaseErrors
        )
    }

    /**
     * Detect database-specific errors from exception information.
     * This helps identify schema mismatches, corruption, and lock issues.
     */
    private fun detectDatabaseErrors(exception: Throwable, stackTrace: String): List<DatabaseError> {
        val errors = mutableListOf<DatabaseError>()

        // Check for common database error patterns
        if (stackTrace.contains("SQLiteException") || stackTrace.contains("android.database.sqlite")) {
            when {
                stackTrace.contains("no such table") -> {
                    errors.add(DatabaseError(
                        databaseName = "Unknown",
                        errorMessage = exception.message ?: "Table not found",
                        errorType = "TableMissing"
                    ))
                }
                stackTrace.contains("schema") && stackTrace.contains("mismatch") -> {
                    errors.add(DatabaseError(
                        databaseName = "Unknown",
                        errorMessage = exception.message ?: "Schema version mismatch",
                        errorType = "SchemaVersionMismatch"
                    ))
                }
                stackTrace.contains("database is locked") -> {
                    errors.add(DatabaseError(
                        databaseName = "Unknown",
                        errorMessage = "Database is locked",
                        errorType = "DatabaseLocked"
                    ))
                }
                stackTrace.contains("corrupt") -> {
                    errors.add(DatabaseError(
                        databaseName = "Unknown",
                        errorMessage = exception.message ?: "Database corruption detected",
                        errorType = "CorruptionDetected"
                    ))
                }
                else -> {
                    errors.add(DatabaseError(
                        databaseName = "Unknown",
                        errorMessage = exception.message ?: "SQLite error",
                        errorType = "SQLiteError"
                    ))
                }
            }
        }

        // Detect Room-specific errors
        if (stackTrace.contains("androidx.room")) {
            errors.add(DatabaseError(
                databaseName = "Unknown",
                errorMessage = exception.message ?: "Room database error",
                errorType = "RoomError"
            ))
        }

        // Try to identify which database file is involved
        val dbName = when {
            stackTrace.contains("sleep.db") -> "SleepDatabase"
            stackTrace.contains("budget.db") -> "BankingDatabase"
            stackTrace.contains("feature_funnel.db") -> "FeatureFunnelDatabase"
            stackTrace.contains("app.db") -> "AppDatabase"
            stackTrace.contains("work.db") -> "WorkDatabase"
            else -> null
        }

        // Rebuild errors with correct database name if identified
        return if (dbName != null) {
            errors.map { it.copy(databaseName = dbName) }
        } else {
            errors
        }
    }
}
