package personal.learning.designpatterns.behavioural.strategy

/**
 * Strategy 1: Round Robin - Take turns picking servers
 */
class RoundRobinStrategy : LoadBalancingStrategy {
    private var currentIndex = 0

    override fun selectServer(servers: List<Server>): Server {
        if (servers.isEmpty()) throw IllegalArgumentException("No servers available")

        val server = servers[currentIndex]
        currentIndex = (currentIndex + 1) % servers.size // Go back to 0 after last server
        return server
    }
}

/**
 * Strategy 2: Random - Pick any server randomly
 */
class RandomStrategy : LoadBalancingStrategy {
    override fun selectServer(servers: List<Server>): Server {
        if (servers.isEmpty()) throw IllegalArgumentException("No servers available")
        return servers.random()
    }
}

/**
 * Strategy 3: Least Busy - Pick server with the fewest active requests
 */
class LeastBusyStrategy : LoadBalancingStrategy {
    override fun selectServer(servers: List<Server>): Server {
        if (servers.isEmpty()) throw IllegalArgumentException("No servers available")
        return servers.minByOrNull { it.activeRequests }!!
    }
}