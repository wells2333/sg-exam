<template>
  <div>
    <div class="header-area">
      <div class="banner" v-if="stateInfo.showBanner" @click="gotoAliYun">
        <i class="iconfont icon-delete banner-delete" @click.stop="closeBanner"></i>
      </div>
      <div class="clever-main-menu">
        <div class="classy-nav-container breakpoint-off">
          <nav class="classy-navbar justify-content-between" id="cleverNav">
            <a class="nav-brand hidden-sm-only" href="/">{{ sysConfig.sys_web_name || '' }}</a>
            <div class="classy-menu">
              <div class="classynav">
                <el-menu :default-active="activeIndex"
                         mode="horizontal"
                         text-color="rgba(0, 0, 0, 0.45)"
                         active-text-color="#3762f0"
                         :unique-opened=true
                         @select="handleSelect">
                  <el-menu-item index="/index" @click="open('/home')">{{ $t('home') }}</el-menu-item>
                  <el-menu-item index="/exams" @click="open('/exams')">{{ $t('examination') }}</el-menu-item>
                  <el-menu-item index="/courses" @click="open('/courses')">{{ $t('course') }}</el-menu-item>
                  <el-menu-item index="/exam-record" @click="open('/exam-record')">{{$t('exam.records.records')}}</el-menu-item>
                  <el-menu-item index="u-admin" @click="openAdmin()">{{$t('header.admin')}}</el-menu-item>
                  <el-submenu v-if="login" index="/user-info">
                    <template slot="title">
                      <img v-if="userInfo.avatar && userInfo.avatar !== ''" :src="userInfo.avatar"
                           style="width: 30px;height: 30px;border-radius: 50%;margin-right:6px;"/>
                      <i v-else class="iconfont icon-user" style="font-size: 28px; margin-right:6px; color: #5a5a5a;"></i>
                      {{ userInfo.identifier }}
                    </template>
                    <el-menu-item index="account" @click="open('/account')">{{$t('header.personal')}}</el-menu-item>
                    <el-menu-item index="password" @click="open('/password')">{{$t('header.changePwd')}}
                    </el-menu-item>
                    <el-menu-item index="logOut" @click="logOut">{{$t('header.logout')}}</el-menu-item>
                  </el-submenu>
                </el-menu>
                <div class="register-login-area" v-if="!login">
                  <a class="btn" target="_blank" @click="open('/register')">{{ $t('register') }}</a>
                  <a class="btn" target="_blank" @click="open('/login')">{{ $t('login') }}</a>
                </div>
                <div>
                  <el-dropdown trigger="click" class="international"
                               @command="handleSetLanguage">
                    <div>
                      {{ language === 'zh' ? '中文' : 'English' }}
                    </div>
                    <el-dropdown-menu slot="dropdown" style="padding-left: 12px; padding-right: 12px;">
                      <el-dropdown-item :disabled="language === 'zh'" command="zh">
                        中文
                      </el-dropdown-item>
                      <el-dropdown-item :disabled="language === 'en'" command="en">
                        English
                      </el-dropdown-item>
                    </el-dropdown-menu>
                  </el-dropdown>
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
import store from '@/store'
import OMain from './common/main'
import {mapGetters, mapState} from 'vuex'
import {messageWarn} from '@/utils/util'
import {setStore, getStore} from '@/utils/store'
import { SEARCH_HISTORY } from '@/utils/storeMap'

export default {
  components: {
    OMain
  },
  computed: {
    ...mapState({
      userInfo: state => state.user.userInfo,
      stateInfo: state => state.stateInfo.stateInfo
    }),
    ...mapGetters([
      'sysConfig'
    ])
  },
  created() {
    this.checkLogin()
  },
  watch: {
    $route() {
      this.checkLogin()
    }
  },
  data() {
    return {
      language: this.$i18n.locale,
      activeIndex: '/index',
      login: false,
      input: '',
      query: ''
    }
  },
  methods: {
    mounted() {
      this.$i18n.locale = process.env.I18N_LOCAL || 'zh'
    },
    closeBanner() {
      store.dispatch('SetStateInfo', {showBanner: false})
    },
    gotoAliYun() {
      window.open('https://www.aliyun.com/minisite/goods?userCode=gg8hcnqt')
    },
    handleSetLanguage(lang) {
      this.$i18n.locale = lang
      this.language = lang
    },
    openAdmin() {
      window.open(window.location.origin + '/admin')
    },
    handleSubmitExam() {
      this.$refs.mainRef.handleSubmitExam()
    },
    open(path) {
      if (path.startsWith('http')) {
        window.open(path)
        return
      }

      if (path !== this.$route.fullPath) {
        if (this.$route.fullPath === '/start') {
          this.$confirm('是否要结束当前考试？', '提示', {
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
          }).catch(() => {
          })
        } else {
          this.$router.push({
            path: path,
            query: {}
          })
        }
      }
    },
    handleSelect(item) {
    },
    handleRegister() {
      // 先退出
      // this.logOut()
      this.$router.push('/register')
    },
    handleLogin() {
      this.$router.push('/login')
    },
    logOut() {
      this.$store.dispatch('LogOut').then(() => {
        this.login = false
        this.$router.push('/home')
      }).catch(() => {
        this.login = false
        this.$router.push('/home')
      })
    },
    checkLogin() {
      if (this.userInfo.id !== undefined) {
        this.login = true
      }
    },
    search() {
      if (this.query && this.query !== '') {
        this.$router.push({name: 'search', query: {query: this.query}})
        const tmp = getStore({
          name: SEARCH_HISTORY
        })
        let res = []
        if (tmp && tmp.length > 0) {
          res = tmp
          let index = tmp.findIndex(item => item === this.query)
          if (index === -1) {
            res.push(this.query)
          }
        } else {
          res.push(this.query)
        }
        setStore({
          name: SEARCH_HISTORY,
          content: res
        })
      }
    }
  }
}
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
@import "../assets/css/style.scss";
@import "../assets/css/subject.scss";

.international {
  padding: 20px;
}
.banner{
  height: 60px;
  background-image: url(https://img.alicdn.com/imgextra/i3/O1CN01Tbj8qb1w835uueixH_!!6000000006262-2-tps-3840-120.png);
  background-position: center center;
  background-size: auto 100%;
  position: relative;
  cursor: pointer;
  .banner-delete {
    font-size: 20px;
    position: absolute;
    top: 20px;
    right: 20px;
    cursor: pointer;
  }
}
</style>
