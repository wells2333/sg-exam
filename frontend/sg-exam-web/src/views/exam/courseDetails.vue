<template>
  <div>
    <div v-loading="loading">
        <div class="single-course-intro d-flex align-items-center justify-content-center"
             :style="'background-image: url(' + course.imageUrl + ');'">
          <div class="single-course-intro-content text-center">
            <h3>{{ course.courseName | simpleStrFilter }}</h3>
            <div class="meta d-flex align-items-center justify-content-center"
                 v-if="course.college">
              <a href="#">{{ course.teacher }}</a>
              <span><i class="fa fa-circle" aria-hidden="true"></i></span>
              <a href="#">{{ course.college }} &amp; {{ course.major }}</a>
            </div>
            <div class="price">{{ course.chargeType === 1 ? $t('exam.course.charge') : $t('exam.course.free') }}
              <h6 v-if="course.chargePrice > 0">{{ course.chargePrice }}</h6>
            </div>
            <div class="favorite-btn" v-show="favoriteBtnText !== undefined" @click="handleFavorite" >
              <i :class="detail.favorite ? 'favorite-icon el-icon-star-on' : 'cancel-favorite-icon el-icon-star-off'"></i>
              <span>{{ favoriteBtnText }}</span>
            </div>
          </div>
        </div>
        <div class="single-course-content padding-50">
          <el-row class="my-content-container ml-100 mr-100">
            <el-col :span="18" style="padding-right: 40px;">
              <el-tabs v-model="activeName">
                <el-tab-pane name="desc">
                  <span slot="label">
                    <span class="exam-content-btn">{{$t('exam.course.courseIntroduction')}}</span>
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
                    <span class="exam-content-btn">{{$t('exam.course.chapter')}}</span>
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
                    <span class="exam-content-btn">{{$t('exam.course.courseEvaluation')}}</span>
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
                      <el-row class="list-pagination" style="margin-top: 16px;" v-show="evaluates && evaluates.length > 0">
                        <el-pagination
                          @size-change="handleEvaluateSizeChange"
                          @current-change="handleEvaluateCurrentChange"
                          :current-page="evaluateQuery.page"
                          :page-sizes="[10, 20, 50]"
                          :page-size="10"
                          layout="total, sizes, prev, pager, next, jumper"
                          :total="evaluateTotal">
                        </el-pagination>
                      </el-row>
                    </div>
                  </div>
                </el-tab-pane>
                <el-tab-pane name="members">
                  <span slot="label">
                    <span class="exam-content-btn">{{$t('exam.course.examinations')}}</span>
                  </span>
                  <div class="about-members mb-30">
                    <h4>{{$t('exam.course.examinations')}}</h4>
                    <p v-if="detail.examinations && detail.examinations.length > 0">
                      <a v-for="(e, index) in detail.examinations" :key="index" :href="'#/exam-details?examId=' + e.id" target="_self">《{{e.examinationName}}》</a>
                    </p>
                  </div>
                </el-tab-pane>
                <el-tab-pane name="learn">
                  <span slot="label">
                    <span class="exam-content-btn">{{$t('exam.course.studyExchange')}}</span>
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
              </div>
            </el-col>
          </el-row>
        </div>
      </div>
  </div>
</template>
<script>
import { mapState } from 'vuex'
import {getCourseDetail, joinCourse, getCourseAttach, favoriteCourse} from '@/api/exam/course'
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
      evaluates: [],
      hasEvaluate: false,
      courseAttachName: '',
      courseAttachUrl: '',
      favoriteBtnText: undefined,
      favoriteBtnLoading: false,
      evaluateTotal: 0,
      evaluateQuery: {
        page: 1,
        courseId: undefined
      }
    }
  },
  created() {
    this.courseId = this.$route.query.courseId
    this.evaluateQuery.courseId = this.$route.query.courseId
    this.getCourseInfo()
    this.getEvaluateList()
    this.getAttach()
  },
  computed: {
    ...mapState({
      userInfo: state => state.user.userInfo
    })
  },
  methods: {
    getCourseInfo() {
      this.loading = true
      getCourseDetail(this.courseId).then(res => {
        this.detail = res.data.result
        this.course = res.data.result.course
        this.updateFavoriteBtnText()
        this.loading = false
      }).catch(error => {
        console.error(error)
        this.loading = false
      })
    },
    getEvaluateList() {
      getEvaluateList({...this.evaluateQuery}).then(res => {
        const {code} = res.data
        if (code === 0) {
          this.evaluates = res.data.result.list
          this.evaluateTotal = res.data.result.total
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
    },
    handleFavorite() {
      const userId = this.userInfo.id
      let type = this.detail && this.detail.favorite ? 0 : 1;
      const tips = type === 1 ? this.$t('fav.favorite') : this.$t('fav.cancelFavorite')
      this.favoriteBtnLoading = true
      favoriteCourse(this.courseId, userId, type).then(res => {
        if (res.data.result) {
          this.detail.favorite = !this.detail.favorite
        } else {
          messageWarn(this, tips + this.$t('failed'))
        }
      }).catch(error => {
        console.error(error)
        messageWarn(this, tips + this.$t('failed'))
      }).finally(() => {
        this.updateFavoriteBtnText()
        this.favoriteBtnLoading = false
      })
    },
    updateFavoriteBtnText() {
      if (this.detail && this.detail.favorite) {
        this.favoriteBtnText = this.$t('fav.cancelFavorite')
      } else {
        this.favoriteBtnText = this.$t('fav.favorite')
      }
    },
    handleEvaluateSizeChange(val) {
      this.evaluateQuery.pageSize = val
      this.getEvaluateList()
    },
    handleEvaluateCurrentChange(val) {
      this.evaluateQuery.page = val
      this.getEvaluateList()
    }
  }
}
</script>

<style lang="scss" rel="stylesheet/scss" scoped>

</style>
