<template>
  <div class="tab-container">
    <div class="filter-container">
      <el-row>
        <el-col :span="20" :offset="2" style="margin-top:10px;">
          <el-card class="box-card">
            <el-form ref="form" :rules="rules" :label-position="labelPosition" :model="userInfo" label-width="100px" style="width: 90%;">
              <el-row>
                <el-col :span="12">
                  <el-row>
                    <el-col :span="24">
                      <el-form-item label="账号" prop="identifier">
                        <el-input :disabled="disabled" v-model="userInfo.identifier"/>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row>
                    <el-col :span="24">
                      <el-form-item label="姓名" prop="name">
                        <el-input v-model="userInfo.name" placeholder="请输入姓名"/>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row>
                    <el-col :span="24">
                      <el-form-item :label="$t('table.sex')" prop="sex">
                        <el-radio-group v-model="userInfo.sex">
                          <el-radio :label="0">男</el-radio>
                          <el-radio :label="1">女</el-radio>
                        </el-radio-group>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row>
                    <el-col :span="24">
                      <el-form-item :label="$t('table.born')" prop="born">
                        <el-date-picker v-model="userInfo.born" type="date" format="yyyy 年 MM 月 dd 日" value-format="timestamp" placeholder="出生日期"/>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row>
                    <el-col :span="24">
                      <el-form-item :label="$t('table.phone')" prop="phone">
                        <el-input v-model="userInfo.phone" placeholder="电话号码"/>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row>
                    <el-col :span="24">
                      <el-form-item :label="$t('table.email')" prop="email">
                        <el-input v-model="userInfo.email" placeholder="邮箱"/>
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
                        action="api/user/v1/attachment/upload"
                        :headers="headers"
                        :data="params"
                        class="avatar-uploader">
                        <img v-if="userInfo.avatarUrl" :src="userInfo.avatarUrl" class="avatar">
                        <i v-else class="el-icon-plus avatar-uploader-icon"/>
                      </el-upload>
                    </el-col>
                  </el-row>
                  <el-row>
                    <el-col :span="12" :offset="6" style="text-align: center">
                      <h4>头像</h4>
                    </el-col>
                  </el-row>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="8" :offset="8">
                  <el-form-item>
                    <el-button type="primary" @click="update">保存</el-button>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { updateObjInfo, updateAvatar } from '@/api/admin/user'
import { mapState } from 'vuex'
import { getToken } from '@/utils/auth'
import { isNotEmpty, notifySuccess, notifyFail } from '@/utils/util'
import store from '@/store'

export default {
  name: 'PersonalMessage',
  components: {},
  data () {
    return {
      labelPosition: 'right',
      disabled: true,
      rules: {
        identifier: [{ required: true, message: '请输入账号', trigger: 'change' }]
      },
      headers: {
        Authorization: 'Bearer ' + getToken()
      },
      params: {
        busiType: '1'
      }
    }
  },
  created () {},
  computed: {
    ...mapState({
      userInfo: state => state.user.userInfo,
      sysConfig: state => state.sysConfig.sysConfig
    })
  },
  methods: {
    update () {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          updateObjInfo(this.userInfo).then(response => {
            if (response.data.data) {
              notifySuccess(this, '修改成功')
              // 重新拉取用户信息
              store.dispatch('GetUserInfo').then(res => {
                console.log('重新获取用户信息成功.')
              }).catch((err) => {
                console.error(err)
              })
            } else {
              notifyFail(this, response.data.msg)
            }
          }).catch(() => {
            notifyFail(this, '修改失败')
          })
        }
      })
    },
    handleAvatarSuccess (res, file) {
      if (!isNotEmpty(res.data)) {
        notifyFail(this, '头像上传失败')
        return
      }
      // 重新获取预览地址
      this.userInfo.avatarUrl = '/user/v1/attachment/preview?id=' + res.data.id
      this.userInfo.avatarId = res.data.id
      updateAvatar(this.userInfo).then(response => {
        notifySuccess(this, '头像上传成功')
        // 更新localStorage
        store.commit('SET_USER_INFO', this.userInfo)
      }).catch((error) => {
        console.log(error)
        notifyFail(this, '头像上传失败')
      })
    },
    beforeAvatarUpload (file) {
      const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
      const isLt2M = file.size / 1024 / 1024 < 2
      if (!isJPG) {
        this.$message.error('上传头像图片只能是 jpg/png 格式!')
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!')
      }
      return isJPG && isLt2M
    }
  }
}
</script>

<style scoped>
  .tab-container{
    margin: 30px;
  }
  .avatar-uploader-icon {
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
  }
</style>
