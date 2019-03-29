# spring-microservice-exam

### 简介

重写[spring-cloud-online-exam](https://gitee.com/wells2333/spring-cloud-online-exam)

采用前后端分离技术：

前端采用`vue2.0`，通过`jwt`和后端交互

后端基于`spring boot`、`spring cloud`、`MySQL`、`Redis`、`RabbitMq`等技术实现单点登录、动态路由等功能。

访问地址：[在线考试](http://182.254.233.125)（服务器资源有限，只部署了前端页面）

### 技术选型

|      名称      |   版本    |
| --------- | -------- |
| Spring Boot    | 2.1.3.RELEASE  |
| Spring Cloud   | Greenwich.SR1  |
| MySql   | Greenwich.SR1  |
| Redis   | Greenwich.SR1  |
| RabbitMQ   | Greenwich.SR1  |

### 系统架构

![image](doc/产品设计/系统架构图.png)

### 功能概述

项目分前台网站和后台管理两部分，前台主要提供考试功能，后台提供考试管理功能。

前台主要提供在线考试、在线学习功能

后台管理分为：系统管理、系统监控、考务管理、附件管理、个人管理

系统管理：提供用户、部门、角色、权限等基础管理
- 用户管理：用户信息增删改查、导入导出
- 部门管理：部门信息增删改查
- 角色管理：角色信息增删改查、分配权限
- 菜单管理：菜单信息增删改查、导入导出

系统监控：监控服务、日志等
- 日志监控：查看系统日志
- `consul`监控：`consul`服务监控
- `zipkin`监控：监控服务的调用链路
- 服务监控：`spring boot admin`服务监控
- 接口文档：`swagger api`文档

考务管理：提供课程、考试、题库、成绩等管理
- 课程管理：课程信息增删改查
- 考试管理：考试信息增删改查、题目管理、发布回收
- 题库管理：题目分类增删改查、题目信息增删改查
- 成绩管理：查看成绩、导出成绩
- 知识库：知识库增删改查、上传附件

附件管理：项目的所有附件存储在`fastDfs`里，提供统一的管理入口
- 附件列表：管理所有附件，如用户头像、考试附件、知识库附件等。

个人管理：管理个人资料和修改密码
- 个人资料：姓名、头像等基本信息的修改
- 修改密码：修改密码

### 功能演示

前台

![image](doc/images/image_web_exam.png)

![image](doc/images/image_web_exam_card.png)

![image](doc/images/image_web_incorrect_answer.png)

后台

![image](doc/images/image_ui_menu.png)

![image](doc/images/image_ui_exam.png)

![image](doc/images/image_ui_exam_subject.png)

![image](doc/images/image_ui_subject.png)

![image](doc/images/image_ui_msg.png)

#### 部署文档

[在线考试系统部署文档](doc/在线考试系统V2.0_部署文档.md)

基于docker-compose部署（待补充）

### 后续

- [ ] 网关实现基于数据库、Redis的动态路由
- [ ] zipkin链路监控
- [ ] 容器化部署文档
- [ ] 业务功能完善
- [ ] 补充相关中间件的文档

### 问题反馈

欢迎提交 issue，请写清楚遇到问题的原因，浏览器和操作系统环境，重现的流程。 

如果有开发能力，建议在本地调试出出错的代码。

### 参考资料

[在线考试系统部署文档](doc/在线考试系统V2.0_部署文档.md)

[在线考试系统项目结构说明文档](doc/在线考试系统V2.0_项目结构说明.md)

[微服务架构下的安全设计方案](http://ehedgehog.net/2019/03/23/%E5%BE%AE%E6%9C%8D%E5%8A%A1%E6%9E%B6%E6%9E%84%E4%B8%8B%E7%9A%84%E5%AE%89%E5%85%A8%E8%AE%BE%E8%AE%A1%E6%96%B9%E6%A1%88/)

***

### 关于

作者：1633736729@qq.com