import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    java
    id("org.springframework.boot") version "3.3.1"
    id("io.spring.dependency-management") version "1.1.5"

    id("com.diffplug.spotless") version "6.25.0"
    id("checkstyle")
    id("jacoco")
}

group = "hu.bono.bigbank"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    checkstyle("com.puppycrawl.tools:checkstyle:${property("checkstyleVersion")}")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.mapstruct:mapstruct:${property("mapstructVersion")}")
    implementation("org.apache.commons:commons-io:${property("apacheCommonsIoVersion")}")
    implementation("org.apache.commons:commons-csv:${property("apacheCommonsCsvVersion")}")

    compileOnly("org.projectlombok:lombok")

    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.mapstruct:mapstruct-processor:${property("mapstructVersion")}")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

spotless {
    java {
        // eclipse().configFile("config/spotless/eclipse.xml")
        // googleJavaFormat("1.22.0")

        removeUnusedImports()
        trimTrailingWhitespace()
    }
}

checkstyle {
    toolVersion = property("checkstyleVersion").toString()
}

tasks.withType<Checkstyle> {
    reports {
        html.required.set(true)
        xml.required.set(false)
    }
}

configure<JacocoPluginExtension> {
    toolVersion = property("jacocoVersion").toString()
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events = setOf(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED, TestLogEvent.STANDARD_ERROR)
        showExceptions = true
        showCauses = true
        showStackTraces = true
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.SHORT
    }
    finalizedBy(tasks.withType<JacocoReport>())
}

tasks.withType<JacocoReport> {
    dependsOn(tasks.named<Test>("test"))
    reports {
        xml.required.set(true)
        csv.required.set(true)
    }

    additionalSourceDirs.setFrom(files("${layout.buildDirectory.get()}/generated/sources/annotationProcessor/java/main"))
}

tasks.withType<JacocoCoverageVerification> {
    dependsOn(tasks.withType<JacocoReport>())
    violationRules {
        rule {
            limit {
                counter = "INSTRUCTION"
                value = "COVEREDRATIO"
                minimum = "0.7".toBigDecimal()
            }
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.7".toBigDecimal()
            }
            limit {
                counter = "BRANCH"
                value = "COVEREDRATIO"
                minimum = "0.7".toBigDecimal()
            }
        }
    }
}

tasks.named<DefaultTask>("check") {
    dependsOn(tasks.withType<JacocoCoverageVerification>())
}
