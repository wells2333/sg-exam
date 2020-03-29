<template>
  <div>
    <transition name="fade-transform" mode="out-in">
      <div v-show="!loading">
        <div class="single-course-intro d-flex align-items-center justify-content-center" :style="'background-image: url(' + course.logoUrl + ');'">
          <div class="single-course-intro-content text-center">
            <div class="rate">
              <el-rate
                v-model="value"
                disabled
                text-color="#ff9900"
                score-template="{value}">
              </el-rate>
            </div>
            <h3>{{ course.courseName }}</h3>
            <div class="meta d-flex align-items-center justify-content-center">
              <a href="#">{{ course.teacher }}</a>
              <span><i class="fa fa-circle" aria-hidden="true"></i></span>
              <a href="#">{{ course.college }} &amp; {{ course.major }}</a>
            </div>
            <div class="price">免费</div>
          </div>
        </div>
        <div class="single-course-content padding-80">
          <el-row class="my-content-container ml-100 mr-100">
            <el-col :span="18" style="padding-right: 40px;">
              <el-tabs>
                <el-tab-pane>
              <span slot="label">
                <el-button type="default" class="course-content-btn">课程介绍</el-button>
              </span>
                  <div class="clever-description">
                    <div class="about-course mb-30">
                      <h4>课程介绍</h4>
                      <p>{{ course.courseDescription }}</p>
                    </div>
                  </div>
                </el-tab-pane>
                <el-tab-pane>
              <span slot="label">
                <el-button type="default" class="course-content-btn">课程安排</el-button>
              </span>
                  <div class="about-curriculum mb-30">
                    <h4>课程安排</h4>
                    <p>{{ course.courseDescription }}</p>
                  </div>
                </el-tab-pane>
                <el-tab-pane>
              <span slot="label">
                <el-button type="default" class="course-content-btn">课程评价</el-button>
              </span>
                  <div class="about-review mb-30">
                    <h4>课程评价</h4>
                    <p>{{ course.courseDescription }}</p>
                  </div>
                </el-tab-pane>
                <el-tab-pane>
              <span slot="label">
                <el-button type="default" class="course-content-btn">报名学员</el-button>
              </span>
                  <div class="about-members mb-30">
                    <h4>报名学员</h4>
                    <p>{{ course.courseDescription }}</p>
                  </div>
                </el-tab-pane>
                <el-tab-pane>
              <span slot="label">
                <el-button type="default" class="course-content-btn">学习交流</el-button>
              </span>
                  <div class="about-review mb-30">
                    <h4>学习交流</h4>
                    <p>{{ course.courseDescription }}</p>
                  </div>
                </el-tab-pane>
              </el-tabs>
            </el-col>
            <el-col :span="6">
              <div class="course-sidebar">
                <el-button type="primary" class="clever-btn mb-30 w-100" @click="buyCourse">购买课程</el-button>
                <div class="sidebar-widget">
                  <h4>课程特色</h4>
                  <ul class="features-list">
                    <li>
                      <h6><i class="el-icon-alarm-clock"></i>课时</h6>
                      <h6>2 周</h6>
                    </li>
                    <li>
                      <h6><i class="el-icon-bell"></i>课程</h6>
                      <h6>10</h6>
                    </li>
                    <li>
                      <h6><i class="el-icon-files"></i>问答</h6>
                      <h6>3</h6>
                    </li>
                    <li>
                      <h6><i class="el-icon-arrow-up"></i>通过率</h6>
                      <h6>60</h6>
                    </li>
                  </ul>
                </div>
                <div class="sidebar-widget">
                  <h4>猜你喜欢</h4>
                  <div class="single--courses d-flex align-items-center" v-for="course in likes" :key="course.id">
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
import { getObj } from '@/api/exam/course'
import { messageWarn } from '@/utils/util'

export default {
  data () {
    return {
      loading: true,
      courseId: '',
      course: {},
      value: 3.7,
      likes: [{
        id: 1,
        courseName: '应用文写作',
        price: '$20'
      }]
    }
  },
  created () {
    this.courseId = this.$route.query.courseId
    this.getCourseInfo()
  },
  methods: {
    getCourseInfo () {
      this.loading = true
      getObj(this.courseId).then(response => {
        this.course = response.data.data
        setTimeout(() => {
          this.loading = false
        }, 500)
      }).catch(error => {
        console.error(error)
      })
    },
    buyCourse () {
      messageWarn(this, '功能正在开发中')
    }
  }
}
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
  .rate {
    margin-bottom: 12px;
  }
  .course-content-btn {
    display: inline-block;
    height: 40px;
    background-color: transparent;
    font-weight: 600;
    text-transform: uppercase;
    letter-spacing: 1px;
    font-size: 14px;
    color: rgba(0, 0, 0, 0.25);
    border: 1px solid #ebebeb;
    border-radius: 6px;
    padding: 0 25px;
    line-height: 40px;
    -webkit-transition-duration: 800ms;
    transition-duration: 800ms;
    text-align: center;
    margin-right: 10px;
    margin-bottom: 10px;
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
</style>
