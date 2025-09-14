package personal.learning.kotlin.patterns.creational.factory


// AFTER - using the Factory method pattern

// If we want Document to include a heading, link etc. we need to...
// 1. create abstract/interface product
// 2. create concrete product
// 3. update creator - to add in the
// 4. update concrete creator

// Products - interfaces/abstract classes of the things being created

interface Element {
    fun render(): String
}
abstract class Paragraph(protected val text: String): Element

abstract class Heading(protected val level: Int, protected val text: String) : Element {
    init { require(level in 1..6) {"Heading level must be between 1 and 6"} }
}

abstract class Link(protected val text: String, protected val url: String) : Element

// Concrete Products - dedicated types for the products

class HtmlParagraph(text: String) : Paragraph(text) {
    override fun render(): String = "<p>$text</p>"
}

class MarkdownParagraph(text: String) : Paragraph(text) {
    override fun render(): String = "$text\n"
}

class HtmlHeading(level: Int, text: String) : Heading(level, text) {
    override fun render(): String = "<h$level>$text</h$level>"
}

class MarkdownHeading(level: Int, text: String) : Heading(level, text) {
    override fun render(): String = "${"#".repeat(level)} $text\n"
}

class HtmlLink(text: String, url: String) : Link(text, url) {
    override fun render(): String = "<a href=\"$url>\">$text</a>"
}

class MarkdownLink(text: String, url: String) : Link(text, url) {
    override fun render(): String = "[$text]($url)"
}

// Abstract Creator
abstract class DocumentFactoryMethod {
    private val elements = mutableListOf<Element>()

    // FACTORY METHODS - declared in abstract creator, and implemented in concrete creators
    protected abstract fun createParagraph(text: String): Paragraph
    protected abstract fun createHeading(level: Int, text: String): Heading
    protected abstract fun createLink(text: String, url: String): Link

    fun addParagraph(text: String) = elements.add(createParagraph(text))
    fun addHeading(level: Int, text: String) = elements.add(createHeading(level, text))
    fun addLink(text: String, url: String) = elements.add(createLink(text, url))

    fun render() = elements.joinToString("\n") { it.render() }
}

// Concrete Creators - classes that are the factories that produce these objects

class HtmlDocument : DocumentFactoryMethod() {
    override fun createParagraph(text: String) = HtmlParagraph(text)
    override fun createHeading(level: Int, text: String): Heading = HtmlHeading(level, text)
    override fun createLink(text: String, url: String): Link = HtmlLink(text, url)
}

class MarkdownDocument : DocumentFactoryMethod() {
    override fun createParagraph(text: String) = MarkdownParagraph(text)
    override fun createHeading(level: Int, text: String): Heading = MarkdownHeading(level, text)
    override fun createLink(text: String, url: String): Link = MarkdownLink(text, url)
}

fun mainWithFactoryMethod() {
    val doc = HtmlDocument()
    doc.addHeading(1, "Introduction")
    doc.addParagraph("This is the first paragraph.")
    doc.addLink("Read More Here", "https://jeffreyjordan.dev")
    println(doc.render())
}




// ABSTRACT FACTORY

// Instead of using inheritance for the Concrete Creators, use composition
// Creating elements is now the responsibility of the factory objects

interface ElementFactory {
    fun createParagraph(text: String): Paragraph
    fun createHeading(level: Int, text: String): Heading
    fun createLink(text: String, url: String): Link
}

// DocumentFactoryMethod class (Document below) no longer needs to be abstract or open, we can pass the factory into its constructor
// Document class doesn't require any inheritance

// Client - code that uses the factory

class Document(private val factory: ElementFactory) {
    private val elements = mutableListOf<Element>()

    fun addParagraph(text: String) = elements.add(factory.createParagraph(text))
    fun addHeading(level: Int, text: String) = elements.add(factory.createHeading(level, text))
    fun addLink(text: String, url: String) = elements.add(factory.createLink(text, url))

    fun render() = elements.joinToString("\n") { it.render() }
}

// Concrete Factories

class HtmlElementFactory : ElementFactory {
    override fun createParagraph(text: String): Paragraph = HtmlParagraph(text)
    override fun createHeading(level: Int, text: String): Heading = HtmlHeading(level, text)
    override fun createLink(text: String, url: String): Link = HtmlLink(text, url)
}

class MarkdownElementFactory : ElementFactory {
    override fun createParagraph(text: String): Paragraph = MarkdownParagraph(text)
    override fun createHeading(level: Int, text: String): Heading = MarkdownHeading(level, text)
    override fun createLink(text: String, url: String): Link = MarkdownLink(text, url)
}

fun main() {
    val doc = Document(HtmlElementFactory())
    doc.addHeading(1, "Introduction")
    doc.addParagraph("This is the first paragraph.")
    doc.addLink("Read More Here", "https://jeffreyjordan.dev")
    println(doc.render())
}


