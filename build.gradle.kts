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

repositories {
    jcenter()
}

dependencies {
    testCompile("junit:junit:4.12")
    testCompile("com.google.guava:guava:16.0.1")
}
