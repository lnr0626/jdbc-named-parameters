
import org.gradle.script.lang.kotlin.`java-library`
import org.gradle.script.lang.kotlin.`maven-publish`
import org.gradle.script.lang.kotlin.dependencies
import org.gradle.script.lang.kotlin.eclipse
import org.gradle.script.lang.kotlin.idea
import org.gradle.script.lang.kotlin.java
import org.gradle.script.lang.kotlin.maven
import org.gradle.script.lang.kotlin.repositories
import org.gradle.script.lang.kotlin.testCompile
import org.gradle.script.lang.kotlin.version
buildscript {
    repositories {
        jcenter()
    }

    dependencies {
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:0.9.13")
    }
}

plugins {
    java
    maven
    idea
    eclipse
    `java-library`
    `maven-publish`
    id("nebula.kotlin") version("1.1.2")
    id("nebula.release") version("4.2.0")
    id("com.jfrog.bintray") version("1.7.3")
}

apply {
    plugin("org.jetbrains.dokka")
    from("bintray.gradle")
}

group = "com.lloydramey.jdbc"

description = """JDBC Named Parameters"""

repositories {
    jcenter()
}

dependencies {
    testCompile("junit:junit:4.12")
    testCompile("commons-io:commons-io:2.5")
}

