<template>
  <div>
    <transition name="fade-transform" mode="out-in">
      <div v-show="!loading">
        <div class="single-course-intro d-flex align-items-center justify-content-center"
             :style="'background-image: url(' + course.imageUrl + ');'">
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
            <div class="meta d-flex align-items-center justify-content-center"
                 v-if="course.college">
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
              <el-tabs v-model="activeName" @tab-click="handleClick">
                <el-tab-pane name="desc">
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
                <el-tab-pane name="chapter">
                  <span slot="label">
                    <el-button type="default" class="course-content-btn">课程目录</el-button>
                  </span>
                  <div class="about-curriculum mb-30">
                    <h4>课程目录</h4>
                    <transition name="fade-transform" mode="out-in"
                                v-for="chapter in detail.chapters" :key="chapter.chapter.id">
                      <div class="chapter-container">
                        <p>{{ chapter.chapter.title }}</p>
                        <div class="section-container" @click="handleClickSection(section)" v-for="section in chapter.sections">
                          <p class="section-title">{{ section.title }}
                            <span class="section-learn-hour">
                              <i class="el-icon-caret-right"></i>&nbsp;{{ section.learnHour }}小时
                            </span>
                          </p>
                        </div>
                      </div>
                    </transition>
                  </div>
                </el-tab-pane>
                <el-tab-pane name="evaluate">
                  <span slot="label">
                    <el-button type="default" class="course-content-btn">课程评价</el-button>
                  </span>
                  <div class="about-review mb-30">
                    <h4>课程评价</h4>
                    <p>{{ course.courseDescription }}</p>
                  </div>
                </el-tab-pane>
                <el-tab-pane name="members">
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
                      <h6>{{ detail.learnHour }}</h6>
                    </li>
                    <li>
                      <h6><i class="el-icon-bell"></i>章节</h6>
                      <h6>{{ detail.chapterSize }}</h6>
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
import {getCourseDetail} from '@/api/exam/course'
import {messageWarn} from '@/utils/util'

export default {
  data() {
    return {
      loading: true,
      courseId: '',
      course: {},
      detail: {},
      value: 3.7,
      activeName: 'desc',
      likes: [{
        id: 1,
        courseName: '应用文写作',
        price: '$20'
      }]
    }
  },
  created() {
    this.courseId = this.$route.query.courseId
    this.getCourseInfo()
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
    buyCourse() {
      messageWarn(this, '功能正在开发中')
    },
    handleClick(tab, event) {

    },
    handleClickSection(section) {
      if (section.videoId === undefined || section.videoId === null) {
        messageWarn(this, '无视频内容')
        return;
      }
      this.$router.push({name: 'course-section', query: {sectionId: section.id}})
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
.section-title {
  margin-left: 30px;
  font-size: 14px;
  cursor: pointer;
}
.section-title:hover,.section-learn-hour:hover {
  color: #409EFF;
}
.section-learn-hour {
  float: right;
  color: rgba(0, 0, 0, .3);
}
</style>
