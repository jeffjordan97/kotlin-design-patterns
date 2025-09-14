package personal.learning.kotlin.patterns.creational.factory

// ABSTRACT FACTORY

class ElementFactoryKotlin(
    val createParagraph: (text: String) -> String,
    val createHeading: (level: Int, text: String) -> String,
    val createLink: (text: String, url: String) -> String
)

// CONCRETE FACTORIES

val HtmlElementFactoryKotlin = ElementFactoryKotlin(
    createParagraph = { text -> "<p>$text</p>" },
    createHeading = { level, text -> "<h$level>$text</h$level>"},
    createLink = { url, text -> "<a href=\"$url>\">$text</a>"}
)

val MarkdownElementFactoryKotlin = ElementFactoryKotlin(
    // FACTORY METHODS - passed as constructor args
    createParagraph = { text -> "$text\n"},
    createHeading = { level, text -> "${"#".repeat(level)} $text\n"},
    createLink = { url, text -> "[$text]($url)" }
)

class DocumentKotlin(private val factory: ElementFactoryKotlin) {
    private val elements = mutableListOf<String>()

    fun addParagraph(text: String) = elements.add(factory.createParagraph(text))
    fun addHeading(level: Int, text: String) = elements.add(factory.createHeading(level, text))
    fun addLink(text: String, url: String) = elements.add(factory.createLink(text, url))

    fun render() = elements.joinToString("\n")
}

fun main() {
    val doc = DocumentKotlin(HtmlElementFactoryKotlin)
    doc.addHeading(1, "Introduction")
    doc.addParagraph("This is the first paragraph.")
    doc.addLink("Read More Here", "https://jeffreyjordan.dev")
    println(doc.render())
}


