dependencies {
    implementation(project(":core"))
    implementation(project(":common"))

    implementation(libs.bundles.custom.libraries)

    implementation(libs.bundles.spring.boot.database)
    implementation(libs.spring.boot.starter.validation)

    implementation(libs.spring.boot.starter.feign)
    implementation(libs.feign.okhttp)

    implementation(libs.jackson.core)
    implementation(libs.jackson.databind)

    implementation(libs.servlet.api)
    implementation(libs.spring.web)

    implementation(libs.mapstruct)
    annotationProcessor(libs.mapstruct.processor)

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    annotationProcessor(libs.lombok.mapstruct.binding)

    testImplementation(libs.spring.boot.starter.test)
    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)
}
