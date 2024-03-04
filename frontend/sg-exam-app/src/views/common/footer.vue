<template>
  <div class="footer">
    <div class="container">
      <div class="site-info">
        <el-row>
          <el-col :span="6" :offset="2" class="footer-col">
            <a target="_blank" href="https://gitee.com/wells2333/sg-exam">{{$t('common.gitee')}}</a>
            <a target="_blank" href="https://github.com/wells2333/sg-exam">{{$t('common.github')}}</a>
            <a target="_blank" href="javascript:void(0);"
               onclick="window.open(window.location.origin + '/admin');return false;">{{$t('common.admin')}}</a>
          </el-col>
          <el-col :span="6" class="footer-col">
            <a target="_blank" href="https://element.eleme.cn/#/zh-CN">Element UI</a>
            <a target="_blank" href="https://cn.vuejs.org/">Vue</a>
          </el-col>
          <el-col :span="6" class="footer-col">
            <a target="_blank" href="https://gitee.com/wells2333/sg-exam/blob/master/CHANGELOG.md">{{$t('common.changeLog')}}</a>
            <a target="_blank" href="https://gitee.com/wells2333/sg-exam/issues">{{$t('common.feedback')}}</a>
          </el-col>
        </el-row>
      </div>
      <el-row :gutter="20">
        <el-col :span="12" :offset="8">
          <div class="copyright">
            <h4 class="content-c2" style="text-align: center;">Copyright ©{{ copyright }}</h4>
            <ul class="privacy">
              <li class="content-c1"><a class="content-c0" @click="openLayer">{{$t('common.legalNotices')}}</a></li>
              <li class="content-c1"><a class="content-c0" @click="openPrivacy">{{$t('common.privacyPolicy')}}</a></li>
              <li class="content-c1"><a class="content-c0" target="_blank"
                                        href="https://gitee.com/wells2333/sg-exam">{{$t('common.developerCenter')}}</a>
              </li>
            </ul>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12" :offset="6">
          <div class="cop">
            <a class="content-c3" href="https://beian.miit.gov.cn/" target="_blank">
              <span class="content-c3">{{ sysConfig.sys_web_copyright}}</span>
            </a>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>
<script>
import {mapGetters} from 'vuex'
export default {
  data() {
    return {}
  },
  computed: {
    ...mapGetters([
      'sysConfig'
    ]),
    copyright() {
      let str = this.sysConfig.sys_web_copyright;
      let pos = str.indexOf("备");
      let copyright = str.substring(pos + 1).trim();
      copyright = copyright.substring(0, 4);
      if (copyright === "" || copyright == null) {
        copyright = 2022;
      }
      return copyright;
    }
  },
  methods: {
    openLayer() {
      this.$notify.info({
        title: this.$t('common.legalNotices'),
        message: this.$t('common.legalNoticesContent'),
        offset: 70
      })
    },
    openPrivacy() {
      this.$notify.info({
        title: this.$t('common.privacyPolicy'),
        message: this.$t('common.privacyPolicyContent'),
        offset: 70
      })
    }
  },
  components: {}
}
</script>
<style lang="scss" rel="stylesheet/scss" scoped>
.footer {
  background-position: 50%;
  background-color: #252525;
  width: 100%;
  box-sizing: border-box;
  padding: 10px 0;
}

.container {
  width: 1150px;
  margin: 0 auto;
  position: relative;
  z-index: 1;
}

.c5 {
  color: #646464;
  right: -70px;
  position: relative;
}

.copyright {
  color: #434d55;
  font-size: 12px;
  padding: 20px 0 0;
  display: flex;
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
  color: #ffffffa6;
  margin: 0 0 0 12px;
}

.cop {
  text-align: center;
  height: 15px;
}

.content-c3 {
  margin-right: 20px;
  color: #ffffffa6;
  font-size: 12px;
  height: 12px;
  line-height: 1;
}

.footer-col {
  width: 200px;
  float: left;

  a {
    display: block;
    margin: 5px 0;
    font-size: 14px;
    color: #ffffffa6;
    font-weight: normal;
  }

  .we-chat {
    position: absolute;
    right: 0;
    width: 216px;
    z-index: 2;
    padding-top: 260px;
    background-image: url("../../../static/img/core-img/qq.png");
    background-repeat: no-repeat;
    background-position: top;
    text-align: center;
    line-height: 22px;
  }

  .we-chat-new {
    position: absolute;
    width: 216px;
    z-index: 2;
    padding-top: 260px;
    color: #ffffff;
    background-image: url("../../../static/img/core-img/qq_new.png");
    background-repeat: no-repeat;
    background-position: top;
    text-align: center;
    line-height: 22px;
  }
}
</style>
