<template>
  <div class="app-container">
    <el-row>
      <el-col :span="20" :offset="2">
        <div class="subject-content" v-for="(tempIncorrectAnswer, index) in list" :key="tempIncorrectAnswer.id">
          <div class="subject-content-option">
            <div class="subject-title">
              <span class="subject-title-number">{{index + 1}} .</span>
              {{tempIncorrectAnswer.subject.subjectName}}（{{tempIncorrectAnswer.subject.score}}分）
            </div>
            <!-- 选择题 -->
            <div v-if="tempIncorrectAnswer.subject.type === 0">
              <ul class="subject-options">
                <li class="subject-option" :class="getClass(tempIncorrectAnswer.subject.answer.answer, tempIncorrectAnswer.answer, 'A')">
                  <label><span class="subject-option-prefix">A.&nbsp;</span><span v-html="tempIncorrectAnswer.subject.options[0].optionContent" class="subject-option-prefix"></span></label>
                </li>
                <li class="subject-option" :class="getClass(tempIncorrectAnswer.subject.answer.answer, tempIncorrectAnswer.answer, 'B')">
                  <label><span class="subject-option-prefix">B.&nbsp;</span><span v-html="tempIncorrectAnswer.subject.options[1].optionContent" class="subject-option-prefix"></span></label>
                </li>
                <li class="subject-option" :class="getClass(tempIncorrectAnswer.subject.answer.answer, tempIncorrectAnswer.answer, 'C')">
                  <label><span class="subject-option-prefix">C.&nbsp;</span><span v-html="tempIncorrectAnswer.subject.options[2].optionContent" class="subject-option-prefix"></span></label>
                </li>
                <li class="subject-option" :class="getClass(tempIncorrectAnswer.subject.answer.answer, tempIncorrectAnswer.answer, 'D')">
                  <label><span class="subject-option-prefix">D.&nbsp;</span><span v-html="tempIncorrectAnswer.subject.options[3].optionContent" class="subject-option-prefix"></span></label>
                </li>
              </ul>
            </div>
          </div>
          <!-- 简答题 -->
          <div v-if="tempIncorrectAnswer.subject.type === 1">
            <p>
              <span v-html="tempIncorrectAnswer.subject.answer"></span>
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
  </div>
</template>
<script>
import { mapState } from 'vuex'
import { getAnswerListInfo } from '@/api/exam/answer'
import { isNotEmpty, notifyFail, notifyWarn, getAttachmentPreviewUrl, formatDate } from '@/utils/util'


export default {
  data () {
    return {
      total: 0,
      listLoading: true,
      tableKey: 0,
      list: [],
      listQuery: {
        sort: 'subject_id',
        order: ' asc',
        pageNum: 1,
        pageSize: 10,
        answerType: 1
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
    this.getList()
  },
  methods: {
    getList () {
      getAnswerListInfo(this.incorrectRecord.id, this.listQuery).then(response => {
        this.list = response.data.list
        this.total = response.data.total
        this.listLoading = false
      }).catch(() => {
        notifyFail(this, '加载错题失败')
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
    getClass (answer, incorrectAnswer, option) {
      // 和参考答案一样
      if (answer === incorrectAnswer && incorrectAnswer === option) {
        return 'right'
      } else if (answer !== incorrectAnswer && incorrectAnswer === option) {
        return 'correct'
      } else {
        return ''
      }
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
  .correct {
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
