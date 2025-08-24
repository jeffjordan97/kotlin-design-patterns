package personal.learning.kotlin.patterns.creational.abstractfactory

/**
 * Abstract Factory Pattern - Product Interfaces
 *
 * These define the different types of components we need to create
 */

/**
 * Interface for data storage component
 */
interface DataStorage {
    fun save(key: String, value: String): Boolean
    fun load(key: String): String?
    val storageName: String
}

/**
 * Interface for logging component
 */
interface Logger {
    fun log(message: String)
    val loggerName: String
}

/**
 * Interface for notification component
 */
interface NotificationService {
    fun sendNotification(message: String): Boolean
    val serviceName: String
}
