<h1 align="center">Welcome to sg-exam-next 👋</h1>
<p>
  <img alt="Version" src="https://img.shields.io/badge/version-5.0.7-blue.svg?cacheSeconds=2592000" />
  <a href="https://mp.weixin.qq.com/s/oal9atlSQxfL4YOiLtYfuA" target="_blank">
    <img alt="Documentation" src="https://img.shields.io/badge/documentation-yes-brightgreen.svg" />
  </a>
  <a href="#" target="_blank">
    <img alt="License: Apache2.0" src="https://img.shields.io/badge/License-Apache2.0-yellow.svg" />
  </a>
</p>

> 硕果云，基于Spring Boot搭建的方便易用、高颜值的教学管理平台，提供权限管理、考试、问卷、练习等功能
>
> 主要功能为在线学习、考试、练习
> 
> 课程内容支持图文、视频，考试类型支持考试、练习、问卷
> 
> 题型支持单选题、多选题、判断题、简答题、视频、语音

> **注意：本版本为Spring Boot版本，没有太多中间件依赖，使用、部署都非常方便，并且持续更新维护**
> 
> **v5.0.0版本以前为Spring Cloud版本，依赖了比较多的中间件，代码在sc分支**
> 
> **Spring Cloud版本已不再维护！已不再维护！已不再维护！**

### ✨ [在线体验](https://www.yunmianshi.com/)

|   平台   |      地址      |   账号密码    |
| --------- | -------- | -------- |
|  前台  |  [www.yunmianshi.com](https://www.yunmianshi.com)  |   账号：preview，密码：123456 |
|  后台  |  [www.yunmianshi.com/admin](https://www.yunmianshi.com/admin)   |  账号：preview，密码：123456 |

小程序和公众号：

|   平台   |      二维码     |   说明  |
| --------- | -------- | -------- |
|  小程序  |  <img src="https://gitee.com/wells2333/sg-exam/raw/master/docs/images/wx.jpg" width="160"/> | 小程序体验 |
|  公众号  |  <img src="https://gitee.com/wells2333/sg-exam/raw/master/docs/images/wxapp.jpeg" height="130"/> | 发布部署文档、源码解析相关的文章 |

## 系统架构

<img src="https://gitee.com/wells2333/sg-exam/raw/master/docs/images/framework.png"/>

## 功能概述

项目分web前台、后台管理、小程序三部分，前台、小程序主要提供考试功能，后台提供基础管理、考试管理功能。

web前台主要功能：提供在线考试、课程学习、练习等功能

后台主要功能：系统管理（单位管理、用户管理、部门管理、角色管理、菜单管理、操作日志、代码生成），考务管理（课程管理、考试管理、题库管理、成绩管理）

课程用于提供在线学习功能，支持配置章、节、知识点，课程内容支持图文、视频

考试类型支持考试、练习、问卷，答题模式支持一次性答题、顺序答题，添加题目支持手动添加、从题库选择、随机组题

用户可收藏考试、题目、课程，更多功能可自行体验

## 部署文档 & 操作手册 & 源码解析 & 视频教程

[SG-EXAM-NEXT](https://mp.weixin.qq.com/s?__biz=Mzg2Mjg2OTcyNA==&amp;mid=2247484080&amp;idx=1&amp;sn=85d5bd3a9d03b710903076ab14b20e92&amp;chksm=ce000303f9778a15c0b7afd21fd82aad685f13eb09a0dafa2c8fad97b6eacd9585dd738a0f0a&token=755573063&lang=zh_CN#rd)

功能规划: [Roadmap](https://www.yuque.com/tangyi-5ldnl/paf15u/cwvtvfd0a07ozfk2?singleDoc#)

## 功能截图

<table>
    <tr>
        <td><img src="https://gitee.com/wells2333/sg-exam/raw/master/docs/images/web_courses.png" height="200"/></td>
        <td><img src="https://gitee.com/wells2333/sg-exam/raw/master/docs/images/web_course_detail.png" height="200"/></td>
    </tr>
    <tr>
        <td><img src="https://gitee.com/wells2333/sg-exam/raw/master/docs/images/web_course_chapter.png" height="200"/></td>
        <td><img src="https://gitee.com/wells2333/sg-exam/raw/master/docs/images/web_course_section.png" height="200"/></td>
    </tr>
    <tr>
        <td><img src="https://gitee.com/wells2333/sg-exam/raw/master/docs/images/admin_course_evaluate.png" height="200"/></td>
        <td><img src="https://gitee.com/wells2333/sg-exam/raw/master/docs/images/web_1.png" height="200"/></td>
    </tr>
    <tr>
        <td><img src="https://gitee.com/wells2333/sg-exam/raw/master/docs/images/admin_course_evaluate.png" height="200"/></td>
        <td><img src="https://gitee.com/wells2333/sg-exam/raw/master/docs/images/admin_dashboard.png" height="200"/></td>
    </tr>
    <tr>
        <td><img src="https://gitee.com/wells2333/sg-exam/raw/master/docs/images/admin_exam_manage.png" height="200"/></td>
        <td><img src="https://gitee.com/wells2333/sg-exam/raw/master/docs/images/admin_subjects.png" height="200"/></td>
    </tr>
    <tr>
        <td><img src="https://gitee.com/wells2333/sg-exam/raw/master/docs/images/admin_subject_detail.png" height="200"/></td>
        <td><img src="https://gitee.com/wells2333/sg-exam/raw/master/docs/images/admin_courses.png" height="200"/></td>
    </tr>
    <tr>
        <td><img src="https://gitee.com/wells2333/sg-exam/raw/master/docs/images/admin_score_detail.png" height="200"/></td>
        <td><img src="https://gitee.com/wells2333/sg-exam/raw/master/docs/images/admin_menus.png" height="200"/></td>
    </tr>
    <tr>
        <td>
           <img src="https://gitee.com/wells2333/sg-exam/raw/master/docs/images/wxapp_1.png" height="200"/>
           <img src="https://gitee.com/wells2333/sg-exam/raw/master/docs/images/wxapp_2.jpeg" height="200"/>
           <img src="https://gitee.com/wells2333/sg-exam/raw/master/docs/images/wxapp_3.png" height="200"/>
           <img src="https://gitee.com/wells2333/sg-exam/raw/master/docs/images/wxapp_6.png" height="200"/>
           <img src="https://gitee.com/wells2333/sg-exam/raw/master/docs/images/wxapp_7.png" height="200"/>
        </td>
        <td> 
           <img src="https://gitee.com/wells2333/sg-exam/raw/master/docs/images/monitor_1.png" height="200"/>
        </td>
    </tr>
</table>

## 作者

👤 **tangyi**

* Gitee: [@wells2333](https://gitee.com/wells2333)

* Github: [@wells2333](https://github.com/wells2333)

## 🤝 参与贡献

欢迎提交PR、[issues](https://gitee.com/wells2333/sg-exam)一起完善项目

## 反馈交流

交流QQ群：

<img src="https://gitee.com/wells2333/sg-exam/raw/master/docs/images/qq.png" width="160"/>

<img src="https://gitee.com/wells2333/sg-exam/raw/master/docs/images/qq_new.png" width="160"/>

<img src="https://gitee.com/wells2333/sg-exam/raw/master/docs/images/qq_3.png" width="160"/>

## 请作者喝咖啡

如果您觉得有帮助，请点右上角 ⭐️ "Star" 或者**微信扫一扫**支持一下，谢谢！

<img src="https://gitee.com/wells2333/sg-exam/raw/master/docs/images/wechat.png" width="160"/>

## License

[Apache License 2.0](https://gitee.com/wells2333/sg-exam/blob/master/LICENSE)