<template>
  <div class="bg">
    <div class="login-wrap animated flipInY">
      <div class="text-center">
        <h3>
          <span class="light-font">SG-</span>Admin
        </h3>
      </div>
      <el-row>
        <el-col :span="24">
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
              <el-row>
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
              <el-button class="login-btn" type="primary" :loading="loading" @click.native.prevent="handleLogin"> {{ $t('login.logIn') }}</el-button>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
    </div>
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
        identifier: 'preview',
        credential: '123456',
        code: '',
        randomStr: '',
        rememberMe: false
      },
      code: {
        src: '/user-service/v1/code',
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
        : (this.code.src = `/user-service/v1/code/${this.loginForm.randomStr}`)
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
  }
  .light-font {
    color: #00abff;
  }
  .login-wrap {
    top: 15%;
    position: absolute;
    width: 420px;
    margin: 0 auto;
    border-radius: 5px;
    color: #fff;
    background: rgba(0,0,0,.55);
    text-align: center;
    padding: 32px;
    background-size: cover;
    .el-form-item {
      margin-bottom: 25px !important;
    }
    h3 {
      text-align: center;
      color: #ebedef;
      margin-top: 0;
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
  .login-form {
    input {
      color: #fff;
      border: 1px solid hsla(0,0%,100%,.6);
      border-radius: 5px;
      background-color: hsla(0,0%,100%,.1);
      box-shadow: none;
      font-size: 14px;
    }
  }
  .login-code-img {
    margin-left: 10px;
    margin-top: -4px;
    width: 110px;
    height: 40px;
    border-radius: 2px;
  }
</style>
