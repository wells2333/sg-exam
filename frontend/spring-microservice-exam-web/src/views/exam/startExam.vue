<template>
  <div class="subject-box">
    <el-row :gutter="30">
      <el-col :span="5">
        <div class="tool-bar">
          <div class="time-remain">
            剩余时间:
            <div class="time">
              <count-down v-on:start_callback="countDownS_cb(1)" v-on:end_callback="countDownE_cb(1)" :current-time="currentTime" :start-time="startTime" :end-time="endTime" :tip-text="'距离考试开始'" :tip-text-end="'距离考试结束'" :end-text="'考试结束'" :hourTxt="':'" :minutesTxt="':'" :secondsTxt="''">
              </count-down>
            </div>
          </div>
          <div class="current-progress">
            当前进度: {{subjectIndex}}/{{subjectIds.length}}
          </div>
          <div class="answer-card">
            <el-button type="text" icon="el-icon-date" @click="answerCard">答题卡</el-button>
          </div>
          <el-button type="success" icon="el-icon-date" @click="submitExam" v-bind:disabled="disableSubmit">提交</el-button>
        </div>
      </el-col>
      <el-col :span="18">
        <div class="subject-box-card">
          <div class="subject-exam-title">{{exam.examinationName}}（共{{subjectIds.length}}题，合计{{exam.totalScore}}分）</div>
          <!-- 题目内容 -->
          <choices ref="choices" v-show="this.query.type === 0"/>
          <short-answer ref="shortAnswer" v-show="this.query.type === 1"/>
          <judgement ref="judgement" v-show="this.query.type === 2"/>
          <multiple-choices ref="multipleChoices" v-show="this.query.type === 3"/>
          <div class="subject-buttons" v-if="query.subjectId !== ''">
            <el-button plain @click="last" :loading="loadingLast">上一题</el-button>
            <el-button plain @click="next" :loading="loadingNext">下一题</el-button>
            <el-button type="success" @click="submitExam" v-bind:disabled="disableSubmit">提交</el-button>
          </div>
        </div>
      </el-col>
    </el-row>
    <el-dialog title="答题卡" :visible.sync="dialogVisible" width="50%" top="10vh" center>
      <div class="answer-card-title" >{{exam.examinationName}}（共{{subjectIds.length}}题，合计{{exam.totalScore}}分）</div>
      <div class="answer-card-split"></div>
      <el-row class="answer-card-content">
        <el-button circle v-for="(value, index) in subjectIds" :key="index" @click="toSubject(value.subjectId, value.type, index)" >&nbsp;{{index + 1}}&nbsp;</el-button>
      </el-row>
    </el-dialog>
  </div>
</template>
<script>
import { mapState, mapGetters } from 'vuex'
import CountDown from 'vue2-countdown'
import { saveAndNext } from '@/api/exam/answer'
import { getSubjectIds } from '@/api/exam/exam'
import { getCurrentTime } from '@/api/exam/examRecord'
import { getSubjectAnswer } from '@/api/exam/subject'
import store from '@/store'
import moment from 'moment'
import { messageSuccess, messageFail, messageWarn, isNotEmpty } from '@/utils/util'
import Tinymce from '@/components/Tinymce'
import Choices from '@/components/Subjects/Choices'
import MultipleChoices from '@/components/Subjects/MultipleChoices'
import ShortAnswer from '@/components/Subjects/ShortAnswer'
import Judgement from '@/components/Subjects/Judgement'
import { nextSubjectType } from '@/const/constant'

export default {
  components: {
    CountDown,
    Tinymce,
    Choices,
    MultipleChoices,
    ShortAnswer,
    Judgement
  },
  data () {
    return {
      loadingLast: false,
      loadingNext: false,
      loadingSubmit: false,
      currentTime: 0,
      startTime: 0,
      endTime: 0,
      disableSubmit: true,
      subjectIndex: 1,
      query: {
        examinationId: undefined,
        examRecordId: undefined,
        subjectId: undefined,
        userId: undefined,
        type: 0
      },
      subject: {},
      option: '',
      answer: '',
      dialogVisible: false,
      tempAnswer: {
        id: null,
        userId: null,
        examinationId: null,
        courseId: null,
        subjectId: null,
        answer: null,
        type: 0
      },
      subjectIds: [],
      subjectStartTime: undefined
    }
  },
  computed: {
    ...mapState({
      userInfo: state => state.user.userInfo
    }),
    ...mapGetters([
      'exam'
    ])
  },
  created () {
    const examInfo = this.$route.params.id
    if (isNotEmpty(examInfo)) {
      let examInfoArr = examInfo.split('-')
      this.query.examinationId = examInfoArr[0]
      this.query.examRecordId = examInfoArr[1]
      this.query.subjectId = isNotEmpty(examInfoArr[2]) ? examInfoArr[2] : this.subject.id
      this.query.type = parseInt(examInfoArr[3])
      this.query.userId = this.userInfo.id
      this.validateExamTime()
    }
  },
  methods: {
    countDownS_cb: function (x) {
      messageSuccess(this, '考试开始')
    },
    validateExamTime () {
      getCurrentTime().then(response => {
        const currentTime = moment(response.data.data)
        if (currentTime.isAfter(this.exam.endTime)) {
          messageWarn(this, '考试已结束')
        } else if (currentTime.isBefore(this.exam.startTime)) {
          messageWarn(this, '考试未开始')
        } else {
          this.startExam()
          const current = currentTime.valueOf()
          this.currentTime = current
          this.startTime = current
          this.subjectStartTime = current
          this.endTime = moment(this.exam.endTime).valueOf()
          this.disableSubmit = false
        }
      }).catch(() => {
        messageFail(this, '开始考试失败！')
      })
    },
    startExam () {
      // 获取题目ID列表
      getSubjectIds(this.query.examinationId).then(subjectResponse => {
        const subjectData = subjectResponse.data.data
        if (subjectData.length > 0) {
          for (let i = 0; i < subjectData.length; i++) {
            const { subjectId, type } = subjectData[i]
            this.subjectIds.push({subjectId, type, index: i + 1})
          }
          this.updateSubjectIndex()
          // 获取当前题目信息
          getSubjectAnswer({
            examRecordId: this.query.examRecordId,
            subjectId: this.query.subjectId,
            type: this.query.type,
            nextType: -1
          }).then(response => {
            if (isNotEmpty(response.data.data)) {
              const { answer } = response.data.data
              this.tempAnswer = answer
              this.setSubjectInfo(response.data.data)
            }
          }).catch(() => {
            messageFail(this, '获取题目失败！')
          })
        }
      }).catch(() => {
        messageFail(this, '开始考试失败！')
      })
    },
    countDownE_cb: function (x) {
      messageWarn(this, '考试结束')
      this.disableSubmit = true
    },
    last () {
      for (let i = 0; i < this.subjectIds.length; i++) {
        if (this.subjectIds[i].subjectId === this.query.subjectId) {
          if (i === 0) {
            messageSuccess(this, '已经是第一题了')
            break
          }
          let { subjectId, type, index } = this.subjectIds[--i]
          this.subjectIndex = index
          this.saveCurrentSubjectAndGetNextSubject(nextSubjectType.last, subjectId, type)
          break
        }
      }
    },
    next () {
      for (let i = 0; i < this.subjectIds.length; i++) {
        if (this.subjectIds[i].subjectId === this.query.subjectId) {
          if (i === this.subjectIds.length - 1) {
            messageSuccess(this, '已经是最后一题了')
            break
          }
          let { subjectId, type, index } = this.subjectIds[++i]
          this.subjectIndex = index
          this.saveCurrentSubjectAndGetNextSubject(nextSubjectType.next, subjectId, type)
          break
        }
      }
    },
    // 保存当前题目，同时根据序号加载下一题
    saveCurrentSubjectAndGetNextSubject (nextType, nextSubjectId, subjectType) {
      const answerId = isNotEmpty(this.tempAnswer) ? this.tempAnswer.id : ''
      const answer = this.getAnswer(answerId)
      this.startLoading(nextType)
      saveAndNext(answer, nextType, nextSubjectId, subjectType).then(response => {
        if (response.data.data !== null) {
          const subject = response.data.data
          const { id, type, answer } = subject
          this.query.subjectId = id
          this.query.type = type
          this.tempAnswer = answer
          this.tempAnswer.index = this.subjectIndex
          this.setSubjectInfo(subject)
          store.dispatch('SetSubjectInfo', subject).then(() => {})
          this.updateSubjectIndex()
        }
        // 更新时间
        getCurrentTime().then(response => {
          this.subjectStartTime = moment(response.data.data)
        })
        this.endLoading(nextType)
      }).catch((error) => {
        console.log(error)
        messageFail(this, '获取题目失败')
        this.endLoading(nextType)
      })
    },
    // 答题卡
    answerCard () {
      this.dialogVisible = true
    },
    // 跳转题目
    toSubject (subjectId, subjectType, index) {
      this.subjectIndex = index + 1
      // 保存当前题目，同时加载下一题
      this.saveCurrentSubjectAndGetNextSubject(nextSubjectType.current, subjectId, subjectType)
      this.dialogVisible = false
    },
    // 提交
    submitExam () {
      this.$confirm('确定要提交吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.disableSubmit = true
        this.doSubmitExam(this.tempAnswer, this.query.examinationId, this.query.examRecordId, this.userInfo, true)
      }).catch(() => {
      })
    },
    doSubmitExam (answer, examinationId, examRecordId, userInfo, toExamRecord) {
      const answerId = isNotEmpty(answer) ? answer.id : ''
      saveAndNext(this.getAnswer(answerId), 0).then(response => {
        // 提交到后台
        store.dispatch('SubmitExam', { examinationId, examRecordId, userId: userInfo.id }).then(() => {
          messageSuccess(this, '提交成功')
          if (toExamRecord) {
            this.$router.push({ name: 'exam-record' })
          }
        }).catch(() => {
          this.disableSubmit = false
          messageFail(this, '提交题目失败')
        })
      }).catch(() => {
        this.disableSubmit = false
        messageFail(this, '提交题目失败')
      })
    },
    // 选中选项
    toggleOption (answer) {
      this.answer = answer
    },
    // 根据题目类型返回填写的答案
    getAnswer (answerId) {
      const ref = this.getSubjectRef()
      if (isNotEmpty(ref)) {
        const answer = ref.getAnswer()
        this.answer = answer
        return {
          id: answerId,
          userId: this.userInfo.id,
          examinationId: this.query.examinationId,
          examRecordId: this.query.examRecordId,
          subjectId: this.query.subjectId,
          answer: answer,
          type: this.query.type,
          startTime: this.subjectStartTime
        }
      }
      return {}
    },
    // 获取题目索引
    getSubjectIndex (targetId) {
      for (let subject of this.subjectIds) {
        let { subjectId, index } = subject
        if (subjectId === targetId) {
          return index
        }
      }
      return 1
    },
    // 更新题目索引
    updateSubjectIndex () {
      this.subjectIndex = this.getSubjectIndex(this.query.subjectId)
    },
    startLoading (nextType) {
      if (nextType === nextSubjectType.next) {
        this.loadingNext = true
      } else if (nextType === nextSubjectType.last) {
        this.loadingLast = true
      } else {
        this.loadingNext = true
      }
    },
    endLoading (nextType) {
      if (nextType === nextSubjectType.next) {
        this.loadingNext = false
      } else if (nextType === nextSubjectType.last) {
        this.loadingLast = false
      } else {
        this.loadingNext = false
      }
    },
    getSubjectRef () {
      let ref
      switch (this.query.type) {
        case 0:
          ref = this.$refs.choices
          break
        case 1:
          ref = this.$refs.shortAnswer
          break
        case 2:
          ref = this.$refs.judgement
          break
        case 3:
          ref = this.$refs.multipleChoices
          break
      }
      return ref
    },
    setSubjectInfo (subject) {
      const ref = this.getSubjectRef()
      if (isNotEmpty(ref)) {
        try {
          ref.setSubjectInfo(subject, this.subjectIds.length, this.subjectIndex)
        } catch (error) {
          console.error(error)
        }
      }
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" rel="stylesheet/scss" scoped>
  .subject-box {
    margin-top: 50px;
    margin-left: 20px;
  }
  .subject-box-card {
    margin-bottom: 30px;
    min-height: 400px;
  }
  .subject-buttons {
    text-align: center;
  }
  .tool-bar {
    margin-left: 20px;
  }
  .time-remain .time {
    display: inline-block;
    font-size: 18px;
    line-height: 22px;
    color: #FF0000;
    font-weight: 400;
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
