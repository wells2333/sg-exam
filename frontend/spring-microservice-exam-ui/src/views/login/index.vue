<template>
  <div class="bg">
    <div class="login-wrap animated flipInY">
      <h3>{{ $t('login.title') }}</h3>
      <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" auto-complete="on" label-position="left">
        <el-form-item prop="tenantCode">
          <el-input :placeholder="$t('login.tenantCode')" v-model="loginForm.tenantCode" name="tenantCode" type="text" auto-complete="on"/>
        </el-form-item>
        <el-form-item prop="identifier">
          <el-input :placeholder="$t('login.identifier')" v-model="loginForm.identifier" name="identifier" type="text" auto-complete="on"/>
        </el-form-item>
        <el-form-item prop="password">
          <el-input :placeholder="$t('login.credential')" :type="passwordType" v-model="loginForm.credential" name="credential" auto-complete="on" @keyup.enter.native="handleLogin"/>
        </el-form-item>
        <el-form-item prop="code">
          <el-row :span="24">
            <el-col :span="14">
              <el-input :maxlength="code.len" v-model="loginForm.code" size="small" auto-complete="off" placeholder="请输入验证码" @keyup.enter.native="handleLogin" />
            </el-col>
            <el-col :span="10">
              <div class="login-code">
                <span v-if="code.type === 'text'" class="login-code-img" @click="refreshCode">{{ code.value }}</span>
                <img v-else :src="code.src" alt="验证码" class="login-code-img" @click="refreshCode">
              </div>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item>
          <el-row type="flex" justify="space-between">
            <el-checkbox v-model="loginForm.rememberMe" style="color:#eee">记住密码</el-checkbox>
            <a href="" style="color:#eee" @click.prevent="openMsg">{{ $t('login.forget') }}</a>
          </el-row>
        </el-form-item>
        <el-form-item>
          <el-button :loading="loading" type="primary" @click.native.prevent="handleLogin">{{ $t('login.logIn') }}</el-button>
        </el-form-item>
      </el-form>
    </div>
    <!-- 粒子漂浮物 -->
    <vue-particles
      :particle-opacity="0.7"
      :particles-number="30"
      :particle-size="5"
      :lines-width="2"
      :line-linked="true"
      :line-opacity="0.4"
      :lines-distance="150"
      :move-speed="3"
      :hover-effect="true"
      :click-effect="true"
      click-mode="push"
      color="#fff"
      shape-type="star"
      hover-mode="grab"
      lines-color="#fff"
    />
  </div>
</template>

<script>
import LangSelect from '@/components/LangSelect'
import SocialSign from './socialsignin'
import { randomLenNum } from '@/utils/util'
import { mapGetters } from 'vuex'

export default {
  name: 'Login',
  components: { LangSelect, SocialSign },
  data () {
    return {
      loginForm: {
        tenantCode: 'gitee',
        identifier: '',
        credential: '',
        code: '',
        randomStr: '',
        rememberMe: false
      },
      code: {
        src: '/api/user/v1/code',
        value: '',
        len: 4,
        type: 'image'
      },
      loginRules: {
        tenantCode: [{ required: true, trigger: 'blur', message: '请输入单位ID' }],
        identifier: [{ required: true, trigger: 'blur', message: '请输入账号' }],
        credential: [
          { required: true, trigger: 'blur', message: '请输入密码' },
          { min: 6, trigger: 'blur', message: '密码长度最少为6位' }],
        code: [
          { required: true, message: '请输入验证码', trigger: 'blur' },
          { min: 4, max: 4, message: '验证码长度为4位', trigger: 'blur' }
        ]
      },
      passwordType: 'password',
      loading: false,
      showDialog: false,
      redirect: undefined
    }
  },
  watch: {
    $route: {
      handler: function (route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  created () {
    this.refreshCode()
  },
  computed: {
    ...mapGetters(['tagWel'])
  },
  destroyed () {

  },
  methods: {
    refreshCode () {
      this.loginForm.code = ''
      this.loginForm.randomStr = randomLenNum(this.code.len, true)
      this.code.type === 'text'
        ? (this.code.value = randomLenNum(this.code.len))
        : (this.code.src = `/api/user/v1/code/${this.loginForm.randomStr}`)
    },
    showPwd () {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
    },
    handleLogin () {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          this.$store.dispatch('LoginByUsername', this.loginForm).then(() => {
            this.loading = false
            this.$store.commit('ADD_TAG', this.tagWel)
            this.$router.push({ path: this.redirect || '/' })
          }).catch(() => {
            this.loading = false
            this.refreshCode()
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    openMsg () {
      // 使用了国际化
      this.$message.warning(this.$t('login.info'))
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
  .bg {
    position: relative;
    overflow: hidden;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    background: url('../../../static/img/login_bg.jpg') -20% 10%;
    background-size: cover;
    #particles-js {
      position: absolute;
      top: 0;
      bottom: 0;
      left: 0;
      right: 0;
    }
  }
  .login-wrap {
    width: 330px;
    border-radius: 5px;
    padding: 20px;
    z-index: 3;
    margin-right: -37%;
    background: rgba(149, 159, 181, 0.5);
    .el-form-item {
      margin-bottom: 25px !important;
    }
    h3 {
      text-align: center;
      color: #ebedef;
      margin-top: 0px;
      margin-bottom: 5px;
      span {
        color: #20a0ff;
      }
    }
    form {
      margin-top: 25px;
      .el-form-item {
        margin-bottom: 15px;
      }
    }
    a {
      text-decoration: none;
      color: #1f2d3d;
    }
    button {
      width: 100%;
      font-weight: 600;
    }
  }
  .login-code-img {
    margin-left: 10px;
    margin-top: -4px;
    width: 110px;
    height: 40px;
  }
</style>
