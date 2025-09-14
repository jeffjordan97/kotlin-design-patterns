# Kotlin Design Patterns

A comprehensive collection of design pattern implementations in Kotlin, focusing on practical, real-world examples with modern Kotlin idioms and best practices.

## Project Overview

This project demonstrates various design patterns through practical examples, particularly in the context of API Gateway and service architecture scenarios. Each pattern includes both traditional implementations and modern Kotlin-specific approaches that leverage language features like type aliases, lambda expressions, and scope functions.

## Design Patterns Implemented

### Creational Patterns

#### Factory Method Pattern
- **Location**: `src/main/kotlin/patterns/creational/factory/`
- **Example**: HTTP Client Factory for different service types
- **Key Files**:
  - `HttpClient.kt` - Product interface
  - `HttpClientTypes.kt` - Concrete implementations (Fast, Reliable, Secure clients)
  - `HttpClientFactory.kt` - Factory for creating appropriate clients
  - `FactoryMethodDemo.kt` - Demonstration and usage examples

**Features**:
- Fast clients for internal services (low latency)
- Reliable clients for external APIs (with retries)
- Secure clients for payment services (with encryption)

#### Abstract Factory Pattern
- **Location**: `src/main/kotlin/patterns/creational/abstractfactory/`
- **Example**: Environment-specific component factories
- **Key Files**:
  - `ServiceComponents.kt` - Product interfaces (DataStorage, Logger, NotificationService)
  - `DevelopmentComponents.kt` - Development environment implementations
  - `ProductionComponents.kt` - Production environment implementations
  - `TestingComponents.kt` - Testing environment implementations
  - `ComponentFactory.kt` - Abstract factory interface
  - `ConcreteFactories.kt` - Environment-specific factories
  - `AbstractFactoryDemo.kt` - Complete demonstration

**Features**:
- Consistent component families for different environments
- Development: In-memory storage, console logging, mock notifications
- Production: Database storage, file logging, email notifications
- Testing: Temporary storage, silent logging, fake notifications

#### Builder Pattern
- **Location**: `src/main/kotlin/patterns/creational/builder/`
- **Example**: Notification Settings Builder
- **Key Files**:
  - `NotificationExample.kt` - Traditional and modern Kotlin builder implementations

**Features**:
- Traditional builder pattern implementation
- Modern Kotlin approaches using lambda receivers
- Support for conditional object construction

### Behavioral Patterns

#### Strategy Pattern
- **Location**: `src/main/kotlin/patterns/behavioural/strategy/`
- **Examples**: Form validation and load balancing strategies

##### Form Validation Example
- **Location**: `formexample/formexample.kt`
- **Features**:
  - Traditional strategy pattern implementation
  - Modern Kotlin version using type aliases and lambda functions
  - Extensible validator strategies with optional validation support

##### Load Balancing Example
- **Location**: `loadbalancingexample/`
- **Key Files**:
  - `LoadBalancingStrategy.kt` - Strategy interface and Server data class
  - `LoadBalancingStrategies.kt` - Multiple concrete strategies
  - `LoadBalancer.kt` - Context class that uses strategies
  - `StrategyPatternDemo.kt` - Comprehensive demonstration

**Strategies Implemented**:
- Round Robin: Distributes requests evenly across servers
- Random: Randomly selects servers
- Least Busy: Routes to server with fewest active requests
- Weighted Response Time: Considers server response times and load
- Weighted Round Robin: Round robin with server capacity weighting

### Language Features

#### Scope Functions
- **Location**: `src/main/kotlin/features/ScopeFunctions.kt`
- **Covers**: `let`, `also`, `apply`, `run` with practical examples

## Project Structure

```
src/main/kotlin/
├── features/
│   └── ScopeFunctions.kt          # Kotlin scope functions examples
├── patterns/
│   ├── behavioural/
│   │   └── strategy/
│   │       ├── formexample/       # Form validation strategy example
│   │       └── loadbalancingexample/ # Load balancing strategy example
│   └── creational/
│       ├── abstractfactory/       # Environment component factories
│       ├── builder/              # Notification builder pattern
│       └── factory/              # HTTP client factory pattern
└── Main.kt                       # Entry point
```

## Getting Started

### Prerequisites
- JDK 21 or higher
- Gradle (included via wrapper)

### Running the Examples

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd kotlin-design-patterns
   ```

2. **Run specific pattern demonstrations**:
   ```bash
   # Factory Method Pattern
   ./gradlew run -PmainClass=personal.learning.kotlin.patterns.creational.factory.httpclient.FactoryMethodDemo

   # Abstract Factory Pattern  
   ./gradlew run -PmainClass=personal.learning.kotlin.patterns.creational.abstractfactory.AbstractFactoryDemo

   # Strategy Pattern - Load Balancing
   ./gradlew run -PmainClass=patterns.behavioural.strategy.StrategyPatternDemo

   # Strategy Pattern - Form Validation
   ./gradlew run -PmainClass=personal.learning.kotlin.patterns.behavioural.strategy.formexample.FormExampleKt
   ```

3. **Build the project**:
   ```bash
   ./gradlew build
   ```

## Key Learning Points

### Pattern Benefits Demonstrated

1. **Flexibility**: Easy to add new implementations without modifying existing code
2. **Runtime Configuration**: Switch between strategies/implementations at runtime
3. **Testability**: Each component can be tested independently
4. **Separation of Concerns**: Clear separation between object creation and usage

### Kotlin-Specific Improvements

1. **Type Aliases**: Replace interfaces with function types where appropriate
2. **Lambda Expressions**: Simplify strategy implementations
3. **Data Classes**: Clean, immutable data modeling
4. **Scope Functions**: Modern object configuration patterns
5. **When Expressions**: Clean conditional logic for factories
6. **Extension Functions**: Add functionality to existing types

### Real-World Applications

- **API Gateway Architecture**: Different HTTP clients for different service types
- **Environment Configuration**: Consistent component setup across dev/staging/prod
- **Load Balancing**: Runtime switching between different distribution algorithms
- **Form Validation**: Composable validation logic with conditional rules

## Documentation

Each pattern includes:
- Detailed markdown documentation explaining the pattern
- Step-by-step implementation guide
- Expected output examples
- Benefits and use cases
- Kotlin-specific improvements over traditional implementations

## Contributing

Feel free to contribute additional patterns or improvements to existing implementations. Each contribution should include:
- Clear documentation and examples
- Demonstration of both traditional and Kotlin-idiomatic approaches
- Real-world use case scenarios

## License

This project is for educational purposes, demonstrating design pattern implementations in Kotlin.
