FROM openjdk:17-jdk-slim
VOLUME /tmp
ADD ./build/libs/*.jar admin-service.jar

ENTRYPOINT ["java", "-jar", "--spring.profiles.active=local", "/admin-service.jar"]