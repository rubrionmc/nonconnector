plugins {
    java
    `maven-publish`
    alias(libs.plugins.shadow) apply false
    alias(libs.plugins.lombok)
}

group = "net.rubrion.server"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://leycm.github.io/repository/")
}

dependencies {
    implementation("org.slf4j:slf4j-simple:2.0.17")
    implementation("de.leycm.linguae:api:1.2.1")
    implementation("de.leycm.linguae:common:1.2.1") {
        exclude(group = "com.google.code.gson", module = "gson")
    }
    implementation("net.minestom:minestom:2026.01.08-1.21.11") {
        exclude(group = "com.google.code.gson", module = "gson")
    }
    implementation("com.google.code.gson:gson:2.13.2")
    implementation("net.kyori:adventure-text-minimessage:4.17.0")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

configurations.all {
    resolutionStrategy {
        force("com.google.code.gson:gson:2.13.2")
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    archiveBaseName.set("nonconnector")
    archiveVersion.set(version.toString())
    manifest {
        attributes["Main-Class"] = "net.rubrion.server.nonconnector.RubrionNonConnectorService"
    }

    from({
        configurations.runtimeClasspath.get()
            .filter { it.name.endsWith("jar") }
            .map { zipTree(it) }
    })

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}