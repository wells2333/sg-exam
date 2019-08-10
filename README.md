## 简介

- 重写[spring-cloud-online-exam](https://gitee.com/wells2333/spring-cloud-online-exam)

- 采用前后端分离技术

- 前端采用`vue2.0`，通过`jwt`和后端交互

- 后端基于`spring boot`、`spring cloud`、`MySQL`等技术实现权限管理、考试管理等功能。

### 在线体验

- 前台：[http://it99.club](http://it99.club)

- 后台：[http://it99.club:81](http://it99.club:81)

默认账号：

单位ID：gitee

1. 管理员：admin/123456
2. 学生：student/123456
3. 教师：teacher/123456

交流QQ群：<a target="_blank" href="https://jq.qq.com/?_wv=1027&k=5RKZNF2"><img border="0" src="http://pub.idqqimg.com/wpa/images/group.png" alt="Spring Cloud考试系统学习" title="Spring Cloud考试系统学习"></a>

QQ群号：996208878

如果您觉得有帮助，请点右上角 "Star" 或者项目底部的“捐助”支持一下，谢谢！

### 部署文档

- [在线考试系统部署文档](docs/在线考试系统V3.0_部署文档.md)

- [基于docker-compose部署](docs/在线考试系统V3.0_部署文档(docker版).md)

### 源码地址

- 前台ui：[spring-microservice-exam-web](https://gitee.com/wells2333/spring-microservice-exam-web.git)
- 后台ui：[spring-microservice-exam-ui](https://gitee.com/wells2333/spring-microservice-exam-ui.git)
- 后端：[spring-microservice-exam](https://gitee.com/wells2333/spring-microservice-exam.git)

## 技术选型

- 服务注册与发现：`Consul`
- 熔断器：`Hystrix` + `Turbine`
- 客户端负载均衡：`Ribbon`
- 内部服务调用：`Feign`
- 网关：`Spring Cloud Gateway`
- 认证鉴权：`Spring Cloud OAuth2` + `JWT`
- 程序监控：`Spring Boot Admin` / `Spring Boot Actuator`
- 分布式配置中心：`Spring Cloud Config`
- 分布式调用链监控：`Spring Cloud Sleuth` + `Zipkin`
- 数据库：`MySQL 5.7`
- 部署：`Docker` + `docker-compose`
- 构建工具：`Maven`
- 后台 API 文档：`Swagger`
- 消息队列：`RabbitMQ`
- 文件系统：`FastDFS`
- 缓存：`Redis`
- 前端：`vue`

## 核心依赖

|      名称      |   版本    |
| --------- | -------- |
| `Spring Boot`    | `2.1.3.RELEASE`  |
| `Spring Cloud`   | `Greenwich.SR1`  |

## 系统架构

![image](docs/产品设计/系统架构图v3.0.jpg)

## 功能概述

项目分前台网站和后台管理两部分，前台主要提供考试功能，后台提供基础管理、考试管理功能。

前台主要提供在线考试、在线学习功能

后台管理分为：系统管理、系统监控、考务管理、附件管理、个人管理

![image](docs/产品设计/系统功能.jpg)

- [在线考试系统V3.0功能概述](docs/在线考试系统V3.0_功能概述.md)

## 功能演示

### 前台功能

1. 登录

![image](docs/images/image_web_login.png)

2. 考试
![image](docs/images/image_web_exam.png)

![image](docs/images/image_web_text_subject.png)

3. 答题卡
![image](docs/images/image_web_exam_card.png)

4. 查看成绩
![image](docs/images/image_web_exam_score.png)

5. 查看错题
![image](docs/images/image_web_incorrect_answer.png)

### 后台功能

1. 总体功能
![image](docs/images/image_ui_menu.png)

2. 考试管理
![image](docs/images/image_ui_exam.png)

3. 题目管理
![image](docs/images/image_ui_exam_subject.png)

![image](docs/images/image_ui_subjects_rich_edit.png)

4. 题库管理
![image](docs/images/image_ui_subject.png)

5. 个人资料
![image](docs/images/image_ui_msg.png)

## 后续

- [ ] 作业、考试、知识点管理、考试成绩排名、图表展示
- [ ] 学生签到、请假
- [x] 短信验证码，第三方登录
- [ ] 简答题，题库完善，智能组卷，在线学习，成绩排名

## 问题反馈

欢迎提交 issue，请写清楚遇到问题的原因、浏览器、操作系统环境、重现的流程和报错日志等。 

如果有开发能力，建议在本地调试出出错的代码。

## 参考资料

- [在线考试系统V3.0系统架构图](https://www.processon.com/view/link/5cf88937e4b0a64c88aaa3e4)

- [在线考试系统V3.0使用文档](docs/在线考试系统V3.0_使用文档.md)

- [在线考试系统V3.0设计文档-数据库设计](docs/在线考试系统V3.0_数据库设计.md)

- [在线考试系统V3.0部署文档](docs/在线考试系统V3.0_部署文档.md)

- [在线考试系统V3.0部署文档(docker版)](docs/在线考试系统V3.0_部署文档(docker版).md)

- [在线考试系统V3.0项目结构说明文档](docs/在线考试系统V3.0_项目结构说明.md)

- [微服务架构下的安全设计方案](http://ehedgehog.net/2019/03/23/%E5%BE%AE%E6%9C%8D%E5%8A%A1%E6%9E%B6%E6%9E%84%E4%B8%8B%E7%9A%84%E5%AE%89%E5%85%A8%E8%AE%BE%E8%AE%A1%E6%96%B9%E6%A1%88/)

- [在线考试系统V3.0镜像构建、推送、部署](http://ehedgehog.net/2019/04/22/%E5%9C%A8%E7%BA%BF%E8%80%83%E8%AF%95%E7%B3%BB%E7%BB%9FV2.0%E9%95%9C%E5%83%8F%E6%9E%84%E5%BB%BA%E3%80%81%E6%8E%A8%E9%80%81%E3%80%81%E9%83%A8%E7%BD%B2/)

***

## 关于

作者：1633736729@qq.com