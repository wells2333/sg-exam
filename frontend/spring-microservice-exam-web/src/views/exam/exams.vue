<template>
  <div class="content-container">
    <!-- 搜索框 -->
    <div class="search-form">
      <el-form ref="examForm" :inline="true" :model="query" label-width="100px" class="examForm">
        <el-form-item label="考试名称" prop="examinationName">
          <el-input v-model="query.examinationName" autocomplete="off" placeholder="考试名称" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('examForm')">搜索</el-button>
          <el-button @click="resetForm('examForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 分类 -->
    <div class="category-list">
      <ul>
        <li :class="activeTag === '1' ? 'active' : ''" @click="changeTag('1')">全部</li>
        <li :class="activeTag === '2' ? 'active' : ''" @click="changeTag('2')">最新发布</li>
        <li :class="activeTag === '3' ? 'active' : ''" @click="changeTag('3')">最多点击</li>
        <li :class="activeTag === '4' ? 'active' : ''" @click="changeTag('4')">参数人数</li>
      </ul>
    </div>

    <spinner-loading v-if="listLoading"/>
    <!-- 考试卡片列表 -->
    <div class="exam-card-list">
      <div class="card-item" v-for="(exam) in examList" :key="exam.id">
        <el-card :body-style="{ padding: '0px' }">
          <div class="card-item-snapshoot">
            <span class="new-card-icon">NEW</span>
            <img v-if="exam.avatarId !== null && exam.avatarId !== ''" :src="exam.avatarId" @click="startExam(exam)"/>
            <img v-else src="../../../static/images/home/157372358886316.png" @click="startExam(exam)"/>
          </div>
          <div class="card-item-bottom">
            <span class="title">{{exam.examinationName}}</span>
            <span class="course" v-if="exam.course !== undefined && exam.course !== null">{{exam.course.courseName}}</span>
          </div>
          <div class="card-item-bottom-btn">
            <div class="card-item-time">
              <span class="time">开始时间：{{ exam.startTime | timeFilter }}</span>
              <br/>
              <span class="time">结束时间：{{ exam.endTime | timeFilter }}</span>
            </div>
            <div class="card-item-btn">
              <el-button class="start-btn" type="info" size="mini" plain round @click="startExam(exam)">开始</el-button>
            </div>
          </div>
        </el-card>
      </div>
    </div>
    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination v-show="total > 0" :current-page="query.pageNum" :page-sizes="[10,20,30, 50]" :page-size="query.pageSize" :total="total" background layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange" @current-change="handleCurrentChange"/>
    </div>
    <div class="" v-if="!listLoading && examList.length === 0">
      <p class="exam-empty">暂无更多数据</p>
    </div>
  </div>
</template>
<script>
import {mapGetters, mapState} from 'vuex'
import { fetchList } from '@/api/exam/exam'
import { getCurrentTime } from '@/api/exam/examRecord'
import { isNotEmpty, messageFail, messageWarn, getAttachmentPreviewUrl, formatDate } from '@/utils/util'
import store from '@/store'
import moment from 'moment'
import PanThumb from '@/components/PanThumb'
import SpinnerLoading from '@/components/SpinnerLoading'

export default {
  components: { PanThumb, SpinnerLoading },
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
      total: 0,
      listLoading: true,
      examList: [],
      query: {
        sort: 'id',
        order: ' asc',
        pageNum: 1,
        pageSize: 50,
        examinationName: '',
        status: 0
      },
      tempExamRecord: {
        id: null,
        userId: null,
        examinationId: null
      },
      // 默认全部
      activeTag: '1'
    }
  },
  computed: {
    // 获取用户信息
    ...mapState({
      userInfo: state => state.user.userInfo,
      course: state => state.course.course,
      sysConfig: state => state.sysConfig.sysConfig,
      examRecord: state => state.exam.examRecord
    }),
    ...mapGetters([
      'subject'
    ])
  },
  created () {
    if (isNotEmpty(this.course)) {
      this.query.courseId = this.course.id
    }
    if (this.$route.query.query !== '') {
      this.query.examinationName = this.$route.query.query
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
        this.total = response.data.total
        this.listLoading = false
      }).catch(() => {
        messageWarn(this, '查看考试失败！')
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
          messageWarn(this, '考试已结束')
        } else if (currentTime.isBefore(exam.startTime)) {
          // 考试未开始
          messageWarn(this, '考试未开始')
        } else {
          this.$confirm('确定要开始吗?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            // 开始考试
            store.dispatch('StartExam', this.tempExamRecord).then(() => {
              if (this.examRecord === undefined || this.subject === undefined) {
                messageWarn(this, '开始考试失败')
                return
              }
              this.$router.push({ path: `/start/${exam.id}-${this.examRecord.id}-${this.subject.id}-${this.subject.type}` })
            }).catch(() => {
              messageWarn(this, '开始考试失败')
            })
          }).catch(() => {
            console.log('取消考试')
          })
        }
      }).catch(() => {
        messageFail(this, '开始考试失败！')
      })
    },
    getAvatar (avatar) {
      return getAttachmentPreviewUrl(this.sysConfig, avatar)
    },
    submitForm () {
      this.getExamList()
    },
    resetForm () {
      this.query.examinationName = ''
    },
    // 切换tag
    changeTag (tag) {
      this.activeTag = tag
      this.getExamList()
    },
    handleSizeChange (val) {
      this.query.limit = val
      this.getExamList()
    },
    handleCurrentChange (val) {
      this.query.pageNum = val
      this.getExamList()
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
  .content-container {
    @extend %content-container;
    margin-left: 50px;
  }
  .exam-empty {
    font-size: 13px;
    color: #999;
    text-align: center;
  }
  .category-list {
    margin: 0 auto 30px;
    padding: 15px 10px;
    width: 98%;
    box-shadow: 0 5px 15px 0 rgba(82,94,102,.1);
    border-radius: 4px;
    ul {
      margin: 0;
      overflow: hidden;
    }
    .active {
      color: #fff;
      background: #409eff;
    }
    li  {
      list-style: none;
      display: block;
      float: left;
      margin: 10px;
      padding: 0 15px;
      height: 24px;
      line-height: 24px;
      color: #666;
      font-size: 13px;
      border-radius: 5px;
      cursor: pointer;
    }
  }

  .exam-card-list {
    width: 100%;
    display: flex;
    flex-wrap: wrap;
    .card-item {
      flex: 0 0 22%;
      width: 25%;
      max-width: 25%;
      padding: 0 15px;
      overflow: hidden;
      margin-bottom: 20px;
      .card-item-snapshoot {
        position: relative;
        box-sizing: border-box;
        display: flex;
        justify-content: center;
        align-items: center;
        height: 200px;
        transition: all .3s ease;
        .new-card-icon {
          position: absolute;
          left: 0;
          top: 0;
          width: 36px;
          height: 20px;
          line-height: 20px;
          text-align: center;
          background-color: #4172ff;
          color: #fff;
          font-size: 12px;
          background: linear-gradient(to top right,#409eff,#bcf7f6);
          border-bottom-right-radius: 5px;
        }
        img {
          max-width: 100%;
          max-height: 100%;
          display: block;
          cursor: pointer;
        }
      }
      .card-item-bottom {
        border-top: 1px solid #eee;
        margin: 0;
        padding: 10px 15px;
        display: flex;
        height: 25px;
        background-color: #304156;
        .title {
          flex: 3;
          height: 25px;
          line-height: 25px;
          color: #fff;
          overflow: hidden;
          white-space: nowrap;
          text-overflow: ellipsis;
        }
        .course {
          float: left;
          height: 20px;
          line-height: 25px;
          font-size: 13px;
          color: #999;
        }
      }
      .card-item-bottom-btn {
        vertical-align: initial;
        line-height: normal;
        background: hsla(0,0%,100%,.9);
        border-top: 1px solid #eee;
        width: 100%;
        .card-item-time {
          display: inline-block;
          float: left;
          padding: 5px;
          .time {
            float: left;
            height: 20px;
            line-height: 25px;
            font-size: 13px;
            color: #999;
          }
        }
        .card-item-btn {
          display: inline-block;
          padding-top: 5px;
          padding-right: 0;
          float: right;
          .start-btn {
            margin: 7px;
          }
        }
      }
    }
  }

  .pagination-container {
    margin: 50px;
    float: right;
  }
</style>
