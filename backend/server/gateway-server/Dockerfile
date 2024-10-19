FROM openjdk:17-jdk-slim
VOLUME /tmp
ADD ./build/libs/*.jar gateway-server.jar

ENTRYPOINT ["java", "-jar", "--spring.profiles.active=local", "/gateway-server.jar"]