package patterns.behavioural.strategy

/**
 * Strategy Pattern - The Strategy Interface
 *
 * This defines WHAT every load balancing strategy must do:
 * - Pick a server from a list of servers
 */
interface LoadBalancingStrategy {
    fun selectServer(servers: List<Server>): Server
}

/**
 * Simple data class for a server
 */
data class Server(
    val name: String,
    val url: String,
    var activeRequests: Int = 0,
    var averageResponseTimeMs: Double = 0.0,
    var processingPower: Double = 0.0
)