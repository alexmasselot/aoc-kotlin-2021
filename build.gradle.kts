plugins {
    kotlin("jvm") version "1.5.31"
}

repositories {
    mavenCentral()
}
dependencies {
    implementation("org.junit.jupiter:junit-jupiter:5.7.0")
    implementation("org.assertj:assertj-core:3.21.0")
}

tasks {


    wrapper {
        gradleVersion = "7.3"
    }
}


tasks.withType<Test> {
    useJUnitPlatform()
}
