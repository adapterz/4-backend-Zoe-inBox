FROM adoptopenjdk/openjdk11:ubi
VOLUME /backend_container_data
ARG jar=build/libs/app.jar
COPY ${jar} app.jar
EXPOSE 80
ENTRYPOINT ["java","-jar","/app.jar"]