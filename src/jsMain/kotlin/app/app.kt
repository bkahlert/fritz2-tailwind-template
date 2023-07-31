package app

import dev.fritz2.core.alt
import dev.fritz2.core.render
import dev.fritz2.core.src
import dev.fritz2.core.storeOf
import model.Framework
import model.name

@JsModule("./images/logo.png")
@JsNonModule
private external val logo: String

fun main() {

    val frameworkStore = storeOf(Framework("fritz2"))
    val nameStore = frameworkStore.map(Framework.name())

    render {
        div("prose-box") {
            h1 { +"Which web-framework do you use?" }
            div("rounded-xl bg-default p-4") {
                div("text-default text-center") {
                    +"I'm using: "
                    nameStore.data.renderText()
                }
                img("animate-pulse [animation-iteration-count:3] h-12 mx-auto") {
                    src(logo)
                    alt("Fritz2 logo")
                }
            }
        }

        hr { }

        toastDemo()
    }
}
