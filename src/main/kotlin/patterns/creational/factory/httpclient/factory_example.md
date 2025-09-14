# Factory Method Pattern - Simple HTTP Client Creation Example

## The Problem: Creating Different Types of HTTP Clients

Your API Gateway needs to make HTTP requests to backend services, but different services need different client configurations:
- **Fast Client**: For quick internal service calls (short timeout)
- **Reliable Client**: For important external APIs (retries, long timeout)
- **Secure Client**: For payment services (extra security headers, certificates)

The Factory Method pattern lets you create the right type of HTTP client based on what you need!

---

## Step 1: The Product Interface (What We're Creating)

Create `src/main/kotlin/patterns/creational/factory/HttpClient.kt`:

```kotlin
package patterns.creational.factory

/**
 * Factory Method Pattern - The Product Interface
 * 
 * This defines WHAT every HTTP client must do:
 * - Make GET and POST requests
 * - Return a simple response
 */
interface HttpClient {
    fun get(url: String): HttpResponse
    fun post(url: String, body: String): HttpResponse
    
    // Optional: Get client name for identification
    val clientName: String
}

/**
 * Simple data class for HTTP responses
 */
data class HttpResponse(
    val statusCode: Int,
    val body: String,
    val headers: Map<String, String> = emptyMap()
) {
    fun isSuccessful(): Boolean = statusCode in 200..299
}
```

---

## Step 2: Concrete Products (The Different HTTP Clients)

Create `src/main/kotlin/patterns/creational/factory/HttpClientTypes.kt`:

```kotlin
package patterns.creational.factory

import kotlin.random.Random

/**
 * Client 1: Fast Client - for quick internal calls
 */
class FastHttpClient : HttpClient {
    override val clientName = "Fast"
    
    override fun get(url: String): HttpResponse {
        println("🚀 FastClient: Quick GET to $url (timeout: 1s)")
        
        // Simulate fast response
        Thread.sleep(100) // 100ms
        
        return HttpResponse(
            statusCode = 200,
            body = """{"message": "Fast response from $url"}""",
            headers = mapOf("X-Client-Type" to "Fast")
        )
    }
    
    override fun post(url: String, body: String): HttpResponse {
        println("🚀 FastClient: Quick POST to $url (timeout: 1s)")
        
        // Simulate fast response
        Thread.sleep(150) // 150ms
        
        return HttpResponse(
            statusCode = 201,
            body = """{"message": "Fast POST successful", "data": "$body"}""",
            headers = mapOf("X-Client-Type" to "Fast")
        )
    }
}

/**
 * Client 2: Reliable Client - for important external APIs
 */
class ReliableHttpClient : HttpClient {
    override val clientName = "Reliable"
    
    override fun get(url: String): HttpResponse {
        println("🔄 ReliableClient: GET to $url (with retries, timeout: 10s)")
        
        return retryRequest {
            // Simulate sometimes failing, then succeeding
            Thread.sleep(300) // Slower but more reliable
            
            if (Random.nextInt(100) < 20) { // 20% chance of failure
                throw Exception("Network hiccup")
            }
            
            HttpResponse(
                statusCode = 200,
                body = """{"message": "Reliable response from $url"}""",
                headers = mapOf("X-Client-Type" to "Reliable", "X-Retry-Count" to "0")
            )
        }
    }
    
    override fun post(url: String, body: String): HttpResponse {
        println("🔄 ReliableClient: POST to $url (with retries, timeout: 10s)")
        
        return retryRequest {
            Thread.sleep(400)
            
            if (Random.nextInt(100) < 15) { // 15% chance of failure
                throw Exception("Temporary error")
            }
            
            HttpResponse(
                statusCode = 201,
                body = """{"message": "Reliable POST successful", "data": "$body"}""",
                headers = mapOf("X-Client-Type" to "Reliable", "X-Retry-Count" to "0")
            )
        }
    }
    
    private fun retryRequest(maxRetries: Int = 2, request: () -> HttpResponse): HttpResponse {
        repeat(maxRetries) { attempt ->
            try {
                return request()
            } catch (e: Exception) {
                println("   ⚠️ Attempt ${attempt + 1} failed: ${e.message}")
                if (attempt < maxRetries - 1) {
                    Thread.sleep(500) // Wait before retry
                }
            }
        }
        // Final attempt
        return request()
    }
}

/**
 * Client 3: Secure Client - for payment and sensitive services
 */
class SecureHttpClient : HttpClient {
    override val clientName = "Secure"
    
    override fun get(url: String): HttpResponse {
        println("🔐 SecureClient: Secure GET to $url (with encryption, certificates)")
        
        // Simulate secure handshake
        Thread.sleep(500) // Slower due to security overhead
        
        return HttpResponse(
            statusCode = 200,
            body = """{"message": "Secure response from $url", "encrypted": true}""",
            headers = mapOf(
                "X-Client-Type" to "Secure",
                "X-Encryption" to "AES-256",
                "X-Certificate-Verified" to "true"
            )
        )
    }
    
    override fun post(url: String, body: String): HttpResponse {
        println("🔐 SecureClient: Secure POST to $url (with encryption, certificates)")
        
        // Simulate secure processing
        Thread.sleep(600)
        
        return HttpResponse(
            statusCode = 201,
            body = """{"message": "Secure POST successful", "encrypted_data": "[ENCRYPTED]"}""",
            headers = mapOf(
                "X-Client-Type" to "Secure",
                "X-Encryption" to "AES-256",
                "X-Certificate-Verified" to "true"
            )
        )
    }
}
```

---

## Step 3: The Factory Method (Creates the Right Client)

Create `src/main/kotlin/patterns/creational/factory/HttpClientFactory.kt`:

```kotlin
package patterns.creational.factory

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
```

---

## Step 4: Demo (How to Use It)

Create `src/main/kotlin/patterns/creational/factory/FactoryMethodDemo.kt`:

```kotlin
package patterns.creational.factory

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
```

---

## Why This is the Factory Method Pattern

### The Key Components:

1. **Product Interface** (`HttpClient`)
    - Defines what every HTTP client must do
    - `get()` and `post()` methods

2. **Concrete Products** (`FastHttpClient`, `ReliableHttpClient`, `SecureHttpClient`)
    - Different implementations of HTTP clients
    - Each optimized for different use cases

3. **Factory** (`HttpClientFactory`)
    - Knows HOW to create the right client
    - Decides which client to create based on input

### The Magic:
```kotlin
// You don't need to know which client to create!
val client = factory.createClient(ServiceType.PAYMENT_SERVICE)  // Factory decides
val response = client.get("https://payment-api.com")            // Just use it!
```

The factory handles the complexity of choosing the right client type.

---

## Expected Output:
```
=== CREATING CLIENTS BY SERVICE TYPE ===

📞 Need to call internal user service...
🏭 Factory: Creating Fast client for internal service
🚀 FastClient: Quick GET to http://localhost:8081/users (timeout: 1s)
Response: {"message": "Fast response from http://localhost:8081/users"}
Success: true

🌤️ Need to call external weather API...
🏭 Factory: Creating Reliable client for external API
🔄 ReliableClient: GET to https://api.weather.com/forecast (with retries, timeout: 10s)
Response: {"message": "Reliable response from https://api.weather.com/forecast"}
Success: true

💳 Need to process payment...
🏭 Factory: Creating Secure client for payment service
🔐 SecureClient: Secure POST to https://api.stripe.com/payments (with encryption, certificates)
Response: {"message": "Secure POST successful", "encrypted_data": "[ENCRYPTED]"}
Success: true

=== CREATING CLIENTS BY URL PATTERN ===

🔍 Analyzing URL: http://internal-service:8080/data
🏭 Factory: URL suggests internal service - creating Fast client
🚀 FastClient: Quick GET to http://internal-service:8080/data (timeout: 1s)
   Client used: Fast
   Response: {"message": "Fast response from http://internal-service:8080/data"}
```

## The Benefits:

1. **Hide Complexity**: Don't need to know which client to create
2. **Easy to Extend**: Add new client types without changing existing code
3. **Centralized Logic**: All creation logic in one place
4. **Type Safety**: Factory ensures you get the right client for the job

## Kotlin-Specific Benefits:

- **When Expressions**: Clean way to decide which product to create
- **Enum Classes**: Type-safe service type definitions
- **Data Classes**: Simple response modeling
- **Extension Functions**: Could add `HttpResponse.isError()` easily

This connects perfectly to your API Gateway - you could use this exact pattern to create different HTTP clients based on which backend service you're calling!