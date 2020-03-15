# 部署相关的配置文件

## mysql

mysql的初始化脚本

## elk

elk的配置文件

## docker-compose.env

所有服务共享的环境变量

## docker-compose-base.yml

基础服务：consul、Redis、rabbitMq、config-service

## docker-compose-services.yml

核心服务：auth-service、user-service、exam-service、gateway-service、monitor-service、msc-service


## docker-compose-nginx.yml

前端服务：ui-service、web-service

## docker-compose-elk.yml

elk服务

## redis.conf

redis的配置

## nginx.conf

前端服务共享的nginx配置，主要配置将api请求转发到网关gateway-service


## start.sh

启动脚本

## QRCode.jar

安装到本地maven仓库命令：mvn install:install-file -Dfile=C:\Users\Administrator\Downloads>\QRCode.jar -DgroupId=QRCode -DartifactId=QRCode -Dversion=3.0 -Dpackaging=jar
