plugins {
    java
    `java-library`
    id("org.springframework.boot") version "3.4.3"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "cupid-match"
version = "1.0.0"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

dependencies {
    implementation(project(":common"))
    testImplementation(testFixtures(project(":common")))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // mybatis
    implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.3")
    
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    implementation("org.springframework.boot:spring-boot-starter-cache")

    // Kafka
    implementation("org.springframework.kafka:spring-kafka")
    testImplementation("org.springframework.kafka:spring-kafka-test")

    // MapStruct
    implementation("org.mapstruct:mapstruct:1.5.5.Final")

    runtimeOnly("com.h2database:h2")
    runtimeOnly("com.mysql:mysql-connector-j")
}

tasks.register<JavaExec>("checkVirtualThreadPinningRun") {
    group = "virtual-thread"
    description = "Run the application with -Djdk.tracePinnedThreads=full to check virtual thread pinning"

    mainClass.set("cupid.CupidApplication")
    classpath = sourceSets.main.get().runtimeClasspath
    jvmArgs = listOf("-Djdk.tracePinnedThreads=full")
}
