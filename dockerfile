FROM openjdk:17-jdk


COPY docker-compose.yml /home/run/
RUN chmod 777 /home/run/docker-compose.yml


COPY automation-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
