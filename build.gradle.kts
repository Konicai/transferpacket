import org.gradle.api.tasks.TaskAction


plugins {
    java
    `java-library`
    `maven-publish`
}

apply(plugin = "java")
apply(plugin = "java-library")
apply(plugin = "maven-publish")

group = "me.konicai"
version = "1.0.0"
java.sourceCompatibility = JavaVersion.VERSION_1_8
java.targetCompatibility = JavaVersion.VERSION_1_8

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

repositories {
    mavenCentral()
    maven("https://repo.opencollab.dev/main/")
    maven("https://jitpack.io") // avoids weird transitive dependency error with Geyser core
}

dependencies {
    annotationProcessor("org.projectlombok:lombok:1.18.22")
    compileOnly("org.projectlombok:lombok:1.18.22")
    compileOnly("com.google.code.findbugs:jsr305:3.0.2") // nullability annotations
    compileOnly("org.geysermc:core:2.0.2-SNAPSHOT")
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks.register<InstallTask>("install")
tasks.named("install") {
    dependsOn(tasks.named("build"))
}

abstract class InstallTask : DefaultTask()
