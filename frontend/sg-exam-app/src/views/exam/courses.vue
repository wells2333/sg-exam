<template>
  <div>
    <div class="clever-category bg-img" style="background-image: url(static/img/bg-img/bg2.jpg);">
      <h3>艺术 & 设计</h3>
    </div>
    <div class="content-container">
      <div class="course-card-list">
        <transition name="fade-transform" mode="out-in" v-for="course in courseList" :key="course.id">
          <div class="single-popular-course mb-80" v-show="course.show">
            <img :src="course.logoUrl" alt="">
            <div class="course-content">
              <h4>{{ course.courseName }}</h4>
              <div class="meta d-flex align-items-center">
                <a href="#">{{ course.college }} & {{ course.major }}</a>
                <span><i class="fa fa-circle" aria-hidden="true"></i></span>
                <a href="#">{{ course.teacher }}</a>
              </div>
              <p>{{ course.courseDescription }}</p>
            </div>
            <div class="seat-rating-fee d-flex justify-content-between">
              <div class="seat-rating h-100 d-flex align-items-center">
                <div class="seat">
                  <i class="el-icon-user-solid" aria-hidden="true"></i> 10
                </div>
                <div class="rating">
                  <i class="el-icon-star-on" aria-hidden="true"></i> 4.5
                </div>
              </div>
              <div class="course-fee h-100">
                <a href="#" @click="handleStartCourse(course)">免费</a>
              </div>
            </div>
          </div>
        </transition>
        <!-- 对齐 -->
        <i v-if="courseList !== undefined && courseList.length > 0" v-for="count in (courseList.length)" :key="count"></i>
      </div>
      <el-row style="text-align: center; margin-bottom: 50px;">
        <el-col :span="24">
          <el-button type="default" @click="scrollList" :loading="loading" style="margin-bottom: 100px;">加载更多</el-button>
        </el-col>
      </el-row>
    </div>
  </div>
</template>
<script>
import { courseList } from '@/api/exam/course'
import { messageWarn, notifyFail } from '@/utils/util'

export default {
  data () {
    return {
      total: 0,
      loading: true,
      isLastPage: false,
      query: {
        sort: 'id',
        order: ' asc',
        pageNum: 1,
        pageSize: 3,
        courseName: '',
        status: 0
      },
      courseList: []
    }
  },
  created () {
    this.getCourseList()
  },
  methods: {
    // 加载课程列表
    getCourseList () {
      this.loading = true
      courseList(this.query).then(response => {
        const { total, isLastPage, list } = response.data
        this.updateCourseList(list)
        this.total = total
        this.isLastPage = isLastPage
        this.loading = false
      }).catch(() => {
        notifyFail(this, '加载数据失败！')
        this.loading = false
      })
    },
    handleStartCourse (course) {
      // messageWarn(this, '功能正在开发中！')
      this.$router.push({name: 'course-details', query: {courseId: course.id}})
    },
    scrollList () {
      if (this.isLastPage) {
        messageWarn(this, '暂无更多数据！')
        return
      }
      if (this.loading) {
        messageWarn(this, '正在拼命加载！')
        return
      }
      this.loading = true
      setTimeout(() => {
        this.query.pageNum++
        courseList(this.query).then(response => {
          const { total, isLastPage, list } = response.data
          this.updateCourseList(list)
          this.total = total
          this.isLastPage = isLastPage
          this.loading = false
        }).catch(() => {
          messageWarn(this, '加载数据失败！')
          this.loading = false
        })
      }, 500)
    },
    updateCourseList (list) {
      if (list === undefined || list.length === 0) {
        return list
      }
      list.forEach(item => {
        item.show = false
      })
      if (this.courseList.length === 0) {
        this.courseList = list
      } else {
        list.forEach(item => {
          let exist = false
          for (let i = 0; i < this.courseList.length; i++) {
            if (this.courseList[i].id === item.id) {
              exist = true
            }
          }
          if (!exist) {
            this.courseList.push(item)
          }
        })
      }
      for (let i = 0; i < list.length; i++) {
        setTimeout(() => {
          list[i].show = true
        }, 250 + (100 * i))
      }
    }
  }
}
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
.clever-category {
  display: flex;
  width: 100%;
  height: 80px;
  position: relative;
  align-items: center;
  justify-content: center;
  -webkit-box-pack: center;
  z-index: 2;
  h3 {
    text-align: center;
    font-size: 30px;
    text-transform: uppercase;
    letter-spacing: 0.75px;
    color: #3762f0;
    margin-bottom: 0;
  }
}
.clever-category::after {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  content: '';
  background-color: rgba(215, 224, 252, 0.9);
  z-index: -1;
}
.bg-img {
  background-position: center center;
  background-size: cover;
  background-repeat: no-repeat;
}
.course-card-list {
  width: 100%;
  height: auto;
  overflow: auto;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  flex-direction: row;
  .single-popular-course {
    width: 30%;
    position: relative;
    margin-bottom: 100px;
    border-radius: 6px;
    box-shadow: 0 3px 8px rgba(0, 0, 0, 0.15);
    .card-item-snapshoot {
      border: 1px solid rgba(0,0,0,.15);
      background-origin: border-box;
      background-size: cover;
      background-color: #f0f0f0;
      background-position: 49% 38% ;
      height: 172px;
      display: block;
      border-radius: 6px 6px 0 0;
    }
    .card-item-detail {
      padding: 20px;
      .card-item-name {
        display: -webkit-box;
        overflow: hidden;
      }
      .card-item-course {
        --x-height-multiplier: 0.342;
        --baseline-multiplier: 0.22;
        font-weight: 300;
        font-style: normal;
        letter-spacing: 0;
        .card-item-course-detail {
          color: rgba(0,0,0,.54);
          fill: rgba(0,0,0,.54);
        }
      }
    }
  }
  >i {
    width: 30%;
    padding-right: 12px;
  }
}
</style>
