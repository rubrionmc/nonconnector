FROM gradle:9.1-jdk25 AS builder
WORKDIR /app

COPY build.gradle.kts settings.gradle.kts ./
COPY gradle ./gradle

RUN gradle --version

COPY src ./src
RUN gradle clean build --no-daemon

RUN JAR_NAME=$(ls /app/build/libs/*.jar | head -n1) && \
    echo "Found JAR: $JAR_NAME" && \
    cp $JAR_NAME /app.jar

FROM eclipse-temurin:25-jdk AS jlink-base

RUN jlink \
    --output /opt/jdk-custom \
    --add-modules java.base,java.logging,java.management,java.instrument,java.desktop,java.net.http,jdk.unsupported, \
    --compress=2 \
    --no-header-files \
    --no-man-pages \
    --strip-debug

COPY --from=builder /app.jar /opt/server/server.jar

FROM debian:12-slim AS runtime
WORKDIR /opt/server

COPY --from=jlink-base /opt/jdk-custom /opt/jdk
ENV PATH="/opt/jdk/bin:$PATH"

COPY --from=jlink-base /opt/server/ /opt/server/

COPY .server /opt/server/

EXPOSE 25575

RUN printf '%s\n' '#!/bin/bash' \
'JAR_FILE=$(find /opt/server -name "*.jar" | head -n1)' \
'exec java -jar "$JAR_FILE"' \
> /opt/server/start.sh && chmod +x /opt/server/start.sh

CMD ["/opt/server/start.sh"]
