package personal.learning.kotlin.patterns.creational.abstractfactory

/**
 * Concrete Factory Implementations - One for each environment
 */

/**
 * Development Environment Factory
 */
class DevelopmentFactory : ComponentFactory() {
    override val factoryName = "Development"

    override fun createStorage(): DataStorage {
        println("🏭 Creating development storage...")
        return InMemoryStorage()
    }

    override fun createLogger(): Logger {
        println("🏭 Creating development logger...")
        return ConsoleLogger()
    }

    override fun createNotificationService(): NotificationService {
        println("🏭 Creating development notification service...")
        return MockNotificationService()
    }
}

/**
 * Production Environment Factory
 */
class ProductionFactory : ComponentFactory() {
    override val factoryName = "Production"

    override fun createStorage(): DataStorage {
        println("🏭 Creating production storage...")
        return DatabaseStorage()
    }

    override fun createLogger(): Logger {
        println("🏭 Creating production logger...")
        return FileLogger()
    }

    override fun createNotificationService(): NotificationService {
        println("🏭 Creating production notification service...")
        return EmailNotificationService()
    }
}

/**
 * Testing Environment Factory
 */
class TestingFactory : ComponentFactory() {
    override val factoryName = "Testing"

    override fun createStorage(): DataStorage {
        println("🏭 Creating testing storage...")
        return TempStorage()
    }

    override fun createLogger(): Logger {
        println("🏭 Creating testing logger...")
        return SilentLogger()
    }

    override fun createNotificationService(): NotificationService {
        println("🏭 Creating testing notification service...")
        return FakeNotificationService()
    }
}