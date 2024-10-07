FROM openjdk:24-ea-17-jdk-slim
VOLUME /tmp
ADD ./build/libs/*.jar auction-service.jar

ENTRYPOINT ["java", "-jar","/auction-service.jar"]