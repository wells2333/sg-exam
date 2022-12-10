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
                  <div class="section-container"
                       v-for="section in chapter.sections" :key="section.section.id">
                    <p
                      :class="(pointId === undefined && section.section.id === sectionId) ? 'section-title-selected section-title' : 'section-title'"
                      @click="handleClickSection(section.section)">
                      {{ section.section.title }}</p>

                    <div class="point-container" v-for="point in section.points" :key="point.id">
                      <p
                        :class="point.id === pointId ? 'point-title-selected point-title':  'point-title'"
                        @click="handleClickPoint(point)">
                       {{ point.title }}</p>
                    </div>
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
                  <h3>{{ title }}</h3>
                  <div class="title-tips">
                    <i class="el-icon-user"></i> {{updateTime}}
                    &nbsp;&nbsp;&nbsp;&nbsp;
                    <i class="el-icon-time"></i> {{operator}}
                  </div>
                </div>
                <div v-if="contentType === 0" class="section-video">
                  <sg-video v-if="videoUrl !== undefined && videoUrl !== null && videoUrl !== ''" ref="sectionVideo"></sg-video>
                  <div class="section-video-content">
                    <div v-html="content"></div>
                  </div>
                </div>
                <div v-else-if="contentType === 1">
                  <div v-html="content"></div>
                </div>
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
import {getKnowledgePointDetail} from "../../api/exam/point";

export default {
  components: {
    SgVideo
  },
  data() {
    return {
      loading: true,
      courseId: undefined,
      sectionId: undefined,
      pointId: undefined,
      section: {},
      point: undefined,
      videoUrl: undefined,
      detail: {},
      contentType: 0,
      clickSectionId: undefined,
      clickPointId: undefined,
      title: undefined,
      content: undefined,
      operator: '',
      updateTime: ''
    }
  },
  created() {
    this.courseId = this.$route.query.courseId
    this.sectionId = this.$route.query.sectionId
    this.pointId = this.$route.query.pointId
    this.getCourseDetail(this.courseId)
    if (this.pointId !== undefined) {
      this.getPoint(this.pointId)
    } else {
      this.getSection(this.sectionId)
    }
  },
  methods: {
    getCourseDetail(courseId) {
      if (courseId === undefined) {
        return
      }
      getCourseDetail(courseId).then(res => {
        this.detail = res.data.result
      }).catch(error => {
        console.error(error)
      })
    },
    getSection(id) {
      if (id === undefined) {
        return
      }
      this.stopVideo();
      this.loading = true
      watchSection(id).then(res => {
        this.contentType = res.data.result.section.contentType
        this.videoUrl = res.data.result.videoUrl
        setTimeout(() => {
          const {title, content, operator, updateTime } = res.data.result.section
          this.section = res.data.result.section
          this.title = title
          this.content = content
          this.operator = operator
          this.updateTime = updateTime
          this.loading = false
          this.updateVideoUrl()
        }, 200)
      }).catch(error => {
        console.error(error)
        this.loading = false
      })
    },
    getPoint(id) {
      if (id === undefined) {
        return
      }
      this.stopVideo();
      this.loading = true
      getKnowledgePointDetail(id).then(res => {
        const {title, videoUrl, contentType, content, operator, updateTime} = res.data.result
        this.contentType = contentType
        this.videoUrl = videoUrl
        setTimeout(() => {
          this.loading = false
          this.title = title
          this.content = content
          this.operator = operator
          this.updateTime = updateTime
          this.updateVideoUrl()
        }, 200)
      }).catch(error => {
        console.error(error)
        this.loading = false
      })
    },
    updateVideoUrl() {
      if (this.videoUrl !== undefined && this.videoUrl !== null) {
        if (this.$refs.sectionVideo) {
          this.$refs.sectionVideo.setSrc(this.videoUrl)
        }
      } else {
        this.videoUrl = undefined
      }
    },
    stopVideo() {
      if (this.$refs.sectionVideo) {
        this.$refs.sectionVideo.pause()
      }
    },
    handleClickSection(sec) {
      this.clickPointId = undefined
      this.pointId = undefined
      if (this.clickSectionId !== undefined && this.clickSectionId === sec.id) {
        return
      }
      this.sectionId = sec.id
      this.clickSectionId = sec.id
      this.getSection(sec.id)
    },
    handleClickPoint(point) {
      this.clickSectionId = undefined
      this.sectionId = undefined
      if (this.clickPointId !== undefined && this.clickPointId === point.id) {
        return
      }
      this.pointId = point.id
      this.clickPointId = point.id
      this.getPoint(point.id)
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
  margin-right: 20px;
}

.section-title:hover, .point-title:hover {
  color: #409EFF;
}

.section-title-selected, .point-title-selected {
  color: #409EFF;
}

.section-video-content {
  padding-top: 20px;
}

.point-container {
  margin-left: 30px;
}
.title-tips {
  font-size: 12px;
  color: grey;
  margin-top: 10px;
}
</style>
