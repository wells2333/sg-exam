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
                      <el-row class="user-evaluate-item-bg">
                        <el-col :span="2" >
                          <img v-if="e.avatarUrl" width="40" height="40" class="user-evaluate-item-avatar" :src="e.avatarUrl">
                          <i class="iconfont icon-user" style="font-size: 42px; color: #5a5a5a;" v-else></i>
                        </el-col>
                        <el-col :span="22">
                          <div class="user-evaluate-item-top">
                            <span style="color: #333; margin-right: 15px;">{{ e.operatorName }}</span>
                            <el-rate v-model="e.evaluateLevel" :disabled="true" style="height: 100%; line-height: initial;"></el-rate>
                          </div>
                          <div class="user-evaluate-item-content" style="color:#666;">
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
import {addObj, getExamEvaluateList} from '@/api/exam/examEvaluate'
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
    this.getExamEvaluateList()
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
    getExamEvaluateList() {
      getExamEvaluateList({examId: this.examId}).then(res => {
        const {code} = res.data
        if (code === 0) {
          this.evaluates = res.data.result.list
        }
      }).catch(error => {
        console.error(error)
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
      addObj({
        examId: this.examId,
        ...this.evaluate
      }).then(res => {
        if (res.data.code === 0) {
          this.evaluate.evaluateContent = ''
          this.hasEvaluate = true
          messageSuccess(this, this.$t('exam.exams.submitSuccess'))
          this.getExamEvaluateList()
        } else {
          messageWarn(this, this.$t('exam.exams.submitFailed'))
        }
      }).catch(error => {
        console.error(error)
      })
    }
  }
}
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
.user-evaluate-item {
  margin-top: 26px;
  .user-evaluate-item-bg {
    border-bottom: 1px solid rgba(233,233,233,.6);
    padding-bottom: 20px;
  }
  .user-evaluate-item-avatar {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    cursor: pointer;
  }
  .user-evaluate-item-top {
    font-size: 13px;
    display: flex;
    flex-direction: row;
    align-items: center;
    height: 23px;
  }
}
.user-evaluate-item-content {
  margin-top: 8px;
}
.user-evaluate-item-time {
  font-size: 12px;
  margin-top: 10px;
  color: #999;
}
</style>
