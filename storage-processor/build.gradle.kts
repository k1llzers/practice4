plugins {
    id("java")
    `java-library`
}

group = "org.ukma"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.squareup:javapoet:1.13.0")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}