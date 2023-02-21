<template>
  <div>
      <div class="header-area">
        <div class="clever-main-menu">
          <div class="classy-nav-container breakpoint-off">
            <nav class="classy-navbar justify-content-between" id="cleverNav">
              <a class="nav-brand hidden-sm-only" href="/">{{sysConfig.sys_web_name}}</a>
              <div class="classy-menu">
                <div class="classynav">
                  <div class="search-area hidden-sm-only">
                    <el-input type="search" prefix-icon="el-icon-search" v-model="query" name="search" id="search" placeholder="搜索" @keyup.enter="search()"/>
                  </div>
                  <el-menu :default-active="activeIndex"
                           mode="horizontal"
                           text-color="rgba(0, 0, 0, 0.45)"
                           active-text-color="#232323"
                           :unique-opened=true
                           @select="handleSelect">
                    <el-menu-item index="/index" @click="open('/home')">首页</el-menu-item>
                    <el-menu-item index="/exams" @click="open('/exams')">考试</el-menu-item>
                    <el-menu-item index="/courses" @click="open('/courses')">课程</el-menu-item>
                    <el-submenu index="/other">
                      <template slot="title">记录</template>
                      <el-menu-item index="exam-record" @click="open('/exam-record')">考试记录</el-menu-item>
                    </el-submenu>
                    <el-submenu index="/u">
                      <template slot="title">帮助</template>
                      <el-menu-item index="u-source" @click="open('https://gitee.com/wells2333/sg-exam')">
                        源码地址
                      </el-menu-item>
                      <el-menu-item index="u-deploy" @click="open('https://mp.weixin.qq.com/s?__biz=Mzg2Mjg2OTcyNA==&amp;mid=2247484080&amp;idx=1&amp;sn=85d5bd3a9d03b710903076ab14b20e92&amp;chksm=ce000303f9778a15c0b7afd21fd82aad685f13eb09a0dafa2c8fad97b6eacd9585dd738a0f0a&token=755573063&lang=zh_CN#rd')">
                        部署文档
                      </el-menu-item>
                      <el-menu-item index="c-log" @click="open('https://gitee.com/wells2333/sg-exam/blob/master/CHANGELOG.md')">
                        更新日志
                      </el-menu-item>
                      <el-menu-item index="c-overview"  @click="open('https://www.yuque.com/tangyi-5ldnl/paf15u/cwvtvfd0a07ozfk2?singleDoc#')">
                        规划总览
                      </el-menu-item>
                      <el-menu-item index="u-admin" @click="openAdmin()">
                        管理后台
                      </el-menu-item>
                    </el-submenu>
                    <el-submenu v-if="login" index="/user-info">
                      <template slot="title">
                        <img :src="userInfo.avatar" style="height: 30px;border-radius: 50%;margin-right: 6px;"/>
                        {{userInfo.identifier}}
                      </template>
                      <el-menu-item index="account" @click="open('/account')">个人中心</el-menu-item>
                      <el-menu-item index="password" @click="open('/password')">修改密码</el-menu-item>
                      <el-menu-item index="logOut" @click="logOut">退出</el-menu-item>
                    </el-submenu>
                  </el-menu>
                  <div class="register-login-area" v-if="!login">
                    <a class="btn" target="_blank" @click="open('/register')">注册</a>
                    <a class="btn" target="_blank" @click="open('/login')">登录</a>
                  </div>
                </div>
              </div>
            </nav>
          </div>
        </div>
      </div>
    <o-main ref="mainRef"></o-main>
  </div>
</template>

<script>
import OMain from './common/main'
import {mapGetters, mapState} from 'vuex'
import { isNotEmpty, messageWarn } from '@/utils/util'

export default {
  components: {
    OMain
  },
  computed: {
    ...mapState({
      userInfo: state => state.user.userInfo,
    }),
    ...mapGetters([
      'sysConfig'
    ])
  },
  created () {
    this.checkLogin()
  },
  watch: {
    $route () {
      this.checkLogin()
    }
  },
  data () {
    return {
      activeIndex: '/index',
      login: false,
      input: '',
      query: ''
    }
  },
  methods: {
    openAdmin () {
      window.open(window.location.origin + '/admin')
    },
    handleSubmitExam () {
      this.$refs.mainRef.handleSubmitExam()
    },
    // 导航栏切换
    open (path) {
      if (path.startsWith('http')) {
        window.open(path)
        return
      }
      if (path !== this.$route.fullPath) {
        if (this.$route.fullPath === '/start') {
          this.$confirm('是否要结束当前考试?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            // TODO 提交当前考试
            this.$emit('handleSubmitExam')
            this.$router.push({
              path: path,
              query: {}
            })
          }).catch(() => {})
        } else {
          this.$router.push({
            path: path,
            query: {}
          })
        }
      }
    },
    // 选择事件
    handleSelect (item) {
    },
    // 注册
    handleRegister () {
      // 先退出
      // this.logOut()
      this.$router.push('/register')
    },
    // 登录
    handleLogin () {
      this.$router.push('/login')
    },
    // 登出
    logOut () {
      this.$store.dispatch('LogOut').then(() => {
        this.login = false
        this.$router.push('/home')
      }).catch(() => {
        this.login = false
        this.$router.push('/home')
      })
    },
    // 检测登录
    checkLogin () {
      if (this.userInfo.id !== undefined) {
        this.login = true
      }
    },
    search () {
      if (isNotEmpty(this.query)) {
        this.$router.push({name: 'exams', query: {query: this.query}})
      }
    },
    todo () {
      messageWarn(this, '功能正在开发中！')
    }
  }
}
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
  @import "../assets/css/style.scss";
</style>
