package personal.learning.kotlin.patterns.creational.factory

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