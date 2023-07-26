package app

import dev.fritz2.core.alt
import dev.fritz2.core.render
import dev.fritz2.core.src
import dev.fritz2.core.storeOf
import model.Framework
import model.name

@JsModule("./logo.png")
@JsNonModule
private external val logo: String

fun main() {

    val frameworkStore = storeOf(Framework("fritz2"))
    val nameStore = frameworkStore.map(Framework.name())

    render {
        // card
        div("max-w-2xl mx-auto sm:px-6 lg:px-8 py-12") {
            div("overflow-hidden shadow-md") {

                // card-header
                div("px-6 py-4 text-white bg-sky-700 border-b border-gray-200 font-bold uppercase") {
                    +"Which web-framework do you use?"
                }

                // card body
                div("p-6 bg-white") {
                    +"I'm using: "
                    nameStore.data.renderText()
                    img("animate-pulse [animation-iteration-count:3] h-12") {
                        src(logo)
                        alt("Fritz2 logo")
                    }
                }
            }
        }

        toastDemo()
    }
}
