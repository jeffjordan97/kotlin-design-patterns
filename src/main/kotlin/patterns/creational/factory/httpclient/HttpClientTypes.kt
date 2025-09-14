package personal.learning.kotlin.patterns.creational.factory.httpclient

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