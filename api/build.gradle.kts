plugins {
    alias(libs.plugins.jib)
}

dependencies {
    implementation(project(":core"))
    implementation(project(":common"))
    implementation(project(":infra"))

    implementation(libs.bundles.custom.libraries)

    implementation(libs.bundles.spring.boot.all)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.security)

    implementation(libs.spring.boot.starter.feign)

    implementation(libs.openapi.doc)

    implementation(libs.jackson.core)
    implementation(libs.jackson.databind)

    implementation(libs.mapstruct)
    annotationProcessor(libs.mapstruct.processor)

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    annotationProcessor(libs.lombok.mapstruct.binding)
}

tasks.jar { enabled = false }

tasks.bootJar {
    enabled = true
    archiveFileName.set("bo-portal-service.jar")
}

springBoot {
    mainClass.set("com.fis.boportalservice.api.BoPortalServiceApplication")
}

jib {
    from {
        image = "eclipse-temurin:25-jre-alpine"
    }
    to {
        image = "bo-portal-service"
    }
    container {
        mainClass = "com.fis.boportalservice.api.BoPortalServiceApplication"
        creationTime = "USE_CURRENT_TIMESTAMP"
    }
}
