#!/bin/bash

# 빌드 파일 경로 및 이름 설정
BUILD_JAR=$(ls /home/ec2-user/action/build/libs/*SNAPSHOT.jar)
JAR_NAME=$(basename $BUILD_JAR)
echo "## build file name : $JAR_NAME" >> /home/ec2-user/action/spring-deploy.log

# 빌드 파일 복사
echo "## copy build file" >> /home/ec2-user/action/spring-deploy.log
DEPLOY_PATH=/home/ec2-user/action/
cp $BUILD_JAR $DEPLOY_PATH

# Redis 서비스 재시작
echo "## restarting Redis service" >> /home/ec2-user/action/spring-deploy.log
sudo systemctl restart redis6.service

if [ $? -eq 0 ]; then
  echo "## Redis service restarted successfully" >> /home/ec2-user/action/spring-deploy.log
else
  echo "## Failed to restart Redis service" >> /home/ec2-user/action/spring-deploy.log
  exit 1
fi

# 현재 실행 중인 애플리케이션 PID 확인 및 종료
echo "## current pid" >> /home/ec2-user/action/spring-deploy.log
CURRENT_PID=$(ps -ef | grep "java" | awk 'NR==1 {print $2}')

if [ -z $CURRENT_PID ]
then
  echo "## 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다." >> /home/ec2-user/action/spring-deploy.log
else
  echo "## kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

# 새로운 애플리케이션 배포
DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
echo "## deploy JAR file"   >> /home/ec2-user/action/spring-deploy.log
#nohup java -jar $DEPLOY_JAR >> /home/ec2-user/action/spring-deploy.log 2> /home/ec2-user/action/spring-deploy_err.log &
nohup java -Dspring.config.import=optional:file:/home/ec2-user/action/chungbaji.env -jar $DEPLOY_JAR >> /home/ec2-user/action/spring-deploy.log 2>> /home/ec2-use
