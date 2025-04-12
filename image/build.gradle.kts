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
    implementation("com.drewnoakes:metadata-extractor:2.18.0")

    // s3
    implementation("com.amazonaws:aws-java-sdk-s3:1.12.174")

    runtimeOnly("com.h2database:h2")
    runtimeOnly("com.mysql:mysql-connector-j")

    annotationProcessor("org.projectlombok:lombok")
    compileOnly("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.register<JavaExec>("checkVirtualThreadPinningRun") {
    group = "virtual-thread"
    description = "Run the application with -Djdk.tracePinnedThreads=full to check virtual thread pinning"

    mainClass.set("cupid.CupidApplication")
    classpath = sourceSets.main.get().runtimeClasspath
    jvmArgs = listOf("-Djdk.tracePinnedThreads=full")
}
