package css_awesome

import org.ilaborie.logger.Logger
import org.ilaborie.slides.*
import org.ilaborie.slides.content.ExternalHtmlContent
import org.ilaborie.slides.content.ExternalResource
import org.ilaborie.slides.content.html
import org.ilaborie.slides.content.web.EditableZone
import org.ilaborie.slides.content.web.StyleEditable
import java.io.File


val browsersThreshold = 0.4

fun cssLiveCode(prefix: String) =
        StyleEditable(ExternalResource("$prefix.css"), ExternalResource("$prefix-final.css")) +
                EditableZone(ExternalHtmlContent(ExternalResource("$prefix.html")))

fun main(args: Array<String>) {
    val logger = Logger("CSS")

    val title = "C<span class=\"logo-askew\">S</span>S is awesome!".html()
    val cssIsAwesome = Presentation(title = title, id = "cssIsAwesome")
            .group("Introduction", skipPart = true) { intro(this) }
            .group("Utiliser un pré&#8209;processeur ?", "preprocessor") { preprocessor(this) }
            .group("Unités") { unites(this) }
            .group("Flexbox et Grid") { flexgrid(this) }
            .group("Pseudo éléments") { pseudoElt(this) }
            .group("Animations") { animations(this) }
            .group("Pseudo classes d'état", "pseudo_classes") { pseudoState(this) }
            .group("HTML") { html(this) }
            .group("Conclusion") { conclusion(this) }

    val slidesDir = File("src/test/resources/")

    if (cssIsAwesome.hasMissingExternals()) {
        logger.info { "Generate missing slides" }
        cssIsAwesome.generateMissingExternals(slidesDir)
    }

    val dist = File("src/main/web")
    buildAll(cssIsAwesome, dist, "devfest-tls")
    buildAll(cssIsAwesome, dist, "devoxx-ma")

    copyExtraFiles(slidesDir.resolve(cssIsAwesome.id), dist)
}

private fun copyExtraFiles(srcDir: File, destDir: File) {
    listOf("holy-grail.html", "holy-grail-calc.html", "holy-grail-flexbox.html", "holy-grail-grid.html")
            .map { it to srcDir.resolve(it) }
            .map { (fileName, srcDir) -> srcDir.copyTo(target = destDir.resolve(fileName), overwrite = true) }
}

private fun buildAll(cssIsAwesome: Presentation, dist: File, key: String) {
    cssIsAwesome.writeHtmlTo(dist, key)
    cssIsAwesome.writeMarkdownTo(dist, key)
    cssIsAwesome.writePdfTo(dist.resolve("$key.html"), dist.resolve("$key.pdf"))
}

