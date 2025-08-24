package personal.learning.designpatterns.behavioural.strategy

/**
 * Demo showing how easy it is to switch strategies
 */
fun main() {
    // Create servers
    val servers = listOf(
        Server("Server-1", "http://server1:8080"),
        Server("Server-2", "http://server2:8080"),
        Server("Server-3", "http://server3:8080")
    )

    // Start with Round Robin strategy
    val loadBalancer = LoadBalancer(RoundRobinStrategy())

    // Add servers
    servers.forEach { loadBalancer.addServer(it) }

    println("=== ROUND ROBIN STRATEGY ===")
    repeat(6) { i ->
        println(loadBalancer.routeRequest("Request-${i + 1}"))
    }

    println("\n=== SWITCHING TO RANDOM STRATEGY ===")
    loadBalancer.changeStrategy(RandomStrategy()) // Easy to switch!
    loadBalancer.resetCounters()

    repeat(6) { i ->
        println(loadBalancer.routeRequest("Request-${i + 1}"))
    }

    println("\n=== SWITCHING TO LEAST BUSY STRATEGY ===")
    loadBalancer.changeStrategy(LeastBusyStrategy()) // Switch again!
    loadBalancer.resetCounters()

    // Make some servers busier
    servers[0].activeRequests = 5
    servers[1].activeRequests = 2
    servers[2].activeRequests = 0
    println("Current server load:")
    println(loadBalancer.getServerStatus())

    println("\nRouting requests (should pick least busy):")
    repeat(3) { i ->
        println(loadBalancer.routeRequest("Request-${i + 1}"))
    }
}