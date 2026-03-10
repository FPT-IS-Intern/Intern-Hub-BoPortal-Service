# Stage 1: Build
FROM eclipse-temurin:25.0.1_8-jdk AS build
WORKDIR /app

# Copy Gradle wrapper and config first for better cache reuse
COPY gradlew gradlew.bat build.gradle settings.gradle ./
COPY gradle/ gradle/

# Copy submodule build files
COPY common/build.gradle common/
COPY core/build.gradle    core/
COPY infra/build.gradle   infra/
COPY api/build.gradle     api/

RUN chmod +x gradlew

# Copy source code
COPY common/src common/src
COPY core/src   core/src
COPY infra/src  infra/src
COPY api/src    api/src

# Build executable JAR (skip tests)
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew :api:bootJar -x test --no-daemon

# Stage 2: Runtime
FROM eclipse-temurin:25.0.1_8-jre
WORKDIR /app

COPY --from=build /app/api/build/libs/api-1.0.0-SNAPSHOT.jar ./app.jar

EXPOSE 8080

# Runtime tuning for low-memory container (~250MB limit)
ENV JAVA_TOOL_OPTIONS="-XX:+UseSerialGC -Xms64m -Xmx160m -XX:MaxMetaspaceSize=64m -Duser.timezone=Asia/Ho_Chi_Minh"

ENTRYPOINT ["java", "-jar", "app.jar"]
