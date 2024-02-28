<template>
  <div class="subject-box">
    <el-row :gutter="30">
      <el-col :span="16" :offset="3">
        <div class="subject-box-card">
          <div class="subject-exam-title">{{ exam.examinationName }}
          </div>
          <div style="padding-top: 6px;">
            {{$t('exam.startExam.subject')}}: {{ cards.length }}
          </div>
          <div style="padding-top: 6px;">
            {{$t('exam.startExam.score')}}: {{ exam.totalScore }}
          </div>
          <choices ref="choices" v-show="subject.type === 0" :onChoice="onChoiceFn"/>
          <short-answer ref="shortAnswer" v-show="subject.type === 1" :onChoice="onChoiceFn"/>
          <judgement ref="judgement" v-show="subject.type === 2" :onChoice="onChoiceFn"/>
          <multiple-choices ref="multipleChoices" v-show="subject.type === 3"
                            :onChoice="onChoiceFn"/>
          <div class="subject-buttons">
            <el-button plain @click="last" :loading="loadingLast">{{$t('exam.startExam.last')}}</el-button>
            <el-button plain @click="next" :loading="loadingNext">{{$t('exam.startExam.next')}}</el-button>
          </div>
        </div>
      </el-col>
      <el-col :span="3">
        <div class="tool-bar">
          <div class="current-progress">
            {{$t('exam.startExam.progress')}}：{{ subject.sort }}/{{ cards.length }}
          </div>
          <div class="answer-card">
            <el-button type="text" icon="el-icon-date" @click="answerCard">{{$t('exam.startExam.answerCard')}}</el-button>
          </div>
          <el-button type="success" icon="el-icon-date" @click="submitExam">{{$t('submit')}}</el-button>
        </div>
      </el-col>
    </el-row>
    <el-dialog :title="$t('exam.startExam.answerCard')" :visible.sync="dialogVisible" width="50%" top="10vh" center>
      <el-row class="answer-card-content">
        <el-button :class="value.isAnswer ? 'answer-card-btn' : ''"
                   v-for="(value, index) in cards" :key="index"
                   @click="toSubject(value.subjectId, value.sort)" style="padding: 12px;">
          &nbsp;{{ value.sort }}&nbsp;
        </el-button>
      </el-row>
    </el-dialog>
  </div>
</template>
<script>
import {mapGetters, mapState} from 'vuex'
import CountDown from 'vue2-countdown'
import {saveAndNext} from '@/api/exam/answer'
import store from '@/store'
import moment from 'moment'
import {isNotEmpty, messageFail, messageSuccess} from '@/utils/util'
import Tinymce from '@/components/Tinymce'
import Choices from '@/components/Subjects/Choices'
import MultipleChoices from '@/components/Subjects/MultipleChoices'
import ShortAnswer from '@/components/Subjects/ShortAnswer'
import Judgement from '@/components/Subjects/Judgement'
import {nextSubjectType} from '@/const/constant'

export default {
  components: {
    CountDown,
    Tinymce,
    Choices,
    MultipleChoices,
    ShortAnswer,
    Judgement
  },
  data() {
    return {
      loadingLast: false,
      loadingNext: false,
      loadingSubmit: false,
      saving: false,
      startTime: 0,
      endTime: 0,
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
      subjectStartTime: undefined
    }
  },
  computed: {
    ...mapState({
      userInfo: state => state.user.userInfo,
      examRecord: state => state.exam.examRecord
    }),
    ...mapGetters([
      'exam',
      'cards',
      'subject'
    ])
  },
  created() {
    if (this.exam === undefined || Object.keys(this.exam).length === 0) {
      this.$router.push({name: 'exams', query: {redirect: true}})
      return
    }
    const examInfo = this.$route.params.id
    if (isNotEmpty(examInfo)) {
      this.startExam()
    }
  },
  mounted() {
    if (window.history && window.history.pushState) {
      window.history.pushState(null, null, document.URL)
      window.addEventListener('popstate', this.goBack, false)
    }
  },
  destroyed() {
    window.removeEventListener('popstate', this.goBack, false)
  },
  methods: {
    onChoiceFn(sort) {
      if (sort) {
        this.cards[sort - 1].isAnswer = true
      }
    },
    goBack() {
      this.$router.push({name: 'exams'})
    },
    startExam() {
      messageSuccess(this, this.$t('exam.startExam.startExam'))
      setTimeout(() => {
        this.setSubjectInfo(this.subject)
      }, 100)
    },
    last() {
      for (let i = 0; i < this.cards.length; i++) {
        if (this.cards[i].subjectId === this.subject.id) {
          if (i === 0) {
            messageSuccess(this, this.$t('exam.startExam.isFirst'))
            break
          }
          let {sort} = this.cards[i - 1]
          this.saveCurrentSubjectAndGetNextSubject(nextSubjectType.last, sort)
          break
        }
      }
    },
    next() {
      for (let i = 0; i < this.cards.length; i++) {
        if (this.cards[i].subjectId === this.subject.id) {
          if (i === this.cards.length - 1) {
            messageSuccess(this, this.$t('exam.startExam.isLast'))
            break
          }
          let {sort} = this.cards[i + 1]
          this.saveCurrentSubjectAndGetNextSubject(nextSubjectType.next, sort)
          break
        }
      }
    },
    // 保存当前题目，同时根据序号加载下一题
    saveCurrentSubjectAndGetNextSubject(nextType, nextSubjectSort, subjectId = undefined) {
      if (this.saving) {
        return
      }

      try {
        this.saving = true
        const answerId = isNotEmpty(this.tempAnswer) ? this.tempAnswer.id : ''
        const answer = this.getAnswer(answerId)
        const ref = this.getSubjectRef()
        if (ref) {
          ref.beforeSave()
        }
        this.startLoading(nextType)
        saveAndNext(answer, nextType, nextSubjectSort, subjectId).then(response => {
          if (response.data.result !== null) {
            const subject = response.data.result
            store.dispatch('SetSubjectInfo', subject).then(() => {
              this.setSubjectInfo(subject)
            })
          }
          this.subjectStartTime = moment().format('YYYY-MM-DD HH:mm:ss')
          this.endLoading(nextType)
        }).catch((error) => {
          console.log(error)
          messageFail(this, this.$t('exam.startExam.getSubjectFailed'))
          this.endLoading(nextType)
        })
      } finally {
        this.saving = false
      }
    },
    answerCard() {
      this.dialogVisible = true
    },
    toSubject(subjectId, subjectSortNo) {
      this.saveCurrentSubjectAndGetNextSubject(nextSubjectType.current, subjectSortNo, subjectId)
      this.dialogVisible = false
    },
    submitExam() {
      this.$confirm(this.$t('confirmSubmit'), this.$t('tips'), {
        confirmButtonText: this.$t('sure'),
        cancelButtonText: this.$t('cancel'),
        type: 'warning'
      }).then(() => {
        this.doSubmitExam(this.tempAnswer, this.exam.id, this.examRecord.id, this.userInfo, true)
      }).catch(() => {
      })
    },
    doSubmitExam(answer, examinationId, examRecordId, userInfo, toExamRecord) {
      const ref = this.getSubjectRef()
      if (ref) {
        ref.beforeSave()
      }
      const answerId = isNotEmpty(answer) ? answer.id : ''
      saveAndNext(this.getAnswer(answerId), 0).then(() => {
        store.dispatch('SubmitExam', {
          examinationId,
          examRecordId,
          userId: userInfo.id
        }).then(() => {
          messageSuccess(this, this.$t('submit') + this.$t('success'))
          store.dispatch('ClearExam')
          if (toExamRecord) {
            this.$router.push({name: 'exam-record'})
          }
        }).catch(() => {
          messageFail(this, this.$t('submit') + this.$t('failed'))
        })
      }).catch(() => {
        messageFail(this, this.$t('submit') + this.$t('failed'))
      })
    },
    toggleOption(answer) {
      this.answer = answer
    },
    getAnswer(answerId) {
      const ref = this.getSubjectRef()
      if (isNotEmpty(ref)) {
        const answer = ref.getAnswer()
        this.answer = answer
        return {
          id: answerId,
          userId: this.userInfo.id,
          examinationId: this.exam.id,
          examRecordId: this.examRecord.id,
          subjectId: this.subject.id,
          answer: answer,
          startTime: this.subjectStartTime
        }
      }
      return {}
    },
    startLoading(nextType) {
      if (nextType === nextSubjectType.next) {
        this.loadingNext = true
      } else if (nextType === nextSubjectType.last) {
        this.loadingLast = true
      } else {
        this.loadingNext = true
      }
    },
    endLoading(nextType) {
      if (nextType === nextSubjectType.next) {
        this.loadingNext = false
      } else if (nextType === nextSubjectType.last) {
        this.loadingLast = false
      } else {
        this.loadingNext = false
      }
    },
    getSubjectRef() {
      let ref
      switch (this.subject.type) {
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
        case 5:
          ref = this.$refs.sVideo
          break
      }
      return ref
    },
    setSubjectInfo(sub) {
      const ref = this.getSubjectRef()
      if (isNotEmpty(ref)) {
        try {
          ref.setSubjectInfo(sub, this.cards.length, null)
        } catch (error) {
          console.error(error)
        }
      }
    }
  },
  beforeRouteLeave(to, from, next) {
    const {name, query} = to
    if (name === 'exam-record' || query.redirect) {
      next()
      return
    }
    this.$confirm(this.$t('exam.startExam.confirmExit'), this.$t('tips'), {
      confirmButtonText: this.$t('sure'),
      cancelButtonText: this.$t('cancel'),
      type: 'warning'
    }).then(() => {
      next()
    }).catch(() => {
      next(false)
    })
  }
}
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
.subject-box {
  margin-top: 30px;
  margin-left: 20px;
}

.subject-box-card {
  margin-bottom: 30px;
  min-height: 400px;
}

.subject-exam-title {
  font-size: 22px;
}

.subject-buttons {
  margin-top: 30px;
  text-align: center;
}

.tool-bar {
  margin-left: 20px;
}

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
    margin-top: 10px;
  }
}

.answer-card-btn {
  color: #fff;
  background-color: #409eff;
  border-color: #409eff;
}
</style>
