<template>
  <!-- 产品功能 -->
  <div id="function">
    <el-row class="function-msg">
      <el-col :span="24" style="color: black;">
        <h1>产品功能</h1>
      </el-col>
    </el-row>
    <el-tabs v-model="activeFunction" type="card">
      <el-tab-pane label="在线考试" name="online-exam">
        <el-row class="function-img">
          <el-col :span="8">
            <router-link to="/exams">
              <img class="img" src="../../../static/images/home/icon_function1.jpg">
              <div class="msg-text">最近发布</div>
              <div class="msg-text-detail">查看、参加最近发布的考试</div>
            </router-link>
          </el-col>
          <el-col :span="8">
            <router-link to="/home">
              <img class="img" src="../../../static/images/home/icon_function2.jpg">
              <div class="msg-text">模拟考试</div>
              <div class="msg-text-detail">主观题，看完考生所答情况手动评分</div>
            </router-link>
          </el-col>
          <el-col :span="8">
            <router-link to="/exam-record">
              <img class="img" src="../../../static/images/home/icon_function3.jpg">
              <div class="msg-text">考试记录</div>
              <div class="msg-text-detail">查看个人的考试记录、批改状态</div>
            </router-link>
          </el-col>
        </el-row>
      </el-tab-pane>
      <el-tab-pane label="在线学习" name="online-practice">
        <el-row class="function-img">
          <el-col :span="8">
            <router-link to="/practices">
              <img class="img" src="../../../static/images/home/icon_function4.jpg">
              <div class="msg-text">在线学习</div>
              <div class="msg-text-detail">支持选择题</div>
            </router-link>
          </el-col>
          <el-col :span="8">
            <router-link to="/incorrect">
              <img class="img" src="../../../static/images/home/icon_function5.jpg">
              <div class="msg-text">错题记录</div>
              <div class="msg-text-detail">收录错误的题目</div>
            </router-link>
          </el-col>
          <el-col :span="8">
            <router-link to="/knowledge">
              <img class="img" src="../../../static/images/home/icon_function6.jpg">
              <div class="msg-text">知识库</div>
              <div class="msg-text-detail">下载学习资料</div>
            </router-link>
          </el-col>
        </el-row>
      </el-tab-pane>
      <el-tab-pane label="考试记录" name="exam-recode">
        <el-row class="function-img">
          <el-col :span="24">
            考试记录
          </el-col>
        </el-row>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
export default {
  data () {
    return {
      activeFunction: 'online-exam'
    }
  },
  methods: {}
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" rel="stylesheet/scss" scoped>
</style>
