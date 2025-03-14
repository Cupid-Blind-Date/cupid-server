plugins {
    java
    `java-library`
    id("org.springframework.boot") version "3.4.3"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "cupid-playground"
version = "1.0.0"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

dependencies {
    implementation(project(":common"))
    implementation(project(":match"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    implementation("org.springframework.kafka:spring-kafka")

    implementation("org.springframework.boot:spring-boot-starter-web")

    annotationProcessor("org.projectlombok:lombok")

    runtimeOnly("com.mysql:mysql-connector-j")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
