package patterns.behavioural.strategy

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

/**
 * Strategy 4 (NEW): Weighted Response Time - Average response time of each server,
 * and combines that with the number of connections each server has open to determine
 * where to send traffic. By sending traffic to the servers with the quickest response time,
 * the algorithm ensures faster service for users.
 */
class WeightedResponseTimeStrategy : LoadBalancingStrategy {
    override fun selectServer(servers: List<Server>): Server {
        if (servers.isEmpty()) throw IllegalArgumentException("No servers available")
        return servers.minByOrNull { it.activeRequests * it.averageResponseTimeMs }!!
    }
}

/**
 * Strategy 5 (NEW): Weighted Round Robin - Take turns picking servers,
 * higher server weight indicates greater processing power or capacity.
 */
class WeightedRoundRobinStrategy : LoadBalancingStrategy {
    private var currentIndex = 0

    override fun selectServer(servers: List<Server>): Server {
        if (servers.isEmpty()) throw IllegalArgumentException("No servers available")

        val sortedServers = servers.sortedByDescending { it.processingPower }
        val server = sortedServers[currentIndex]
        currentIndex = (currentIndex + 1) % servers.size // Go back to 0 after last server
        return server
    }
}