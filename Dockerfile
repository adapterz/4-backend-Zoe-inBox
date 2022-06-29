FROM adoptopenjdk/openjdk11:ubi
VOLUME /backend_container_data
RUN mkdir /server/
COPY app.jar /server/
EXPOSE 80
ENTRYPOINT ["java","-jar","/server/app.jar"]