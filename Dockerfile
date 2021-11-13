FROM openjdk:11
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
EXPOSE 5432
ENTRYPOINT ["java","-jar","/app.jar", "-Dspring.profiles.active=dev"]
