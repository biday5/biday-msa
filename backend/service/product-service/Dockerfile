FROM openjdk:17-jdk-slim
VOLUME /tmp
ADD ./build/libs/*.jar product-service.jar

ENTRYPOINT ["java", "-jar", "--spring.profiles.active=local", "/product-service.jar"]