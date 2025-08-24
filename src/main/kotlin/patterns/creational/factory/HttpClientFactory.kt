package personal.learning.kotlin.patterns.creational.factory

/**
 * Factory Method Pattern - The Factory
 *
 * This class knows HOW to create the right HTTP client
 * based on what type of service you're calling
 */
class HttpClientFactory {

    /**
     * Factory Method - creates the right client based on service type
     */
    fun createClient(serviceType: ServiceType): HttpClient {
        return when (serviceType) {
            ServiceType.INTERNAL_SERVICE -> {
                println("🏭 Factory: Creating Fast client for internal service")
                FastHttpClient()
            }

            ServiceType.EXTERNAL_API -> {
                println("🏭 Factory: Creating Reliable client for external API")
                ReliableHttpClient()
            }

            ServiceType.PAYMENT_SERVICE -> {
                println("🏭 Factory: Creating Secure client for payment service")
                SecureHttpClient()
            }
        }
    }
    /**
     * Alternative factory method - create based on URL pattern
     */
    fun createClientForUrl(url: String): HttpClient {
        return when {
            url.contains("internal") || url.contains("localhost") -> {
                println("🏭 Factory: URL suggests internal service - creating Fast client")
                FastHttpClient()
            }

            url.contains("payment") || url.contains("stripe") || url.contains("paypal") -> {
                println("🏭 Factory: URL suggests payment service - creating Secure client")
                SecureHttpClient()
            }

            else -> {
                println("🏭 Factory: External URL detected - creating Reliable client")
                ReliableHttpClient()
            }
        }
    }
}

/**
 * Enum for different service types
 */
enum class ServiceType {
    INTERNAL_SERVICE,    // Fast, low latency
    EXTERNAL_API,        // Reliable, with retries
    PAYMENT_SERVICE      // Secure, encrypted
}