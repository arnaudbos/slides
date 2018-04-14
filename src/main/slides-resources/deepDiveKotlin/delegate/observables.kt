import kotlin.properties.Delegates

fun main(args: Array<String>) {

    var obserbable: String by Delegates.observable("Initial value") {
        _, old, new ->
            println("$old -> $new")
    }

    obserbable = "new value"
}
