package css_awesome

import org.ilaborie.slides.*
import org.ilaborie.slides.content.*
import java.io.File


fun main(args: Array<String>) {

    val prefix = "cssIsAwesome"
    val titleLeastPower = Link("The Rule of Least Power", "https://www.w3.org/2001/tag/doc/leastPower.html")
    val cssIsAwesome = Presentation(
            "CSS is Awesome !",
            Group("Introduction", skipPart = true, prefix = prefix)
                    .slide(title = Code(code = "$ whoami"), id = "whoami")
                    .slide(title = titleLeastPower, id = "least-power")
                    .slide(title = "Règles du jeu")
                    .slide(title = "Le CSS c'est vaste")
                    .slide(title = "Plan"),
            Group("Utiliser un pré-processeur ?", prefix = prefix)
                    .slide(title = "LiveCoding: boutons", contentType = "html", styleClass = setOf("hide-title"))
                    .slide(title = "Alors utilise-t-on un pré-processeurs ?"),
            Group("Unités", prefix = prefix)
                    .slide(title = "Une histoire d’unités CSS")
                    .slide(title = "Les unités de longueur") {
                        Definitions(
                                "px, cm, pt, ..." to "longueurs absolues (mesure physique)".html(),
                                "em, rem" to "fonction de la <code>font-size</code>".html(),
                                "ex, ch" to "hauteur d'un <code>x</code>, largeur d'un <code>0</code>".html(),
                                "vh, vw" to "(100vh, 100vw) = (hauteur, largeur) du <i>viewport</i>".html(),
                                "vmin, vmax" to "min(1vh, 1vw), max(1vh, 1vw)".raw())
                    }
                    .slide(title = "LiveCoding: Holy Grail Layout avec calc", styleClass = setOf("hide-title"))
                    .slide(title = "Bilan unités"),
            Group("Flexbox et Grid", prefix = prefix)
                    .slide(title = "LiveCoding: Holy Grail Layout avec flexbox", styleClass = setOf("hide-title"))
                    .slide(title = "LiveCoding: Holy Grail Layout avec grid", styleClass = setOf("hide-title"))
                    .slide(title = "Bilan Flexbox & Grid"),
            Group("Pseudo éléments", prefix = prefix)
                    .slide(title = "LiveCoding: le dinner d'un philosophe", contentType = "html", styleClass = setOf("hide-title"))
                    .slide(title = "LiveCoding: Triangle avec des bordures", contentType = "html", styleClass = setOf("hide-title"))
                    .slide(title = "LiveCoding: Info-bulle", contentType = "html", styleClass = setOf("hide-title"))
                    .slide(title = "Bilan pseudo éléments"),
            Group("Animations", prefix = prefix)
                    .slide(title = "LiveCoding: texte de chargement", contentType = "html", styleClass = setOf("hide-title"))
                    .slide(title = "Bilan animations"),
            Group("Pseudo classes d'état", prefix = prefix)
                    .slide(title = "Usage des info-bulles")
                    .slide(title = "LiveCoding: Checkbox", contentType = "html", styleClass = setOf("hide-title"))
                    .slide(title = "LiveCoding: Switch", contentType = "html", styleClass = setOf("hide-title"))
                    .slide(title = "LiveCoding: Collapsible panel", contentType = "html", styleClass = setOf("hide-title"))
                    .slide(title = "Principe pour les onglets")
                    .slide(title = "Démo des onglets")
                    .slide(title = "Bilan Pseudo classes"),
            Group("Compatibilité des navigateurs", prefix = prefix)
                    .slide(title = "Partie 1", styleClass = setOf("hide-title"))
                    .slide(title = "Partie 2", styleClass = setOf("hide-title")),
            Group("Conclusion", prefix = prefix)
                    .slide(title = "Bilan", styleClass = setOf("hide-title"))
                    .slide(title = "Traitez le CSS comme du code")
                    .slide(title = "Liens")
                    .slide(title = "Pour apprendre")
                    .slide(title = "🦄 rocks !", contentType = "html")
    )

    val slidesDir = File("src/test/resources/cssIsAwesome")
    cssIsAwesome.writeHtmlTo(File("src/main/web"), "index")

    if (cssIsAwesome.hasMissingExternals()) {
        cssIsAwesome.generateMissingExternals(slidesDir)
    }
}