<template>
  <div class="subject-box">
    <div class="subject-box-content">
      <div class="tool-bar">
        <div class="tool-bar-timer">
          <span>{{ duration }}</span>
        </div>
        <div class="tool-bar-card">
          <div class="current-progress">
            {{ $t('exam.startExam.progress') }}：{{ cards.length }} / {{ subject.sort }}
          </div>
          <el-button class="answer-card" type="text" icon="el-icon-s-grid" size="medium"
                     style="font-size: 30px; color: #606266;" @click="answerCard"></el-button>
        </div>
      </div>
      <div class="subject-box-card">
        <choices ref="choices" v-show="subject.type === 0" :onChoice="onChoiceFn"/>
        <short-answer ref="shortAnswer" v-show="subject.type === 1" :onChoice="onChoiceFn"/>
        <judgement ref="judgement" v-show="subject.type === 2" :onChoice="onChoiceFn"/>
        <multiple-choices ref="multipleChoices" v-show="subject.type === 3"
                          :onChoice="onChoiceFn"/>
        <fill-blank ref="fillBlank" v-show="subject.type === 4"
                    :onChoice="onChoiceFn"/>
        <div class="subject-buttons">
          <div>
            <el-button plain @click="last" :loading="loadingLast">{{ $t('exam.startExam.last') }}
            </el-button>
          </div>
          <div>
            <el-button plain @click="next" :loading="loadingNext">{{ $t('exam.startExam.next') }}
            </el-button>
            <el-button type="success" @click="submitExam">{{ $t('submit') }}
            </el-button>
          </div>
        </div>
      </div>
    </div>

    <el-dialog :title="$t('exam.startExam.answerCard')" :visible.sync="dialogVisible" width="50%"
               top="10vh" center>
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
import {saveAndNext} from '@/api/exam/answer'
import store from '@/store'
import moment from 'moment'
import {isNotEmpty, messageFail, messageSuccess} from '@/utils/util'
import Tinymce from '@/components/Tinymce'
import Choices from '@/components/Subjects/Choices'
import MultipleChoices from '@/components/Subjects/MultipleChoices'
import ShortAnswer from '@/components/Subjects/ShortAnswer'
import Judgement from '@/components/Subjects/Judgement'
import FillBlank from '@/components/Subjects/FillBlank'
import {nextSubjectType} from '@/const/constant'

export default {
  components: {
    Tinymce,
    Choices,
    MultipleChoices,
    ShortAnswer,
    Judgement,
    FillBlank
  },
  data() {
    return {
      examinationId: undefined,
      recordId: undefined,
      timer: undefined,
      loadingLast: false,
      loadingNext: false,
      loadingSubmit: false,
      saving: false,
      startTime: 0,
      endTime: 0,
      duration: '',
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
      'cards',
      'subject'
    ])
  },
  created() {
    this.examinationId = this.$route.params.id
    this.recordId = this.$route.query.recordId
    if (this.examinationId && this.recordId) {
      this.updateDuration()
      this.timer = setInterval(this.updateDuration, 1000)
      this.startExam()
    } else {
      this.$router.push({name: 'exams', query: {redirect: true}})
    }
  },
  mounted() {
    if (window.history && window.history.pushState) {
      window.history.pushState(null, null, document.URL)
      window.addEventListener('popstate', this.goBack, false)
    }
  },
  destroyed() {
    clearInterval(this.timer)
    window.removeEventListener('popstate', this.goBack, false)
  },
  methods: {
    updateDuration() {
      const durationMs = new Date().getTime() - new Date(this.examRecord.createTime).getTime()
      const level1 = durationMs % (24 * 3600 * 1000)
      const hours = Math.floor(level1 / (3600 * 1000))

      const level2 = level1 % (3600 * 1000)
      const minutes = Math.floor(level2 / (60 * 1000))

      const level3 = level2 % (60 * 1000)
      const seconds = Math.round(level3 / 1000)

      let result = hours > 0 ? (hours < 10 ? '0' + hours : hours) : '00' + ':'
      result = result + (minutes > 0 ? (minutes < 10 ? '0' + minutes : minutes) : '00') + ':'
      result = result + (seconds > 0 ? (seconds < 10 ? '0' + seconds : seconds) : '00')
      this.duration = result;
    },
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
        this.doSubmitExam(this.tempAnswer, this.examinationId, this.recordId, this.userInfo, true)
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
          examinationId: this.examinationId,
          examRecordId: this.recordId,
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
        case 4:
          ref = this.$refs.fillBlank
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

.subject-box-content {
  margin: 16px auto;
  max-width: 1000px;
  padding-top: 8px;
  padding-left: 16px;
  padding-right: 16px;
  border: 1px solid #ccc;
  border-radius: 8px;
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
  display: flex;
  justify-content: space-between;
}

.tool-bar {
  display: flex;
  justify-content: space-between;
}

.tool-bar-timer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.tool-bar-timer span {
  color: #606266;
  font-size: 1.5em;
  font-weight: bold;
}

.current-progress {
  color: #303133;
}

.tool-bar-card {
  font-size: 1.5em;
  font-weight: bold;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.answer-card {
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
  background-color: #67C23A;
  border-color: #67C23A;
}
</style>
