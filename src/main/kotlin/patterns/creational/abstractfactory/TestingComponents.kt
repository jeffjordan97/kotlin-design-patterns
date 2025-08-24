package personal.learning.kotlin.patterns.creational.abstractfactory

/**
 * Testing Environment Components - Fast and temporary for automated tests
 */

/**
 * Temporary storage for testing
 */
class TempStorage : DataStorage {
    override val storageName = "Temporary"
    private val tempData = mutableMapOf<String, String>()

    override fun save(key: String, value: String): Boolean {
        tempData[key] = value
        println("🧪 [TEST] Saved '$key' to temp storage")
        return true
    }

    override fun load(key: String): String? {
        val value = tempData[key]
        println("🔬 [TEST] Loaded '$key' from temp: $value")
        return value
    }
}

/**
 * Silent logger for testing (no output to avoid test noise)
 */
class SilentLogger : Logger {
    override val loggerName = "Silent"

    override fun log(message: String) {
        // Silent - don't output anything during tests
        // Could store logs in memory for test verification
    }
}

/**
 * Fake notification service for testing
 */
class FakeNotificationService : NotificationService {
    override val serviceName = "Fake"
    val sentNotifications = mutableListOf<String>()

    override fun sendNotification(message: String): Boolean {
        sentNotifications.add(message)
        println("🎭 [TEST] Fake notification recorded: $message")
        return true
    }
}