package personal.learning.kotlin.patterns.creational.builder

// Supporting data class to the example
data class Subscription(val destination: Destination, val topic: Topic, val frequency: Frequency) {
    enum class Topic { NEWS, ANALYTICS, SECURITY_ALERTS }
    enum class Frequency { IMMEDIATELY, DAILY, WEEKLY }
}

// Supporting interface to the example
sealed interface Destination
@JvmInline value class EmailAddress(val value: String) : Destination
@JvmInline value class PhoneNumber(val value: String) : Destination


// Builder Pattern
// Purpose: Split up construction of an object into smaller steps

// PRODUCT: the object to be created with the builder
// Defined default values so we can specify properties in any order
data class NotificationSettings(val enabled: Boolean = false, val subscriptions: List<Subscription> = emptyList())

// CONCRETE BUILDER: class instantiated to build the product/object
// class that replaces constructor properties with class properties
// subscriptions declared private so you can only modify via addSubscription func
class NotificationSettingsBuilder : INotificationSettingsBuilder {
    override var enabled: Boolean = false
    private val subscriptions = mutableListOf<Subscription>()

    override fun addSubscription(dest: Destination, topic: Subscription.Topic, freq: Subscription.Frequency) {
        subscriptions.add(Subscription(dest, topic, freq))
    }

    // Alternatively to addSubscription, use Pair for readability (see notificationSettingsLambdaReceiver below)
    fun send(topicToDestination: Pair<Subscription.Topic, Destination>, frequency: Subscription.Frequency) {
        val (topic, destination) = topicToDestination
        subscriptions.add(Subscription(destination, topic, frequency))
    }

    override fun build(): NotificationSettings = NotificationSettings(enabled, subscriptions)

}

// BUILDER: the interface defining the operations you can perform when building the product/object
interface INotificationSettingsBuilder {
    var enabled: Boolean
    fun addSubscription(dest: Destination, topic: Subscription.Topic, freq: Subscription.Frequency)
    fun build(): NotificationSettings
}

// BEFORE using builder pattern:
fun createNotificationSettingsWithoutBuilder(email: EmailAddress?, phone: PhoneNumber?): NotificationSettings {
    return NotificationSettings(
        enabled = true,
        subscriptions = listOf(
            Subscription(EmailAddress("example@example.com"), Subscription.Topic.ANALYTICS, Subscription.Frequency.DAILY),
            Subscription(EmailAddress("example@example.com"), Subscription.Topic.ANALYTICS, Subscription.Frequency.DAILY),
            Subscription(PhoneNumber("0151-111-1111"), Subscription.Topic.SECURITY_ALERTS, Subscription.Frequency.IMMEDIATELY),
        )
    )
}

// AFTER using builder pattern:
// DIRECTOR: code (fun or class) that tells the builder what to do
fun createNotificationSettings(email: EmailAddress?, phone: PhoneNumber?) :  NotificationSettings {
    val builder: INotificationSettingsBuilder = NotificationSettingsBuilder()
    builder.enabled = true

    // can now use conditionals to add subscriptions only when values are present
    if(email != null) builder.addSubscription(email, Subscription.Topic.ANALYTICS, Subscription.Frequency.DAILY)
    if(email != null) builder.addSubscription(email, Subscription.Topic.NEWS, Subscription.Frequency.WEEKLY)
    if(phone != null) builder.addSubscription(phone, Subscription.Topic.SECURITY_ALERTS, Subscription.Frequency.IMMEDIATELY)
    if(phone != null) Subscription.Topic.entries.forEach { builder.addSubscription(phone, it, Subscription.Frequency.IMMEDIATELY) }

    return builder.build()
}

// Modernising the Builder pattern in Kotlin

// buildList scope function to build argument using multiple statements
fun createNotificationSettingsWithBuilder(email: EmailAddress?, phone: PhoneNumber?): NotificationSettings {
    return NotificationSettings(
        enabled = true,
        subscriptions = buildList {
            if (email != null) add(Subscription(email, Subscription.Topic.ANALYTICS, Subscription.Frequency.DAILY))
            if (email != null) add(Subscription(email, Subscription.Topic.ANALYTICS, Subscription.Frequency.DAILY))
            if (phone != null) add(Subscription(phone, Subscription.Topic.SECURITY_ALERTS, Subscription.Frequency.IMMEDIATELY))
        }
    )
}

// Lambda with receiver
fun notificationSettingsLambdaReceiver(block: NotificationSettingsBuilder.() -> Unit) = NotificationSettingsBuilder().apply(block).build()

fun createNotificationSettingsWithLambdaReceiver(email: EmailAddress?, phone: PhoneNumber?): NotificationSettings {
    val settings = notificationSettingsLambdaReceiver {
        enabled = true
        if (email != null) send(Subscription.Topic.ANALYTICS to email, Subscription.Frequency.DAILY)
        if (email != null) send(Subscription.Topic.ANALYTICS to email, Subscription.Frequency.DAILY)
        if (phone != null) send(Subscription.Topic.SECURITY_ALERTS to phone, Subscription.Frequency.IMMEDIATELY)
    }
    return settings
}


fun main() {
    createNotificationSettings(EmailAddress("example@example.com"), null)
}