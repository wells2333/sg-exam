<template>
  <div>
    <div class="courses" v-loading="listLoading">
      <el-row v-for="practice in practices" :key="practice.id">
        <el-col :offset="8">
          <div class="practice">
            <a href="javascript:void(0);" class="practice-title">
              <img :src="getAvatar(practice.avatar)" v-if="practice.avatar" class="practice-image">
              <p>
                <span class="practice-name">{{practice.examinationName}}</span>
              </p>
            </a>
            <div style="padding: 14px;" class="practice-right">
              <h4>
                <label>{{practice.examinationName}}<span>客观题练习</span></label>
              </h4>
              <p>{{ practice.remark }}</p>
              <div class="practice-details">
                <label><span>{{practice.peoples}}</span>5人在学习</label>
              </div>
              <el-button type="success" @click="startPractice(practice)">开始练习</el-button>
            </div>
          </div>
        </el-col>
      </el-row>
      <el-col v-if="!listLoading && practices.length === 0" :span="24">
        <p class="exam-empty">暂无更多数据</p>
      </el-col>
    </div>
  </div>
</template>
<script>
import { mapState } from 'vuex'
import { fetchList } from '@/api/exam/exam'
import { getAttachmentPreviewUrl } from '@/utils/util'

export default {
  data () {
    return {
      listLoading: true,
      practices: [],
      listQuery: {
        type: '2'
      },
      tempExamRecord: {
        id: '',
        userId: '',
        examinationId: ''
      }
    }
  },
  computed: {
    // 获取用户信息
    ...mapState({
      userInfo: state => state.user.userInfo,
      sysConfig: state => state.sysConfig.sysConfig
    })
  },
  created () {
    this.getPractices()
  },
  methods: {
    // 加载课程列表
    getPractices () {
      this.listLoading = true
      fetchList(this.listQuery).then(response => {
        this.practices = response.data.result.list
        this.listLoading = false
      })
    },
    // 加载考试类表
    getExamList (practice) {
      this.$router.push({name: 'exams', query: {practiceId: practice.id}})
    },
    getAvatar (avatar) {
      return getAttachmentPreviewUrl(this.sysConfig, avatar)
    },
    // 开始练习
    startPractice (practice) {
      // this.tempExamRecord.examinationId = practice.id
      // this.tempExamRecord.userId = this.userInfo.id
      // store.dispatch('StartPractice', this.tempExamRecord).then(() => {
      //   this.$router.push({name: 'practice'})
      // }).catch(() => {
      //   this.$notify({
      //     title: '提示',
      //     message: '开始练习失败',
      //     type: 'warn',
      //     duration: 2000
      //   })
      // })
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" rel="stylesheet/scss" scoped>
  .practice {
    margin-top: 50px;
    padding-bottom: 50px;
    .practice-title {
      float: left;
      position: relative;
      .practice-name {
        border-bottom: 2px solid #fff;
      }
      span {
        display: block;
        line-height: 53px;
      }
    }
    .practice-right {
      margin-left: 470px;
    }
    .practice-right p {
      font-size: 14px;
      color: #848484;
      line-height: 24px;
      margin: 28px 0;
      min-height: 70px;
    }
    .practice-details label {
      font-size: 14px;
      color: #17b7f2;
      margin-right: 50px;
    }
    .practice-image {
      width: 420px;
      height: 267px;
      display: block;
      cursor: pointer;
    }
  }
  .exam-empty {
    text-align: center;
  }
</style>
