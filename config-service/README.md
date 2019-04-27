Config Service
=============

### 手动刷新配置

1. 修改`config-service`服务的`/resources/conf`下对应服务的配置文件

2. 访问对应服务的`/actuator/refresh`，如：`localhost:8000/actuator/refresh`