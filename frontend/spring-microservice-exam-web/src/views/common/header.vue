<template>
  <div id="header-box" class="header-box">
    <div>
      <header class="header-w">
        <div class="header-w-box">
          <div class="nav-logo">
            <div style="color: rgb(255, 255, 255); font-size: 30px; text-align: center; margin-bottom: 20px; font-family: Roboto; padding-top: 10px;"><div></div>
              <a href="/" class="home-link router-link-exact-active router-link-active">
                <span class="site-name">硕果云</span>
              </a>
              硕果云
            </div>
          </div>
          <div class="nav-bar" style="max-width: 1331px">
            <div class="search-box">
              <input v-model="query" aria-label="Search" autocomplete="off" spellcheck="false" value="" class="" placeholder="搜索看" @keyup.enter="search()">
            </div>
            <div class="nav-links">
              <el-menu :default-active="activeIndex"
                       mode="horizontal"
                       text-color="#FFFFFF"
                       active-text-color="#46bd87"
                       background-color="#3f3955"
                       :unique-opened=true
                       @select="handleSelect">
                <el-menu-item index="/index" @click="open('/home')">首页</el-menu-item>
                <el-submenu index="/functions">
                  <template slot="title">功能</template>
                  <el-menu-item index="exams" @click="open('/exams')">在线考试</el-menu-item>
                  <el-menu-item index="practices" @click="open('/practices')">在线学习</el-menu-item>
                  <el-menu-item index="exam-record" @click="open('/exam-record')">考试记录</el-menu-item>
                  <el-menu-item index="incorrect" @click="open('/incorrect')">错题本</el-menu-item>
                </el-submenu>
                <el-submenu index="/u">
                  <template slot="title">用户指南</template>
                  <el-menu-item index="u-source">
                    <a href="https://gitee.com/wells2333/spring-microservice-exam" target="_blank">源码地址</a>
                  </el-menu-item>
                  <el-menu-item index="u-deploy">
                    <a href="https://www.kancloud.cn/tangyi/spring-microservice-exam/1322870" target="_blank">部署文档</a>
                  </el-menu-item>
                  <el-menu-item index="u-admin">
                    <a href="http://118.25.138.130:81" target="_blank">管理后台</a>
                  </el-menu-item>
                </el-submenu>
                <el-submenu index="/c">
                  <template slot="title">版本更新</template>
                  <el-menu-item index="c-log">
                    <a href="https://gitee.com/wells2333/spring-microservice-exam/blob/master/CHANGELOG.md" target="_blank">更新日志</a>
                  </el-menu-item>
                  <el-menu-item index="c-overview">
                    <a href="https://www.kancloud.cn/tangyi/spring-microservice-exam/1322864#6__112" target="_blank">规划总览</a>
                  </el-menu-item>
                </el-submenu>
                <el-menu-item v-if="!login" index="/login" @click="open('/login')">登录</el-menu-item>
                <el-menu-item v-if="!login" index="/register" @click="open('/register')">注册</el-menu-item>
                <el-submenu v-if="login" index="/user-info">
                  <template slot="title">
                    <img :src="userInfo.avatarUrl" style="height: 30px;border-radius: 50%;"/>
                    {{userInfo.identifier}}
                  </template>
                  <el-menu-item index="account" @click="open('/account')">个人中心</el-menu-item>
                  <el-menu-item index="password" @click="open('/password')">修改密码</el-menu-item>
                  <el-menu-item index="logOut" @click="logOut">退出</el-menu-item>
                </el-submenu>
              </el-menu>
            </div>
          </div>
        </div>
      </header>
      <div class="sidebar-mask"></div>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import { isNotEmpty } from '@/utils/util'

export default {
  mounted () {
    // handleScroll为页面滚动的监听回调
    window.addEventListener('scroll', this.handleScroll)
  },
  destroyed () {
    // 同时在destroyed回调中移除监听：
    window.removeEventListener('scroll', this.handleScroll)
  },
  computed: {
    // 获取用户信息
    ...mapState({
      userInfo: state => state.user.userInfo
    })
  },
  created () {
    this.checkLogin()
  },
  // 检测路由变化
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
    // 导航栏切换
    open (path) {
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
    handleScroll () {
      let nav = document.getElementById('header-box')
      let scrollTop = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop
      if (scrollTop > 50) {
        nav.style.position = 'fixed'
        nav.style.top = '0'
        nav.style.zIndex = '99999'
      } else {
        nav.style.position = 'relative'
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
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" rel="stylesheet/scss" scoped>
  @import "../../assets/css/mixin.scss";
  header {
    height: 70px;
    padding: 0;
    line-height: 70px;
    background: #3f3955;
    color: #fff;
    border-bottom: 0;
    box-sizing: border-box;
  }
  .header-box {
    width: 100%;
    background-color: #3f3955;
  }
  .header-w-box {
    display: flex;
    justify-content: space-between;
    align-items: center;
    height: 100%;
    .nav-logo {
      vertical-align: top;
      cursor: pointer;
      .home-link {
        margin-right: -100px;
      }
      .site-name {
        display: inline-block;
        font-size: 0;
        width: 104px;
        height: 32px;
        background-size: 100%;
        margin: 17px 0 0 33px;
      }
    }
    .nav-bar {
      background-color: transparent;
      top: 17px;
      line-height: 36px;
      margin-right: 10px;
      .search-box {
        margin-top: 13px;
        display: inline-block;
        position: relative;
        margin-right: 1rem;
        -webkit-box-flex: 0;
        flex: 0 0 auto;
        vertical-align: top;
        input {
          cursor: text;
          width: 10rem;
          height: 2rem;
          display: inline-block;
          border-radius: 2rem;
          font-size: .9rem;
          line-height: 2rem;
          padding: 0 .5rem 0 2rem;
          outline: none;
          -webkit-transition: all .2s ease;
          transition: all .2s ease;
          color: #877fa3;
          border: 1px solid #42e2c3;
          background: transparent url("../../../static/images/home/search.png") no-repeat .6rem .5rem;
          background-size: 20px;
        }
      }
      .nav-links {
        display: inline-block;
      }
    }
    .el-menu.el-menu--horizontal {
      border-bottom: 0;
      .el-menu-item {
        position: relative;
        display: inline-block;
      }
    }
    .nav-list {
      display: flex;
      justify-content: center;
      align-items: center;
      margin-right: 22px;
      .el-autocomplete {
        width: 305px;
      }
      a {
        width: 110px;
        color: #c8c8c8;
        display: block;
        font-size: 14px;
        padding: 0 25px;
        &:hover {
          color: #fff;
        }
      }
      a:nth-child(2) {
        margin-left: -10px;
      }
    }
    .right-box {
      display: flex;
    }
  }
  // 用户信息弹出
  .nav-user-wrapper {
    position: absolute;
    z-index: 30;
    padding-top: 18px;
    opacity: 0;
    visibility: hidden;
    top: -3000px;
    .nav-user-list {
      position: relative;
      padding-top: 20px;
      background: #fff;
      border: 1px solid rgba(0, 0, 0, .08);
      border-radius: 8px;
      box-shadow: 0 20px 40px rgba(0, 0, 0, .15);
      z-index: 10;
      li {
        list-style: none;
      }
    }
  }
  .nav-bar {
    display: flex;
  }
  .sidebar-mask {
    z-index: 9;
    width: 100vw;
    height: 100vh;
    display: none;
    position: fixed;
    top: 0;
    left: 0;
  }

  /* 注册登录按钮 */
  .login-button {
    margin: 10px;
  }

  /* 头像 */
  .avatar {
    border-radius: 50%;
    display: block;
    width: 40px;
    height: 40px;
    background-repeat: no-repeat;
    background-size: contain;
    cursor: pointer;
  }

  .username {
    padding: 8px;
    color: rgb(134, 134, 134);
    position: relative;
    ul {
      padding: 10px;
    }
    &.fixed {
      width: 262px;
      position: fixed;
      left: 50%;
      margin-left: 451px;
      margin-top: 0;
      z-index: 32;
      top: -40px;
      -webkit-transform: translate3d(0, 59px, 0);
      transform: translate3d(0, 59px, 0);
      -webkit-transition: -webkit-transform .3s cubic-bezier(.165, .84, .44, 1);
      transition: transform .3s cubic-bezier(.165, .84, .44, 1);
      .user {
        &:hover {
          a:before {
            background-position: -215px 0;
          }
        }
      }
    }
    .nav-user-wrapper {
      width: 168px;
      transform: translate(-50%);
      left: 50%;
    }
    .nav-user-list {
      width: 168px;
      &:before {
        left: 50%;
      }
    }
    &:hover {
      a:before {
        background-position: -5px 0;
      }
      .nav-user-wrapper {
        top: 30px;
        visibility: visible;
        opacity: 1;
        -webkit-transition: opacity .15s ease-out;
        transition: opacity .15s ease-out;
      }
    }
    > a {
      position: relative;
    }
    li {
      list-style: none;
      text-align: center;
      position: relative;
      border-top: 1px solid #f5f5f5;
      line-height: 44px;
      height: 44px;
      color: #616161;
      font-size: 12px;
      &:hover {
        background: #fafafa;
      }
      a {
        display: block;
        color: #616161;
      }
    }
  }
</style>
