<template>
  <div id="header-box" class="header-box">
    <div>
      <header class="header-w">
        <div class="header-w-box">
          <div class="nav-logo">
            <h1>
              <router-link to="/" title="在线考试">在线考试</router-link>
            </h1>
          </div>
          <div class="nav-bar">
            <el-menu
              :show-timeout="200"
              :default-active="$route.path"
              class="el-menu-header"
              mode="horizontal">
              <el-menu-item index="/home" @click="open('/home')">首页</el-menu-item>
              <el-menu-item index="/functions" @click="open('/functions')">功能</el-menu-item>
              <el-menu-item index="/us" @click="open('/us')">关于我们</el-menu-item>
            </el-menu>
          </div>
          <div class="line"></div>
          <div class="right-box">
            <div class="nav-list">
              <el-autocomplete
                placeholder="请输入考试信息"
                v-model="input"
                suffix-icon="search"
                :minlength=1
                :maxlength=100
                :fetch-suggestions="querySearchAsync"
                :trigger-on-focus="false"
                :on-icon-click="handleIconClick"
                @keydown.enter.native="handleIconClick"
                @select="handleSelect">
              </el-autocomplete>
            </div>
            <el-button v-if="!login" type="primary" plain size="medium" class="login-button" @click="handleRegister">注册</el-button>
            <el-button v-if="!login" size="medium" plain class="login-button" @click="handleLogin">登录</el-button>
            <div v-if="login">
              <div>
                <img class="avatar" :src="userInfo.avatarUrl">
              </div>
            </div>
            <div class="username" v-if="login">
              <a href="javascript:void(-1);">
                {{userInfo.identifier}}
              </a>
              <i class="el-icon-caret-bottom"></i>
              <div class="nav-user-wrapper">
                <div class="nav-user-list">
                  <ul>
                    <li>
                      <router-link to="/account">个人中心</router-link>
                    </li>
                    <li>
                      <router-link to="/password">修改密码</router-link>
                    </li>
                    <li>
                      <a href="javascript:void(-1);" @click="logOut">退出</a>
                    </li>
                  </ul>
                </div>
              </div>
            </div>
          </div>
        </div>
      </header>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import { fetchList } from '@/api/exam/exam'
export default {
  mounted () {
    // 监听滚动
    // window.addEventListener('scroll', this.handleScroll)
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
      login: false,
      input: ''
    }
  },
  methods: {
    // 导航栏切换
    open (path) {
      this.$router.push({
        path: path,
        query: {}
      })
    },
    handleScroll () {
      let nav = document.getElementById('header-box')
      let scrollTop = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop
      if (scrollTop > 50) {
        nav.style.position = 'fixed'
        nav.style.top = '0'
        nav.style.zIndex = '99999'
      } else {
        // nav.style.position = 'relative'
      }
    },
    querySearchAsync (queryString, callback) {
      const query = {
        examinationName: queryString
      }
      fetchList(query).then(response => {
        this.list = response.data.list
        if (this.list.length > 0) {
          let exams = []
          for (let i = 0; i < this.list.length; i++) {
            exams.push({ name: this.list[i].examinationName, value: this.list[i].examinationName })
          }
          callback(exams)
        }
      })
    },
    // 选择事件
    handleSelect (item) {
      console.log(item)
    },
    // 点击搜索图标
    handleIconClick (item) {
      console.log(item)
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
      }).catch(error => {
        console.error(error)
        this.login = false
        this.$router.push('/home')
      })
    },
    // 检测登录
    checkLogin () {
      if (this.userInfo.id !== undefined) {
        this.login = true
      }
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" rel="stylesheet/scss" scoped>
  @import "../../assets/css/mixin.scss";
  header {
    height: 80px;
    z-index: 30;
    position: relative;
  }
  .header-box {
    width: 100%;
    background-color: #ffffff;
  }
  .header-w-box {
    display: flex;
    justify-content: space-between;
    align-items: center;
    height: 100%;
    h1 {
      height: 100%;
      display: flex;
      align-items: center;
      > a {
        background-size: cover;
        display: block;
        @include wh(213px, 54px);
        text-indent: -9999px;
        background: url(/static/images/home/logoko.png) no-repeat 0 0;
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
  .el-menu-header {
    color: #666666;
    text-transform: uppercase;
    font-size: 14px;
    font-weight: 700;
    padding: 12px 20px 0px 19px;
    border: 1px solid transparent;
    bottom: -1px;
    -moz-transition: none;
    -o-transition: none;
    transition: none;
    background-color: transparent;
    border-radius: 0;
    margin: 12px 0 5px;
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
