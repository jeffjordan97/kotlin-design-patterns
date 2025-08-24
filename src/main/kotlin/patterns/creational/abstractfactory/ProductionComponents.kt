package personal.learning.kotlin.patterns.creational.abstractfactory

import java.io.File

/**
 * Production Environment Components - Robust and persistent for live systems
 */

/**
 * Database storage for production
 */
class DatabaseStorage : DataStorage {
    override val storageName = "Database"

    override fun save(key: String, value: String): Boolean {
        // Simulate database save
        Thread.sleep(50) // Database operations are slower
        println("🗄️ [PROD] Saved '$key' to database with replication")
        return true
    }

    override fun load(key: String): String? {
        // Simulate database load
        Thread.sleep(30)
        val value = "db_value_for_$key" // Simulated database value
        println("🔍 [PROD] Loaded '$key' from database: $value")
        return value
    }
}

/**
 * File logger for production
 */
class FileLogger : Logger {
    override val loggerName = "File"
    private val logFile = "production.log"

    override fun log(message: String) {
        val timestamp = java.time.LocalDateTime.now()
        val logEntry = "[$timestamp] $message\n"

        try {
            File(logFile).appendText(logEntry)
            println("📄 [PROD] Logged to file: $message")
        } catch (e: Exception) {
            println("❌ [PROD] Failed to write log: ${e.message}")
        }
    }
}

/**
 * Email notification service for production
 */
class EmailNotificationService : NotificationService {
    override val serviceName = "Email"

    override fun sendNotification(message: String): Boolean {
        // Simulate email sending
        Thread.sleep(100) // Email sending takes time
        println("✉️ [PROD] Email sent to admin@company.com: $message")
        return true
    }
}