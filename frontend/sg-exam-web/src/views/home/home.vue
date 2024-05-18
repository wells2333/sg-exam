<template>
  <div>
    <el-row class="hero-area" type="flex" justify="center" align="middle">
      <el-col :span="24">
        <div class="hero-content">
          <h4>{{ sysConfig.sys_web_sub_title_one }}</h4>
          <h4>{{ sysConfig.sys_web_sub_title_two }}</h4>
          <router-link to="/courses" class="btn clever-btn">{{ $t('homePage.startUsing') }}
          </router-link>
        </div>
      </el-col>
    </el-row>
    <div class="cool-facts-area padding-30-0">
      <el-row type="flex" justify="center" :gutter="50">
        <el-col :span="4">
          <transition name="fade-transform" mode="out-in">
            <div class="single-cool-facts-area mb-30" v-show="showFacts">
              <div class="icon">
                <img src="static/img/core-img/star.png" alt="">
              </div>
              <h2>
                <count-to :start-val="0" :end-val="userCount" :duration="2600" class="counter"/>
              </h2>
              <h5>{{ $t('homePage.userCnt') }}</h5>
            </div>
          </transition>
        </el-col>
        <el-col :span="4">
          <transition name="fade-transform" mode="out-in">
            <div class="single-cool-facts-area mb-30" v-show="showFacts">
              <div class="icon">
                <img src="static/img/core-img/star.png" alt="">
              </div>
              <h2>
                <count-to :start-val="0" :end-val="examinationCount" :duration="2600"
                          class="counter"/>
              </h2>
              <h5>{{ $t('homePage.examinationCnt') }}</h5>
            </div>
          </transition>
        </el-col>
        <el-col :span="4">
          <transition name="fade-transform" mode="out-in">
            <div class="single-cool-facts-area mb-30" v-show="showFacts">
              <div class="icon">
                <img src="static/img/core-img/star.png" alt="">
              </div>
              <h2>
                <count-to :start-val="0" :end-val="subjectCount" :duration="2600" class="counter"/>
              </h2>
              <h5>{{ $t('homePage.subjectCnt') }}</h5>
            </div>
          </transition>
        </el-col>
        <el-col :span="4">
          <transition name="fade-transform" mode="out-in">
            <div class="single-cool-facts-area mb-30" v-show="showFacts">
              <div class="icon">
                <img src="static/img/core-img/star.png" alt="">
              </div>
              <h2>
                <count-to :start-val="0" :end-val="courseCount" :duration="2600" class="counter"/>
              </h2>
              <h5>{{ $t('homePage.courseCnt') }}</h5>
            </div>
          </transition>
        </el-col>
      </el-row>
    </div>

    <div class="popular-courses-area padding-30-0" ref="coursesContainer">
      <el-row>
        <el-col :span="24">
          <div class="section-heading">
            <h3>{{ $t('homePage.popularCourses') }}</h3>
          </div>
        </el-col>
      </el-row>
      <el-row type="flex" justify="center" :gutter="20" class="custom-row">
        <el-col :span="6" v-for="(course, index) in courses" :key="index">
          <transition name="fade-transform" mode="out-in">
            <div class="single-popular-course" v-show="showCourses" @click="courseDetail(course)">
              <img :src="course.imageUrl" alt="">
              <div class="course-content">
                <h3>{{ course.courseName | simpleStrFilter }}</h3>
                <div class="meta d-flex align-items-center">
                  <a href="#">{{ course.teacher }}</a>
                  <span><i class="fa fa-circle" aria-hidden="true"></i></span>
                  <a href="#">{{ course.major !== null ? course.major : '' }}</a>
                </div>
                <p class="clamp">
                  {{ course.simpleDesc !== null ? course.simpleDesc : '暂无简介' }}</p>
              </div>
              <div class="seat-rating-fee d-flex justify-content-between">
                <div class="seat-rating h-100 d-flex align-items-center">
                  <div class="seat">
                    <i class="el-icon-user-solid" aria-hidden="true"></i> {{ course.memberCount }}
                  </div>
                </div>
                <div class="course-fee h-100">
                  <a href="#"
                     class="free">{{ course.chargeType === 0 ? $t('exam.course.free') : '$' + course.chargePrice }}</a>
                </div>
              </div>
            </div>
          </transition>
        </el-col>
      </el-row>
    </div>
    <div v-if="isActive" class="go-top-box" @click="goTop(step)">
      <i class="top-icon el-icon-caret-top"></i>
    </div>
    <o-footer></o-footer>
  </div>
</template>

<script>
import OFooter from '../common/footer'
import CountTo from 'vue-count-to'
import {mapGetters} from 'vuex'
import {popularCourses} from '@/api/exam/course'
import {summary} from '@/api/exam/home'
import {notifyFail} from '@/utils/util'

export default {
  props: {
    step: {
      type: Number,
      default: 50
    }
  },
  data() {
    return {
      isActive: false,
      showFacts: false,
      showCourses: false,
      showBlog: false,
      query: {
        findFav: false
      },
      courses: [],
      userCount: 0,
      courseCount: 0,
      examinationCount: 0,
      subjectCount: 0
    }
  },
  components: {
    OFooter,
    CountTo
  },
  methods: {
    getHomeSummary: function () {
      summary({}).then(response => {
        const result = response.data.result
        if (result.userCount) {
          this.userCount = result.userCount
        }
        if (result.courseCount) {
          this.courseCount = result.courseCount
        }
        if (result.examinationCount) {
          this.examinationCount = result.examinationCount
        }
        if (result.subjectCount) {
          this.subjectCount = result.subjectCount
        }
      }).catch(() => {
        notifyFail(this, this.$t('homePage.loadFailed'))
      })
    },
    getPopularCourses: function () {
      popularCourses(this.query).then(response => {
        const list = response.data.result
        this.courses = []
        for (let i = 0; i < list.length; i++) {
          this.courses.push(list[i])
        }
        let num = list.length / 4;
        const container = this.$refs.coursesContainer;
        container.style.height = (num + 1) * 300 + 'px';
      }).catch(() => {
        notifyFail(this, this.$t('homePage.loadCoursesFailed'))
      })
    },
    courseDetail: function (course) {
      this.$router.push({name: 'course-details', query: {courseId: course.id}})
    },
    // 返回顶部
    goTop: function (i) {
      document.documentElement.scrollTop -= i
      if (document.documentElement.scrollTop > 0) {
        setTimeout(() => this.goTop(i), 16)
      }
    }
  },
  computed: {
    ...mapGetters([
      'sysConfig'
    ])
  },
  created() {
    let vm = this
    window.onscroll = function () {
      vm.isActive = document.documentElement.scrollTop > 60
      if (document.documentElement.scrollTop > 250) {
        setTimeout(() => {
          vm.showCourses = true
        }, 350)
      }

      if (document.documentElement.scrollTop > 650) {
        setTimeout(() => {
          vm.showBlog = true
        }, 350)
      }
    }
    this.getHomeSummary()
    this.getPopularCourses()
    setTimeout(() => {
      vm.showFacts = true
    }, 350)
  }
}
</script>

<style scoped>
.custom-row {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
}

.custom-row.el-row--flex {
  justify-content: flex-start; /* 水平左对齐 */
}

.el-col {
  margin-bottom: 20px; /* 可选的间距 */
}

.clamp {
  height: 70px;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  overflow: hidden;
  -webkit-line-clamp: 3;
  text-overflow: ellipsis; /* 超出部分显示省略号 */
}
</style>
