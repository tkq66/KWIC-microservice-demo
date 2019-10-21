FROM openjdk:8-jdk-alpine

<<<<<<< HEAD:chairwise-input-service/Dockerfile
COPY target/chairwise-input-service-0.0.1-SNAPSHOT.jar /home/app.jar
ENTRYPOINT ["java","-jar","/home/app.jar"]
=======
# Build the current project
COPY src ./src
RUN mvn package -DskipTests --activate-profiles dev-docker

# Host the application
FROM openjdk:8
RUN mkdir /opt/files
COPY --from=build /home/app/target/chairwise-input-service-0.0.1-SNAPSHOT.jar /usr/local/lib/app.jar
ENTRYPOINT ["java","-jar","/usr/local/lib/app.jar"]
>>>>>>> 2c63fb05357b50ad24a2e5e6888ee369dd41af79:chairwise-input-service/src/build/docker/Dockerfile
