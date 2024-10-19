FROM openjdk:17-jdk-slim
VOLUME /tmp
ADD ./build/libs/*.jar order-service.jar

ENTRYPOINT ["java", "-jar", "--spring.profiles.active=local", "/order-service.jar"]