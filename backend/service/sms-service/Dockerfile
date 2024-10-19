FROM openjdk:17-jdk-slim
VOLUME /tmp
ADD ./build/libs/*.jar sms-service.jar

ENTRYPOINT ["java", "-jar", "--spring.profiles.active=local", "/sms-service.jar"]