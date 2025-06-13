FROM openjdk:17-jdk-slim
VOLUME /tpm
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

ENV DEBUG_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5006"

ENTRYPOINT exec java ${DEBUG_OPTS} -jar app.jar
