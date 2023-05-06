<template>
  <div>
    <el-row class="hero-area" type="flex" justify="center" align="middle">
      <el-col :span="24">
        <div class="hero-content">
          <h2>{{sysConfig.sys_web_main_title}}</h2>
          <h4>{{sysConfig.sys_web_sub_title_one}}</h4>
          <h4>{{sysConfig.sys_web_sub_title_two}}</h4>
          <router-link to="/courses" class="btn clever-btn">开始使用</router-link>
        </div>
      </el-col>
    </el-row>
    <div class="cool-facts-area padding-80-0">
      <el-row type="flex" justify="center" :gutter="50">
        <el-col :span="4">
          <transition name="fade-transform" mode="out-in">
            <div class="single-cool-facts-area mb-80" v-show="showFacts">
              <div class="icon">
                <img src="static/img/core-img/star.png" alt="">
              </div>
              <h2>
                <count-to :start-val="0" :end-val="userCount" :duration="2600" class="counter"/>
              </h2>
              <h5>用户数</h5>
            </div>
          </transition>
        </el-col>
        <el-col :span="4">
          <transition name="fade-transform" mode="out-in">
            <div class="single-cool-facts-area mb-80" v-show="showFacts">
              <div class="icon">
                <img src="static/img/core-img/star.png" alt="">
              </div>
              <h2>
                <count-to :start-val="0" :end-val="examinationCount" :duration="2600" class="counter"/>
              </h2>
              <h5>考试数</h5>
            </div>
          </transition>
        </el-col>
        <el-col :span="4">
          <transition name="fade-transform" mode="out-in">
            <div class="single-cool-facts-area mb-80" v-show="showFacts">
              <div class="icon">
                <img src="static/img/core-img/star.png" alt="">
              </div>
              <h2>
                <count-to :start-val="0" :end-val="subjectCount" :duration="2600" class="counter"/>
              </h2>
              <h5>题目数</h5>
            </div>
          </transition>
        </el-col>
        <el-col :span="4">
          <transition name="fade-transform" mode="out-in">
            <div class="single-cool-facts-area mb-80" v-show="showFacts">
              <div class="icon">
                <img src="static/img/core-img/star.png" alt="">
              </div>
              <h2>
                <count-to :start-val="0" :end-val="courseCount" :duration="2600" class="counter"/>
              </h2>
              <h5>课程数</h5>
            </div>
          </transition>
        </el-col>
      </el-row>
    </div>

    <div class="popular-courses-area padding-80-0">
      <el-row>
        <el-col :span="24">
          <div class="section-heading">
            <h3>热门课程</h3>
          </div>
        </el-col>
      </el-row>
      <el-row v-if="courses.length === 3" type="flex" justify="center" :gutter="50">
        <el-col :span="6" v-for="(course, index) in courses" :key="index">
          <transition name="fade-transform" mode="out-in">
            <div class="single-popular-course" v-show="showCourses" @click="courseDetail(course)">
              <img :src="course.imageUrl" alt="">
              <div class="course-content">
                <h4>{{course.courseName}}</h4>
                <div class="meta d-flex align-items-center">
                  <a href="#">{{course.teacher}}</a>
                  <span><i class="fa fa-circle" aria-hidden="true"></i></span>
                  <a href="#">{{course.major !== null ? course.major : ''}}</a>
                </div>
                <p>{{course.simpleDesc !== null ? course.simpleDesc : '-'}}</p>
              </div>
              <div class="seat-rating-fee d-flex justify-content-between">
                <div class="seat-rating h-100 d-flex align-items-center">
                  <div class="seat">
                    <i class="el-icon-user-solid" aria-hidden="true"></i> {{course.memberCount}}
                  </div>
                </div>
                <div class="course-fee h-100">
                  <a href="#" class="free">{{course.chargeType === 0 ? '免费' : '$' + course.chargePrice}}</a>
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
import { popularCourses } from '@/api/exam/course'
import { summary } from '@/api/exam/home'
import { notifyFail } from '@/utils/util'

export default {
  props: {
    step: {
      type: Number,
      default: 50
    }
  },
  data () {
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
        notifyFail(this, '加载首页数据失败！')
      })
    },
    getPopularCourses: function () {
      popularCourses(this.query).then(response => {
        const list = response.data.result
        if (list && list.length >= 3) {
          this.courses = []
          for (let i = 0; i < 3; i++) {
            this.courses.push(list[i])
          }
        }
      }).catch(() => {
        notifyFail(this, '加载热门课程数据失败！')
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
  created () {
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
