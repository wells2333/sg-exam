<template>
  <div>
    <!--主页介绍 -->
    <div id="home-banner">
      <div class="banner-left"></div>
      <div class="banner-main">
        <h1>硕果云</h1>
        <h2>方便好用的教学管理平台</h2>
        <h3>SPRING-MICROSERVICE-EXAM</h3>
        <p>
          基于Spring Cloud搭建的新一代微服务教学管理平台<br>
          提供多租户、权限管理、考试、练习等功能 <br>
        </p>
        <div class="banner-buttons">
<!--
          <router-link to="/register" title="免费注册" class="register-now">免费注册</router-link>
-->
        </div>
      </div>
      <div class="banner-right1"></div>
      <div class="banner-right2">
        <span class="s1"/>
        <span class="s2"/>
        <span class="s3"/>
        <span class="s4"/>
        <span class="s5"/>
      </div>
      <div class="banner-right3">
        <span class="s1"/>
        <span class="s2"/>
        <span class="s3"/>
        <span class="s4"/>
        <span class="s5"/>
        <span class="s6"/>
      </div>
    </div>

    <!-- 产品特色 -->
    <div id="features" class="features">
      <div class="feature">
        <div class="features-msg">
          产品特色
        </div>
        <div class="features-img-list">
          <div class="features-img">
            <div class="img1"></div>
            <div class="name">
              实时保存
            </div>
            <p>
              实时保存答题，不用担心崩溃、重启
            </p>
          </div>

          <div class="features-img">
            <div class="img2"></div>
            <div class="name">
              智能判分
            </div>
            <p>
              交卷后立即出分、分析正确率，主观题可人工阅卷
            </p>
          </div>

          <div class="features-img">
            <div class="img3"></div>
            <div class="name">
              自动分析
            </div>
            <p>
              图表展现最高分、最低分、列表展示每个考试分数
            </p>
          </div>

          <div class="features-img">
            <div class="img4"></div>
            <div class="name">
              错题记录
            </div>
            <p>
              在线练习完后自动判分、错题记录
            </p>
          </div>
        </div>
      </div>
    </div>

    <!-- 适用场景 -->
    <div id="scenes" class="scenes">
      <div class="scene">
        <div class="scenes-msg">
          适用场景
        </div>
        <div class="scene-img-list">
          <div class="scenes-img">
            <div class="img1"></div>
            <div class="name">
              内部考核
            </div>
            <p>
              入职培训、岗位评级
            </p>
          </div>

          <div class="scenes-img">
            <div class="img2"></div>
            <div class="name">
              经销商考核
            </div>
            <p>
              产品知识、销售技巧
            </p>
          </div>

          <div class="scenes-img">
            <div class="img3"></div>
            <div class="name">
              培训机构
            </div>
            <p>
              随堂测验、课后作业
            </p>
          </div>

          <div class="scenes-img">
            <div class="img4"></div>
            <div class="name">
              招聘考试
            </div>
            <p>
              校园招聘、社会招聘
            </p>
          </div>
        </div>
      </div>
    </div>
    <div v-if="isActive" class="go-top-box" @click="goTop(step)">
      <i class="top-icon el-icon-caret-top"></i>
    </div>
    <o-footer></o-footer>
  </div>
</template>

<script>
import OFooter from '../common/footer'

export default {
  props: {
    step: {
      type: Number,
      default: 50
    }
  },
  data () {
    return {
      isActive: false
    }
  },
  components: {
    OFooter
  },
  methods: {
    // 返回顶部
    goTop: function (i) {
      document.documentElement.scrollTop -= i
      if (document.documentElement.scrollTop > 0) {
        setTimeout(() => this.goTop(i), 16)
      }
    }
  },
  created () {
    let vm = this
    window.onscroll = function () {
      vm.isActive = document.documentElement.scrollTop > 60
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" rel="stylesheet/scss" scoped>
  #home-banner {
    height: 768px;
    width: 1920px;
    left: 50%;
    -webkit-transform: translateX(-50%);
    transform: translateX(-50%);
    background-image: url("../../../static/images/home/banner-bg.png");
    background-position: 50%;
    position: relative;
    z-index: 1;
    margin-bottom: 60px;
  }
  .banner-left {
    width: 163px;
    height: 435px;
    position: absolute;
    top: 170px;
    left: 0;
    z-index: 1;
    background-image: url("../../../static/images/home/banner-left.png");
    -webkit-animation: fadeInUp .8s ease-out both;
    animation: fadeInUp .8s ease-out both;
  }
  .banner-main {
    width: 1220px;
    margin: 0 auto;
    position: relative;
    z-index: 2;
    font-family: PingFangSC-Medium,PingFangSC;
    padding-top: 140px;
    h1 {
      color: #fff;
      font-size: 100px;
      margin: 0;
      line-height: 1;
      padding-left: 20px;
    }
    h2 {
      color: #67eedf;
      font-size: 34px;
      margin: 0;
      border-bottom: 0;
      padding-left: 20px;
    }
    h3 {
      color: #c2bbdd;
      margin: 0;
      font-size: 18px;
      font-weight: 400;
      position: relative;
      z-index: 1;
      padding-left: 20px;
      letter-spacing: 3px;
    }
    h3:after {
      content: "";
      position: absolute;
      z-index: 2;
      left: 20px;
      bottom: -10px;
      width: 45px;
      height: 5px;
      border-radius: 1px;
      background-color: #14f2d5;
    }
    p {
      color: #c2bbdd;
      font-size: 22px;
      line-height: 30px;
      margin: 40px 0 65px;
      padding-left: 20px;
    }
  }
  .banner-right1 {
    width: 127px;
    height: 170px;
    top: 130px;
    right: 385px;
    position: absolute;
    z-index: 2;
    background-size: 100%;
    background-image: url("../../../static/images/home/banner-right01.png");
    -webkit-animation: fadeInUp .8s ease-out .6s both;
    animation: fadeInUp .8s ease-out .6s both;
  }
  .banner-right2 {
    width: 452px;
    height: 446px;
    top: 110px;
    right: 0;
    z-index: 3;
    background-image: url("../../../static/images/home/banner-right02.png");
    -webkit-animation: fadeInUp .8s ease-out .3s both;
    animation: fadeInUp .8s ease-out .3s both;
    .s1 {
      width: 68px;
      height: 148px;
      top: 28px;
      left: 315px;
      background-image: url("../../../static/images/home/banner-right02-04.png");
    }
    .s2 {
      width: 68px;
      height: 118px;
      top: 88px;
      left: 270px;
      background-image: url("../../../static/images/home/banner-right02-02.png");
    }
    .s3 {
      width: 68px;
      height: 97px;
      top: 138px;
      left: 225px;
      background-image: url("../../../static/images/home/banner-right02-03.png");
    }
    .s4 {
      width: 148px;
      height: 172px;
      top: -10px;
      left: 80px;
      background-image: url("../../../static/images/home/banner-right02-05.png");
      background-repeat: no-repeat;
      -webkit-animation: upAndDown 4.5s ease-in-out 1s infinite both;
      animation: upAndDown 4.5s ease-in-out 1s infinite both;
    }
    .s5 {
      width: 360px;
      height: 310px;
      top: -50px;
      left: 74px;
      background-image: url("../../../static/images/home/banner-right02-01.png");
      -webkit-animation: showAndHide 4.5s ease-in-out 1s infinite both;
      animation: showAndHide 4.5s ease-in-out 1s infinite both;
    }
  }
  .banner-right2, .banner-right2 span {
    position: absolute;
    background-size: 100%;
  }
  .banner-right3 {
    width: 565px;
    height: 710px;
    top: 107px;
    right: 300px;
    z-index: 4;
    background-image: url("../../../static/images/home/banner-right03.png");
    -webkit-animation: fadeInUp .8s ease-out both;
    animation: fadeInUp .8s ease-out both;
    .s1 {
      background-image: url("../../../static/images/home/banner-right03-03.png");
      -webkit-animation: showBanner 20s 0s infinite;
      animation: showBanner 20s 0s infinite;
    }
    .s2 {
      background-image: url("../../../static/images/home/banner-right03-04.png");
      -webkit-animation: showBanner 20s 5s infinite;
      animation: showBanner 20s 5s infinite;
    }
    .s3 {
      background-image: url("../../../static/images/home/banner-right03-05.png");
      -webkit-animation: showBanner 20s 10s infinite;
      animation: showBanner 20s 10s infinite;
    }
    .s4 {
      background-image: url("../../../static/images/home/banner-right03-06.png");
      -webkit-animation: showBanner 20s 15s infinite;
      animation: showBanner 20s 15s infinite;
    }
    .s5 {
      width: 152px;
      height: 249px;
      top: 223px;
      left: 85px;
      background-image: url("../../../static/images/home/banner-right03-01.png");
    }
    .s6 {
      width: 182px;
      height: 234px;
      top: 310px;
      left: 200px;
      background-image: url("../../../static/images/home/banner-right03-02.png");
    }
  }
  .banner-right3, .banner-right3 span {
    position: absolute;
    background-size: 100%;
  }

  .banner-right3 .s1, .banner-right3 .s2, .banner-right3 .s3, .banner-right3 .s4 {
    width: 305px;
    height: 385px;
    top: 31px;
    left: 122px;
    opacity: 0;
  }

  @-webkit-keyframes showBanner {
    0% {
      opacity: 0
    }
    5% {
      opacity: 1
    }
    30% {
      opacity: 1
    }
    35% {
      opacity: 0
    }
    to {
      opacity: 0
    }
  }

  @keyframes showBanner {
    0% {
      opacity: 0
    }
    5% {
      opacity: 1
    }
    30% {
      opacity: 1
    }
    35% {
      opacity: 0
    }
    to {
      opacity: 0
    }
  }

  @-webkit-keyframes showAndHide {
    0%, to {
      opacity: 1
    }
    50% {
      opacity: .5
    }
  }

  @keyframes showAndHide {
    0%, to {
      opacity: 1
    }
    50% {
      opacity: .5
    }
  }
  .features {
    .feature {
      position: relative;
      .features-img-list {
        display: flex;
        .features-img {
          -webkit-box-flex: 1;
          flex: 1;
          position: relative;
          z-index: 1;
          .img1 {
            background: url("../../../static/images/home/icon_features1.png") no-repeat center;
            height: 120px;
          }
          .img2 {
            background: url("../../../static/images/home/icon_features2.png") no-repeat center;
            height: 120px;
          }
          .img3 {
            background: url("../../../static/images/home/icon_features3.png") no-repeat center;
            height: 120px;
          }
          .img4 {
            background: url("../../../static/images/home/icon_features4.png") no-repeat center;
            height: 120px;
          }
          .name {
            font-size: 30px;
            line-height: 35px;
            color: #4f403b;
          }
          p {
            font-size: 22px;
            color: #999;
            line-height: 26px;
          }
        }
      }
    }
  }
  .scenes {
    .scene {
      position: relative;
      .scene-img-list {
        display: flex;
        .scenes-img {
          -webkit-box-flex: 1;
          flex: 1;
          position: relative;
          z-index: 1;
          .img1 {
            background: url("../../../static/images/home/icon_scenes1.jpg") no-repeat center;
            height: 280px;
          }
          .img2 {
            background: url("../../../static/images/home/icon_scenes2.jpg") no-repeat center;
            height: 280px;
          }
          .img3 {
            background: url("../../../static/images/home/icon_scenes3.jpg") no-repeat center;
            height: 280px;
          }
          .img4 {
            background: url("../../../static/images/home/icon_scenes4.jpg") no-repeat center;
            height: 280px;
          }
          .name {
            font-size: 30px;
            line-height: 35px;
            color: #4f403b;
          }
          p {
            font-size: 22px;
            color: #999;
            line-height: 26px;
          }
        }
      }
    }
  }
</style>
