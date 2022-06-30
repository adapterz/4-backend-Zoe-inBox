FROM adoptopenjdk/openjdk11:ubi
VOLUME /backend_container_data
ARG now
ENV build_at=$now
ARG jar=build/libs/app.jar
COPY ${jar} app.jar
EXPOSE 80
ENTRYPOINT ["java","-jar","/app.jar"]