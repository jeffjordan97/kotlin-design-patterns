package patterns.behavioural.strategy

/**
 * LoadBalancer - The Context Class
 *
 * This class USES a strategy but doesn't know which one.
 * You can change the strategy at runtime!
 */
class LoadBalancer(
    private var strategy: LoadBalancingStrategy // This is the key!
) {
    private val servers = mutableListOf<Server>()

    /** Add a server to the load balancer
    */
    fun addServer(server: Server) {
        servers.add(server)
    }

    /**
     * Route a request - uses whatever strategy is currently set
     */
    fun routeRequest(requestName: String): String {
        if (servers.isEmpty()) return "No servers available"

        val selectedServer = strategy.selectServer(servers) // Delegate to strategy!
        selectedServer.activeRequests++

        return "Request '$requestName' routed to ${selectedServer.name} (${selectedServer.url})"
    }

    /**
     * Change the strategy at runtime - this is the power of Strategy pattern!
     */
    fun changeStrategy(newStrategy: LoadBalancingStrategy) {
        this.strategy = newStrategy
        println("Load balancing strategy changed to: ${newStrategy::class.simpleName}")
    }

    /**
     * Get current server status
     */
    fun getServerStatus(): String {
        return servers.joinToString("\n") {
            "${it.name}: ${it.activeRequests} active requests"
        }
    }

    /**
     * Get current server weighted response times
     */
    fun getServerWeightedResponseTime(): String {
        return servers.joinToString("\n") {
            "${it.name}: ${it.activeRequests} active requests, ${it.averageResponseTimeMs * it.activeRequests} weighted response time"
        }
    }

    /**
     * Get current server processing power
     */
    fun getServerProcessingPower(): String {
        return servers.joinToString("\n") {
            "${it.name}: ${it.processingPower}GHz processing power"
        }
    }

    /**
     * Reset request counters
     */
    fun resetCounters() {
        servers.forEach { it.activeRequests = 0 }
    }

    /**
     * Reset request average response times
     */
    fun resetAverageResponseTimes() {
        servers.forEach { it.averageResponseTimeMs = 0.0 }
    }

}