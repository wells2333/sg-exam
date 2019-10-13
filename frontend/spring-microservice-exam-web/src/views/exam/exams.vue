<template>
  <div>
    <el-row class="exam-msg">
      <el-col :span="24" style="color: black;">
        <h1>所有考试</h1>
      </el-col>
    </el-row>
    <el-row :gutter="40" class="exams" v-loading="listLoading">
      <div style="margin: 0 60px;">
        <el-col :xs="12" :sm="12" :lg="6" class="exams-col" v-for="(exam) in examList" :key="exam.id">
          <el-card class="exam-panel">
            <el-row>
              <el-col :span="24">
                <div class="exam-panel-div" title="">
                  <img v-if="exam.avatar" :src="getAvatar(exam.avatar)" :alt="exam.examinationName" class="exam-avatar">
                  <img v-else src="../../../static/images/home/default_exam.png" :alt="exam.examinationName" class="exam-avatar">
                </div>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <div class="exam-panel-exam-name" title="">
                  <el-tag type="success" size="small" v-if="exam.course !== undefined && exam.course !== null">{{ exam.course.courseName }}</el-tag>
                </div>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <div class="exam-panel-exam-course" title=""><span class="exam-panel-exam-course-span">{{exam.examinationName}}</span></div>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <div class="exam-panel-bottom">
                  <time class="exam-time">开始时间：{{ exam.startTime | timeFilter }}</time>
                  <br>
                  <time class="exam-time">结束时间：{{ exam.endTime | timeFilter }}</time>
                  <el-button type="text" class="exam-button" @click="startExam(exam)">开始考试</el-button>
                </div>
              </el-col>
            </el-row>
          </el-card>
        </el-col>
      </div>
      <el-col v-if="!listLoading && examList.length === 0" :span="24">
        <p class="exam-empty">暂无更多数据</p>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import { mapState } from 'vuex'
import { fetchList } from '@/api/exam/exam'
import { getCurrentTime } from '@/api/exam/examRecord'
import { isNotEmpty, notifyFail, getAttachmentPreviewUrl, formatDate } from '@/utils/util'
import store from '@/store'
import moment from 'moment'
import PanThumb from '@/components/PanThumb'

export default {
  components: { PanThumb },
  filters: {
    examTypeFilter (type) {
      const typeMap = {
        0: '正式考试',
        1: '模拟考试',
        2: '在线练习'
      }
      return typeMap[type]
    },
    timeFilter (time) {
      return formatDate(new Date(time), 'yyyy-MM-dd hh:mm')
    }
  },
  data () {
    return {
      listLoading: true,
      examList: [],
      query: {
        courseId: null,
        status: null,
        type: null
      },
      tempExamRecord: {
        id: null,
        userId: null,
        examinationId: null
      }
    }
  },
  computed: {
    // 获取用户信息
    ...mapState({
      userInfo: state => state.user.userInfo,
      course: state => state.course.course,
      sysConfig: state => state.sysConfig.sysConfig
    })
  },
  created () {
    if (isNotEmpty(this.course)) {
      this.query.courseId = this.course.id
    }
    // 加载考试列表
    this.getExamList()
  },
  methods: {
    // 加载考试列表
    getExamList () {
      this.listLoading = true
      fetchList(this.query).then(response => {
        this.examList = response.data.list
        this.listLoading = false
      }).catch(() => {
        this.$notify({
          title: '失败',
          message: '查看考试失败',
          type: 'error',
          duration: 2000
        })
        this.listLoading = false
      })
    },
    // 开始考试
    startExam (exam) {
      this.tempExamRecord.examinationId = exam.id
      this.tempExamRecord.userId = this.userInfo.id
      getCurrentTime().then(response => {
        // 校验考试时间
        const currentTime = moment(response.data.data)
        // 校验结束时间
        if (currentTime.isAfter(exam.endTime)) {
          this.$notify({
            title: '提示',
            message: '考试已结束',
            type: 'warn',
            duration: 2000
          })
        } else if (currentTime.isBefore(exam.startTime)) {
          // 考试未开始
          this.$notify({
            title: '提示',
            message: '考试未开始',
            type: 'warn',
            duration: 2000
          })
        } else {
          this.$confirm('确定要开始吗?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            // 开始考试
            store.dispatch('StartExam', this.tempExamRecord).then(() => {
              this.$router.push({name: 'start'})
            }).catch(() => {
              this.$notify({
                title: '提示',
                message: '开始考试失败',
                type: 'warn',
                duration: 2000
              })
            })
          }).catch(() => {
            console.log('取消考试')
          })
        }
      }).catch(() => {
        notifyFail(this, '开始考试失败！')
      })
    },
    getAvatar (avatar) {
      return getAttachmentPreviewUrl(this.sysConfig, avatar)
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" rel="stylesheet/scss" scoped>
  @import "../../assets/css/common.scss";
  .exam-msg {
    @extend %message-common;
  }
  .exams {
    .exams-col{
      margin-bottom: 32px;
    }
    .exam-panel {
      margin: 0 20px 20px 0;
      padding: 0;
      position: relative;
      .icon-people {
        color: #40c9c6;
      }
      .exam-panel-course {
        position: relative;
        display: inline-block;
        margin: 12px;
      }
      .exam-panel-div {
        border-radius: 0px;
        overflow: hidden;
        position: relative;
      }
      .exam-panel-exam-name {
        padding: 2px;
      }
      .exam-panel-exam-course {
        padding: 0;
      }
      .exam-panel-bottom {
        .exam-button {
          padding: 0;
          float: right;
        }
        .exam-time {
          font-size: 13px;
          color: #999;
        }
      }
      .exam-avatar {
        width: 100%;
        height: 100%;
        max-width: 400px;
        max-height: 300px;
        display: block;
        border-style: none;
        cursor: pointer;
        border-radius: 0;
        transition: all 0.2s linear;
        &:hover {
          transform: scale(1.1, 1.1);
          filter: contrast(130%);
        }
      }
    }
  }
  .exam-empty {
    font-size: 13px;
    color: #999;
    text-align: center;
  }
</style>
