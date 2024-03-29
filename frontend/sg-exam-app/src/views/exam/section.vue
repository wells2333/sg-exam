<template>
  <div>
    <div class="section-content">
      <el-row>
        <el-col :span="6">
          <transition name="el-fade-in">
            <div class="section-title mb-30 sidebar-widget">
              <div v-for="(chapter, index) in detail.chapters" class="chapters-item" :key="chapter.chapter.id" @click="toggleIcon(chapter, index)">
                <div class="chapter-container">
                  <div class="chapter-title-box">
                    <p class="chapter-title">{{ chapter.chapter.title }}</p>
                    <i :class="(chapter.sectionHeight ? 'el-icon-arrow-up':'el-icon-arrow-down')"></i>
                  </div>
                  <div class="section-container" :style="{height: chapter.sectionHeight + 'px', display: chapter.sectionHeight ? 'block': 'none'}">
                    <div class="section-container-item"
                       v-for="section in chapter.sections" :key="section.section.id">
                      <p
                        :class="((pointId === undefined || pointId === -1) && section.section.id === sectionId) ? 'section-title-selected section-title' : 'section-title'"
                        @click.stop="handleClickSection(section)">
                        {{ section.section.title }}</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </transition>
        </el-col>
        <el-col :span="18" style="padding-left: 20px; padding-right: 20px;">
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
                <div class="points-box" v-if="currSec && currSec.points">
                     <div class="points-item" v-for="point in currSec.points" :key="point.id">
                        <p
                          :class="point.id === pointId ? 'point-title-selected point-title':  'point-title'"
                          @click="handleClickPoint(point)">
                        {{ point.title }}</p>
                      </div>
                </div>
                <div  class="section-video">
                  <sg-video v-if="videoUrl !== undefined && videoUrl !== null && videoUrl !== ''" ref="sectionVideo">
                  </sg-video>
                  <sg-audio v-if="audioUrl !== undefined && audioUrl !== null && audioUrl !== ''" ref="sectionAudio"></sg-audio>
                  <div class="section-video-content">
                    <div v-html="content"></div>
                  </div>
                </div>
                <!-- <div v-else-if="contentType === 1">
                  <div v-html="content"></div>
                </div> -->
              </div>
              <div class="section-button">
                <el-button type="primary" class="clever-btn mb-30 w-10" @click="goBack">{{$t('return')}}
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
import SgAudio from '@/components/SgAudio'
import {getKnowledgePointDetail} from '../../api/exam/point'

export default {
  components: {
    SgVideo,
    SgAudio
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
      audioUrl: undefined,
      detail: {},
      contentType: 0,
      clickSectionId: undefined,
      clickPointId: undefined,
      currChapt: undefined,
      currSec: undefined,
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
        const { chapters } = res.data.result
        chapters.forEach(item => {
          const { sections } = item
          item.sectionHeight = sections.length * 30
          item.secHeight = item.sectionHeight
        })
      }).catch(error => {
        console.error(error)
      })
    },
    getSection(id) {
      if (id === undefined) {
        return
      }
      this.stopVideo()
      this.loading = true
      watchSection(id).then(res => {
        this.contentType = res.data.result.section.contentType
        this.videoUrl = res.data.result.section.videoUrl
        this.audioUrl = res.data.result.section.speechUrl
        setTimeout(() => {
          const { title, content, operator, updateTime } = res.data.result.section
          this.section = res.data.result.section
          this.handleSection(this.section)
          this.title = title
          this.content = content
          this.operator = operator
          this.updateTime = updateTime
          this.loading = false
          this.updateVideoUrl()
          this.updateAudioUrl()
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
      this.stopVideo()
      this.loading = true
      getKnowledgePointDetail(id).then(res => {
        const {title, videoUrl, contentType, content, operator, updateTime, speechUrl} = res.data.result
        this.contentType = contentType
        this.videoUrl = videoUrl
        this.audioUrl = speechUrl
        setTimeout(() => {
          this.loading = false
          this.title = title
          this.content = content
          this.operator = operator
          this.updateTime = updateTime
          this.updateVideoUrl()
          this.updateAudioUrl()
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
    updateAudioUrl() {
      if (this.audioUrl !== undefined && this.audioUrl !== null) {
        if (this.$refs.sectionAudio) {
          this.$refs.sectionAudio.setSrcWithoutSubjectId(this.audioUrl, 0)
        }
      } else {
        this.audioUrl = undefined
      }
    },
    stopVideo() {
      if (this.$refs.sectionVideo) {
        this.$refs.sectionVideo.pause()
      }
    },
    handleSection(sec, isInit = false) {
      const tmp = JSON.parse(JSON.stringify(sec))
      if (tmp.points) {
        const res = tmp.points
        res.unshift({
          id: -1,
          title: '章节概览',
          secId: tmp.section.id
        })
        if (!this.pointId) {
          this.pointId = -1
        }
        this.currSec = tmp
      }
    },
    handleClickSection(section) {
      const { section: sec, points } = section
      if (points && points.length > 0) {
        this.handleSection(section)
      } else {
        this.pointId = undefined
        this.currSec = undefined
      }
      this.clickPointId = undefined
      if (this.clickSectionId !== undefined && this.clickSectionId === sec.id) {
        return
      }
      this.sectionId = sec.id
      this.clickSectionId = sec.id
      // 当前下的知识点
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
      if (point.id === -1) {
        // 章节概览
        this.getSection(point.secId)
        return
      }
      this.getPoint(point.id)
    },
    goBack() {
      if (this.$refs.sectionVideo) {
        this.$refs.sectionVideo.pause()
      }
      this.$router.go(-1)
    },
    toggleIcon(opt, index) {
      this.detail.chapters[index].sectionHeight = this.detail.chapters[index].sectionHeight ? 0 : opt.secHeight
      this.detail = Object.assign({}, this.detail)
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
  padding: 10px 0;
  border-bottom: 1px solid #eee;
  .chapter-title-box {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    .chapter-title{
      font-weight: bold;
      font-size: 18px;
      &:hover {
        text-decoration: underline;
      }
    }
  }
}
.section-container {
  transition: height .3s ease;
  .section-title {
    height: 30px;
    line-height: 30px;
  }
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

.points-box {
  display: flex;
  flex-direction: row;
  align-content: center;
  .points-item {
    padding: 0 15px;
    margin-right: 14px;
    background: #F6F7FB;
    border-radius: 3px;
    font-size: 12px;
    color: #4c4c4c;
    height: 40px;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    margin-bottom: 10px;
  }
}

.title-tips {
  font-size: 12px;
  color: grey;
  margin-top: 10px;
}
</style>
