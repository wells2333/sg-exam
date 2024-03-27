<template>
  <div class="subject-box">
    <div class="subject-box-content">
      <div class="tool-bar">
        <div class="tool-bar-timer">
          <span>{{ duration }}</span>
        </div>
        <div class="tool-bar-card">
          <div class="current-progress">
            {{ $t('exam.startExam.progress') }}：{{ cards.length }} / {{ answeredSubjectCnt }}
          </div>
          <el-button class="answer-card" type="text" icon="el-icon-s-grid" size="medium"
                     style="font-size: 30px; color: #606266;" @click="showAnswerCard"></el-button>
        </div>
      </div>
      <div class="subject-box-card">
        <div v-show="!loading">
          <div v-for="(item, index) in subjects" :key="index">
            <choices :ref="'choices_' + index" v-show="item.type === 0" :onChoice="onChoiceFn"/>
            <short-answer :ref="'shortAnswer_' + index" v-show="item.type === 1"
                          :onChoice="onChoiceFn"/>
            <judgement :ref="'judgement_' + index" v-show="item.type === 2"
                       :onChoice="onChoiceFn"/>
            <multiple-choices :ref="'multipleChoices_' + index" v-show="item.type === 3"
                              :onChoice="onChoiceFn"/>
            <fill-blank :ref="'fillBlank_' + index" v-show="item.type === 4"
                        :onChoice="onChoiceFn"/>
            <material :ref="'material_' + index" v-show="item.type === 5"
                        :onChoice="onChoiceFn"/>            
          </div>
        </div>
        <div class="subject-buttons">
          <el-button type="success" @click="handleSubmit" :loading="loadingSubmit">{{ $t('submit') }}
          </el-button>
        </div>
      </div>
    </div>
    <el-dialog :title="$t('exam.startExam.answerCard')" :visible.sync="dialogVisible" width="50%"
               top="10vh" center>
      <el-row class="answer-card-content">
        <el-button
          :class="value.answered !== undefined && value.answered === true ? 'answer-card-btn' : ''"
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
import {messageFail, messageSuccess, calculateDuration} from '@/utils/util'
import {getSubjectRef, setSubjectInfo, beforeSaveSubject} from '@/utils/busi'
import Tinymce from '@/components/Tinymce'
import Choices from '@/components/Subjects/Choices'
import MultipleChoices from '@/components/Subjects/MultipleChoices'
import ShortAnswer from '@/components/Subjects/ShortAnswer'
import Judgement from '@/components/Subjects/Judgement'
import FillBlank from '@/components/Subjects/FillBlank'
import Material from '@/components/Subjects/Material'

export default {
  components: {
    CountDown,
    Tinymce,
    Choices,
    MultipleChoices,
    ShortAnswer,
    Judgement,
    FillBlank,
    Material
  },
  data() {
    return {
      examinationId: undefined,
      recordId: undefined,
      timer: undefined,
      loading: true,
      loadingSubmit: false,
      startTime: 0,
      endTime: 0,
      duration: '',
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
      this.duration = calculateDuration(new Date(this.examRecord.createTime));
    },
    onChoiceFn(sort) {
      // 标识已答题状态
      if (sort) {
        this.cards[sort - 1].answered = true
      }
      // 更新答题进度
      let cnt = 0
      for (let i = 0; i < this.cards.length; i++) {
        const c = this.cards[i]
        if (c.answered !== undefined && c.answered === true) {
          cnt++
        }
      }
      this.answeredSubjectCnt = cnt
    },
    goBack() {
      this.$router.push({name: 'exams'})
    },
    startExam() {
      this.loading = true
      messageSuccess(this, this.$t('exam.startExam.startExam'))
      getAllSubjects(this.examinationId).then(res => {
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
              setSubjectInfo(this.$refs, i, this.subjects[i], this.subjects)
            }
          }, 100)
        }
      })
    },
    showAnswerCard() {
      this.dialogVisible = true
    },
    handleSubmit() {
      this.$confirm(this.$t('confirmSubmit'), this.$t('tips'), {
        confirmButtonText: this.$t('sure'),
        cancelButtonText: this.$t('cancel'),
        type: 'warning'
      }).then(() => {
        this.loadingSubmit = true
        beforeSaveSubject(this.$refs, this.subjects)
        this.doSubmitExam(this.examRecord.id)
      }).catch(() => {
      })
    },
    doSubmitExam(examRecordId) {
      const data = []
      for (let i = 0; i < this.subjects.length; i++) {
        const item = this.subjects[i]
        const ref = getSubjectRef(this.$refs, i, item)
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

.subject-buttons {
  margin-top: 30px;
  display: flex;
  justify-content: center;
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

.subject-box-card {
  margin-bottom: 30px;
  min-height: 400px;
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
