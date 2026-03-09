# ── Stage 1: Build ────────────────────────────────────────────────────────────
FROM eclipse-temurin:25.0.1_8-jdk AS build
WORKDIR /app

# Copy Gradle wrapper & config files first (for layer caching)
COPY gradlew gradlew.bat build.gradle settings.gradle ./
COPY gradle/ gradle/

# Copy submodule build files
COPY common/build.gradle common/
COPY core/build.gradle    core/
COPY infra/build.gradle   infra/
COPY api/build.gradle     api/

RUN chmod +x gradlew

# Download dependencies (cached layer — only re-runs when build files change)
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew dependencies --no-daemon

# Copy source code
COPY common/src common/src
COPY core/src   core/src
COPY infra/src  infra/src
COPY api/src    api/src

# Build executable JAR (skip tests)
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew :api:bootJar -x test --no-daemon

# ── Stage 2: Create minimal custom JRE using jlink ────────────────────────────
RUN jdeps --ignore-missing-deps -q \
    --recursive \
    --multi-release 25 \
    --print-module-deps \
    --class-path 'common/build/libs/*:core/build/libs/*:infra/build/libs/*' \
    api/build/libs/api-1.0.0-SNAPSHOT.jar > deps.txt

RUN jlink \
    --add-modules $(cat deps.txt),java.base,java.logging,java.naming,java.desktop,java.management,java.security.jgss,java.instrument,java.sql,java.compiler,jdk.crypto.ec,jdk.unsupported \
    --compress zip-9 \
    --strip-debug \
    --no-header-files \
    --no-man-pages \
    --output /custom-jre

# ── Stage 3: Minimal runtime image ────────────────────────────────────────────
FROM gcr.io/distroless/base-debian12

WORKDIR /app

COPY --from=build /custom-jre /opt/java/openjdk
COPY --from=build /app/api/build/libs/api-1.0.0-SNAPSHOT.jar ./app.jar

EXPOSE 8080

# ZGC for low-latency GC | timezone Vietnam | memory limits
ENV JAVA_TOOL_OPTIONS="-XX:+UseZGC -Xms128m -Xmx512m -Duser.timezone=Asia/Ho_Chi_Minh"

ENTRYPOINT ["/opt/java/openjdk/bin/java", "-jar", "app.jar"]
