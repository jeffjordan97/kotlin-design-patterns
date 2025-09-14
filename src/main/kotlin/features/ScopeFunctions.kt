package personal.learning.kotlin.features

// SCOPE FUNCTIONS
private var number: Int? = null
private var number2: Int = 0
private var numberString: String? = ""

//let: Check nulls, returns last line, better than simple null check in multi-threading case
val l = number?.let {
    val number2 = it + 1
    number2
}

//also: same as 'let'  but it doesn't return the last line as 'let', instead 'also'  will return the object it was called on and 'not the last line!'
val al = number?.also {
    number2++
}

//apply: helpful function to modify objects, if you want to change in properties of the objects, and it uses 'this' instead of 'it' as we work inside the class of the object
val ap = number.apply {
    numberString = toString()
}

//run: equivalent to 'apply', but it won't return the object it was called, instead it will return the last line
val rn = number?.run {
    numberString = toString()
    numberString
}