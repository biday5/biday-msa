FROM openjdk:24-ea-17-jdk-slim
VOLUME /tmp
ADD ./build/libs/*.jar eureka-server.jar

ENTRYPOINT ["java", "-jar", "--spring.profiles.active=local", "/eureka-server.jar"]