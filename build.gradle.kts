import org.gradle.api.JavaVersion
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.script.lang.kotlin.dependencies
import org.gradle.script.lang.kotlin.eclipse
import org.gradle.script.lang.kotlin.idea
import org.gradle.script.lang.kotlin.java
import org.gradle.script.lang.kotlin.maven
import org.gradle.script.lang.kotlin.repositories
import org.gradle.script.lang.kotlin.testCompile

plugins {
    java
    maven
    idea
    eclipse
}

group = "com.lloydramey.jdbc"
version = "0.0.1-SNAPSHOT"

description = """JDBC Named Parameters"""

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType(JavaCompile::class.java) {
    options.encoding = "UTF-8"
}

repositories {
    jcenter()
}

dependencies {
    testCompile("junit:junit:4.12")
    testCompile("com.google.guava:guava:16.0.1")
}
