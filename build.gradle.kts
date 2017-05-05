
import org.gradle.script.lang.kotlin.`maven-publish`
import org.gradle.script.lang.kotlin.dependencies
import org.gradle.script.lang.kotlin.eclipse
import org.gradle.script.lang.kotlin.idea
import org.gradle.script.lang.kotlin.java
import org.gradle.script.lang.kotlin.maven
import org.gradle.script.lang.kotlin.publishing
import org.gradle.script.lang.kotlin.repositories
import org.gradle.script.lang.kotlin.testCompile
import org.gradle.script.lang.kotlin.version

plugins {
    java
    maven
    idea
    eclipse
    publishing
    `maven-publish`
    id("nebula.kotlin") version("1.1.2")
    id("nebula.release") version("4.2.0")
    id("com.jfrog.bintray") version("1.7")
}

apply { from("bintray.gradle") }

group = "com.lloydramey.jdbc"

description = """JDBC Named Parameters"""

repositories {
    jcenter()
}

dependencies {
    testCompile("junit:junit:4.12")
    testCompile("commons-io:commons-io:2.5")
}

