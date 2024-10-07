#!/bin/bash

# IntelliJ IDEA JDK 경로, 실제 경로로 변경해주세요
export JAVA_HOME="C:\\Program Files\\Java\\jdk-17"

# 모듈 리스트
all_modules=(
    "server:eureka-server"
    "server:config-server"
    "server:gateway-server"
    "service:admin-service"
    "service:auction-service"
    "service:ftp-service"
    "service:order-service"
    "service:product-service"
    "service:sms-service"
    "service:user-service"
)

# Gradle clean
echo "Cleaning..."
./gradlew clean

# Gradle BootJar
for module in "${all_modules[@]}"
do
    echo "Building BootJar for $module"
    ./gradlew :$module:bootJar
done
