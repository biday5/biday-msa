#!/bin/bash

chmod +x ./gradlew

# 모듈 리스트
all_modules=(
    "server:config-server"
    "server:eureka-server"    
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
    echo "Building for $module"
    ./gradlew :$module:bootJar
done
