<template>
  <div class="m-subject-content">
    <transition name="fade-transform" mode="out-in">
      <div>
        <el-row class="m-exam-title" v-show="!loading">
          <el-col :span="24">
            <h2>{{ exam.examinationName }}</h2>
            <p v-if="exam.attention !== undefined">{{ exam.attention }}</p>
          </el-col>
        </el-row>
        <el-row>
          <div>
            <span class="time-remain-msg">剩余时间:</span>
            <count-down class="time-remain" v-on:start_callback="countDownS_cb(1)" v-on:end_callback="countDownE_cb(1)" :current-time="currentTime" :start-time="startTime" :end-time="endTime" :tip-text="'距离考试开始'" :tip-text-end="'距离考试结束'" :end-text="'考试结束'" :hourTxt="':'" :minutesTxt="':'" :secondsTxt="''">
            </count-down>
            <el-progress :text-inside="true" :stroke-width="16" :percentage="percentage" status="success"></el-progress>
          </div>
        </el-row>
      </div>
    </transition>
    <transition name="fade-transform" mode="out-in">
      <el-row v-show="tempSubject.show">
        <el-col :span="24">
          <!-- 题目内容 -->
          <choices ref="choices" v-if="tempSubject.type === 0"/>
          <short-answer ref="shortAnswer" v-if="tempSubject.type === 1"/>
          <judgement ref="judgement" v-if="tempSubject.type === 2"/>
          <multiple-choices ref="multipleChoices" v-if="tempSubject.type === 3"/>
        </el-col>
      </el-row>
    </transition>
    <el-row class="m-subject-button">
      <el-button plain size="medium" @click="last" :loading="loadingLast">上一题</el-button>
      <el-button plain size="medium" @click="next" :loading="loadingNext">下一题</el-button>
      <el-button type="success" size="medium" @click="handleSubmitExam" v-show="!loading">提交</el-button>
    </el-row>
  </div>
</template>

<script>
import { anonymousUserGetObj, anonymousUserGetSubjectIds } from '@/api/exam/exam'
import { anonymousUserSubmit, anonymousUserSaveAndNext } from '@/api/exam/answer'
import { getCurrentTime, anonymousUserStart } from '@/api/exam/examRecord'
import { anonymousUserGetSubjectAnswer } from '@/api/exam/subject'
import { isNotEmpty, messageWarn, messageFail, messageSuccess } from '@/utils/util'
import Choices from '@/components/Subjects/Choices'
import MultipleChoices from '@/components/Subjects/MultipleChoices'
import ShortAnswer from '@/components/Subjects/ShortAnswer'
import Judgement from '@/components/Subjects/Judgement'
import { nextSubjectType } from '@/const/constant'
import moment from 'moment'
import store from '@/store'
import CountDown from 'vue2-countdown'

export default {
  components: {
    CountDown,
    Choices,
    MultipleChoices,
    ShortAnswer,
    Judgement
  },
  data () {
    return {
      currentTime: 0,
      startTime: 0,
      endTime: 0,
      identifier: 'admin',
      loading: true,
      exam: {},
      query: {
        examinationId: undefined,
        subjectId: undefined,
        type: 0
      },
      subjectIds: [],
      tempAnswer: {},
      tempExamRecord: {},
      tempSubject: {
        show: false,
        type: undefined
      },
      subjectIndex: 1,
      loadingLast: false,
      loadingNext: false,
      subjectStartTime: undefined,
      percentage: 0
    }
  },
  created () {
    this.query.examinationId = this.$route.query.id
    this.$prompt('请输入考生账号', '提示', {
      type: 'info',
      roundButton: true,
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      inputPlaceholder: '自行注册或联系管理员创建账号',
      inputValidator: function (value) {
        if (value.length < 4) {
          return false
        }
        const reg = /^[A-Za-z]+$/
        return reg.test(value)
      },
      inputErrorMessage: '请输入合法英文字符且长度>4！',
      center: true
    }).then(({ value }) => {
      this.identifier = value
      this.startExam()
    }).catch(() => {
      console.log('取消考试')
      messageWarn(this, '取消考试')
      window.close()
    })
  },
  methods: {
    countDownS_cb: function (x) {},
    countDownE_cb: function (x) {
      messageWarn(this, '考试结束')
    },
    startExam () {
      if (this.query.examinationId === undefined) {
        return
      }
      // 查询考试信息
      anonymousUserGetObj(this.query.examinationId).then(response => {
        this.exam = response.data.data
        // 校验时间
        getCurrentTime().then(response => {
          const currentTime = moment(response.data.data)
          if (currentTime.isAfter(this.exam.endTime)) {
            messageWarn(this, '考试已结束')
          } else if (currentTime.isBefore(this.exam.startTime)) {
            // 考试未开始
            messageWarn(this, '考试未开始')
          } else {
            // 创建考试记录
            anonymousUserStart({ examinationId: this.query.examinationId, identifier: this.identifier }).then(response => {
              const { examRecord, subjectDto } = response.data.data
              this.tempExamRecord = examRecord
              subjectDto.show = true
              this.tempSubject = subjectDto
              this.query.subjectId = subjectDto.id
              this.query.type = subjectDto.type
              // 时间
              const current = currentTime.valueOf()
              this.currentTime = current
              this.startTime = current
              this.subjectStartTime = current
              this.endTime = moment(this.exam.endTime).valueOf()
              this.doStart()
            }).catch(error => {
              console.error(error)
            })
          }
        })
      }).catch((error) => {
        console.error(error)
        messageFail(this, '系统出了点问题')
        this.loading = false
      })
    },
    doStart () {
      this.loading = true
      // 获取题目ID列表
      anonymousUserGetSubjectIds(this.query.examinationId).then(subjectResponse => {
        const subjectData = subjectResponse.data.data
        if (subjectData.length > 0) {
          for (let i = 0; i < subjectData.length; i++) {
            const { subjectId, type } = subjectData[i]
            this.subjectIds.push({subjectId, type, index: i + 1})
          }
          this.updateSubjectIndex()
          // 获取当前题目信息
          anonymousUserGetSubjectAnswer({
            examRecordId: this.tempExamRecord.id,
            subjectId: this.tempSubject.id,
            type: this.tempSubject.type,
            nextType: -1
          }).then(response => {
            if (isNotEmpty(response.data.data)) {
              const { answer } = response.data.data
              this.tempAnswer = answer
              this.setSubjectInfo(response.data.data)
            }
            this.loading = false
          }).catch(error => {
            console.error(error)
            messageFail(this, '获取题目失败！')
          })
        }
      }).catch(error => {
        console.error(error)
        messageFail(this, '开始考试失败！')
      })
    },
    getSubjectRef (type) {
      let ref
      switch (type) {
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
    // 提交考试
    handleSubmitExam () {
      this.$confirm('确定要提交吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(() => {
        this.doSubmitExam(this.tempAnswer, this.query.examinationId, this.tempExamRecord.id)
      }).catch(() => {
      })
    },
    updateSubjectIndex () {
      this.subjectIndex = this.getSubjectIndex(this.query.subjectId)
      this.percentage = Math.trunc((this.subjectIndex / this.subjectIds.length) * 100)
    },
    setSubjectInfo (subject) {
      subject.show = true
      this.tempSubject = subject
      setTimeout(() => {
        const ref = this.getSubjectRef(subject.type)
        if (isNotEmpty(ref)) {
          try {
            ref.setSubjectInfo(subject, this.subjectIds.length, this.subjectIndex)
          } catch (error) {
            console.error(error)
          }
        }
      }, 100)
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
    // 保存当前题目，同时根据序号加载下一题
    saveCurrentSubjectAndGetNextSubject (nextType, nextSubjectId, subjectType) {
      const answerId = isNotEmpty(this.tempAnswer) ? this.tempAnswer.id : ''
      const answer = this.getAnswer(answerId, this.query.type)
      this.startLoading(nextType)
      anonymousUserSaveAndNext(answer, nextType, nextSubjectId, subjectType).then(response => {
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
    getSubjectIndex (targetId) {
      for (let subject of this.subjectIds) {
        let { subjectId, index } = subject
        if (subjectId === targetId) {
          return index
        }
      }
      return 1
    },
    getAnswer (answerId, type) {
      const ref = this.getSubjectRef(type)
      if (isNotEmpty(ref)) {
        const answer = ref.getAnswer()
        this.answer = answer
        return {
          id: answerId,
          examinationId: this.exam.id,
          examRecordId: this.tempExamRecord.id,
          subjectId: this.query.subjectId,
          answer: answer,
          type: this.query.type,
          startTime: this.subjectStartTime
        }
      }
      return {}
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
    doSubmitExam (answer, examinationId, examRecordId) {
      const answerId = isNotEmpty(answer) ? answer.id : ''
      anonymousUserSaveAndNext(this.getAnswer(answerId, this.query.type), this.query.type, undefined, this.query.type).then(response => {
        // 提交到后台
        anonymousUserSubmit({ examinationId, examRecordId }).then(() => {
          messageSuccess(this, '提交成功，5s后自动退出')
          setTimeout(() => {
            window.close()
          }, 5000)
        }).catch(() => {
          messageFail(this, '提交失败，请稍后重试！')
        })
      }).catch(() => {
        messageFail(this, '提交失败，请稍后重试！')
      })
    }
  }
}
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
  .m-subject-content {
    margin: 14px;
    .m-exam-title {
      text-align: left;
    }
   .m-subject-button {
     margin: 16px;
     text-align: center;
   }
  }
  .time-remain {
    display: inline-block;
  }
  .time-remain-msg {
    color: #5a5a5a;
  }
</style>
