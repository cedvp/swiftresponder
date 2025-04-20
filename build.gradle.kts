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

repositories {
    mavenCentral() // Or jcenter() if you're using it

    maven {
        name = "OSSRH"
        url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
        credentials {
            username = project.property("ossrhUsername") as? String ?: System.getenv("OSSRH_USERNAME")
            password = project.property("ossrhPassword") as? String ?: System.getenv("OSSRH_PASSWORD")
        }
    }
}

publishing {
    publications {



        create<MavenPublication>("mavenJava") {
            from(components["java"])


            // Add metadata for Maven Central
            groupId = "nl.bluetrails" // Replace with your groupId
            artifactId = "swiftresponder" // Replace with your artifactId
            version = "1.0.1" // Replace with your version

            pom {
                name.set("Swift Responder")
                description.set("Get Swift Mocked answers")
                url.set("https://github.com/cedvp/swiftresponder")

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/cedvp/SwiftResponder.git") // Replace
                    developerConnection.set("scm:git:ssh://git@github.com/cedvp/SwiftResponder.git") // Replace
                    url.set("https://github.com/cedvp/swiftresponder") // Replace
                }
                developers {
                    developer {
                        id.set("cedvp")
                        name.set("Cedric Van Pelt")
                        email.set("cedric@bluetrails.nl")
                    }
                }

        }
    }
        repositories {
            maven {
                name = "OSSRH"
                val snapshoturl = "https://central.sonatype.com/repository/maven-snapshots/"
                val releaseurl = "https://central.sonatype.org/service/local/staging/deploy/maven2/"
                url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshoturl else snapshoturl)
                credentials {

                    username = project.property("ossrhUsername") as? String ?: System.getenv("OSSRH_USERNAME")
                    password = project.property("ossrhPassword") as? String ?: System.getenv("OSSRH_PASSWORD")


                }
            }
        }
    }
}

println("user="+project.property("ossrhUsername") as? String ?: System.getenv("OSSRH_USERNAME"))

println("password="+project.property("ossrhPassword") as? String ?: System.getenv("OSSRH_PASSWORD"))
tasks.register<Zip>("zipJarsAndSha") {
    archiveFileName.set("jars-and-sha.zip")
    destinationDirectory.set(layout.buildDirectory.dir("distributions"))

    from(layout.buildDirectory.dir("libs")) {
        include("*.jar")
        include("*.jar.sha256")
        include("*.md5")
        include("*.sha1")
        include("*.sha512")
        include("*.asc")
        include("*.pom")
    }
}

signing {
    sign(publishing.publications["mavenJava"])
}


nexusPublishing {
    repositories {
        sonatype {  //only for users registered in Sonatype after 24 Feb 2021
            nexusUrl.set(uri("https://central.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://central.sonatype.com/repository/maven-snapshots/"))
            username = project.property("ossrhUsername") as? String ?: System.getenv("OSSRH_USERNAME")
            password = project.property("ossrhPassword") as? String ?: System.getenv("OSSRH_PASSWORD")
        }
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

        // Generate SHA1, SHA256, and SHA512 checksums for each file in the bundle directory
        bundleDir.listFiles()?.forEach { file ->
            if (!file.isDirectory && !file.name.endsWith(".asc")) {
                listOf("SHA-1", "SHA-256", "SHA-512").forEach { algorithm ->
                    val checksumFile = File(file.parentFile, "${file.name}.${algorithm.toLowerCase().replace("-", "")}")
                    ant.withGroovyBuilder {
                        "checksum"(
                            "file" to file,
                            "algorithm" to algorithm,
                            "property" to "checksumValue"
                        )
                    }
                    checksumFile.writeText(ant.properties["checksumValue"] as String)
                }
            }
        }

        println("Checksums generated for files in: ${bundleDir.absolutePath}")
    }
}

tasks.build {
    finalizedBy("bundleRelease")
}