plugins {
    kotlin("jvm") version "1.8.10"
    id("org.jetbrains.compose") version "1.3.1"
}

group = "ml.dev.kotlin"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.0-Beta")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-rx3:1.7.0-Beta")
    implementation("io.ktor:ktor-client-core:2.2.4")
    implementation("io.ktor:ktor-client-cio:2.2.4")
    implementation("io.reactivex.rxjava3:rxkotlin:3.0.1")
    implementation(compose.desktop.currentOs)
}

compose.desktop {
    application {
        mainClass = "MainKt"
    }
}
