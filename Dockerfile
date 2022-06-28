FROM openjdk:17-jdk-slim-buster
VOLUME /backend_container_data
ARG JAR_FILE=/backend_build/
COPY ${JAR_FILE} app.jar
EXPOSE 80
ENTRYPOINT ["java","-jar","/app.jar"]