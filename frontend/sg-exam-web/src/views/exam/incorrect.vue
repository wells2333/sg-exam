<template>
  <div class="app-container">
    <el-card>
      <el-row v-show="!examRecordLoading">
        <el-row>
          <el-col :span="20" :offset="2">
            <el-divider>{{ $t('exam.scoreDetail') }}</el-divider>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="20" :offset="2">
            <el-row>
              <el-col :span="8">
                <label class="el-form-item__label">{{ $t('exam.examinationName') }}：<span
                  :title="examRecordDetail.examinationName">{{
                    examRecordDetail.examinationName | simpleStrFilter
                  }}</span>
                </label>
              </el-col>
              <el-col :span="8">
                <label class="el-form-item__label">{{ $t('exam.incorrect.userName') }}：{{
                    examRecordDetail.userName
                  }}</label>
              </el-col>
              <el-col :span="8">
                <label class="el-form-item__label">{{ $t('exam.incorrect.score') }}：{{
                    examRecordDetail.score
                  }}</label>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="8">
                <label class="el-form-item__label">{{ $t('exam.incorrect.startTime') }}：
                  {{ examRecordDetail.startTime | fmtDate('yyyy-MM-dd hh:mm') }}</label>
              </el-col>
              <el-col :span="8">
                <label class="el-form-item__label">{{ $t('exam.incorrect.endTime') }}：
                  {{ examRecordDetail.endTime | fmtDate('yyyy-MM-dd hh:mm') }}</label>
              </el-col>
              <el-col :span="8">
                <label class="el-form-item__label">{{ $t('exam.incorrect.duration') }}：{{
                    examRecordDetail.duration
                  }}</label>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="8">
                <label class="el-form-item__label">{{ $t('exam.incorrect.operator') }}</label>
              </el-col>
              <el-col :span="8">
                <label class="el-form-item__label">{{ $t('exam.incorrect.correctNumber') }}：</label>
                <el-tag type="success" effect="dark">{{ examRecordDetail.correctNumber }}</el-tag>
              </el-col>
              <el-col :span="8">
                <label
                  class="el-form-item__label">{{ $t('exam.incorrect.inCorrectNumber') }}：</label>
                <el-tag type="danger" effect="dark">{{
                    examRecordDetail.inCorrectNumber
                  }}
                </el-tag>
              </el-col>
            </el-row>
          </el-col>
        </el-row>
      </el-row>
      <el-row v-show="!examRecordLoading">
        <el-col :span="20" :offset="2">
          <el-divider>{{ $t('exam.incorrect.subjectList') }}</el-divider>
        </el-col>
      </el-row>
      <el-row v-for="(item) in list"
              :key="item.id">
        <el-col :span="20" :offset="2">
          <div class="subject-content" v-show="item.show">
            <div class="subject-content-option">
              <div class="subject-title">
                <span
                  v-html="customReplaceFirstP(item.subject.subjectName, item.subject.type, item.subject.sort)"></span>
              </div>
              <div v-if="item.subject.subjectVideoUrl">
                <sg-video ref="sgVideo" :src="item.subject.subjectVideoUrl"></sg-video>
              </div>
              <!-- 选择题 -->
              <div>
                <ul class="subject-options" v-for="option in item.subject.options"
                    :key="option.id">
                  <li class="subject-option" :class="getClass(option.right)">
                    <label>
                      <span class="subject-option-prefix">{{
                          option.optionName
                        }}&nbsp;</span>
                      <span v-html="option.optionContent"
                            class="subject-option-prefix"></span></label>
                  </li>
                </ul>
              </div>
            </div>
            <!-- 简答题 -->
            <div v-if="item.subject.type === 1">
              <p>
                {{ $t('exam.incorrect.userAnswer') }}：<span
                :class="getShortAnswerClass(item.answer, item.subject.answer.answer)">{{
                  item.answer
                }}</span>
              </p>
            </div>
            <!-- 判断 -->
            <div v-if="item.subject.type === 2">
              <p>
                {{ $t('exam.incorrect.userAnswer') }}：
                <span :class="getJudgeClass(item.answer, item.subject.answer.answer)">
                  {{ processJudgementUserAnswer(item.answer) }}
                </span>
              </p>
            </div>
            <!-- 语音 -->
            <div v-if="item.subject.type === 4">
              <p>
                {{ $t('exam.incorrect.userAnswer') }}：<span
                :class="getShortAnswerClass(item.answer, item.subject.answer.answer)">{{
                  item.answer
                }}</span>
              </p>
            </div>
            <!-- 视频 -->
            <div v-if="item.subject.type === 5">
              <p>
                {{ $t('exam.incorrect.userAnswer') }}：<span
                :class="getShortAnswerClass(item.answer, item.subject.answer.answer)">{{
                  item.answer
                }}</span>
              </p>
            </div>
            <p class="subject-content-answer"
               v-if="item.subject.answer !== undefined">
              {{ $t('exam.incorrect.answer') }}：
              <span v-if="item.subject.type === 2">
                {{ getJudgementStdAnswerText(item.subject.answer.answer) }}
              </span>
              <span v-else v-html="item.subject.answer.answer"></span>
            </p>
            <p class="subject-content-analysis"
               v-if="item.subject.analysis !== ''">
              {{ $t('exam.incorrect.analysis') }}：<span v-html="item.subject.analysis"></span>
            </p>
          </div>
        </el-col>
      </el-row>
      <el-row style="text-align: center; margin-bottom: 50px;">
        <el-col :span="24">
          <el-button v-if="!isLastPage" type="default" @click="scrollList" :loading="loading"
                     style="margin-bottom: 100px;">{{ $t('load.loadMore') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>
<script>
import {mapState} from 'vuex'
import {getAnswerListInfo} from '@/api/exam/answer'
import {examRecordDetails} from '@/api/exam/examRecord'
import {notifyFail, messageWarn} from '@/utils/util'
import {answerType} from '@/const/constant'
import SgVideo from '@/components/SgVideo'
import {replaceFirstP} from '@/utils/busi'

export default {
  name: 'Incorrect',
  components: {
    SgVideo
  },
  data() {
    return {
      recordId: undefined,
      loading: true,
      examRecordLoading: true,
      total: 0,
      isLastPage: false,
      tableKey: 0,
      list: [],
      listQuery: {
        sort: 'subject_id',
        order: ' asc',
        page: 1,
        pageSize: 10,
        answerType: 1
      },
      examRecordDetail: {
        correctNumber: 0,
        endTime: 0,
        duration: '',
        examinationName: '',
        inCorrectNumber: 0,
        score: 0,
        startTime: 0,
        submitStatus: 0,
        totalScore: 0
      }
    }
  },
  computed: {
    ...mapState({
      userInfo: state => state.user.userInfo
    })
  },
  created() {
    this.recordId = this.$route.query.recordId
    if (this.recordId) {
      this.getRecordDetail()
    }
  },
  methods: {
    customReplaceFirstP(str, type, sort) {
      let typeStr = '';
      switch (type) {
        case 0:
          typeStr = this.$t('exam.subject.subjectTypeChoices')
          break;
        case 1:
          typeStr = this.$t('exam.subject.subjectTypeShortAnswer')
          break;
        case 2:
          typeStr = this.$t('exam.subject.subjectTypeJudgement')
          break;
        case 3:
          typeStr = this.$t('exam.subject.subjectTypeMultiChoices')
          break;
        case 4:
          typeStr = this.$t('exam.subject.subjectTypeFillBlank')
          break;
        case 5:
          typeStr = this.$t('exam.subject.subjectTypeMaterial')
          break;
      }
      return replaceFirstP(str, typeStr, sort)
    },
    getRecordDetail() {
      this.loading = true
      this.examRecordLoading = true
      examRecordDetails(this.recordId).then(response => {
        setTimeout(() => {
          this.examRecordDetail = response.data.result.record
          this.examRecordLoading = false
        }, 500)
        getAnswerListInfo(this.recordId, this.listQuery).then(response => {
          const {total, isLastPage, list} = response.data
          this.updateList(list)
          this.total = total
          this.isLastPage = isLastPage
          this.loading = false
        }).catch(error => {
          console.error(error)
          notifyFail(this, this.$t('exam.incorrect.loadSubjectFailed'))
          this.loading = false
        })
      })
    },
    scrollList() {
      if (this.isLastPage) {
        messageWarn(this, this.$t('load.noMoreData'))
        return
      }
      if (this.loading) {
        messageWarn(this, this.$t('load.loading'))
        return
      }
      this.loading = true
      setTimeout(() => {
        this.listQuery.page++
        getAnswerListInfo(this.recordId, this.listQuery).then(response => {
          const {total, isLastPage, list} = response.data
          this.updateList(list)
          this.total = total
          this.isLastPage = isLastPage
          this.loading = false
        }).catch(() => {
          messageWarn(this, this.$t('load.loadFailed'))
          this.loading = false
        })
      }, 500)
    },
    updateList(list) {
      if (list === undefined || list === null || list.length === 0) {
        return list
      }
      list.forEach(item => {
        item.show = false
      })
      if (this.list.length === 0) {
        this.list = list
      } else {
        list.forEach(item => {
          let exist = false
          for (let i = 0; i < this.list.length; i++) {
            if (this.list[i].id === item.id) {
              exist = true
            }
          }
          if (!exist) {
            this.list.push(item)
          }
        })
      }
      for (let i = 0; i < list.length; i++) {
        setTimeout(() => {
          list[i].show = true
        }, 250 + (100 * i))
      }
    },
    getClass(right) {
      return answerType[right]
    },
    getShortAnswerClass(userAnswer, standardAnswer) {
      if (userAnswer === null || userAnswer === undefined) {
        return ''
      }

      let right = userAnswer === standardAnswer
      return answerType[right]
    },
    getJudgeClass(userAnswer, standardAnswer) {
      if (userAnswer === null || userAnswer === undefined) {
        return ''
      }

      let right = false
      if (userAnswer === '正确') {
        right = standardAnswer === '0'
      } else if (userAnswer === '错误') {
        right = standardAnswer === '1'
      }
      return answerType[right]
    },
    processJudgementUserAnswer(answer) {
      if (answer === null || answer === undefined) {
        return ''
      }

      return answer === '正确' ? this.$t('exam.incorrect.right') : this.$t('exam.incorrect.wrong')
    },
    getJudgementStdAnswerText(answer) {
      return answer === '0' ? this.$t('exam.incorrect.right') : this.$t('exam.incorrect.wrong')
    }
  }
}
</script>
<style lang="scss" rel="stylesheet/scss" scoped>
.incorrect-answer-gray-box {
  margin-top: 50px;
  margin-bottom: 50px;
  min-height: 200px;
}

.incorrect-answer-gray-box-title {
  text-align: center;
}

.subject-title {
  color: #333333;
  margin-bottom: 10px;
  position: relative;
  display: inline-flex;

  .subject-title-number {
    position: absolute;
    left: -25px;
    top: 0;
    display: inline-block;
    width: 40px;
    line-height: 22px;
    text-align: right;
  }
}

.subject-option {
  padding-bottom: 10px;
  padding-left: 10px;
}

.score {
  margin: 20px;
}

.subject-content {
  background: #F6F7FA;
  border-radius: 4px;
  margin-bottom: 21px;
  padding: 12px 0 12px 22px;
  position: relative;
  color: #666666;
  text-align: left;
}

.incorrect {
  color: #F56C6C;
}

.right {
  color: #67C23A;
}

.score-gray-box {
  margin-top: 50px;
}

.score-gray-box-title {
  text-align: center;
}

.subject-options {
  margin: 0;
  padding: 0;
  list-style: none;

  > li {
    position: relative;
    font-size: 24px;

    label {
      word-break: break-all;
      display: block;
      line-height: 1.0;
      transition: color 0.4s;
      font-weight: normal;
    }

    .subject-option-prefix {
      font-size: 16px;
      display: inline-block
    }
  }
}

</style>
