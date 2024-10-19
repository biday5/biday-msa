FROM openjdk:17-jdk-slim
VOLUME /tmp
ADD ./build/libs/*.jar eureka-server.jar

ENTRYPOINT ["java", "-jar", "--spring.profiles.active=local", "/eureka-server.jar"]