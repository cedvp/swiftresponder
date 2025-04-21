import org.gradle.internal.impldep.org.eclipse.jgit.lib.ObjectChecker.type


plugins {

    id("java")
    id("java-library")
    id("maven-publish")
    id("signing")
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"

}


group = "nl.bluetrails" // Replace with your group ID
version = "1.0.1" // or whatever version you're using

tasks.register<Jar>("swiftresponder") {
    from(sourceSets.main.get().output)
    archiveBaseName.set("my-library")
    archiveClassifier.set("")
}


repositories {
    mavenCentral()
}



dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.concordion:concordion:4.0.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
    implementation("org.bouncycastle:bcpg-jdk15on:1.70")
}

tasks.test {
    useJUnitPlatform()
}

java {
    withSourcesJar()
    withJavadocJar()
    toolchain {
        languageVersion = JavaLanguageVersion.of(16) // Replace 11 with your desired version
    }
}




tasks.register<Zip>("bundleRelease") {
    archiveFileName.set("${project.name}-${project.version}-release-bundle.zip")
    destinationDirectory.set(layout.buildDirectory.dir("distributions"))

    val jarTask = tasks.named<Jar>("jar")
    val sourcesJarTask = tasks.named<Jar>("sourcesJar")
    val javadocJarTask = tasks.named<Jar>("javadocJar")
    val generatePomTask = tasks.named<GenerateMavenPom>("generatePomFileForMavenJavaPublication")
    val signingTasks = tasks.withType<Sign>()

    dependsOn(jarTask, sourcesJarTask, javadocJarTask, generatePomTask, signingTasks)

    into("${project.name}-${project.version}") {
        from(jarTask) { rename { "${project.name}-${project.version}.jar" } }
        from(sourcesJarTask) { rename { "${project.name}-${project.version}-sources.jar" } }
        from(javadocJarTask) { rename { "${project.name}-${project.version}-javadoc.jar" } }
        from(generatePomTask) { rename { "${project.name}-${project.version}.pom" } }

        signingTasks.forEach { signTask ->
            from(signTask.outputs.files)
        }
    }

    doLast {
        // Directory where files are prepared for zipping
        val bundleDir = file("${layout.buildDirectory.get()}/distributions/${project.name}-${project.version}")
        bundleDir.mkdirs()

        // Copy all files into the bundle directory
        copy {
            from(archiveFile)
            into(bundleDir)
        }

    }
}

tasks.build {
    finalizedBy("bundleRelease")
}