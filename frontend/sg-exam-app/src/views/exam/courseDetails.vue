<template>
  <div>
    <transition name="el-fade-in">
      <div v-show="!loading">
        <div class="single-course-intro d-flex align-items-center justify-content-center"
             :style="'background-image: url(' + course.imageUrl + ');'">
          <div class="single-course-intro-content text-center">
            <div class="rate">
              <el-rate
                v-model="course.level"
                disabled
                text-color="#ff9900">
              </el-rate>
            </div>
            <h3>{{ course.courseName }}</h3>
            <div class="meta d-flex align-items-center justify-content-center"
                 v-if="course.college">
              <a href="#">{{ course.teacher }}</a>
              <span><i class="fa fa-circle" aria-hidden="true"></i></span>
              <a href="#">{{ course.college }} &amp; {{ course.major }}</a>
            </div>
            <div class="price">{{ course.chargeType === 1 ? $t('exam.course.charge') : $t('exam.course.free') }}
              <h6 v-if="course.chargePrice > 0">{{ course.chargePrice }}</h6>
            </div>
          </div>
        </div>
        <div class="single-course-content padding-50">
          <el-row class="my-content-container ml-100 mr-100">
            <el-col :span="18" style="padding-right: 40px;">
              <el-tabs v-model="activeName">
                <el-tab-pane name="desc">
                  <span slot="label">
                    <span class="course-content-btn">{{$t('exam.course.courseIntroduction')}}</span>
                  </span>
                  <div class="clever-description">
                    <div class="about-course mb-30">
                      <h4>{{$t('exam.course.courseIntroduction')}}</h4>
                      <p v-html="course.courseDescription"></p>
                    </div>
                  </div>
                </el-tab-pane>
                <el-tab-pane name="chapter">
                  <span slot="label">
                    <span class="course-content-btn">{{$t('exam.course.chapter')}}</span>
                  </span>
                  <div class="about-curriculum mb-30">
                    <h4>{{$t('exam.course.chapter')}}</h4>
                    <transition name="fade-transform" mode="out-in"
                                v-for="chapter in detail.chapters" :key="chapter.chapter.id">
                      <div class="chapter-container">
                        <p>{{ chapter.chapter.title }}</p>
                        <div class="section-container"
                             v-for="section in chapter.sections" :key="section.section.id">
                          <p class="section-title" @click="handleClickSection(section.section)">
                            {{ section.section.title }}
                            <span class="section-learn-hour">
                              <i class="el-icon-caret-right"></i>&nbsp;{{
                                section.section.learnHour
                              }}{{$t('exam.course.hour')}}
                            </span>
                          </p>
                          <div class="point-container" v-for="point in section.points"
                               :key="point.id">
                            <p class="point-title" @click="handleClickPoint(section.section, point)">
                              {{ point.title }}
                              <span class="section-learn-hour">
                              <i class="el-icon-caret-right"></i>&nbsp;{{
                                  point.learnHour
                                }}{{$t('exam.course.hour')}}
                            </span>
                            </p>
                          </div>
                        </div>
                      </div>
                    </transition>
                  </div>
                </el-tab-pane>
                <el-tab-pane name="evaluate">
                  <span slot="label">
                    <span class="course-content-btn">{{$t('exam.course.courseEvaluation')}}</span>
                  </span>
                  <div class="about-review mb-30">
                    <h4>{{$t('exam.course.courseEvaluation')}}</h4>
                    <div>
                      <el-form :model="evaluate">
                        <el-form-item label="">
                          <el-input type="textarea" :rows="3" :placeholder="$t('exam.course.inputEvaluation')"
                                    v-model="evaluate.evaluateContent"></el-input>
                        </el-form-item>
                        <el-form-item label="">
                          <el-rate v-model="evaluate.evaluateLevel"></el-rate>
                        </el-form-item>
                        <el-form-item>
                          <el-button type="primary" class="clever-btn"
                                     @click="handleSubmitEvaluate">{{$t('submit')}}
                          </el-button>
                        </el-form-item>
                      </el-form>
                    </div>
                    <div>
                      <div v-for="item in evaluates" :key="item.id">
                        <evaluate-item :item="item"></evaluate-item>
                      </div>
                    </div>
                  </div>
                </el-tab-pane>
                <el-tab-pane name="members">
                  <span slot="label">
                    <span class="course-content-btn">{{$t('exam.course.examinations')}}</span>
                  </span>
                  <div class="about-members mb-30">
                    <h4>{{$t('exam.course.examinations')}}</h4>
                    <p v-if="detail.examinations && detail.examinations.length > 0">
                      <a v-for="(e, index) in detail.examinations" :key="index" :href="'#/exam-details?examId=' + e.id" target="_self" style="color: #409EFF;">《{{e.examinationName}}》</a>
                    </p>
                  </div>
                </el-tab-pane>
                <el-tab-pane name="learn">
                  <span slot="label">
                    <span class="course-content-btn">{{$t('exam.course.studyExchange')}}</span>
                  </span>
                  <div class="about-review mb-30">
                    <h4>{{$t('exam.course.studyExchange')}}</h4>
                    <p>
                      {{$t('exam.course.courseAttach')}}：<a :href="courseAttachUrl" target="_blank">{{courseAttachName}}</a>
                    </p>
                  </div>
                </el-tab-pane>
              </el-tabs>
            </el-col>
            <el-col :span="6">
              <div class="course-sidebar">
                <el-button type="primary" class="clever-btn mb-30 w-100" style="margin-left: 0;" @click="handleStartLearn">
                  {{ $t('exam.course.startLearn') }}
                </el-button>
                <div class="sidebar-widget">
                  <h4>{{$t('exam.course.courseFeatures')}}</h4>
                  <ul class="features-list">
                    <li>
                      <h6><i class="el-icon-alarm-clock"></i>{{$t('exam.course.learnHour')}}</h6>
                      <h6>{{ detail.learnHour }}</h6>
                    </li>
                    <li>
                      <h6><i class="el-icon-bell"></i>{{$t('exam.course.chapter1')}}</h6>
                      <h6>{{ detail.chapterSize }}</h6>
                    </li>
                    <li>
                      <h6><i class="el-icon-files"></i>{{$t('exam.course.memberCount')}}</h6>
                      <h6>{{ detail.memberCount }}</h6>
                    </li>
                    <li>
                      <h6><i class="el-icon-files"></i>{{$t('exam.course.evaluatesCount')}}</h6>
                      <h6>{{ evaluates.length }}</h6>
                    </li>
                  </ul>
                </div>
                <div class="sidebar-widget">
                  <h4>{{$t('exam.course.youMayAlsoLike')}}</h4>
                  <div class="single--courses d-flex align-items-center" v-for="course in likes"
                       :key="course.id">
                    <div class="thumb">
                      <img src="static/img/bg-img/yml.jpg" alt="">
                    </div>
                    <div class="content">
                      <h5>{{ course.courseName }}</h5>
                      <h6>{{ course.price }}</h6>
                    </div>
                  </div>
                </div>
              </div>
            </el-col>
          </el-row>
        </div>
      </div>
    </transition>
  </div>
</template>
<script>
import {getCourseDetail, joinCourse, getCourseAttach} from '@/api/exam/course'
import {addObj, getEvaluateList} from '@/api/exam/courseEvaluate'
import {messageSuccess, messageWarn} from '@/utils/util'
import EvaluateItem from '@/components/EvaluateItem'

export default {
  components: {
    EvaluateItem
  },
  data() {
    return {
      loading: true,
      courseId: '',
      course: {},
      detail: {},
      value: 3.7,
      activeName: 'desc',
      evaluate: {
        evaluateContent: '',
        evaluateLevel: 5
      },
      likes: [{
        id: 1,
        courseName: this.$t('exam.course.demoCourseName'),
        price: '$20'
      }],
      evaluates: [],
      hasEvaluate: false,
      courseAttachName: '',
      courseAttachUrl: ''
    }
  },
  created() {
    this.courseId = this.$route.query.courseId
    this.getCourseInfo()
    this.getEvaluateList()
    this.getAttach()
  },
  methods: {
    getCourseInfo() {
      this.loading = true
      getCourseDetail(this.courseId).then(res => {
        this.detail = res.data.result
        this.course = res.data.result.course
        setTimeout(() => {
          this.loading = false
        }, 500)
      }).catch(error => {
        console.error(error)
        this.loading = false
      })
    },
    getEvaluateList() {
      getEvaluateList({courseId: this.courseId}).then(res => {
        const {code} = res.data
        if (code === 0) {
          this.evaluates = res.data.result.list
        }
      }).catch(error => {
        console.error(error)
      })
    },
    getAttach() {
      getCourseAttach(this.courseId).then(res => {
        const {code, result} = res.data
        if (code === 0 && result) {
          this.courseAttachName = result.attachName
          this.courseAttachUrl = result.attachUrl
        }
      }).catch(error => {
        console.error(error)
      })
    },
    handleClick(tab, event) {
    },
    handleClickSection(section) {
    },
    handleClickPoint(section, point) {
    },
    handleSubmitEvaluate() {
      if (this.hasEvaluate) {
        messageWarn(this, this.$t('exam.course.doNotResubmit'))
        return
      }

      if (this.evaluate.evaluateContent === '') {
        this.evaluate.evaluateContent = this.$t('exam.course.defaultEvaluate')
      }
      addObj({
        courseId: this.courseId,
        ...this.evaluate
      }).then(res => {
        if (res.data.code === 0) {
          this.evaluate.evaluateContent = ''
          this.hasEvaluate = true
          messageSuccess(this, this.$t('exam.course.submitSuccess'))
          this.getEvaluateList()
        } else {
          messageWarn(this, this.$t('exam.course.submitFailed'))
        }
      }).catch(error => {
        console.error(error)
      })
    },
    handleStartLearn() {
      joinCourse(this.courseId).then(res => {
        if (res.data.result) {
          const chapters = this.detail.chapters
          if (chapters && chapters.length > 0) {
            const chapter = chapters[0]
            const courseId = chapter.chapter.courseId
            const sections = chapter.sections
            if (sections && sections.length > 0) {
              const sectionId = sections[0].section.id
              this.$router.push({
                name: 'course-section',
                query: {sectionId: sectionId, courseId}
              })
            }
          }
        }
      }).catch(error => {
        console.error(error)
      })
    }
  }
}
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
.course-content-btn {
  display: inline-block;
  height: 40px;
  background-color: transparent;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 1px;
  font-size: 14px;
  border-radius: 6px;
  padding: 0 25px;
  line-height: 40px;
  -webkit-transition-duration: 800ms;
  transition-duration: 800ms;
  text-align: center;
  margin-right: 10px;
  margin-bottom: 10px;
  white-space: nowrap;
  cursor: pointer;
  background: #FFF;
  border: 1px solid #DCDFE6;
}
.clever-btn {
  display: inline-block;
  min-width: 160px;
  height: 40px;
  background-color: #3762f0;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 1px;
  font-size: 14px;
  color: #ffffff;
  border: 1px solid transparent;
  border-radius: 6px;
  padding: 0 30px;
  line-height: 40px;
  text-align: center;
  -webkit-transition-duration: 300ms;
  transition-duration: 300ms;
}
.my-content-container {
  margin-top: 0;
}
.section-title {
  margin-left: 16px;
  font-size: 14px;
  cursor: pointer;
}
.section-title:hover, .section-learn-hour:hover {
  color: #409EFF;
}
.section-learn-hour {
  float: right;
  color: rgba(0, 0, 0, .3);
}

.point-container {
  margin-left: 32px;
}
.point-title {
  font-size: 14px;
  cursor: pointer;
}
.point-title:hover {
  color: #409EFF;
}
</style>
