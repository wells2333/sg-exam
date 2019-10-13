<template>
  <div class="footer">
    <div class="container">
      <div class="site-info">
        <el-row>
          <el-col :span="4" :offset="2" class="footer-col">
            <h3 class="c1">服务支持</h3>
            <ul>
              <li class="c2"><a class="c3" target="_blank" href="https://gitee.com/wells2333/spring-microservice-exam">官方开源</a></li>
              <li class="c2"><a class="c3" target="_blank" href="https://gitee.com/wells2333">前后端项目</a></li>
            </ul>
          </el-col>
          <el-col :span="4" class="footer-col">
            <h3 class="c1">自助服务</h3>
            <ul>
              <li class="c2"><a class="c3" target="_blank" href="http://ehedgehog.net/">个人博客</a></li>
              <li class="c2"><a class="c3" target="_blank" href="https://gitee.com/wells2333">个人简介</a></li>
            </ul>
          </el-col>
          <el-col :span="4" class="footer-col">
            <h3 class="c1">其他项目</h3>
            <ul>
              <li class="c2"><a class="c3" target="_blank" href="http://spring.io/">开发框架</a></li>
              <li class="c2"><a class="c3" target="_blank" href="https://gitee.com/wells2333">小程序工厂</a></li>
            </ul>
          </el-col>
          <el-col :span="4" class="footer-col">
            <h3 class="c1">友情链接</h3>
            <ul>
              <li class="c2"><a class="c3" target="_blank" href="https://cn.vuejs.org/">Vue</a></li>
              <li class="c2"><a class="c3" target="_blank" href="http://element-cn.eleme.io">element</a></li>
            </ul>
          </el-col>
          <el-col :span="4" class="footer-col">
            <h3 class="c1">关注我</h3>
            <ul>
              <li class="c2"><a class="c3" target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=1633736729&site=qq&menu=yes">腾讯 QQ</a></li>
              <li class="c2"><a class="c3" target="_blank" href="mailto:1633736729@qq.com">官方邮箱</a></li>
            </ul>
          </el-col>
        </el-row>
      </div>

      <div class="copyright">
        <h4 class="content-c2">Copyright ©2019</h4>
        <ul class="privacy">
          <li class="content-c1"><a class="content-c0" @click="openLayer">法律声明</a></li>
          <li class="content-c1"><a class="content-c0" @click="openPrivacy">隐私条款</a></li>
          <li class="content-c1"><a class="content-c0" target="_blank" href="javascript:void(-1);">开发者中心</a></li>
        </ul>
      </div>
      <div class="cop">
        <a class="content-c3" href="https://gitee.com/wells2333" target="_blank">
          <span class="content-c3">粤ICP备18038322号</span>
          <span class="content-c3">粤ICP备18038322号-1</span>
        </a>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  data () {
    return {
    }
  },
  methods: {
    openLayer () {
      this.$notify.info({
        title: '法律声明',
        message: '此仅为个人练习开源模仿项目，仅供学习参考，承担不起任何法律问题'
      })
    },
    openPrivacy () {
      this.$notify.info({
        title: '隐私条款',
        message: '本网站将不会严格遵守有关法律法规和本隐私政策所载明的内容收集、使用您的信息'
      })
    },
    openHelp () {
      this.$notify({
        title: '离线帮助',
        message: '没人会帮助你，请自己靠自己',
        type: 'warning'
      })
    }
  },
  components: {}
}
</script>
<style lang="scss" rel="stylesheet/scss" scoped>
  .footer {
    background: #f6f6f6;
    height: 350px;
    display: flex;
    flex-direction: column;
    align-items: center;
  }

  .container {
    width: 1220px;
  }

  .site-info {
    height: 100px;
    padding: 50px 0 130px;
    border-bottom: 1px solid #e6e6e6;
    position: relative;
  }

  .c1 {
    color: #646464;
    font-size: 12px;
    padding: 0 0 14px;
  }

  .c2 {
    color: #c3c3c3;
    font-size: 12px;
    padding: 6px 0;
    list-style: none;
  }

  .c3 {
    color: #969696;
  }

  .c4 {
    position: absolute;
    right: 0;
    overflow: hidden;
    line-height: 34px;
  }

  .tel {
    font-size: 30px;
    line-height: 1;
    color: #646464;
    top: -2px;
    position: relative;
  }

  .c5 {
    color: #646464;
    right: -70px;
    position: relative;
  }

  .time {
    margin-top: 5px;
    right: -4px;
    position: relative;
    clear: both;
    width: 241px;
    font-size: 12px;
    line-height: 18px;
    color: #c3c3c3;
    text-align: right;
  }

  .online {
    clear: both;
    width: 241px;
    font-size: 12px;
    line-height: 18px;
    color: #c3c3c3;
    text-align: right;
  }

  .button {
    width: 130px;
    height: 34px;
    font-size: 14px;
    color: #5079d9;
    border: 1px solid #dcdcdc;
    margin-top: 8px;
  }

  .copyright {
    color: #434d55;
    font-size: 12px;
    padding: 10px 0 0;
    display: flex;
    align-items: left;
  }

  .privacy {
    float: left;
    margin: 0 0 0 12px;
  }

  .content-c0 {
    color: #5079d9;
    cursor: pointer;
    text-decoration: none;
    &:hover {
      color: #3A5FCD;
    }
  }

  .content-c1 {
    float: left;
    line-height: 12px;
    padding: 1px 10px 0;
    border-left: 1px solid #ccc;
    list-style: none;
  }

  .content-c2 {
    float: left;
    height: 15px;
    line-height: 15px;
    color: #757575;
    margin: 0 0 0 12px;
  }

  .cop {
    clear: both;
    height: 15px;
  }

  .content-c3 {
    margin-right: 20px;
    color: #bdbdbd;
    font-size: 12px;
    height: 12px;
    line-height: 1;
  }

  .footer-col ul {
    padding-inline-start: 0px;
  }
</style>
