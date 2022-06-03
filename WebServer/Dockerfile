# the first stage of our build will extract the layers
FROM adoptopenjdk/openjdk15:jre-15.0.2_7-alpine as builder
WORKDIR app
ARG JAR_FILE=*.jar
COPY ${JAR_FILE} app.jar
RUN java -Djarmode=layertools -jar app.jar extract

# the second stage of our build will copy the extracted layers
FROM adoptopenjdk/openjdk15:jre-15.0.2_7-alpine
WORKDIR /app
RUN addgroup -S -g 1000 java && adduser -S -u 1000 -G java java && \
    mkdir -p /config/dummy && ln -s /config && \
    chown -h -R java:java /app && chown -R java:java /config
COPY --chown=java --from=builder app/dependencies/ ./
COPY --chown=java --from=builder app/spring-boot-loader/ ./
COPY --chown=java --from=builder app/snapshot-dependencies/ ./
COPY --chown=java --from=builder app/application/ ./
USER java
HEALTHCHECK --start-period=30s \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8081/actuator/health/liveness || exit 1
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]

ARG NAME=unknown
ARG VERSION=unknown
ARG DESCRIPTION=unknown
ARG VCS_REF=unknown
ARG VCS_URL=unknown

LABEL org.label-schema.name="${NAME}" \
      org.label-schema.version="${VERSION}" \
      org.label-schema.description="${DESCRIPTION}" \
      org.label-schema.vcs-ref="${VCS_REF}" \
      org.label-schema.vcs-url="${VCS_URL}" \
      org.label-schema.schema-version="1.0"
