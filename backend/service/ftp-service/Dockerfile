FROM openjdk:24-ea-17-jdk-slim
VOLUME /tmp
ADD ./build/libs/*.jar ftp-service.jar

ENTRYPOINT ["java", "-jar","/ftp-service.jar"]