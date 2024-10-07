FROM openjdk:24-ea-17-jdk-slim
VOLUME /tmp
ADD ./build/libs/*.jar admin-service.jar

ENTRYPOINT ["java", "-jar","/admin-service.jar"]