plugins {
    java
    id("io.spring.dependency-management") version libs.versions.dependency.management.get() apply false
    id("org.springframework.boot") version libs.versions.spring.boot.get() apply false
    alias(libs.plugins.jib) apply false
}

allprojects {
    group = "com.fis.boportalservice"
    version = "1.0.0-SNAPSHOT"

    repositories {
        mavenLocal()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

subprojects {
    apply {
        plugin("java")
        plugin("io.spring.dependency-management")
        plugin("org.springframework.boot")
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(25))
        }
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.compilerArgs.addAll(
            listOf("-parameters", "-Xlint:unchecked", "-Xlint:deprecation")
        )
    }

    configurations {
        compileOnly {
            extendsFrom(configurations.annotationProcessor.get())
        }
    }

    tasks.jar { enabled = true }

    tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") { enabled = false }

    tasks.test { useJUnitPlatform() }
}

tasks.jar { enabled = false }
