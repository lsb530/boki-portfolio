
import org.springframework.boot.gradle.tasks.bundling.BootJar
import java.nio.file.Files
import java.nio.file.Path
import java.text.SimpleDateFormat
import java.util.*

plugins {
    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.6"
    kotlin("plugin.jpa") version "1.9.24"
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
    id("io.qameta.allure") version "2.11.2"
    id("io.qameta.allure-report") version "2.11.2"
    jacoco
}

group = "boki"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

jacoco {
    toolVersion = "0.8.12"
}

val jwtVersion = "0.12.6"
val swaggerUiVersion = "2.6.0"
val koTestVersion = "5.9.0"
val koTestAllureExtensionVersion = "1.4.0"
val mockkVersion = "1.13.12"
val springMockkVersion = "4.0.2"
val minioVersion = "8.5.11"
//val jdslVersion = "3.5.1"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.springframework.security:spring-security-test")
    runtimeOnly("com.mysql:mysql-connector-j")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // JWT
    implementation("io.jsonwebtoken:jjwt-api:${jwtVersion}")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:${jwtVersion}")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:${jwtVersion}")

    // Swagger-UI
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${swaggerUiVersion}")

    // Prometheus
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")

    // Ko-test
    testImplementation("io.kotest:kotest-runner-junit5:$koTestVersion")
    testImplementation("io.kotest:kotest-extensions-htmlreporter:$koTestVersion")

    // allure
    implementation("io.kotest.extensions:kotest-extensions-allure:$koTestAllureExtensionVersion")

    // mockk
    testImplementation("io.mockk:mockk:${mockkVersion}")
    testImplementation("com.ninja-squad:springmockk:${springMockkVersion}")

    // AOP
    implementation("org.springframework.boot:spring-boot-starter-aop")

    // MinIO
    implementation("io.minio:minio:$minioVersion")

    // Kotlin JDSL from line
//    implementation("com.linecorp.kotlin-jdsl:jpql-dsl:$jdslVersion")
//    implementation("com.linecorp.kotlin-jdsl:jpql-render:$jdslVersion")
//    implementation("com.linecorp.kotlin-jdsl:spring-data-jpa-support:$jdslVersion")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    systemProperty("kotest.framework.classpath.scanning.autoscan.disable", "true")
    finalizedBy(tasks.jacocoTestReport, tasks.allureReport)
    doLast {
        val rootDir = project.rootDir.toPath()
        Files.newDirectoryStream(rootDir, "ajcore.*.txt").use { directoryStream ->
            directoryStream.forEach { path: Path? ->
                path?.let { Files.delete(it) }
                println("Deleted file: $path")
            }
        }
    }
}

tasks.allureReport {
    dependsOn(tasks.test)
    doFirst {
        delete(layout.buildDirectory.dir("reports/allure-report/allureReport"))
    }
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(false)
        html.required.set(true)
    }
}

tasks.getByName<BootJar>("bootJar") {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    val dateFormat = SimpleDateFormat("yyMMdd_HHmm")
    archiveFileName.set("${project.name}-${dateFormat.format(Date())}_${project.version}.jar")
    enabled = true
}

tasks.getByName<Jar>("jar") {
    enabled = false
}
