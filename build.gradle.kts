

plugins {
    id("java")
    id("java-library")
    id("maven-publish")
    id("signing")
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
    toolchain {
        languageVersion = JavaLanguageVersion.of(16) // Replace 11 with your desired version
    }
}


publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            // Add metadata for Maven Central
            groupId = "nl.bluetrails" // Replace with your groupId
            artifactId = "swiftresponder" // Replace with your artifactId
            version = "1.0.0-SNAPSHOT" // Replace with your version

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
    }

    repositories {
        maven {
            name = "OSSRH"
            val releasesRepoUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
            val snapshotsRepoUrl = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
            url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
            credentials {
                username = project.findProperty("mavenUsername") as String? ?: System.getenv("MAVEN_USERNAME")
                password = project.findProperty("mavenPassword") as String? ?: System.getenv("MAVEN_PASSWORD")            }
        }
    }
}





signing {
    useGpgCmd()
    sign(publishing.publications["mavenJava"])
}
