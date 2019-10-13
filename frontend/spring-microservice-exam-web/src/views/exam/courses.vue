<template>
  <div>
    <el-row class="course-msg">
      <el-col :span="24" style="color: black;">
        <h1>在线学习</h1>
      </el-col>
    </el-row>
    <div class="courses" v-loading="listLoading">
      <el-row v-for="course in courseList" :key="course.id">
        <el-col :offset="8">
          <div class="course">
            <a href="javascript:void(0);" class="course-title">
              <img :src="course.avatar" v-if="isNotEmpty(course.avatar)" class="course-image">
              <img src="../../../static/images/practices/practice.png" v-else class="course-image">
              <p>
                <span class="course-name">{{course.courseName}}</span>
              </p>
            </a>
            <div style="padding: 14px;" class="course-right">
              <h4>
                <label>{{course.courseName}}<span>客观题练习</span></label>
              </h4>
              <p>{{ course.courseDescription }}</p>
              <div class="course-details">
                <label><span>{{course.peoples}}</span>5人在学习</label>
              </div>
              <el-button type="success">开始练习</el-button>
            </div>
          </div>
        </el-col>
      </el-row>
      <el-col v-if="!listLoading && courseList.length === 0" :span="24">
        <p class="exam-empty">暂无更多数据</p>
      </el-col>
    </div>
  </div>
</template>
<script>
import { courseList } from '@/api/exam/course'
import store from '@/store'
import { isNotEmpty, notifyFail } from '@/utils/util'

export default {
  data () {
    return {
      listLoading: true,
      courseList: []
    }
  },
  created () {
    this.getCourseList()
  },
  methods: {
    // 加载课程列表
    getCourseList () {
      this.listLoading = true
      courseList().then(response => {
        this.courseList = response.data.list
        this.listLoading = false
      })
    },
    // 加载考试类表
    getExamList (course) {
      store.dispatch('GetCourseInfo', course).then(res => {
        this.$router.push({name: 'exams'})
      }).catch((err) => {
        notifyFail(this, '获取课程信息失败')
      })
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" rel="stylesheet/scss" scoped>
  @import "../../assets/css/common.scss";
  .course-msg {
    @extend %message-common;
  }
  .course {
    margin-top: 50px;
    padding-bottom: 50px;
    .course-title {
      float: left;
      position: relative;
      .course-name {
        border-bottom: 2px solid #fff;
      }
      span {
        display: block;
        line-height: 53px;
      }
    }
    .course-right {
      margin-left: 470px;
    }
    .course-right p {
      font-size: 14px;
      color: #848484;
      line-height: 24px;
      margin: 28px 0;
      min-height: 70px;
    }
    .course-details label {
      font-size: 14px;
      color: #17b7f2;
      margin-right: 50px;
    }
    .course-image {
      width: 100%;
      display: block;
      cursor: pointer;
    }
  }
  .exam-empty {
    text-align: center;
  }
</style>
