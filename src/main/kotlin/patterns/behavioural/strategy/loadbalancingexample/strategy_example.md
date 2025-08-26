# Strategy Pattern - Simple Load Balancing Example

## The Problem: Choosing Which Server to Send Requests To

Imagine you have 3 servers and need to decide which one to send each request to. You have different ways to choose:
- **Round Robin**: Server 1, then 2, then 3, then back to 1
- **Random**: Pick any server randomly
- **Least Busy**: Pick the server handling fewest requests

The Strategy pattern lets you switch between these methods easily!

---

### Without Strategy Pattern:
```kotlin
// Bad - hard to extend and test
class LoadBalancer {
    fun selectServer(servers: List<Server>, method: String): Server {
        return when (method) {
            "round-robin" -> /* round robin logic */
            "random" -> /* random logic */
            "least-busy" -> /* least busy logic */
            // Adding new method = modifying this class
        }
    }
}
```
### With Strategy Pattern:
```kotlin
// Good - easy to extend and test
class LoadBalancer(private var strategy: LoadBalancingStrategy) {
    fun selectServer(servers: List<Server>): Server {
        return strategy.selectServer(servers) // Just delegate!
    }

    fun changeStrategy(newStrategy: LoadBalancingStrategy) {
        this.strategy = newStrategy // Switch anytime!
    }
}
```

---

## Step 1: The Strategy Interface

Create `src/main/kotlin/patterns/behavioral/strategy/LoadBalancingStrategy.kt`:

```kotlin
package patterns.behavioral.strategy

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
    var activeRequests: Int = 0
)
```

---

## Step 2: Concrete Strategies (The Different Ways)

Create `src/main/kotlin/patterns/behavioral/strategy/LoadBalancingStrategies.kt`:

```kotlin
package patterns.behavioral.strategy

import kotlin.random.Random

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
 * Strategy 3: Least Busy - Pick server with fewest active requests
 */
class LeastBusyStrategy : LoadBalancingStrategy {
    override fun selectServer(servers: List<Server>): Server {
        if (servers.isEmpty()) throw IllegalArgumentException("No servers available")
        return servers.minByOrNull { it.activeRequests }!!
    }
}
```

---

## Step 3: The Context Class (Uses the Strategy)

Create `src/main/kotlin/patterns/behavioral/strategy/LoadBalancer.kt`:

```kotlin
package patterns.behavioral.strategy

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
    
    /**
     * Add a server to the load balancer
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
     * Reset request counters
     */
    fun resetCounters() {
        servers.forEach { it.activeRequests = 0 }
    }
}
```

---

## Step 4: Demo (How to Use It)

Create `src/main/kotlin/patterns/behavioral/strategy/StrategyPatternDemo.kt`:

```kotlin
package patterns.behavioral.strategy

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
```

---

## Why This is the Strategy Pattern

### The Key Components:

1. **Strategy Interface** (`LoadBalancingStrategy`)
    - Defines what every strategy must do
    - Just one method: `selectServer()`

2. **Concrete Strategies** (`RoundRobinStrategy`, `RandomStrategy`, `LeastBusyStrategy`)
    - Different ways to do the same thing
    - Each implements the interface differently

3. **Context** (`LoadBalancer`)
    - Uses a strategy but doesn't know which one
    - Can change strategy at runtime

### The Magic:
```kotlin
// You can switch strategies anytime!
loadBalancer.changeStrategy(RandomStrategy())      // Now it's random
loadBalancer.changeStrategy(RoundRobinStrategy())  // Now it's round robin
loadBalancer.changeStrategy(LeastBusyStrategy())   // Now it's least busy
```

The `LoadBalancer` doesn't care HOW servers are selected, it just says "Hey strategy, pick a server!" and the strategy does its job.

---

## Expected Output:
```
=== ROUND ROBIN STRATEGY ===
Request 'Request-1' routed to Server-1 (http://server1:8080)
Request 'Request-2' routed to Server-2 (http://server2:8080)
Request 'Request-3' routed to Server-3 (http://server3:8080)
Request 'Request-4' routed to Server-1 (http://server1:8080)
Request 'Request-5' routed to Server-2 (http://server2:8080)
Request 'Request-6' routed to Server-3 (http://server3:8080)

=== SWITCHING TO RANDOM STRATEGY ===
Load balancing strategy changed to: RandomStrategy
Request 'Request-1' routed to Server-2 (http://server2:8080)
Request 'Request-2' routed to Server-1 (http://server1:8080)
Request 'Request-3' routed to Server-3 (http://server3:8080)
Request 'Request-4' routed to Server-1 (http://server1:8080)
Request 'Request-5' routed to Server-3 (http://server3:8080)
Request 'Request-6' routed to Server-2 (http://server2:8080)

=== SWITCHING TO LEAST BUSY STRATEGY ===
Load balancing strategy changed to: LeastBusyStrategy
Current server load:
Server-1: 5 active requests
Server-2: 2 active requests
Server-3: 0 active requests

Routing requests (should pick least busy):
Request 'Request-1' routed to Server-3 (http://server3:8080)
Request 'Request-2' routed to Server-3 (http://server3:8080)
Request 'Request-3' routed to Server-2 (http://server2:8080)
```

## The Benefits:
1. **Easy to add new strategies** - just implement the interface
2. **Easy to switch strategies** - one line of code
3. **No if/else chains** - each strategy knows what to do
4. **Testable** - test each strategy separately

This is exactly how your API Gateway could switch between different load balancing methods at runtime!