<template>
  <div>
    <transition name="el-fade-in">
      <div v-show="!loading">
        <div class="single-course-intro d-flex align-items-center justify-content-center"
             :style="'background-image: url(' + examination.imageUrl + ');'">
          <div class="single-course-intro-content text-center">
            <h3>{{ examination.examinationName | simpleStrFilter}}</h3>
            <div class="meta d-flex align-items-center justify-content-center">
              <a v-if="examination.typeLabel">{{ examination.typeLabel }}</a>
              <span>
                <i class="fa fa-circle" aria-hidden="true"></i>
              </span>
              <a>{{ $t('exam.exams.score') + ' ' + examination.totalScore}}</a>
            </div>
            <div class="favorite-btn" v-show="favoriteBtnText !== undefined" @click="handleFavorite" >
              <i :class="examination.favorite ? 'favorite-icon el-icon-star-on' : 'cancel-favorite-icon el-icon-star-off'"></i>
              <span>{{ favoriteBtnText }}</span>
            </div>
          </div>
        </div>
        <div class="single-course-content padding-50">
          <el-row class="my-content-container ml-100 mr-100">
            <el-col :span="18" style="padding-right: 40px;">
              <el-tabs v-model="activeName">
                <el-tab-pane name="desc">
                   <span slot="label">
                    <span class="exam-content-btn">{{$t('exam.examIntroduction')}}</span>
                  </span>
                  <div class="clever-description">
                    <div class="about-course mb-30">
                      <h4>{{ $t('exam.exams.examTime') }}</h4>
                      <div style="margin-bottom: 16px;">
                        <p>
                          {{ $t('exam.exams.startTime') }}
                          <span v-if="examination.startTime">
                        {{ examination.startTime}}
                      </span>
                          <span v-else>{{ $t('exam.exams.unLimitTime') }}</span>
                        </p>
                        <p>
                          {{ $t('exam.exams.examDuration') }}
                          <span v-if="examination.examDurationMinute">
                        {{ examination.examDurationMinute }}
                      </span>
                          <span v-else>
                        {{ $t('exam.exams.unLimitTime') }}
                      </span>
                        </p>
                      </div>
                      <h4>{{ $t('exam.exams.examRemark') }}</h4>
                      <div style="margin-bottom: 16px;">
                        <p v-html="examination.remark"></p>
                      </div>
                      <h4>{{ $t('exam.exams.examAttention') }}</h4>
                      <div style="margin-bottom: 16px;">
                        <p v-html="examination.attention"></p>
                      </div>
                    </div>
                  </div>
                </el-tab-pane>
                <el-tab-pane name="evaluate">
                   <span slot="label">
                    <span class="exam-content-btn">{{$t('exam.examEvaluation')}}</span>
                  </span>
                  <div class="about-review mb-30">
                    <h4>{{ $t('exam.exams.evaluation') }}</h4>
                    <div>
                      <el-form :model="evaluate">
                        <el-form-item label="">
                          <el-input type="textarea" :rows="3"
                                    :placeholder="$t('exam.exams.inputEvaluation')"
                                    v-model="evaluate.evaluateContent"></el-input>
                        </el-form-item>
                        <el-form-item label="">
                          <el-rate v-model="evaluate.evaluateLevel"></el-rate>
                        </el-form-item>
                        <el-form-item>
                          <el-button type="primary" class="clever-btn"
                                     @click="handleSubmitEvaluate">{{ $t('submit') }}
                          </el-button>
                        </el-form-item>
                      </el-form>
                    </div>
                    <div>
                      <div class="user-evaluate-item" v-for="e in evaluates" :key="e.id">
                        <el-row class="user-evaluate-item-bg">
                          <el-col :span="2">
                            <img v-if="e.avatarUrl" width="40" height="40"
                                 class="user-evaluate-item-avatar" :src="e.avatarUrl">
                            <i class="iconfont icon-user" style="font-size: 42px; color: #5a5a5a;"
                               v-else></i>
                          </el-col>
                          <el-col :span="22">
                            <div class="user-evaluate-item-top">
                            <span style="color: #333; margin-right: 15px;">{{
                                e.operatorName
                              }}</span>
                              <el-rate v-model="e.evaluateLevel" :disabled="true"
                                       style="height: 100%; line-height: initial;"></el-rate>
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
                      <el-row class="list-pagination" style="margin-top: 16px;" v-show="evaluates && evaluates.length > 0">
                        <el-pagination
                          @size-change="handleEvaluateSizeChange"
                          @current-change="handleEvaluateCurrentChange"
                          :current-page="evaluateQuery.page"
                          :page-sizes="[10, 20, 50]"
                          :page-size="10"
                          layout="total, sizes, prev, pager, next, jumper"
                          :total="evaluateTotal">
                        </el-pagination>
                      </el-row>
                    </div>
                  </div>
                </el-tab-pane>
              </el-tabs>
            </el-col>
            <el-col :span="6">
              <div class="course-sidebar">
                <el-button type="primary" class="clever-btn mb-30 w-100" @click="handleStartExam" :loading="startBtnLoading">
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
import {messageFail, messageWarn, messageSuccess} from '@/utils/util'
import {getObjDetail, canStart, favoriteExamination} from '@/api/exam/exam'
import {addObj, getExamEvaluateList} from '@/api/exam/examEvaluate'
import store from '@/store'
import {mapGetters, mapState} from 'vuex'

export default {
  data() {
    return {
      loading: true,
      startBtnLoading: false,
      examId: '',
      examination: {},
      activeName: 'desc',
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
      hasEvaluate: false,
      favoriteBtnText: undefined,
      favoriteBtnLoading: false,
      evaluateTotal: 0,
      evaluateQuery: {
        page: 1,
        examId: undefined
      }
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
    this.evaluateQuery.examId = this.$route.query.examId
    this.getExamInfo()
    this.getExamEvaluateList()
  },
  methods: {
    getExamInfo() {
      this.loading = true
      getObjDetail(this.examId).then(res => {
        this.examination = res.data.result
        this.updateFavoriteBtnText()
        setTimeout(() => {
          this.loading = false
        }, 500)
      }).catch(error => {
        console.error(error)
        this.loading = false
      })
    },
    getExamEvaluateList() {
      getExamEvaluateList({...this.evaluateQuery}).then(res => {
        const {code} = res.data
        if (code === 0) {
          this.evaluates = res.data.result.list
          this.evaluateTotal = res.data.result.total
        }
      }).catch(error => {
        console.error(error)
      })
    },
    handleStartExam() {
      this.tempExamRecord.examinationId = this.examination.id
      this.tempExamRecord.userId = this.userInfo.id
      canStart(this.examination.id).then(response => {
        const {code, result, message} = response.data
        if (code !== 0 || result === null || !result) {
          messageFail(this, this.$t('exam.exams.startFailed') + ': ' + message)
          return
        }

        this.$confirm(this.$t('exam.exams.sureStart'), this.$t('tips'), {
          confirmButtonText: this.$t('sure'),
          cancelButtonText: this.$t('cancel'),
          type: 'warning'
        }).then(() => {
          this.startBtnLoading = true
          store.dispatch('StartExam', this.tempExamRecord).then((result) => {
            if (this.examRecord === undefined) {
              messageWarn(this, this.$t('exam.exams.startFailed'))
              return
            }

            const recordId = result.examRecord.id
            // 单页模式
            if (this.examination.answerType === 0) {
              const resolvedRoute = this.$router.resolve({
                path: `/start-exam-b/${this.examination.id}`,
                query: {recordId: recordId}
              });
              // 使用 window.open() 在新标签页中打开链接
              const newTab = window.open(resolvedRoute.href, "_blank");
              // 关闭当前标签页（这部分的可靠性取决于浏览器的设置）
              if (newTab) {
                window.opener = null;
                window.open("about:blank", "_top").close()
              }
            } else {
              // 顺序模式
              this.$router.push({path: `/start-exam-a/${this.examination.id}?recordId=${recordId}`})
            }
          }).catch((err) => {
            console.error(err)
            messageWarn(this, this.$t('exam.exams.startFailed'))
          }).finally(() => {
            this.startBtnLoading = false
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
    },
    handleFavorite() {
      const userId = this.userInfo.id
      let type = this.examination && this.examination.favorite ? 0 : 1;
      const tips = type === 1 ? this.$t('fav.favorite') : this.$t('fav.cancelFavorite')
      this.favoriteBtnLoading = true
      favoriteExamination(this.examination.id, userId, type).then(res => {
        if (res.data.result) {
          this.examination.favorite = !this.examination.favorite
        } else {
          messageWarn(this, tips + this.$t('failed'))
        }
      }).catch(error => {
        console.error(error)
        messageWarn(this, tips + this.$t('failed'))
      }).finally(() => {
        this.updateFavoriteBtnText()
        this.favoriteBtnLoading = false
      })
    },
    updateFavoriteBtnText() {
      if (this.examination && this.examination.favorite) {
        this.favoriteBtnText = this.$t('fav.cancelFavorite')
      } else {
        this.favoriteBtnText = this.$t('fav.favorite')
      }
    },
    handleEvaluateSizeChange(val) {
      this.evaluateQuery.pageSize = val
      this.getExamEvaluateList()
    },
    handleEvaluateCurrentChange(val) {
      this.evaluateQuery.page = val
      this.getExamEvaluateList()
    }
  }
}
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
.favorite-btn {
  margin-top: 16px;
  color: #909399;
  cursor: pointer;
}
.favorite-btn :hover {
  color: #3762f0;
}
.favorite-icon {
  color: #3762f0;
}
</style>
