# Base image로 OpenJDK 21을 사용
FROM openjdk:21-jdk-slim

# 작업 디렉토리 설정
WORKDIR /app

# 로컬의 JAR 파일을 컨테이너의 /app 디렉토리로 복사
COPY ./build/libs/ClassicJeans-0.0.1-SNAPSHOT.jar /app/ClassicJeans-0.0.1-SNAPSHOT.jar

# JMX Exporter와 함께 JAR 파일 실행
CMD ["java", "-javaagent:/app/jmx_prometheus_javaagent-0.16.1.jar=1234:/app/jmx_exporter_config.yml", "-jar", "/app/ClassicJeans-0.0.1-SNAPSHOT.jar"]
