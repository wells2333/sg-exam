<template>
  <div id="password" class="content-container">
    <el-row>
      <el-col :span="20" :offset="2" style="margin-top:10px;">
        <el-form ref="form" :rules="rules" :label-position="labelPosition" :model="userInfo" label-width="200px" style="width: 90%;">
          <el-row>
            <el-col :span="12" :offset="6">
              <el-form-item :label="$t('personal.pwd.oldPwd') + '：'" prop="oldPassword">
                <el-input v-model="userInfo.oldPassword" auto-complete="off" type="password"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12" :offset="6">
              <el-form-item :label="$t('personal.pwd.newPwd') + '：'" prop="newPassword">
                <el-input v-model="userInfo.newPassword" auto-complete="off" type="password"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12" :offset="6">
              <el-form-item :label="$t('personal.pwd.confirmPwd') + '：'" prop="newPassword1">
                <el-input v-model="userInfo.newPassword1" auto-complete="off" type="password"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12" :offset="8">
              <el-form-item>
                <el-button type="primary" @click="update">{{$t('save')}}</el-button>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import OFooter from '../common/footer'
import { updatePassword } from '@/api/admin/user'
import { mapState } from 'vuex'
import { notifySuccess, notifyFail, isNotEmpty } from '@/utils/util'

export default {
  data () {
    const validatePass = (rule, value, callback) => {
      if (this.userInfo.oldPassword !== '') {
        if (!isNotEmpty(value)) {
          callback(new Error(this.$t('personal.pwd.inputNewPwd')))
        } else if (value.length < 6) {
          callback(new Error(this.$t('personal.pwd.pwdLenTips')))
        } else {
          callback()
        }
      } else {
        callback()
      }
    }
    const validatePass1 = (rule, value, callback) => {
      if (this.userInfo.oldPassword !== '') {
        if (!isNotEmpty(value)) {
          callback(new Error(this.$t('personal.pwd.inputPwdAgain')))
        } else if (value !== this.userInfo.newPassword) {
          callback(new Error(this.$t('personal.pwd.pwdNotEqual')))
        } else {
          callback()
        }
      } else {
        callback()
      }
    }
    return {
      labelPosition: 'right',
      rules: {
        oldPassword: [{ required: true, message: this.$t('personal.pwd.inputOldPwd'), trigger: 'blur' }],
        newPassword: [{ required: true, validator: validatePass, trigger: 'blur' }],
        newPassword1: [{ required: true, validator: validatePass1, trigger: 'blur' }]
      },
      readOnly: false
    }
  },
  components: {
    OFooter
  },
  computed: {
    ...mapState({
      userInfo: state => state.user.userInfo
    })
  },
  methods: {
    update () {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          updatePassword(this.userInfo).then(response => {
            if (response.data.result) {
              notifySuccess(this, this.$t('personal.pwd.modifySuccess'))
              this.$store.dispatch('LogOut').then(() => {
                this.$router.push({ path: '/login' })
              })
            } else {
              notifyFail(this, response.data.message)
            }
          }).catch(() => {
            notifyFail(this, this.$t('personal.pwd.modifyFailed'))
          })
        }
      })
    }
  }
}
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
  #password {
    margin-bottom: 20px;
  }
</style>
