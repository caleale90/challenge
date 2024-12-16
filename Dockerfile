FROM openjdk:23
VOLUME /tmp
EXPOSE 8080
COPY target/*.jar start.jar
ENTRYPOINT ["java","-jar","/start.jar"]