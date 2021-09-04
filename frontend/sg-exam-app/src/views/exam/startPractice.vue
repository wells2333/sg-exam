<template>
  <div>
    <el-row :gutter="30">
      <el-col :span="18" :offset="2">
        <el-card class="subject-box-card" v-loading="loading">
          <div class="subject-exam-title" v-if="!loading && tempSubject.id !== ''">{{practice.examinationName}}（共{{practice.totalSubject}}题，合计{{practice.totalScore}}分）</div>
          <div class="subject-content" v-if="!loading && tempSubject.id !== ''">
            <div class="subject-title">
              <span class="subject-title-number">{{tempSubject.serialNumber}} .</span>
              {{tempSubject.subjectName}}（{{tempSubject.score}}分）
            </div>
            <div class="subject-option">
              <el-radio v-model="option" label="A">A. {{tempSubject.optionA}}</el-radio>
            </div>
            <div class="subject-option">
              <el-radio v-model="option" label="B">B. {{tempSubject.optionB}}</el-radio>
            </div>
            <div class="subject-option">
              <el-radio v-model="option" label="C">C. {{tempSubject.optionC}}</el-radio>
            </div>
            <div class="subject-option">
              <el-radio v-model="option" label="D">D. {{tempSubject.optionD}}</el-radio>
            </div>
          </div>
          <div class="subject-buttons" v-if="!loading && tempSubject.id !== ''">
            <el-button plain @click="last">上一题</el-button>
            <el-button v-if="tempSubject.serialNumber !== practice.totalSubject" plain @click="next">下一题</el-button>
            <el-button v-if="tempSubject.serialNumber !== 0 && tempSubject.serialNumber === practice.totalSubject" type="success" @click="submitPractice" v-bind:disabled="disableSubmit">提交</el-button>
          </div>
        </el-card>
      </el-col>
      <el-col :span="2">
        <div class="current-progress">
          当前进度: {{tempSubject.serialNumber}}/{{practice.totalSubject}}
        </div>
        <div class="answer-card">
          <el-button type="text" icon="el-icon-date" @click="answerCard">答题卡</el-button>
        </div>
        <el-button type="success" icon="el-icon-date" @click="submitPractice" v-bind:disabled="disableSubmit">提交</el-button>
      </el-col>
    </el-row>
    <el-dialog title="答题卡" :visible.sync="dialogVisible" width="50%" top="10vh" center>
      <div class="answer-card-title" >{{practice.examinationName}}（共{{practice.totalSubject}}题，合计{{practice.totalScore}}分）</div>
      <div class="answer-card-split"></div>
      <el-row class="answer-card-content">
        <el-button circle v-for="index in practice.totalSubject" :key="index" @click="toSubject(index)">&nbsp;{{index}}&nbsp;</el-button>
      </el-row>
    </el-dialog>
  </div>
</template>
<script>
import { mapState } from 'vuex'
import { saveAndNext } from '@/api/exam/answer'
import store from '@/store'
import { notifySuccess, notifyFail, isNotEmpty } from '@/utils/util'

export default {
  data () {
    return {
      loading: false,
      currentTime: 0,
      startTime: 0,
      endTime: 0,
      disableSubmit: true,
      tempSubject: {
        id: '',
        examinationId: '',
        subjectName: '',
        type: 0,
        content: '',
        optionA: '',
        optionB: '',
        optionC: '',
        optionD: '',
        optionE: '',
        optionF: '',
        answer: '',
        score: '',
        analysis: '',
        level: 2
      },
      query: {
        serialNumber: 1,
        examRecordId: '',
        userId: ''
      },
      option: '',
      dialogVisible: false,
      tempAnswer: {
        id: '',
        userId: '',
        examinationId: '',
        courseId: '',
        subjectId: '',
        answer: ''
      }
    }
  },
  computed: {
    // 获取用户信息
    ...mapState({
      userInfo: state => state.user.userInfo,
      practice: state => state.practice.practice,
      practiceRecord: state => state.practice.practiceRecord,
      practiceSubject: state => state.practice.practiceSubject
    })
  },
  created () {
    // 考试ID
    this.query.examinationId = this.practice.id
    // 考试记录ID
    this.query.examRecordId = this.practiceRecord.id
    // 用户ID
    this.query.userId = this.userInfo.id
    // 开始练习
    this.startPractice()
  },
  methods: {
    // 开始考试
    startPractice () {
      // 获取服务器的当前时间
      this.currentTime = parseInt(this.practice.currentTime)
      // 考试开始时间
      this.startTime = parseInt(this.practice.startTime)
      // 考试已经考试
      this.startTime = this.currentTime
      // 题目数
      this.practice.totalSubject = parseInt(this.practice.totalSubject)
      // 开启提交按钮
      this.disableSubmit = false
      // 考试结束时间
      this.endTime = parseInt(this.practice.endTime)
      // 初始化题目和答题
      this.tempSubject = this.practiceSubject
      // 答题
      this.tempAnswer = this.tempSubject.answer
      // 选项
      this.option = isNotEmpty(this.tempAnswer) ? this.tempAnswer.answer : ''
      // 题号
      this.query.serialNumber = this.practiceSubject.serialNumber
    },
    // 上一题
    last () {
      if (this.query.serialNumber === 1) {
        this.$notify({
          title: '提示',
          message: '已经是第一题了',
          type: 'warn',
          duration: 2000
        })
      } else {
        // 题目序号减一
        this.query.serialNumber--
        // 保存当前题目，同时加载下一题
        this.saveCurrentSubjectAndGetNextSubject()
      }
    },
    // 下一题
    next () {
      // 增加序号
      this.query.serialNumber++
      // 保存当前题目，同时加载下一题
      this.saveCurrentSubjectAndGetNextSubject()
    },
    // 保存当前题目，同时根据序号加载下一题
    saveCurrentSubjectAndGetNextSubject () {
      this.loading = true
      let answerId = this.tempAnswer.id || ''
      let answer = {
        id: answerId,
        userId: this.userInfo.id,
        examinationId: this.practice.id,
        examRecordId: this.practiceRecord.id,
        subjectId: this.tempSubject.id,
        answer: this.option,
        serialNumber: this.query.serialNumber
      }
      // 提交到后台
      saveAndNext(answer).then(response => {
        if (response.data.data === null) {
          notifySuccess(this, '已经是最后一题了')
          this.query.serialNumber--
        } else {
          // 题目内容
          this.tempSubject = response.data.data
          // 答题
          this.tempAnswer = response.data.data.answer
          // 选项
          this.option = this.tempAnswer.answer || ''
          // 保存题目答案到sessionStorage
          this.practiceSubject.answer = this.tempAnswer
          store.dispatch('SetPracticeSubjectInfo', this.tempSubject).then(() => {})
        }
        this.loading = false
      }).catch(() => {
        notifyFail(this, '加载题目失败')
        this.loading = false
      })
    },
    // 答题卡
    answerCard () {
      this.dialogVisible = true
    },
    // 跳转题目
    toSubject (index) {
      this.query.serialNumber = index
      // 保存当前题目，同时加载下一题
      this.saveCurrentSubjectAndGetNextSubject()
      this.dialogVisible = false
    },
    // 提交
    submitPractice () {
      this.$confirm('确定要提交吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 提交到后台
        store.dispatch('SubmitPractice', { examinationId: this.practice.id, examRecordId: this.practiceRecord.id, userId: this.userInfo.id }).then(() => {
          notifySuccess(this, '提交成功')
          // 禁用提交按钮
          this.disableSubmit = true
          this.$router.push({name: 'score', query: {type: 'practice'}})
        }).catch(() => {
          notifyFail(this, '提交失败')
        })
      })
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" rel="stylesheet/scss" scoped>
  .subject-box-card {
    margin-bottom: 30px;
    min-height: 400px;
    .subject-exam-title{
      font-size: 18px;
      line-height: 25px;
      padding: 18px 20px;
      border-bottom: 1px solid #DEDEDE;
    }
    .subject {
      padding-left: 30px;
      padding-right: 75px;
    }
    .subject-content{
      padding: 30px 0;
      position: relative;
    }
    /* 题目 */
    .subject-title {
      font-size: 16px;
      line-height: 22px;
      margin-bottom: 10px;
      padding-left: 20px;
      position: relative;
      .subject-title-number {
        position: absolute;
        left: -25px;
        top: 0;
        display: inline-block;
        width: 40px;
        line-height: 22px;
        text-align: right;
      }
    }
    /* 题目选项 */
    .subject-option {
      padding: 10px 15px 10px 45px;
    }
  }
  .subject-buttons {
    text-align: center;
  }

  /* 答题卡 */
  .answer-card-title {
    font-size: 13px;
    color: #3A3E51;
    line-height: 17px;
    padding: 10px 0;
  }
  .answer-card-split {
    width: 100%;
    border-bottom: 1px solid #E6E6E6;
  }
  .answer-card-content {
    padding-bottom: 10px;
    font-size: 0;
    margin-right: -15px;
    > button {
      margin-top: 5px;
    }
  }
</style>
