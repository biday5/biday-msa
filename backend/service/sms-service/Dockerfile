FROM openjdk:24-ea-17-jdk-slim
VOLUME /tmp
ADD ./build/libs/*.jar sms-service.jar

ENTRYPOINT ["java", "-jar","/sms-service.jar"]