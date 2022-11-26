<template>
  <div>
    <transition name="fade-transform" mode="out-in">
      <div class="section-content" v-show="!loading">
        <el-row>
          <el-col :span="6">
            <div class="section-title mb-30 sidebar-widget">
              <h4>{{ section.title }}</h4>
              <ul class="features-list">
                <li>
                  <h6><i class="el-icon-alarm-clock"></i>时长</h6>
                  <h6>{{ section.learnHour }}小时</h6>
                </li>
                <li>
                  <h6><i class="el-icon-user"></i>人数</h6>
                  <h6>400人</h6>
                </li>
              </ul>
            </div>
          </el-col>
          <el-col :span="18">
            <div class="section-video">
              <sg-video ref="sectionVideo"></sg-video>
            </div>
            <div class="section-button">
              <el-button type="primary" class="clever-btn mb-30 w-10" @click="goBack">返回</el-button>
            </div>
          </el-col>
        </el-row>
      </div>
    </transition>
  </div>
</template>
<script>
import {watchSection} from '@/api/exam/section'
import SgVideo from '@/components/SgVideo'

export default {
  components: {
    SgVideo
  },
  data() {
    return {
      loading: true,
      sectionId: '',
      section: {},
      videoUrl: 'https://cdn.yunmianshi.com/exam/video/%E6%9C%8D%E5%8A%A1%E6%9B%B4%E6%96%B0.mp4?e=1669444324&token=8-9rcJPtTrLOJP4fLNWXy_qwiLVc3Exu52iuGlxt:gvg4YWPvNnGldvX9p5JjbmmEHPU='
    }
  },
  created() {
    this.sectionId = this.$route.query.sectionId
    this.watchSection(this.sectionId)
  },
  methods: {
    watchSection(id) {
      this.loading = true
      watchSection(id).then(res => {
        this.section = res.data.result.section
        this.videoUrl = res.data.result.videoUrl
        this.updateVideoUrl()
        setTimeout(() => {
          this.loading = false
        }, 500)
      }).catch(error => {
        console.error(error)
        this.loading = false
      })
    },
    updateVideoUrl() {
      this.$refs.sectionVideo.setSrc(this.videoUrl)
    },
    goBack() {
      this.$refs.sectionVideo.pause();
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
.section-content {
  width: 100%;
  padding-top: 50px;
  padding-bottom: 50px;
}

.section-title {
  margin: 30px;
}

.section-button {
  margin-top: 50px;
  text-align: center;
}
.sg-video-component {
  width: 100%;
}
.section-video {
  margin-right: 30px;
}
</style>
