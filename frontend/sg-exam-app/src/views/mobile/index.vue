<template>
  <div class="m-subject-content">
    <transition name="fade-transform" mode="out-in">
      <div>
        <el-row class="m-exam-title" v-show="!loading">
          <el-col :span="24">
            <h2 v-if="examination">{{ examination.examinationName }}</h2>
            <p v-if="examination && examination.attention !== undefined">{{
                examination.attention
              }}</p>
          </el-col>
        </el-row>
        <el-row>
          <div>
            <el-progress :text-inside="true" :stroke-width="16" :percentage="answeredSubjectCnt"
                         status="success"></el-progress>
          </div>
        </el-row>
      </div>
    </transition>
    <transition name="fade-transform" mode="out-in">
      <div v-show="!loading">
        <el-row v-for="(item, index)  in subjects" :key="index">
          <el-col :span="24">
            <choices :ref="'choices_' + index" v-if="item.type === 0" :onChoice="onChoiceFn"/>
            <short-answer :ref="'shortAnswer_' + index" v-if="item.type === 1"
                          :onChoice="onChoiceFn"/>
            <judgement :ref="'judgement_' + index" v-if="item.type === 2" :onChoice="onChoiceFn"/>
            <multiple-choices :ref="'multipleChoices_' + index" v-if="item.type === 3"
                              :onChoice="onChoiceFn"/>
          </el-col>
        </el-row>
      </div>
    </transition>
    <el-row class="m-subject-button">
      <el-button type="success" size="medium" @click="handleSubmitExam" v-show="!loading">
        {{ $t('submit') }}
      </el-button>
    </el-row>
  </div>
</template>

<script>
import {canStart} from '@/api/exam/exam'
import {anonymousUserSubmitAll} from '@/api/exam/answer'
import {anonymousUserGetAllSubjects, anonymousUserStart} from '@/api/exam/examRecord'
import {messageFail, messageSuccess, messageWarn} from '@/utils/util'
import Choices from '@/components/Subjects/Choices'
import MultipleChoices from '@/components/Subjects/MultipleChoices'
import ShortAnswer from '@/components/Subjects/ShortAnswer'
import Judgement from '@/components/Subjects/Judgement'

export default {
  components: {
    Choices,
    MultipleChoices,
    ShortAnswer,
    Judgement
  },
  data() {
    return {
      currentTime: 0,
      startTime: 0,
      endTime: 0,
      loading: true,
      loadingSubmit: false,
      query: {
        examinationId: undefined,
        subjectId: undefined,
        type: 0
      },
      examination: undefined,
      answer: undefined,
      examRecord: undefined,
      answeredSubjectCnt: 0,
      subjects: []
    }
  },
  created() {
    this.query.examinationId = this.$route.query.id
    this.startExam()
  },
  methods: {
    countDownS_cb: function (x) {
    },
    countDownE_cb: function (x) {
      messageWarn(this, '考试结束')
    },
    startExam() {
      if (this.query.examinationId === undefined) {
        return
      }

      // 是否能开始考试
      canStart(this.query.examinationId).then(response => {
        const {code, result} = response.data
        if (code !== 0 || result === null || !result) {
          messageFail(this, this.$t('exam.exams.startFailed'))
          return
        }

        // 开始考试
        this.loading = true
        anonymousUserStart({
          examinationId: this.query.examinationId,
        }).then(startRes => {
          const tmpResult = startRes.data.result
          this.examination = tmpResult.examination
          this.examRecord = tmpResult.examRecord
          const {subjectDto} = tmpResult
          this.tempSubject = subjectDto
          this.query.subjectId = subjectDto.id
          this.query.type = subjectDto.type

          anonymousUserGetAllSubjects(this.examination.id).then(subjectsRes => {
            const result = subjectsRes.data.result
            if (result && result.length > 0) {
              this.subjects = result
            }
            this.loading = false
            if (this.subjects.length > 0) {
              setTimeout(() => {
                for (let i = 0; i < this.subjects.length; i++) {
                  this.setSubjectInfo(i, this.subjects[i])
                }
              }, 100)
            }
          }).catch(error => {
            console.error(error)
            this.loading = false
          })
        }).catch(error => {
          console.error(error)
          this.loading = false
          messageFail(this, '系统出了点问题')
        })
      })
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
          ref.setSubjectInfo(item, this.subjects.length, null)
        } catch (error) {
          console.error(error)
        }
      }
    },
    onChoiceFn(sort) {
      // 标识已答题状态
      if (sort) {
        this.subjects[sort - 1].isAnswer = true
      }
      // 更新答题进度
      let cnt = 0
      for (let i = 0; i < this.subjects.length; i++) {
        const item = this.subjects[i]
        if (item.isAnswer !== undefined && item.isAnswer === true) {
          cnt++
        }
      }
      this.answeredSubjectCnt = (cnt / this.subjects.length) * 100
    },
    // 提交考试
    handleSubmitExam() {
      this.$confirm('确定要提交吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(() => {
        this.beforeSave()
        this.doSubmitExam(this.examRecord.id)
      }).catch(() => {
      })
    },
    beforeSave() {
      for (let i = 0; i < this.subjects.length; i++) {
        const ref = this.getSubjectRef(i, this.subjects[i])
        if (ref) {
          ref.beforeSave()
        }
      }
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

      anonymousUserSubmitAll(this.examination.id, data).then(() => {
        this.loadingSubmit = false
        messageSuccess(this, this.$t('submit') + this.$t('successAndExit'))
        setTimeout(() => {
          window.close()
        }, 2000)
      }).catch(() => {
        messageFail(this, this.$t('submit') + this.$t('failed'))
        this.loadingSubmit = false
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
    margin-bottom: 8px;
  }

  .m-subject-button {
    margin: 16px 16px 50px;
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
