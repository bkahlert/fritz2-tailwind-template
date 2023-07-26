package app

import dev.fritz2.core.RenderContext
import dev.fritz2.core.Tag
import dev.fritz2.core.afterMount
import dev.fritz2.headless.components.toast
import dev.fritz2.headless.components.toastContainer
import kotlinx.coroutines.delay
import org.w3c.dom.HTMLLIElement
import kotlin.time.Duration.Companion.seconds

private const val toastContainerDefault = "toast-container-default"
private const val containerImportant = "toast-container-important"

fun RenderContext.toastDemo() {
    div {
        toastContainer(
            toastContainerDefault,
            "absolute top-5 right-5 z-10 flex flex-col gap-2 items-start",
            id = toastContainerDefault
        )

        toastContainer(
            containerImportant,
            "absolute top-5 left-1/2 -translate-x-1/2 z-10 flex flex-col gap-2 items-center",
            id = containerImportant
        )
    }

    div("container mx-auto flex flex-col gap-6 border-2 border-sky-700 rounded p-4") {
        h2("text-lg font-semibold") {
            +"Toast demo"
        }
        div("grid grid-cols-1 md:grid-cols-2 gap-4 max-w-1/2 max-h-1/2") {
            button(
                """flex justify-center items-center px-4 py-2.5
                    | rounded shadow-sm
                    | border border-transparent
                    | text-sm font-sans text-white
                    | bg-gradient-to-r from-sky-700 to-violet-500
                    | hover:bg-gradient-to-r hover:from-sky-600 hover:to-violet-400
                    | focus:outline-none focus:ring-4""".trimMargin(),
                id = "btn-toast-default"
            ) {
                +"Create regular toast"

                clicks handledBy {
                    showToast(toastContainerDefault) {
                        +"Regular toast"
                        className("bg-gray-500/10 shadow-xl")
                    }
                }
            }

            button(
                """flex justify-center items-center px-4 py-2.5
                    | rounded shadow-sm
                    | border border-transparent
                    | text-sm font-sans text-white
                    | bg-gradient-to-r from-fuchsia-700 to-rose-500
                    | hover:bg-gradient-to-r hover:from-fuchsia-600 hover:to-rose-400
                    | focus:outline-none focus:ring-4""".trimMargin(),
                id = "btn-toast-important"
            ) {
                +"Create important toast"

                clicks handledBy {
                    showToast(containerImportant) {
                        className("bg-gradient-to-r from-fuchsia-700 to-rose-500 shadow-xl")
                        div("flex flex-row items-center gap-4 mr-2 text-white") {
                            span { +"⚠️" }
                            div {
                                div("font-semibold") { +"Important" }
                                div { +"This toast is rendered in another container" }
                            }
                        }
                    }
                }
            }
        }
    }

    span {
        afterMount { _, _ ->
            listOf(
                "from-slate-700",
                "from-red-700",
                "from-orange-700",
                "from-amber-700",
                "from-yellow-700",
                "from-lime-700",
                "from-green-700",
                "from-emerald-700",
                "from-teal-700",
                "from-cyan-700",
                "from-sky-700",
                "from-blue-700",
                "from-indigo-700",
                "from-violet-700",
                "from-purple-700",
                "from-fuchsia-700",
                "from-pink-700",
                "from-rose-700",
            ).forEach { fromColor ->
                delay(0.2.seconds)
                showToast(toastContainerDefault) {
                    className("bg-gradient-to-r $fromColor to-transparent shadow-xl")
                    +"A ${fromColor.split('-')[1]}-colored toast to show some activity"
                }
            }
        }
    }
}


private var toastCount = 0
private fun nextToastId() = "toast-${toastCount++}"

private fun showToast(container: String, initialize: Tag<HTMLLIElement>.() -> Unit) {
    toast(
        container,
        duration = 6000L,
        """flex flex-row flex-shrink-0 gap-2 justify-center
            | w-max px-4 py-2.5
            | rounded-xl shadow-sm
            | border-none
            | text-sm font-sans
        """.trimMargin(),
        nextToastId()
    ) {
        initialize()

        // FIXME: Werden Toast schnell hintereinander geöffnet, werden einige von ihnen bim
        //  Schließen zwar aus dem ToastStore entfernt, bleiben jedoch als DOM-Leiche übrig.
        //  Dies könnte an der Kombination aus renderEach und transition liegen; evtl. kann
        //  ein Element nicht aus dem DOM entfernt werden, solange es animiert wird?
        //  Vgl.: https://github.com/jwstegemann/fritz2/issues/714
        /*transition(
            enter = "transition-all duration-200 ease-in-out",
            enterStart = "opacity-0",
            enterEnd = "opacity-100",
            leave = "transition-all duration-200 ease-in-out",
            leaveStart = "opacity-100",
            leaveEnd = "opacity-0"
        )*/

        button("font-bold") {
            +"✖"
//            icon(
//                classes = "w-4 h-4 text-primary-900",
//                content = HeroIcons.x
//            )
            clicks handledBy close
        }
    }
}
