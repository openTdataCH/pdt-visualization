FROM openjdk:17.0.1-jdk-slim
COPY trafficcounter-backend/target/trafficcounter-backend.jar trafficcounter-backend.jar
ENTRYPOINT [ "sh", "-c", "java -Djava.security.egd=file:/dev/./urandom -jar trafficcounter-backend.jar" ]
