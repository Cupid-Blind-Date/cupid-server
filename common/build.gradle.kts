import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    java
    `java-library`
    `java-test-fixtures`
    id("org.springframework.boot") version "3.4.3"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "cupid-common"
version = "1.0.0"

tasks.named<BootJar>("bootJar") {
    enabled = false
}

tasks.named<Jar>("jar") {
    enabled = true
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")

    // JPA
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    testFixturesImplementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // JWT
    implementation("io.jsonwebtoken:jjwt-api:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.5")

    // Kafka
    implementation("org.springframework.kafka:spring-kafka")
    testFixturesImplementation("org.springframework.kafka:spring-kafka-test")

    // MapStruct
    implementation("org.mapstruct:mapstruct:1.5.5.Final")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    runtimeOnly("com.h2database:h2")
    runtimeOnly("com.mysql:mysql-connector-j")

    testFixturesImplementation("org.springframework.boot:spring-boot-starter-test")
    testFixturesRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Fixture Monkey
    testFixturesApi("com.navercorp.fixturemonkey:fixture-monkey-starter:1.1.9")
}
