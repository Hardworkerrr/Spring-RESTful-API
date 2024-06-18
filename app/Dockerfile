FROM openjdk:17-slim
WORKDIR /myapp
COPY target/app-0.0.1-SNAPSHOT.jar /myapp/myapp.jar
ENTRYPOINT ["java", "-jar", "myapp.jar"]
EXPOSE 8080