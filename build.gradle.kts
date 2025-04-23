import org.gradle.internal.impldep.org.eclipse.jgit.lib.ObjectChecker.type


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



plugins {
    java
}




