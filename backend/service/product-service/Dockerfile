FROM openjdk:24-ea-17-jdk-slim
VOLUME /tmp
ADD ./build/libs/*.jar product-service.jar

ENTRYPOINT ["java", "-jar","/product-service.jar"]