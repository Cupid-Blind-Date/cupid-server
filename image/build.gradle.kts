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
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.drewnoakes:metadata-extractor:2.18.0")

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
