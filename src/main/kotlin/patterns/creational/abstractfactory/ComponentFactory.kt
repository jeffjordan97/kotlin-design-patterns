package personal.learning.kotlin.patterns.creational.abstractfactory

/**
 * Abstract Factory Pattern - The Abstract Factory
 *
 * This defines HOW to create a complete family of related components
 */
abstract class ComponentFactory {

    // Factory methods for creating each type of component
    abstract fun createStorage(): DataStorage
    abstract fun createLogger(): Logger
    abstract fun createNotificationService(): NotificationService

    // Optional: Factory name for identification
    abstract val factoryName: String

    /**
     * Convenience method to create all components at once
     */
    fun createAllComponents(): ComponentSet {
        return ComponentSet(
            storage = createStorage(),
            logger = createLogger(),
            notificationService = createNotificationService()
        )
    }
}

/**
 * Data class to hold a complete set of components
 */
data class ComponentSet(
    val storage: DataStorage,
    val logger: Logger,
    val notificationService: NotificationService
) {
    fun getInfo(): String {
        return "Components: ${storage.storageName} storage, " +
                "${logger.loggerName} logger, " +
                "${notificationService.serviceName} notifications"
    }
}