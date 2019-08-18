#!/bin/sh

# 配置文件根目录，固定是spring-microservice-exam
DOCKERHOME=/spring-microservice-exam

# 镜像名称前缀、标签
BASE_IMAGE_NAME=registry.cn-hangzhou.aliyuncs.com/spring-microservice-exam
BSEE_IMAGE_TAG=3.2.0-SNAPSHOT

# 各服务的镜像名称
CONFIG_SERVICE=$BASE_IMAGE_NAME/config-service:$BSEE_IMAGE_TAG
AUTH_SERVICE=$BASE_IMAGE_NAME/auth-service:$BSEE_IMAGE_TAG
USER_SERVICE=$BASE_IMAGE_NAME/user-service:$BSEE_IMAGE_TAG
EXAM_SERVICE=$BASE_IMAGE_NAME/exam-service:$BSEE_IMAGE_TAG
GATEWAY_SERVICE=$BASE_IMAGE_NAME/gateway-service:$BSEE_IMAGE_TAG
MSC_SERVICE=$BASE_IMAGE_NAME/msc-service:$BSEE_IMAGE_TAG
MONITOR_SERVICE=$BASE_IMAGE_NAME/monitor-service:$BSEE_IMAGE_TAG
UI_SERVICE=$BASE_IMAGE_NAME/spring-microservice-exam-ui:$BSEE_IMAGE_TAG
WEB_SERVICE=$BASE_IMAGE_NAME/spring-microservice-exam-web:$BSEE_IMAGE_TAG

case "$1" in

    # 删除容器
    removeAll)
        echo "* 正在删除容器..."
        time docker rm $(docker ps -aq) -f
        echo "* 删除容器成功..."
        ;;
    # 拉取镜像
    pull)
        echo "* 正在拉取后端镜像..."
        time docker pull $CONFIG_SERVICE
        time docker pull $AUTH_SERVICE
        time docker pull $USER_SERVICE
        time docker pull $EXAM_SERVICE
        time docker pull $GATEWAY_SERVICE
        time docker pull $MSC_SERVICE
        time docker pull $MONITOR_SERVICE
        echo "* 开始拉取前端镜像..."
        time docker pull $UI_SERVICE
        time docker pull $WEB_SERVICE
        echo "* 拉取镜像成功..."
        ;;
    # 运行镜像
    run)
        echo "* 开始运行基础镜像..."
        time docker-compose -f $DOCKERHOME/docker-compose-base.yml up -d
        echo "* 等待10s..."
        sleep 10
        echo "* 开始运行后端服务镜像..."
        time docker-compose -f $DOCKERHOME/docker-compose-services.yml up -d
        echo "* 等待10s..."
        sleep 10
        echo "* 开始运行前端服务镜像..."
        time docker-compose -f $DOCKERHOME/docker-compose-nginx.yml up -d
        echo "* 运行成功..."
        ;;
    # 拉取镜像并运行
    pullrun)
        echo "* 正在拉取后端镜像..."
        time docker pull $CONFIG_SERVICE
        time docker pull $AUTH_SERVICE
        time docker pull $USER_SERVICE
        time docker pull $EXAM_SERVICE
        time docker pull $GATEWAY_SERVICE
        time docker pull $MSC_SERVICE
        time docker pull $MONITOR_SERVICE
        echo "* 开始拉取前端镜像..."
        time docker pull $UI_SERVICE
        time docker pull $WEB_SERVICE
        echo "* 拉取镜像成功..."

         echo "* 开始运行基础镜像..."
        time docker-compose -f $DOCKERHOME/docker-compose-base.yml up -d
        echo "* 等待10s..."
        sleep 10
        echo "* 开始运行后端服务镜像..."
        time docker-compose -f $DOCKERHOME/docker-compose-services.yml up -d
        echo "* 等待10s..."
        sleep 10
        echo "* 开始运行前端服务镜像..."
        time docker-compose -f $DOCKERHOME/docker-compose-nginx.yml up -d
        echo "* 运行成功..."
        ;;
    # 停止容器
    stop)
        echo "* 正在停止容器..."
        time docker-compose -f $DOCKERHOME/docker-compose-nginx.yml stop
        time docker-compose -f $DOCKERHOME/docker-compose-services.yml stop
        time docker-compose -f $DOCKERHOME/docker-compose-base.yml stop
        echo "* 停止容器成功..."
        ;;
    # 重启容器
    restart)
        echo "* 正在停止镜像..."
        time docker-compose -f $DOCKERHOME/docker-compose-nginx.yml restart
        time docker-compose -f $DOCKERHOME/docker-compose-services.yml restart
        time docker-compose -f $DOCKERHOME/docker-compose-base.yml restart
        ;;
    # 其它
    *)
        echo "* ..."
        ;;
esac
exit 0