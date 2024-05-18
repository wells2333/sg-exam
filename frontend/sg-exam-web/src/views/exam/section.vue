<template>
  <div>
    <div class="section-content">
      <el-row>
        <el-col :span="isCollapse ? 0 : 4">
          <transition name="el-fade-in">
            <div class="section-title mb-18 sidebar-widget" :class="isCollapse ? 'collapse': ''">
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
        <el-col :span="isCollapse ? 24 : 20" style="padding-left: 20px; padding-right: 20px;">
          <div v-loading="loading">
              <div>
                <div class="section-detail-title">
                  <i class="iconfont icon-collapse collapse-btn" @click.stop="handleCollapse"></i>
                  <h3 class="article-title">{{ title }}</h3>
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
                <div v-if="contentType === 2" class="pdf-container">
                  <div class="pdf-container-pagination mb-30">
                    <el-pagination
                      background
                      @current-change="handlePdfCurrentChange"
                      :current-page="pdf.page"
                      layout="prev, pager, next"
                      :page-size="1"
                      :pager-count="5"
                      :total="pdf.numPages"
                      :hide-on-single-page="true">
                    </el-pagination>
                  </div>
                  <pdf ref="pdf" :src="pdfUrl" :page="pdf.page" @page-loaded="handlePdfPageLoaded($event)"></pdf>
                </div>
                <div class="section-video" v-else>
                  <sg-video v-if="videoUrl !== undefined && videoUrl !== null && videoUrl !== ''" ref="sectionVideo"></sg-video>
                  <sg-audio v-if="audioUrl !== undefined && audioUrl !== null && audioUrl !== ''" ref="sectionAudio"></sg-audio>
                  <div class="section-video-content">
                    <div v-html="content"></div>
                  </div>
                </div>
              </div>
            </div>
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
import pdf from 'vue-pdf'

export default {
  components: {
    SgVideo,
    SgAudio,
    pdf
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
      pdfUrl: undefined,
      detail: {},
      contentType: 0,
      clickSectionId: undefined,
      clickPointId: undefined,
      currChapt: undefined,
      currSec: undefined,
      title: undefined,
      content: undefined,
      operator: '',
      updateTime: '',
      isCollapse: false,
      pdf: {
        page: 1,
        numPages: 1
      }
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
        // PDF 类型
        if (this.contentType === 2) {
          this.getPdfNumPages(res.data.result.section.content)
        } else {
          this.pdfUrl = undefined
        }
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
    handleSection(sec) {
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
    },
    getPdfNumPages(url) {
      this.loading = true
      let loadingTask = pdf.createLoadingTask(url)
      loadingTask.promise.then(pdf => {
        this.pdfUrl = loadingTask
        this.pdf.numPages = pdf.numPages
        this.loading = false
        this.$forceUpdate()
      }).catch((err) => {
        this.loading = false
        console.error(err)
      })
    },
    handleCollapse() {
      this.isCollapse = !this.isCollapse;
    },
    handlePdfCurrentChange(val) {
      this.loading = true
      this.pdf.page = val
    },
    handlePdfPageLoaded(event) {
      this.loading = false
    }
  }
}
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
.pdf-container {

}

.pdf-container-pagination {
  text-align: center;
}
</style>
