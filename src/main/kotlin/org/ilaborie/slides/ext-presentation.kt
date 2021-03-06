package org.ilaborie.slides

import mu.KotlinLogging
import org.ilaborie.slides.ContentType.HTML
import org.ilaborie.slides.ContentType.INTERNAL
import org.ilaborie.slides.ContentType.MARKDOWN
import org.ilaborie.slides.content.Content
import org.ilaborie.slides.content.External
import org.ilaborie.slides.content.ExternalHtmlContent
import org.ilaborie.slides.content.ExternalMarkdownContent
import org.ilaborie.slides.content.ExternalResource
import org.ilaborie.slides.content.HtmlContent
import org.ilaborie.slides.content.Link
import org.ilaborie.slides.content.OrderedList
import org.ilaborie.slides.content.createClient
import org.ilaborie.slides.content.renderAsHtml
import org.ilaborie.slides.content.renderAsMarkdown
import org.ilaborie.slides.content.renderAsString
import org.ilaborie.slides.content.toExternal
import java.io.File
import java.nio.charset.Charset

val logger = KotlinLogging.logger("PresExt")

fun <T> safe(dangerous: () -> T): T =
    try {
        dangerous()
    } catch (e: Throwable) {
        logger.error(e) { "Oops" }
        throw RuntimeException(e)
    }

fun <T> catchWithDefault(default: T, dangerous: () -> T): T =
    try {
        dangerous()
    } catch (e: Throwable) {
        logger.warn(e) { "Use default" }
        default
    }

// Outputs

fun Presentation.writeHtmlTo(folder: File, key: String = "index", charset: Charset = Charsets.UTF_8) {
    val file = folder.resolve("$key.html")
    logger.debug { "Write '${this.title.renderAsString()}' to $file" }
    file.writeText(renderAsHtml(key), charset)
}

fun htmlToPdf(from: File, to: File) {
    val file = from.absolutePath
    val helperClient = createClient("http://localhost:5000/")
    helperClient.pdf("file://$file", to.absolutePath)
}

fun Presentation.writeMarkdownTo(folder: File, key: String = "index", charset: Charset = Charsets.UTF_8) {
    val file = folder.resolve("$key.md")
    logger.debug { "Write '${this.title.renderAsString()}' to $file" }
    file.writeText(renderAsMarkdown(), charset)
}

fun Presentation.buildAll(dist: File, key: String,
                          exportMarkdown: Boolean = false,
                          exportPdf: Boolean = false) {
    val output = dist.resolve(this.id)
    output.mkdirs()
    this.writeHtmlTo(output, key)
    if (exportMarkdown) {
        this.writeMarkdownTo(output, key)
    }
    if (exportPdf) {
        htmlToPdf(output.resolve("$key.html"), output.resolve("$key.pdf"))
    }
}

// Externals
private fun Presentation.getExternals() = slides
    .flatMap { slides -> slides.toList().map { slides to it } }
    .filterNot { (_, slide) -> slide.contentType() == INTERNAL }
    .flatMap { (parent, slide) ->
        val content = slide.content { this.defaultContent(parent, slide) }
        content.toExternal()
    }

fun Presentation.hasMissingExternals(): Boolean {
    val externals = this.getExternals()
    val result = externals.any { !it.exists() }
    logger.trace { "'${this.title.renderAsString()}' hasMissingExternals? $result" }
    return result
}

// Default content
fun Presentation.defaultContent(parent: Slides, slide: Slide): Content =
    when (slide) {
        is RoadMapSlide -> OrderedList(
            slides.filterIsInstance<Group>()
                .filterNot { it.skipPart }
                .map { it.title to "part_${it.title.normalize()}" }
                .map { (title, key) -> Link(content = HtmlContent(title), link = "#$key") })
        else            -> {
            val contentType = slide.contentType().toFileExtension()
            val resource = (listOf(this.id) +
                    when (parent) {
                        is Group ->
                            listOf(
                                "${this(parent).format("00")}_${parent.id}",
                                "${parent(slide).format("00")}_${slide.id()}$contentType"
                            )
                        else     ->
                            listOf("${this(slide).format("00")}_${slide.id()}$contentType")
                    }).joinToString(prefix = "/", separator = "/")

            logger.trace { "Default content for $resource" }

            when (slide.contentType()) {
                HTML     -> ExternalHtmlContent(ExternalResource(resource))
                MARKDOWN -> ExternalMarkdownContent(ExternalResource(resource))
                else     -> TODO()
            }
        }
    }


fun Presentation.generateMissingExternals(folder: File = File("")): List<External> {
    val result = this.getExternals().filterNot { it.exists() }

    result.forEach { external ->
        logger.info { "Create $external" }
        external.create(folder)
    }
    return result
}

