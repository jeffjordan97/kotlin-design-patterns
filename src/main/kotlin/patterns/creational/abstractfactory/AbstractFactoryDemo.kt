package personal.learning.kotlin.patterns.creational.abstractfactory

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

//        println("=" * 50 + "\n")
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