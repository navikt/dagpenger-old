plugins {
    kotlin("jvm")
    application
}

application {
    version = "0.0.1"
    applicationName = "joark-mottak"
    mainClassName = "no.nav.dagpenger.joark.mottak.JoarkMottak"
}

val kotlinLoggingVersion = "1.4.9"
val junitJupiterVersion = "5.2.0"
val junitPlatformVersion = "1.0.0-M3"

java {
    sourceCompatibility = JavaVersion.VERSION_1_10
    targetCompatibility = JavaVersion.VERSION_1_10
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":events"))
    implementation(project(":streams"))

    implementation("io.github.microutils:kotlin-logging:$kotlinLoggingVersion")
    implementation(group = "com.squareup.okhttp3", name = "okhttp", version = "3.11.0")

    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
    testImplementation("junit:junit:4.12")

    testImplementation(group = "au.com.dius", name = "pact-jvm-consumer-java8_2.12", version = "3.6.0-rc.0")

}
