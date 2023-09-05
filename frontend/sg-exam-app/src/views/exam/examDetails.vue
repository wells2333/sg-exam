<template>
  <div>
    <transition name="el-fade-in">
      <div v-show="!loading">
        <div class="single-course-intro d-flex align-items-center justify-content-center"
             :style="'background-image: url(' + examination.imageUrl + ');'">
          <div class="single-course-intro-content text-center">
            <h3>{{ examination.examinationName }}</h3>
            <div class="meta d-flex align-items-center justify-content-center">
              <a v-if="examination.typeLabel">{{ examination.typeLabel }}</a>
              <span>
                <i class="fa fa-circle" aria-hidden="true"></i>
              </span>
              <a>{{ examination.totalScore }}分</a>
            </div>
          </div>
        </div>
        <div class="single-course-content padding-80">
          <el-row class="my-content-container ml-100 mr-100">
            <el-col :span="18" style="padding-right: 40px;">
              <div class="clever-description">
                <div class="about-course mb-30">
                  <h4>{{$t('exam.exams.examTime')}}</h4>
                  <div style="margin-bottom: 16px;">
                    <p v-if="examination.startTime">
                      {{examination.startTime}} ~ {{examination.endTime}}
                    </p>
                    <p v-else>{{$t('exam.exams.unLimitTime')}}</p>
                  </div>
                  <h4>{{$t('exam.exams.examRemark')}}</h4>
                  <div style="margin-bottom: 16px;">
                    <p v-html="examination.remark"></p>
                  </div>
                  <h4>{{$t('exam.exams.examAttention')}}</h4>
                  <div style="margin-bottom: 16px;">
                    <p v-html="examination.attention"></p>
                  </div>
                  <h4>{{$t('exam.exams.evaluation')}}</h4>
                  <div>
                    <el-form :model="evaluate">
                      <el-form-item label="">
                        <el-input type="textarea" :rows="3" :placeholder="$t('exam.exams.inputEvaluation')"
                                  v-model="evaluate.evaluateContent"></el-input>
                      </el-form-item>
                      <el-form-item label="">
                        <el-rate v-model="evaluate.evaluateLevel"></el-rate>
                      </el-form-item>
                      <el-form-item>
                        <el-button type="primary" class="clever-btn"
                                   @click="handleSubmitEvaluate">{{$t('submit')}}
                        </el-button>
                      </el-form-item>
                    </el-form>
                  </div>
                  <div>
                    <div class="user-evaluate-item" v-for="e in evaluates" :key="e.id">
                      <el-row>
                        <el-col :span="3" style="color: #666;">
                          {{ e.operatorName }}
                        </el-col>
                        <el-col :span="21">
                          <div>
                            <el-rate v-model="e.evaluateLevel" :disabled="true"></el-rate>
                          </div>
                          <div class="user-evaluate-item-content" style="color:#333;">
                            {{ e.evaluateContent }}
                          </div>
                          <div class="user-evaluate-item-time">
                            {{ e.createTime }}
                          </div>
                        </el-col>
                      </el-row>
                    </div>
                  </div>
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="course-sidebar">
                <el-button type="primary" class="clever-btn mb-30 w-100" @click="handleStartExam">
                  {{ $t('exam.exams.start') }}
                </el-button>
              </div>
            </el-col>
          </el-row>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import { messageFail, messageWarn, messageSuccess } from '@/utils/util'
import { getObjDetail, canStart } from '@/api/exam/exam'
import store from '@/store'
import {mapGetters, mapState} from 'vuex'

export default {
  data() {
    return {
      loading: true,
      examId: '',
      examination: {},
      tempExamRecord: {
        id: null,
        userId: null,
        examinationId: null
      },
      evaluate: {
        evaluateContent: '',
        evaluateLevel: 5
      },
      evaluates: [],
      hasEvaluate: false
    }
  },
  computed: {
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
  created() {
    this.examId = this.$route.query.examId
    this.getExamInfo()
  },
  methods: {
    getExamInfo() {
      this.loading = true
      getObjDetail(this.examId).then(res => {
        this.examination = res.data.result
        setTimeout(() => {
          this.loading = false
        }, 500)
      }).catch(error => {
        console.error(error)
        this.loading = false
      })
    },
    handleStartExam() {
      this.tempExamRecord.examinationId = this.examination.id
      this.tempExamRecord.userId = this.userInfo.id
      canStart(this.examination.id).then(response => {
        const {code, result} = response.data
        if (code !== 0 || result === null) {
          messageFail(this, this.$t('exam.exams.startFailed'))
          return
        }
        if (!result) {
          messageFail(this, this.$t('exam.exams.startFailed'))
        }
        this.$confirm(this.$t('exam.exams.sureStart'), this.$t('tips'), {
          confirmButtonText: this.$t('sure'),
          cancelButtonText: this.$t('cancel'),
          type: 'warning'
        }).then(() => {
          store.dispatch('StartExam', this.tempExamRecord).then(() => {
            if (this.examRecord === undefined || this.subject === undefined) {
              messageWarn(this, this.$t('exam.exams.startFailed'))
              return
            }
            this.$router.push({ path: `/start/${this.examination.id}` })
          }).catch(() => {
            messageWarn(this, this.$t('exam.exams.startFailed'))
          })
        }).catch((err) => {
          console.error(err)
        })
      }).catch(() => {
        messageFail(this, this.$t('exam.exams.startFailed'))
      })
    },
    handleSubmitEvaluate() {
      if (this.hasEvaluate) {
        messageWarn(this, this.$t('exam.exams.doNotResubmit'))
        return
      }
      if (this.evaluate.evaluateContent === '') {
        this.evaluate.evaluateContent = this.$t('exam.exams.defaultEvaluate')
      }
      messageSuccess(this, this.$t('exam.exams.submitSuccess'))
    }
  }
}
</script>
