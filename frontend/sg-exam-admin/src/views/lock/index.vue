<template>
  <div class="bg">
    <div class="lock-container">
      <div class="lock-form animated bounceInDown">
        <div class="animated" :class="{'shake':passwdError,'bounceOut':pass}">
          <h3 class="text-white">{{userInfo.username}}</h3>
          <el-input placeholder="请输入锁屏密码" type="password" class="input-with-select animated" v-model="passwd" @keyup.enter.native="handleLogin">
            <el-button slot="append" @click="handleLogin">
              <svg-icon class="lock-svg" icon-class="lock"/>
            </el-button>
            <el-button slot="append" @click="handleLogout">
              <svg-icon class="exit-svg" icon-class="exit"/>
            </el-button>
          </el-input>
        </div>
      </div>
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
import { mapGetters, mapState } from 'vuex'
import { resolveUrlPath } from '@/utils/util'
export default {
  name: 'lock',
  data () {
    return {
      passwd: '',
      passwdError: false,
      pass: false
    }
  },
  created () {},
  mounted () {},
  computed: {
    ...mapState({
      userInfo: state => state.user.userInfo
    }),
    ...mapGetters(['tag', 'lockPasswd'])
  },
  props: [],
  methods: {
    handleLogout () {
      this.$confirm('是否退出系统, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.dispatch('LogOut').then(() => {
          this.$router.push({ path: '/login' })
        })
      })
    },
    handleLogin () {
      if (this.passwd !== this.lockPasswd) {
        this.passwd = ''
        this.$message({
          message: '解锁密码错误,请重新输入',
          type: 'error'
        })
        this.passwdError = true
        setTimeout(() => {
          this.passwdError = false
        }, 1000)
        return
      }
      this.pass = true
      setTimeout(() => {
        this.$store.commit('CLEAR_LOCK')
        this.$router.push({ path: resolveUrlPath(this.tag.value || '/') })
      }, 1000)
    }
  },
  components: {}
}
</script>

<style lang="scss">
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
  .lock-container {
    width: 330px;
    border-radius: 5px;
    padding: 20px;
    z-index: 3;
    margin-top: -20%;
    background: rgba(149, 159, 181, 0.5);
    h3 {
      text-align: center;
      color: #ebedef;
      margin-top: 0;
      margin-bottom: 5px;
    }
    form {
      margin-top: 25px;
      .el-form-item {
        margin-bottom: 15px;
      }
    }
    button {
      width: 100%;
      font-weight: 600;
    }
  }
  .lock-form {
    width: 300px;
  }
</style>
