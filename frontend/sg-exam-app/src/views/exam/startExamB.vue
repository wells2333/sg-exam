<template>
  <div class="subject-box">
    <el-row :gutter="30">
      <el-col :span="16" :offset="3">
        <div class="subject-box-card">
          <div class="subject-exam-title">{{ exam.examinationName }}
          </div>
          <div style="padding-top: 6px;">
            {{ $t('exam.startExam.subject') }}: {{ cards.length }}
          </div>
          <div style="padding-top: 6px;">
            {{ $t('exam.startExam.score') }}: {{ exam.totalScore }}
          </div>
          <div v-show="!loading">
            <div v-for="(item, index) in subjects" :key="index">
              <choices :ref="'choices_' + index" v-show="item.type === 0" :onChoice="onChoiceFn"/>
              <short-answer :ref="'shortAnswer_' + index" v-show="item.type === 1"
                            :onChoice="onChoiceFn"/>
              <judgement :ref="'judgement_' + index" v-show="item.type === 2"
                         :onChoice="onChoiceFn"/>
              <multiple-choices :ref="'multipleChoices_' + index" v-show="item.type === 3"
                                :onChoice="onChoiceFn"/>
            </div>
          </div>
          <div class="subject-buttons">
            <el-button @click="handleSubmit">{{ $t('submit') }}
            </el-button>
          </div>
        </div>
      </el-col>
      <el-col :span="3">
        <div class="tool-bar">
          <div class="current-progress">
            {{ $t('exam.startExam.progress') }}：{{ answeredSubjectCnt }}/{{ cards.length }}
          </div>
          <div class="answer-card">
            <el-button type="text" icon="el-icon-date" @click="showAnswerCard">
              {{ $t('exam.startExam.answerCard') }}
            </el-button>
          </div>
          <el-button type="success" icon="el-icon-date" @click="handleSubmit"
                     :loading="loadingSubmit">{{ $t('submit') }}
          </el-button>
        </div>
      </el-col>
    </el-row>
    <el-dialog :title="$t('exam.startExam.answerCard')" :visible.sync="dialogVisible" width="50%"
               top="10vh" center>
      <el-row class="answer-card-content">
        <el-button
          :class="value.isAnswer !== undefined && value.isAnswer === true ? 'answer-card-btn' : ''"
          v-for="(value, index) in cards"
          :key="index" style="padding: 12px;">
          &nbsp;{{ value.sort }}&nbsp;
        </el-button>
      </el-row>
    </el-dialog>
  </div>
</template>
<script>
import {mapGetters, mapState} from 'vuex'
import CountDown from 'vue2-countdown'
import {submitAll} from '@/api/exam/answer'
import {getAllSubjects} from '@/api/exam/examRecord'
import store from '@/store'
import {isNotEmpty, messageFail, messageSuccess} from '@/utils/util'
import Tinymce from '@/components/Tinymce'
import Choices from '@/components/Subjects/Choices'
import MultipleChoices from '@/components/Subjects/MultipleChoices'
import ShortAnswer from '@/components/Subjects/ShortAnswer'
import Judgement from '@/components/Subjects/Judgement'

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
      loading: true,
      loadingSubmit: false,
      startTime: 0,
      endTime: 0,
      dialogVisible: false,
      subjects: [],
      answeredSubjectCnt: 0
    }
  },
  computed: {
    ...mapState({
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

    const examinationId = this.$route.params.id
    if (isNotEmpty(examinationId)) {
      this.startExam(examinationId)
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
      // 标识已答题状态
      if (sort) {
        this.cards[sort - 1].isAnswer = true
      }
      // 更新答题进度
      let cnt = 0
      for (let i = 0; i < this.cards.length; i++) {
        const c = this.cards[i]
        if (c.isAnswer !== undefined && c.isAnswer === true) {
          cnt++
        }
      }
      this.answeredSubjectCnt = cnt
    },
    goBack() {
      this.$router.push({name: 'exams'})
    },
    startExam(examinationId) {
      this.loading = true
      messageSuccess(this, this.$t('exam.startExam.startExam'))
      getAllSubjects(examinationId).then(res => {
        const {data} = res
        if (data && data.code === 0 && data.result) {
          this.subjects = data.result
        }
      }).catch(e => {
        console.error(e)
      }).finally(() => {
        this.loading = false
        if (this.subjects.length > 0) {
          setTimeout(() => {
            for (let i = 0; i < this.subjects.length; i++) {
              this.setSubjectInfo(i, this.subjects[i])
            }
          }, 100)
        }
      })
    },
    showAnswerCard() {
      this.dialogVisible = true
    },
    getSubjectRef(index, item) {
      let ref
      switch (item.type) {
        case 0:
          ref = this.$refs['choices_' + index]
          break
        case 1:
          ref = this.$refs['shortAnswer_' + index]
          break
        case 2:
          ref = this.$refs['judgement_' + index]
          break
        case 3:
          ref = this.$refs['multipleChoices_' + index]
          break
        case 5:
          ref = this.$refs['sVideo_' + index]
          break
      }
      return ref[0]
    },
    setSubjectInfo(index, item) {
      const ref = this.getSubjectRef(index, item)
      if (ref) {
        try {
          ref.setSubjectInfo(item, this.cards.length, null)
        } catch (error) {
          console.error(error)
        }
      }
    },
    handleSubmit() {
      this.$confirm(this.$t('confirmSubmit'), this.$t('tips'), {
        confirmButtonText: this.$t('sure'),
        cancelButtonText: this.$t('cancel'),
        type: 'warning'
      }).then(() => {
        this.loadingSubmit = true
        this.beforeSave()
        this.doSubmitExam(this.examRecord.id)
      }).catch(() => {
      })
    },
    doSubmitExam(examRecordId) {
      const data = []
      for (let i = 0; i < this.subjects.length; i++) {
        const item = this.subjects[i]
        const ref = this.getSubjectRef(i, item)
        if (ref) {
          const answer = ref.getAnswer()
          data.push({examRecordId, subjectId: item.id, answer})
        }
      }
      if (data.length === 0) {
        this.loadingSubmit = false
        return
      }

      submitAll(data).then(() => {
        this.loadingSubmit = false
        messageSuccess(this, this.$t('submit') + this.$t('success'))
        setTimeout(() => {
          store.dispatch('ClearExam')
          this.$router.push({name: 'exam-record'})
        }, 800)
      }).catch(() => {
        messageFail(this, this.$t('submit') + this.$t('failed'))
        this.loadingSubmit = false
      })
    },
    beforeSave() {
      for (let i = 0; i < this.subjects.length; i++) {
        const ref = this.getSubjectRef(i, this.subjects[i])
        if (ref) {
          ref.beforeSave()
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
