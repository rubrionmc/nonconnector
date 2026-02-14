plugins {
    id("java")
}

group = "net.rubrion.server"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.minestom:minestom:2026.02.09-1.21.11")
    implementation("org.slf4j:slf4j-simple:2.0.17")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    archiveBaseName.set("server")
    archiveVersion.set("local")
    manifest {
        attributes["Main-Class"] = "net.rubrion.server.nonconnector.RubrionNonConnectorService"
    }

    // ich weiss nicht ob das gut ist das da 7 errors sind aber es funktioniert
    from({
        configurations.runtimeClasspath.get()
            .filter { it.name.endsWith("jar") }
            .map { zipTree(it) }
    })

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
