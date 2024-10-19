FROM openjdk:17-jdk-slim
VOLUME /tmp
ADD ./build/libs/*.jar gateway-server.jar

ENTRYPOINT ["java", "-jar", "/gateway-server.jar"]