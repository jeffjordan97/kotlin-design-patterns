package personal.learning.kotlin.patterns.creational.factory.httpclient

/**
 * Demo showing how the factory creates different clients
 */
fun main() {
    val factory = HttpClientFactory()

    println("=== CREATING CLIENTS BY SERVICE TYPE ===\n")

    // Scenario 1: Calling an internal user service
    println("📞 Need to call internal user service...")
    val fastClient = factory.createClient(ServiceType.INTERNAL_SERVICE)
    val userResponse = fastClient.get("http://localhost:8081/users")
    println("Response: ${userResponse.body}")
    println("Success: ${userResponse.isSuccessful()}\n")

    // Scenario 2: Calling an external weather API
    println("🌤️ Need to call external weather API...")
    val reliableClient = factory.createClient(ServiceType.EXTERNAL_API)
    val weatherResponse = reliableClient.get("https://api.weather.com/forecast")
    println("Response: ${weatherResponse.body}")
    println("Success: ${weatherResponse.isSuccessful()}\n")

    // Scenario 3: Processing a payment
    println("💳 Need to process payment...")
    val secureClient = factory.createClient(ServiceType.PAYMENT_SERVICE)
    val paymentResponse = secureClient.post(
        "https://api.stripe.com/payments",
        """{"amount": 100, "currency": "USD"}"""
    )
    println("Response: ${paymentResponse.body}")
    println("Success: ${paymentResponse.isSuccessful()}\n")

    println("=== CREATING CLIENTS BY URL PATTERN ===\n")

    // Smart factory - decides based on URL
    val urls = listOf(
        "http://internal-service:8080/data",
        "https://api.github.com/repos",
        "https://payment-gateway.stripe.com/charge"
    )

    urls.forEach { url ->
        println("🔍 Analyzing URL: $url")
        val client = factory.createClientForUrl(url)
        val response = client.get(url)
        println("   Client used: ${client.clientName}")
        println("   Response: ${response.body}\n")
    }
}