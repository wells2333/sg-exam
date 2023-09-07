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
        <div class="single-course-content padding-80">
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
                      <div class="user-evaluate-item" v-for="e in evaluates" :key="e.id">
                        <el-row class="user-evaluate-item-bg">
                          <el-col :span="2" >
                            <img width="40" height="40" class="user-evaluate-item-avatar" :src="e.avatarUrl ? e.avatarUrl:'https://yunmianshi.com/attach-storage/yunmianshi/default/124/user.png'">
                          </el-col>
                          <el-col :span="22">
                            <div class="user-evaluate-item-top">
                              <span style="color: #333; margin-right: 15px;">{{ e.operatorName }}</span>
                              <el-rate v-model="e.evaluateLevel" :disabled="true" style="height: 100%; line-height: initial;"></el-rate>
                            </div>
                            <div class="user-evaluate-item-content" style="color:#666;">
                              {{ e.evaluateContent }}
                            </div>
                            <div class="user-evaluate-item-time">
                              {{ e.createTime }}
                            </div>
                          </el-col>
                        </el-row>
                      </div>
                    </div>
                  </div>
                </el-tab-pane>
                <el-tab-pane name="members">
                  <span slot="label">
                    <span class="course-content-btn">{{$t('exam.course.registerStudents')}}</span>
                  </span>
                  <div class="about-members mb-30">
                    <h4>{{$t('exam.course.registerStudents')}}</h4>
                    <p>{{$t('exam.course.registerStudents1')}}：{{ detail.memberCount }}</p>
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
                <el-button type="primary" class="clever-btn mb-30 w-100" @click="handleJoin">
                  {{ joinBtnText }}
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

export default {
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
      joinBtnText: '',
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
        this.joinBtnText = this.detail.isUserJoin === true ? this.$t('exam.course.cancelRegistration') : this.$t('exam.course.registration')
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
      if (this.detail.isUserJoin !== true) {
        messageWarn(this, this.$t('exam.course.pleaseRegistration'))
        return
      }
      this.$router.push({
        name: 'course-section',
        query: {sectionId: section.id, courseId: this.courseId}
      })
    },
    handleClickPoint(section, point) {
      if (this.detail.isUserJoin !== true) {
        messageWarn(this, this.$t('exam.course.pleaseRegistration'))
        return
      }
      this.$router.push({
        name: 'course-section',
        query: {sectionId: section.id, courseId: this.courseId, pointId: point.id}
      })
    },
    handleSubmitEvaluate() {
      if (this.detail.isUserJoin !== true) {
        messageWarn(this, this.$t('exam.course.pleaseRegistration'))
        return
      }
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
    handleJoin() {
      const type = this.detail.isUserJoin ? '0' : '1'
      const text = this.detail.isUserJoin ? this.$t('exam.course.cancelRegistration') : this.$t('exam.course.registration')
      this.$confirm(this.$t('sure') + text + '?', this.$t('tips'), {
        confirmButtonText: this.$t('sure'),
        cancelButtonText: this.$t('cancel'),
        type: 'warning'
      }).then(() => {
        joinCourse(this.courseId, type).then(res => {
          if (res.data.result) {
            messageSuccess(this, text + this.$t('success'))
            this.getCourseInfo()
          }
        }).catch(error => {
          console.error(error)
          messageWarn(this, text + this.$t('failed'))
        })
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
.user-evaluate-item {
  margin-top: 26px;
  .user-evaluate-item-bg {
    border-bottom: 1px solid rgba(233,233,233,.6);
    padding-bottom: 20px;
  }
  .user-evaluate-item-avatar {
    width: 40px;
    height: 40px;
    border-radius: 50%;
  }
  .user-evaluate-item-top{
    font-size: 13px;
    display: flex;
    flex-direction: row;
    align-items: center;
    height: 23px;
  }
}
.user-evaluate-item-content {
  margin-top: 8px;
}
.user-evaluate-item-time {
  font-size: 12px;
  margin-top: 10px;
  color: #999;
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
