# Abstract Factory Pattern - Simple Service Components Creation Example

## The Problem: Creating Families of Related Components

Your API Gateway needs to work in different environments, and each environment needs a complete set of components that work together:
- **Development Environment**: Mock services, console logging, in-memory storage
- **Production Environment**: Real services, file logging, database storage
- **Testing Environment**: Fake services, no logging, temporary storage

The Abstract Factory pattern lets you create a complete family of related components that are guaranteed to work together!

---

## Step 1: The Product Interfaces (What We're Creating)

Create `src/main/kotlin/patterns/creational/abstractfactory/ServiceComponents.kt`:

```kotlin
package patterns.creational.abstractfactory

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
```

---

## Step 2: Concrete Products for Development Environment

Create `src/main/kotlin/patterns/creational/abstractfactory/DevelopmentComponents.kt`:

```kotlin
package patterns.creational.abstractfactory

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
```

---

## Step 3: Concrete Products for Production Environment

Create `src/main/kotlin/patterns/creational/abstractfactory/ProductionComponents.kt`:

```kotlin
package patterns.creational.abstractfactory

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
```

---

## Step 4: Concrete Products for Testing Environment

Create `src/main/kotlin/patterns/creational/abstractfactory/TestingComponents.kt`:

```kotlin
package patterns.creational.abstractfactory

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
```

---

## Step 5: The Abstract Factory Interface

Create `src/main/kotlin/patterns/creational/abstractfactory/ComponentFactory.kt`:

```kotlin
package patterns.creational.abstractfactory

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
```

---

## Step 6: Concrete Factory Implementations

Create `src/main/kotlin/patterns/creational/abstractfactory/ConcreteFactories.kt`:

```kotlin
package patterns.creational.abstractfactory

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
```

---

## Step 7: Demo (How to Use It)

Create `src/main/kotlin/patterns/creational/abstractfactory/AbstractFactoryDemo.kt`:

```kotlin
package patterns.creational.abstractfactory

/**
 * Demo showing how Abstract Factory creates families of related components
 */

/**
 * Simple service that uses the components
 */
class ApiGatewayService(private val components: ComponentSet) {
    
    fun handleRequest(requestId: String, data: String) {
        components.logger.log("Processing request: $requestId")
        
        val saved = components.storage.save(requestId, data)
        if (saved) {
            components.logger.log("Request $requestId saved successfully")
            components.notificationService.sendNotification("Request $requestId processed")
        } else {
            components.logger.log("Failed to save request $requestId")
        }
    }
    
    fun getRequest(requestId: String): String? {
        components.logger.log("Retrieving request: $requestId")
        return components.storage.load(requestId)
    }
}

fun main() {
    println("=== ABSTRACT FACTORY PATTERN DEMO ===\n")
    
    // Different environments use different factories
    val environments = mapOf(
        "Development" to DevelopmentFactory(),
        "Production" to ProductionFactory(),
        "Testing" to TestingFactory()
    )
    
    environments.forEach { (envName, factory) ->
        println("🌍 Setting up $envName Environment")
        println("Factory: ${factory.factoryName}")
        
        // Create complete family of components
        val components = factory.createAllComponents()
        println("✅ ${components.getInfo()}\n")
        
        // Use the components in our service
        val service = ApiGatewayService(components)
        
        // Simulate some operations
        service.handleRequest("req-001", "sample data")
        service.getRequest("req-001")
        
        println("=" * 50 + "\n")
    }
    
    println("=== SWITCHING ENVIRONMENTS AT RUNTIME ===\n")
    
    // Simulate switching environments (like dev -> production deployment)
    fun createServiceForEnvironment(env: String): ApiGatewayService {
        val factory = when (env.lowercase()) {
            "dev", "development" -> DevelopmentFactory()
            "prod", "production" -> ProductionFactory()  
            "test", "testing" -> TestingFactory()
            else -> DevelopmentFactory() // Default
        }
        
        println("🔄 Creating service for $env environment...")
        return ApiGatewayService(factory.createAllComponents())
    }
    
    // Demo switching environments
    listOf("dev", "prod", "test").forEach { env ->
        val service = createServiceForEnvironment(env)
        service.handleRequest("switch-test", "environment switch data")
        println()
    }
}
```

---

## Why This is the Abstract Factory Pattern

### The Key Components:

1. **Product Interfaces** (`DataStorage`, `Logger`, `NotificationService`)
    - Define what each type of component must do

2. **Concrete Products** (Development/Production/Testing implementations)
    - Different implementations for each environment
    - Components within same family work well together

3. **Abstract Factory** (`ComponentFactory`)
    - Defines HOW to create a complete family of components

4. **Concrete Factories** (`DevelopmentFactory`, `ProductionFactory`, `TestingFactory`)
    - Create specific families of components

### The Magic:
```kotlin
// Just change the factory to get a completely different set of components!
val factory = ProductionFactory()     // Or DevelopmentFactory(), or TestingFactory()
val components = factory.createAllComponents()

// All components are guaranteed to work together
val service = ApiGatewayService(components)
```

## Expected Output:
```
=== ABSTRACT FACTORY PATTERN DEMO ===

🌍 Setting up Development Environment
Factory: Development
🏭 Creating development storage...
🏭 Creating development logger...
🏭 Creating development notification service...
✅ Components: In-Memory storage, Console logger, Mock notifications

📝 [DEV] [10:30:15.123] Processing request: req-001
💾 [DEV] Saved 'req-001' to memory
📝 [DEV] [10:30:15.124] Request req-001 saved successfully
📨 [DEV] Mock notification sent: Request req-001 processed
📝 [DEV] [10:30:15.125] Retrieving request: req-001
📖 [DEV] Loaded 'req-001' from memory: sample data

==================================================

🌍 Setting up Production Environment
Factory: Production
🏭 Creating production storage...
🏭 Creating production logger...
🏭 Creating production notification service...
✅ Components: Database storage, File logger, Email notifications

📄 [PROD] Logged to file: Processing request: req-001
🗄️ [PROD] Saved 'req-001' to database with replication
📄 [PROD] Logged to file: Request req-001 saved successfully
✉️ [PROD] Email sent to admin@company.com: Request req-001 processed
```

## The Benefits:

1. **Consistency**: All components in a family work together perfectly
2. **Easy Environment Switching**: Change one line to switch entire component set
3. **Type Safety**: Can't accidentally mix components from different families
4. **Easy Testing**: Can create entire test environment with one factory

## Key Difference from Factory Method:

- **Factory Method**: Creates ONE type of object
- **Abstract Factory**: Creates FAMILIES of related objects that work together

## Real-World Connection:

In your API Gateway:
- **Development**: Fast in-memory components for quick iteration
- **Production**: Robust database-backed components with real notifications
- **Testing**: Silent, temporary components that don't interfere with tests

This pattern ensures all your components are compatible and optimized for their environment!

## Factory Method vs Abstract Factory:
Key Insights:
The Problem Abstract Factory Solves:
Imagine if you accidentally mixed environments:
```kotlin
// BAD - mixing components from different environments
val storage = DatabaseStorage()        // Production component
val logger = ConsoleLogger()          // Development component  
val notifications = FakeNotificationService() // Testing component

// This creates inconsistencies and bugs!
```
Abstract Factory Solution:
```kotlin
// GOOD - all components from same family
val factory = ProductionFactory()
val components = factory.createAllComponents()

// Guaranteed: Database storage + File logger + Email notifications
// All optimized for production environment!
```

## Real-World Benefits:

1. **Environment Consistency**: All components match the environment
2. **Easy Deployment**: Switch from dev to prod with one line change
3. **No Mix-ups**: Impossible to accidentally use wrong component combination
4. **Family Optimization**: Components designed to work together efficiently

## Kotlin-Specific Advantages:

- **Data Classes**: ComponentSet bundles the family together cleanly
- **Sealed Classes**: Could make factory types sealed for exhaustive checking
- **Extension Functions**: Could add ComponentSet.validateConfiguration()
- **Companion Objects**: Could add ComponentFactory.forEnvironment(env)

## Pattern Comparison:
- **Factory Method**: creates Single objects, Example: HttpClient for different services
- **Abstract Factory**: creates Families of objects, Example: Complete environment setup

## In your API Gateway, you'd use:

- Factory Method for creating individual clients
- Abstract Factory for setting up entire environments (dev/staging/prod)

This pattern is huge in enterprise applications - think Spring profiles, where changing one configuration gives you a complete different set of beans that all work together!