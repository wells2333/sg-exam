<template>
  <div>
    <div class="section-content">
      <el-row>
        <el-col :span="6">
          <transition name="el-fade-in">
            <div class="section-title mb-30 sidebar-widget">
              <div v-for="chapter in detail.chapters" :key="chapter.chapter.id">
                <div class="chapter-container">
                  <p>{{ chapter.chapter.title }}</p>
                  <div class="section-container" @click="handleClickSection(section)"
                       v-for="section in chapter.sections" :key="section.id">
                    <p
                      :class="section.id === sectionId ? 'section-title-selected section-title' : 'section-title' ">
                      {{ section.title }}</p>
                  </div>
                </div>
              </div>
            </div>
          </transition>
        </el-col>
        <el-col :span="18" style="padding-left: 20px;">
          <transition name="el-fade-in">
            <div v-show="!loading">
              <div>
                <div class="section-detail-title">
                  <h3>{{ section.title }}</h3>
                </div>
                <div v-if="contentType === 0 && videoUrl !== null" class="section-video">
                  <sg-video ref="sectionVideo"></sg-video>
                  <div class="section-video-content">
                    <div v-html="section.content"></div>
                  </div>
                </div>
                <div v-else-if="contentType === 1" v-html="section.content"></div>
              </div>
              <div class="section-button">
                <el-button type="primary" class="clever-btn mb-30 w-10" @click="goBack">返回
                </el-button>
              </div>
            </div>
          </transition>
        </el-col>
      </el-row>
    </div>
  </div>
</template>
<script>
import {getCourseDetail} from '@/api/exam/course'
import {watchSection} from '@/api/exam/section'
import SgVideo from '@/components/SgVideo'

export default {
  components: {
    SgVideo
  },
  data() {
    return {
      loading: true,
      courseId: '',
      sectionId: '',
      section: {},
      videoUrl: null,
      detail: {},
      contentType: 0,
      clickSectionId: null
    }
  },
  created() {
    this.courseId = this.$route.query.courseId
    this.sectionId = this.$route.query.sectionId
    this.getCourseDetail(this.courseId);
    this.getSection(this.sectionId)
  },
  methods: {
    getCourseDetail(courseId) {
      getCourseDetail(courseId).then(res => {
        this.detail = res.data.result
      }).catch(error => {
        console.error(error)
      })
    },
    getSection(id) {
      this.loading = true
      watchSection(id).then(res => {
        this.videoUrl = res.data.result.videoUrl
        this.contentType = res.data.result.section.contentType
        setTimeout(() => {
          this.section = res.data.result.section
          this.loading = false
          if (this.videoUrl !== null) {
            this.updateVideoUrl()
          }
        }, 200)
      }).catch(error => {
        console.error(error)
        this.loading = false
      })
    },
    updateVideoUrl() {
      if (this.$refs.sectionVideo) {
        this.$refs.sectionVideo.setSrc(this.videoUrl)
      }
    },
    handleClickSection(sec) {
      // 重复点击
      if (this.clickSectionId !== null && this.clickSectionId === sec.id) {
        return
      }
      this.sectionId = sec.id
      this.clickSectionId = sec.id
      this.getSection(this.sectionId)
    },
    goBack() {
      if (this.$refs.sectionVideo) {
        this.$refs.sectionVideo.pause()
      }
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
.section-content .sg-video-component .video-js {
  height: 400px !important;
}

.section-content {
  width: 100%;
  padding-top: 16px;
}

.section-detail-title {
  margin-top: 16px;
  margin-bottom: 16px;
}

.chapter-container {
  cursor: pointer;
}
.section-title {
  margin-left: 10px;
  cursor: pointer;
}

.section-button {
  margin-top: 50px;
  text-align: center;
}

.sg-video-component {
  width: 100%;
}

.section-video {
  margin-left: 20px;
  margin-right: 20px;
}

.section-title:hover, .section-learn-hour:hover {
  color: #409EFF;
}

.section-title-selected {
  color: #409EFF;
}
.section-video-content {
  padding-top: 20px;
}
</style>
