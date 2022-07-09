FROM openjdk:11-jdk
ARG jar=./build/libs/app.jar
COPY ${jar} app.jar
COPY ./env.sh env.sh
EXPOSE 8080
ENTRYPOINT ["sh","env.sh"]
ENTRYPOINT ["java","-jar","/app.jar"]