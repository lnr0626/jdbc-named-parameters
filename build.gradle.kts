
import org.gradle.script.lang.kotlin.dependencies
import org.gradle.script.lang.kotlin.eclipse
import org.gradle.script.lang.kotlin.idea
import org.gradle.script.lang.kotlin.java
import org.gradle.script.lang.kotlin.maven
import org.gradle.script.lang.kotlin.repositories
import org.gradle.script.lang.kotlin.testCompile
import org.gradle.script.lang.kotlin.version

plugins {
    java
    maven
    idea
    eclipse
    id("nebula.kotlin") version("1.1.2")
}

group = "com.lloydramey.jdbc"
version = "0.0.1-SNAPSHOT"

description = """JDBC Named Parameters"""

repositories {
    jcenter()
}

dependencies {
    testCompile("junit:junit:4.12")
    testCompile("commons-io:commons-io:2.5")
}
