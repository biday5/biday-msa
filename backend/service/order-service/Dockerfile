FROM openjdk:17-jdk-slim
VOLUME /tmp
ADD ./build/libs/*.jar order-service.jar

ENTRYPOINT ["java", "-jar", "/order-service.jar"]