package personal.learning.kotlin.patterns.creational.abstractfactory

/**
 * Development Environment Components - Fast and simple for local development
 */

/**
 * In-memory storage for development
 */
class InMemoryStorage : DataStorage {
    override val storageName = "In-Memory"
    private val data = mutableMapOf<String, String>()

    override fun save(key: String, value: String): Boolean {
        data[key] = value
        println("💾 [DEV] Saved '$key' to memory")
        return true
    }

    override fun load(key: String): String? {
        val value = data[key]
        println("📖 [DEV] Loaded '$key' from memory: $value")
        return value
    }
}

/**
 * Console logger for development
 */
class ConsoleLogger : Logger {
    override val loggerName = "Console"

    override fun log(message: String) {
        val timestamp = java.time.LocalTime.now()
        println("📝 [DEV] [$timestamp] $message")
    }
}

/**
 * Mock notification service for development
 */
class MockNotificationService : NotificationService {
    override val serviceName = "Mock"

    override fun sendNotification(message: String): Boolean {
        println("📨 [DEV] Mock notification sent: $message")
        return true
    }
}