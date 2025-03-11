plugins {
    id("java")
        id("maven-publish")
             id("java-library")
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
            artifactId = "swift-responder" // Replace with your artifactId
            version = "1.0.0" // Replace with your version

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
                developers {
                    developer {
                        id.set("your-id")
                        name.set("Your Name")
                        email.set("your-email@example.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/username/repository.git")
                    developerConnection.set("scm:git:ssh://git@github.com/username/repository.git")
                    url.set("https://github.com/username/repository")
                }
            }
        }
    }

    repositories {
        maven {
            name = "OSSRH"
            url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = System.getenv("MAVEN_USERNAME") // OSSRH username
                password = System.getenv("MAVEN_PASSWORD") // OSSRH password
            }
        }
    }
}

signing {
    useInMemoryPgpKeys(
        System.getenv("GPG_PRIVATE_KEY"),
        System.getenv("GPG_PASSPHRASE")
    )
    sign(publishing.publications["mavenJava"])
}

