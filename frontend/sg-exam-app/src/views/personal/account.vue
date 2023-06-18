<template>
  <div class="content-container">
    <el-row>
      <el-col :span="20" :offset="2" style="margin-top:10px;">
        <el-form ref="form" :rules="rules" :label-position="labelPosition" :model="userInfo" label-width="200px" style="width: 90%;">
          <el-row>
            <el-col :span="12">
              <el-row>
                <el-col :span="24">
                  <el-form-item :label="$t('personal.account.identifier') + '：'" prop="identifier">
                    <el-input :disabled="disabled" v-model="userInfo.identifier"/>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                  <el-form-item :label="$t('personal.account.name') + '：'" prop="name">
                    <el-input v-model="userInfo.name" :placeholder="$t('personal.account.name')"/>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                  <el-form-item :label="$t('personal.account.gender') + '：'" prop="sex">
                    <el-radio-group v-model="userInfo.gender">
                      <el-radio :label="0">{{$t('status.men')}}</el-radio>
                      <el-radio :label="1">{{$t('status.women')}}</el-radio>
                    </el-radio-group>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                  <el-form-item :label="$t('personal.account.born') + '：'" prop="born">
                    <el-date-picker v-model="userInfo.born" format="yyyy 年 MM 月 dd 日" value-format="yyyy-MM-dd HH:mm:ss" placeholder="出生日期"/>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                  <el-form-item :label="$t('personal.account.phone') + '：'" prop="phone">
                    <el-input v-model="userInfo.phone" :placeholder="$t('personal.account.phone')"/>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                  <el-form-item :label="$t('personal.account.email') + '：'" prop="email">
                    <el-input v-model="userInfo.email" :placeholder="$t('personal.account.email')"/>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-col>
            <el-col :span="10" :offset="2">
              <el-row>
                <el-col :span="12" :offset="6" style="text-align: center">
                  <el-upload
                    :show-file-list="false"
                    :on-success="handleAvatarSuccess"
                    :before-upload="beforeAvatarUpload"
                    action="sg-user-service/v1/user/uploadAvatar"
                    :headers="headers"
                    :data="params"
                    class="avatar-uploader">
                    <img v-if="userInfo.avatar" :src="userInfo.avatar" class="avatar">
                    <i v-else class="el-icon-plus avatar-uploader-icon"/>
                  </el-upload>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12" :offset="6" style="text-align: center; margin-top: 20px;">
                  <h4>{{$t('personal.account.avatar')}}</h4>
                </el-col>
              </el-row>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8" :offset="8">
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
import { updateObjInfo, updateAvatar } from '@/api/admin/user'
import OFooter from '../common/footer'
import { getToken, getTenantCode } from '@/utils/auth'
import { mapState } from 'vuex'
import { isNotEmpty, notifySuccess, notifyFail } from '@/utils/util'
import store from '@/store'

export default {
  data () {
    return {
      labelPosition: 'right',
      disabled: true,
      rules: {
        identifier: [{
          required: true,
          message: this.$t('personal.account.inputIdentifier'),
          trigger: 'change' }]
      },
      headers: {
        Authorization: getToken(),
        'Tenant-Code': getTenantCode()
      },
      params: {
        busiType: '1'
      }
    }
  },
  components: {
    OFooter
  },
  created () {
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
          updateObjInfo(this.userInfo).then(response => {
            if (response.data.result) {
              notifySuccess(this, this.$t('personal.account.modifySuccess'))
              store.dispatch('GetUserInfo').then(() => {
              }).catch((err) => {
                console.error(err)
              })
            } else {
              notifyFail(this, response.data.msg)
            }
          }).catch(() => {
            notifyFail(this, this.$t('personal.account.modifyFailed'))
          })
        }
      })
    },
    handleAvatarSuccess (res) {
      if (!isNotEmpty(res.result)) {
        notifyFail(this, this.$t('personal.account.avatarUploadFailed'))
        return
      }
      this.userInfo.avatar = res.result.url
      this.userInfo.avatarId = res.result.id
      updateAvatar(this.userInfo).then(() => {
        store.dispatch('GetUserInfo').then(() => {
          notifySuccess(this, this.$t('personal.account.avatarUploadSuccess'))
        })
      }).catch((error) => {
        console.log(error)
        notifyFail(this, this.$t('personal.account.avatarUploadFailed'))
      })
    },
    beforeAvatarUpload (file) {
      const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
      const isLt2M = file.size / 1024 / 1024 < 2
      if (!isJPG) {
        this.$message.error(this.$t('personal.account.avatarUploadFormat'))
      }
      if (!isLt2M) {
        this.$message.error(this.$t('personal.account.avatarUploadSize'))
      }
      return isJPG && isLt2M
    }
  }
}
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
  .message{
    margin-bottom: 20px;
  }
  .avatar-uploader-icon {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
    border-radius: 6px;
  }
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
</style>
