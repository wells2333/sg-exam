<template>
  <div class="app-container">
    <el-card>
      <spinner-loading v-if="listLoading"/>
      <el-row>
        <el-col :span="20" :offset="2">
          <el-divider>成绩详情</el-divider>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="20" :offset="2">
          <el-row>
            <el-col :span="8">
                <label class="el-form-item__label">考试名称: {{ examRecordDetail.examinationName }}</label>
            </el-col>
            <el-col :span="8">
              <label class="el-form-item__label">考生姓名: {{ examRecordDetail.userName }}</label>
            </el-col>
            <el-col :span="8">
              <label class="el-form-item__label">总得分: {{ examRecordDetail.score }}</label>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <label class="el-form-item__label">开始时间: {{ examRecordDetail.startTime | fmtDate('yyyy-MM-dd hh:mm') }}</label>
            </el-col>
            <el-col :span="8">
              <label class="el-form-item__label">结束时间: {{ examRecordDetail.endTime | fmtDate('yyyy-MM-dd hh:mm') }}</label>
            </el-col>
            <el-col :span="8">
              <label class="el-form-item__label">耗时: {{ examRecordDetail.duration }}</label>
            </el-col>
          </el-row>
         <el-row>
           <el-col :span="8">
             <label class="el-form-item__label">评卷人: 系统自动评分</label>
           </el-col>
           <el-col :span="8">
             <label class="el-form-item__label">正确题数: </label><el-tag type="success" size="small" effect="dark">{{ examRecordDetail.correctNumber }}</el-tag>
           </el-col>
           <el-col :span="8">
             <label class="el-form-item__label">错误题数: </label><el-tag type="danger" size="small" effect="dark">{{ examRecordDetail.inCorrectNumber }}</el-tag>
           </el-col>
         </el-row>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="20" :offset="2">
          <el-divider>错题列表</el-divider>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="20" :offset="2">
          <div class="subject-content" v-for="(tempIncorrectAnswer, index) in list" :key="tempIncorrectAnswer.id">
            <div class="subject-content-option">
              <div class="subject-title">
                <span class="subject-title-number">{{index + 1}} .</span>
                {{tempIncorrectAnswer.subject.subjectName}}（{{tempIncorrectAnswer.subject.score}}分）
              </div>
              <!-- 选择题 -->
              <div>
                <ul class="subject-options" v-for="option in tempIncorrectAnswer.subject.options" :key="option.id">
                  <li class="subject-option" :class="getClass(option.right)">
                    <label><span class="subject-option-prefix">{{ option.optionName }}&nbsp;</span><span v-html="option.optionContent" class="subject-option-prefix"></span></label>
                  </li>
                </ul>
              </div>
            </div>
            <!-- 简答题 -->
            <div v-if="tempIncorrectAnswer.subject.type === 1">
              <p>
                <span v-html="tempIncorrectAnswer.subject.answer.answer"></span>
              </p>
            </div>
            <p class="subject-content-answer">
              参考答案：{{tempIncorrectAnswer.subject.answer.answer}}
            </p>
            <p class="subject-content-analysis">
              解析：{{tempIncorrectAnswer.subject.analysis}}
            </p>
          </div>
          <div class="pagination-container">
            <el-pagination v-show="total > 0" :current-page="listQuery.pageNum" :page-sizes="[10,20,30, 50]" :page-size="listQuery.pageSize" :total="total" background layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange" @current-change="handleCurrentChange"/>
          </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>
<script>
import { mapState } from 'vuex'
import { getAnswerListInfo } from '@/api/exam/answer'
import { examRecordDetails } from '@/api/exam/examRecord'
import { notifyFail } from '@/utils/util'
import { answerType } from '@/const/constant'
import SpinnerLoading from '@/components/SpinnerLoading'

export default {
  name: 'Incorrect',
  components: {
    SpinnerLoading
  },
  data () {
    return {
      listLoading: true,
      total: 0,
      tableKey: 0,
      list: [],
      listQuery: {
        sort: 'subject_id',
        order: ' asc',
        pageNum: 1,
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
    // 获取用户信息
    ...mapState({
      userInfo: state => state.user.userInfo,
      incorrectRecord: state => state.exam.incorrectRecord
    })
  },
  created () {
    this.getRecordDetail()
  },
  methods: {
    getRecordDetail () {
      this.listLoading = true
      examRecordDetails(this.incorrectRecord.id).then(response => {
        this.examRecordDetail = response.data.data
        getAnswerListInfo(this.incorrectRecord.id, this.listQuery).then(response => {
          this.list = response.data.list
          this.total = response.data.total
          this.listLoading = false
        }).catch(() => {
          notifyFail(this, '加载错题失败')
        })
      })
    },
    handleSizeChange (val) {
      this.listQuery.limit = val
      this.getList()
    },
    handleCurrentChange (val) {
      this.listQuery.pageNum = val
      this.getList()
    },
    getClass (right) {
      return answerType[right]
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" rel="stylesheet/scss" scoped>
  @import "../../assets/css/common.scss";
  .app-container {
    @extend .common-container;
  }

  .incorrect-answer-gray-box {
    @extend .gray-box;
    margin-top: 50px;
    margin-bottom: 50px;
    min-height: 200px;
  }
  .incorrect-answer-gray-box-title {
    text-align: center;
  }
  /* 题目 */
  .subject-title {
    color: #333333;
    font-size: 16px;
    line-height: 22px;
    margin-bottom: 10px;
    padding-left: 20px;
    position: relative;
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
  /* 题目选项 */
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
    min-height: 240px;
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
    @extend .gray-box;
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
      /* 选项名称 */
      .subject-option-prefix {
        font-size: 16px;
        display: inline-block
      }
    }
  }

</style>
