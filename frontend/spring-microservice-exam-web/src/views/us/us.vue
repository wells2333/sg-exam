<template>
  <div id="us">
    <el-row class="us-msg">
      <el-col :span="24" style="color: black;">
        <h1>联系我们</h1>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="6" :offset="1">
        <p>喜欢这个系统? 留下联系方式吧.</p>
        <p><span class="glyphicon glyphicon-map-marker"></span>广州</p>
        <p><span class="glyphicon glyphicon-phone"></span>电话: +00 1515151515</p>
        <p><span class="glyphicon glyphicon-envelope"></span>邮箱: mail@mail.com</p>
      </el-col>
      <el-col :span="15">
        <el-form label-position="right" label-width="100px" :model="temp">
          <el-row>
            <el-col :span="12">
              <el-form-item label="姓名" prop="name">
                <el-input v-model="temp.name" placeholder="姓名" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="邮箱" prop="email">
                <el-input v-model="temp.email" placeholder="邮箱" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="备注" prop="comments">
                <el-input :autosize="{ minRows: 4, maxRows: 6}" v-model="temp.comments" type="textarea" placeholder="备注"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8" :offset="8">
              <el-button @click="send" type="primary">发送</el-button>
            </el-col>
          </el-row>
        </el-form>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import OFooter from '../common/footer'
export default {
  data () {
    return {
      temp: {
        name: '',
        email: '',
        comments: ''
      }
    }
  },
  components: {
    OFooter
  },
  methods: {
    send: function () {
      console.log(this.temp)
      this.$notify.success({
        title: '成功',
        message: '发送成功，感谢！'
      })
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" rel="stylesheet/scss" scoped>
  #us {
    margin-bottom: 20px;
  }
</style>
