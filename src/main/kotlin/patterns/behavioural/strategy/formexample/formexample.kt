package personal.learning.kotlin.patterns.behavioural.strategy.formexample

// --- Without the Strategy Pattern ---

interface FormField {
    val name: String
    val value: String
    fun isValid(): Boolean
}

class EmailField(override val value: String) : FormField {
    override val name = "email"
    override fun isValid(): Boolean {
        return value.contains("@") && value.contains(".")
    }
}

class OptionalEmailField(override val value: String = "") : FormField {
    override val name = "email"
    override fun isValid(): Boolean {
        return value.isEmpty() || value.contains("@") && value.contains(".")
    }
}

class UsernameField(override val value: String) : FormField {
    override val name = "username"
    override fun isValid(): Boolean {
        return value.isNotEmpty()
    }
}

class PasswordField(override val value: String) : FormField {
    override val name = "password"
    override fun isValid(): Boolean {
        return value.length >= 8
    }
}

// Strategy Pattern GOAL: encapsulate what varies / extract what differs
// What differs between the classes?
// - value of the name field
// - impl of isValid function (the validation logic)

// --- Solution ---

// STRATEGY
interface Validator {
    fun isValid(value: String) : Boolean
}

// CONCRETE STRATEGY
class EmailValidator: Validator {
    override fun isValid(value: String): Boolean = value.contains("@") && value.contains(".")
}
// CONCRETE STRATEGY
class UsernameValidator: Validator {
    override fun isValid(value: String): Boolean = value.isNotEmpty()
}
// CONCRETE STRATEGY
class PasswordValidator: Validator {
    override fun isValid(value: String): Boolean = value.length >= 8
}

// CONTEXT
class FormFieldValidator(val name: String, val value: String, private val validator: Validator) {
    fun isValid(): Boolean {
        return name.isNotEmpty() && validator.isValid(value)
    }
}

// --- Modernized Solution in Kotlin ---

// replaced interface with typealias, so anything that accepts a string and returns a boolean fits the definition of a validator
typealias ValidatorStrategy = (String) -> Boolean

// Replaced classes with variables that are assigned lambda functions
val emailValidatorStrategy: ValidatorStrategy = { it.contains("@") && it.contains(".") }
val usernameValidatorStrategy: ValidatorStrategy = { it.isNotEmpty() }
val passwordValidator: ValidatorStrategy = { it.length >= 8 }

class FormFieldValidatorKotlin(val name: String, val value: String, private val validator: ValidatorStrategy) {
    fun isValid() = name.isNotEmpty() && validator(value)
}

// add a function that wraps the existing validator strategy to allow for optional values
fun ValidatorStrategy.optional(): ValidatorStrategy = { it.isEmpty() || this(it) }

fun main() {
    val emailField = FormFieldValidatorKotlin("email", "jeffrey.jordan@dizplai.com", emailValidatorStrategy.optional())
    println("Email validation: ${emailField.isValid()}")

    val usernameField = FormFieldValidatorKotlin("username", "jeff123", usernameValidatorStrategy)
    println("Username validation: ${usernameField.isValid()}")

    val passwordField = FormFieldValidatorKotlin("password", "password123", passwordValidator)
    println("Password validation: ${passwordField.isValid()}")
}