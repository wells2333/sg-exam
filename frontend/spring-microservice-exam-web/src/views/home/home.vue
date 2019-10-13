<template>
  <div>
    <!--主页介绍 -->
    <div id="home">
      <img id="bg" src="../../../static/images/home/index.png" alt="方便好用的考试平台">
      <div id="home-msg">方便好用的考试平台</div>
      <router-link to="/register" title="免费注册" class="register-now">免费注册</router-link>
    </div>

    <!-- 产品特色 -->
    <div id="features">
      <el-row class="features-msg">
        <el-col :span="24" style="color: black;">
          <h1>产品特色</h1>
        </el-col>
      </el-row>
      <el-row class="features-img">
        <el-col :span="6">
          <router-link to="/home">
            <img class="img" src="../../../static/images/home/icon_features1.png">
            <div class="msg-text">实时保存</div>
            <div class="msg-text-detail">实时保存答题记录，不用担心重启、崩溃等现象。</div>
          </router-link>
        </el-col>
        <el-col :span="6">
          <router-link to="/home">
            <img class="img" src="../../../static/images/home/icon_features2.png">
            <div class="msg-text">智能判分</div>
            <div class="msg-text-detail">交卷后立即出分、分析正确率，主观题还可人工阅卷后打分。</div>
          </router-link>
        </el-col>
        <el-col :span="6">
          <router-link to="/home">
            <img class="img" src="../../../static/images/home/icon_features3.png">
            <div class="msg-text">自动分析</div>
            <div class="msg-text-detail">图表展现最高分、最低分、列表展示每个考试分数。</div>
          </router-link>
        </el-col>
        <el-col :span="6">
          <router-link to="/home">
            <img class="img" src="../../../static/images/home/icon_features4.png">
            <div class="msg-text">错题记录</div>
            <div class="msg-text-detail">在线练习完后自动判分、错题记录。</div>
          </router-link>
        </el-col>
      </el-row>
    </div>

    <!-- 适用场景 -->
    <div id="scenes">
      <el-row class="scenes-msg">
        <el-col :span="24" style="color: black;">
          <h1>适用场景</h1>
        </el-col>
      </el-row>
      <el-row class="scenes-img">
        <el-col :span="6">
          <router-link to="/home">
            <img class="img" src="../../../static/images/home/icon_scenes1.jpg">
            <div class="msg-text">内部考核</div>
            <div class="msg-text-detail">入职培训、岗位评级、企业文化普及</div>
          </router-link>
        </el-col>
        <el-col :span="6">
          <router-link to="/home">
            <img class="img" src="../../../static/images/home/icon_scenes2.jpg">
            <div class="msg-text">经销商考核</div>
            <div class="msg-text-detail">产品知识、销售技巧和政策的考核</div>
          </router-link>
        </el-col>
        <el-col :span="6">
          <router-link to="/home">
            <img class="img" src="../../../static/images/home/icon_scenes3.jpg">
            <div class="msg-text">培训机构</div>
            <div class="msg-text-detail">随堂测验、课后作业、模考练习</div>
          </router-link>
        </el-col>
        <el-col :span="6">
          <router-link to="/home">
            <img class="img" src="../../../static/images/home/icon_scenes4.jpg">
            <div class="msg-text">招聘考试</div>
            <div class="msg-text-detail">高效组织校园招聘和社会招聘</div>
          </router-link>
        </el-col>
      </el-row>
    </div>
    <div v-if="isActive" class="go-top-box" @click="goTop(step)">
      <i class="top-icon el-icon-caret-top"></i>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    step: {
      type: Number,
      default: 50
    }
  },
  data () {
    return {
      isActive: false
    }
  },
  methods: {
    // 返回顶部
    goTop: function (i) {
      document.documentElement.scrollTop -= i
      if (document.documentElement.scrollTop > 0) {
        setTimeout(() => this.goTop(i), 16)
      }
    }
  },
  created () {
    let vm = this
    window.onscroll = function () {
      vm.isActive = document.documentElement.scrollTop > 60
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" rel="stylesheet/scss" scoped>
  #home {
    height: 500px;
  }
</style>
