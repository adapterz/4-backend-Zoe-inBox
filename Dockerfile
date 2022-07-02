FROM adoptopenjdk/openjdk11:ubi
ARG jar=build/libs/app.jar
COPY ${jar} app.jar
EXPOSE 80
ENTRYPOINT ["java","-jar","app.jar"]