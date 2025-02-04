import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnLockMismatchReport
import org.jetbrains.kotlin.gradle.targets.js.yarn.yarn

plugins {
    kotlin("multiplatform") version "1.8.22"
    id("com.google.devtools.ksp") version "1.8.22-1.0.11" // KSP support
}


repositories {
    mavenCentral()
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/") // new repository here
}

val fritz2Version = "1.0-RC6"

//group = "my.fritz2.app"
//version = "0.0.1-SNAPSHOT"

kotlin {
    jvm()
    js(IR) {
        browser {
            commonWebpackConfig(Action<KotlinWebpackConfig> {
                devServer = devServer?.copy(open = false)
            })
        }
        yarn.apply {
            ignoreScripts = false // suppress "warning Ignored scripts due to flag." warning
            yarnLockMismatchReport = YarnLockMismatchReport.NONE
            reportNewYarnLock = true // true
            yarnLockAutoReplace = true // true
        }
    }.binaries.executable()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("dev.fritz2:core:$fritz2Version")
                implementation("dev.fritz2:headless:$fritz2Version") // optional
            }
        }
        val jvmMain by getting {
            dependencies {
            }
        }
        val jsMain by getting {
            dependencies {
                // tailwind
                implementation(npm("tailwindcss", "^3.3.3")) { because("low-level CSS classes") }

                // optional tailwind plugins
                implementation(devNpm("@tailwindcss/typography", "^0.5")) { because("prose classes to format arbitrary text") }
                // implementation(devNpm("@tailwindcss/forms", "^0.5")) { because("form-specific tailwind features") }
                implementation(devNpm("tailwind-heropatterns", "^0.0.8")) { because("hero-pattern like striped backgrounds") }

                // webpack
                implementation(devNpm("postcss", "^8.4.17")) { because("CSS post transformation, e.g. auto-prefixing") }
                implementation(devNpm("postcss-loader", "^7.0.1")) { because("Loader to process CSS with PostCSS") }
                implementation(devNpm("postcss-import", "^15.1")) { because("@import support") }
                implementation(devNpm("autoprefixer", "10.4.12")) { because("auto-prefixing by PostCSS") }
                implementation(devNpm("css-loader", "6.7.1"))
                implementation(devNpm("style-loader", "3.3.1"))
                implementation(devNpm("cssnano", "5.1.13")) { because("CSS minification by PostCSS") }
            }
        }
    }
}

/**
 * KSP support - start
 */
// FIXME: Broken with upgrade to Kotlin 1.9.0
dependencies {
    add("kspCommonMainMetadata", "dev.fritz2:lenses-annotation-processor:$fritz2Version")
}
kotlin.sourceSets.commonMain { kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin") }

tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>>().all {
    if (name != "kspCommonMainKotlinMetadata") dependsOn("kspCommonMainKotlinMetadata")
}
/**
 * KSP support - end
 */
