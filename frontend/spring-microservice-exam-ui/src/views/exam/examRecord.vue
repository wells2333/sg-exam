<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.examinationName" placeholder="考试名称" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter"/>
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">{{ $t('table.search') }}</el-button>
      <el-button v-if="exam_record_btn_export" class="filter-item" icon="el-icon-download" plain @click="handleExportExamRecord">{{ $t('table.export') }}</el-button>
    </div>
    <spinner-loading v-if="listLoading"/>
    <el-table
      ref="multipleTable"
      :key="tableKey"
      :data="list"
      :default-sort="{ prop: 'id', order: 'descending' }"
      highlight-current-row
      style="width: 100%;"
      @selection-change="handleSelectionChange"
      @sort-change="sortChange">
      <el-table-column type="selection" width="55"/>
      <el-table-column :label="$t('table.examinationName')" min-width="200">
        <template slot-scope="scope">
          <span>{{ scope.row.examinationName }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.examRecord.userName')" min-width="90">
        <template slot-scope="scope">
          <span>{{ scope.row.userName }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.examRecord.deptName')" min-width="90">
        <template slot-scope="scope">
          <span>{{ scope.row.deptName }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.examRecord.score')" min-width="90">
        <template slot-scope="scope">
          <span>{{ scope.row.score }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.examRecord.examTime')" min-width="90">
        <template slot-scope="scope">
          <span>{{ scope.row.startTime | timeFilter }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.examRecord.submitStatus')" min-width="90">
        <template slot-scope="scope">
          <el-tag :type="scope.row.submitStatus | submitStatusTypeFilter">{{ scope.row.submitStatus | submitStatusFilter }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.actions')" class-name="status-col" width="300px">
        <template slot-scope="scope">
          <el-button type="text" @click="handleUpdate(scope.row)" icon="el-icon-edit">{{ $t('table.examRecord.details') }}</el-button>
          <el-button type="text" @click="handleMarking(scope.row)" icon="el-icon-check">{{ $t('table.examRecord.marking') }}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-container">
      <el-pagination v-show="total > 0" :current-page="listQuery.pageNum" :page-sizes="[10,20,30, 50]" :page-size="listQuery.pageSize" :total="total" background layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange" @current-change="handleCurrentChange"/>
    </div>

    <!-- 成绩详情 -->
    <el-dialog :visible.sync="dialogVisible" :title="$t('table.examRecord.details')">
      <el-row>
        <el-col :span="24">
          <div slot="header" class="score-gray-box-title">
            <span>考试成绩</span>
          </div>
          <div class="score">
            <h4>成绩: <span type="success">{{temp.score}}</span></h4>
            <h4>正确题数: <span type="success">{{temp.correctNumber}}</span></h4>
            <h4>错误题数: <span type="success">{{temp.inCorrectNumber}}</span></h4>
            <h4>开始时间: <span type="success">{{temp.startTime | timeFilter}}</span></h4>
            <h4>结束时间: <span type="success">{{temp.endTime | timeFilter}}</span></h4>
          </div>
        </el-col>
      </el-row>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">{{ $t('table.cancel') }}</el-button>
        <el-button type="primary" @click="dialogVisible = false">{{ $t('table.confirm') }}</el-button>
      </div>
    </el-dialog>

    <!-- 批改 -->
    <el-dialog :visible.sync="dialogMarkingVisible" :title="$t('table.examRecord.marking')" width="80%" top="10vh">
      <el-row :gutter="20">
        <el-col :span="20"  class="subject-box-card" v-loading="markLoading">
          <el-form ref="dataAnswerForm" :model="tempAnswer" :label-position="labelPosition" label-width="100px">
            <div class="user-info">
              <el-row>
                <el-col :span="6">
                  <el-form-item label="考生姓名：">
                    <span>{{currentRecord.userName}}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item label="题目序号：">
                    <span>{{currentIndex}}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item label="题目分值：">
                    <span v-html="tempAnswer.subject.score"></span>
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item label="耗时：">
                    <span>{{tempAnswer | consumingFilter }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
            </div>
            <div class="subject-content">
              <!-- 选择题 -->
              <div class="subject-content-option">
                <div class="subject-title">
                  <span class="subject-title-number">{{currentIndex}} .</span>
                  {{tempAnswer.subject.subjectName}}
                </div>
                <div v-if="tempAnswer.type === 0">
                  <ul class="subject-options">
                    <li class="subject-option" :class="getClass(tempAnswer.subject.answer.answer, tempAnswer.answer, 'A')">
                      <label><span class="subject-option-prefix">A.&nbsp;</span><span v-html="tempAnswer.subject.options[0].optionContent" class="subject-option-prefix"></span></label>
                    </li>
                    <li class="subject-option" :class="getClass(tempAnswer.subject.answer.answer, tempAnswer.answer, 'B')">
                      <label><span class="subject-option-prefix">B.&nbsp;</span><span v-html="tempAnswer.subject.options[1].optionContent" class="subject-option-prefix"></span></label>
                    </li>
                    <li class="subject-option" :class="getClass(tempAnswer.subject.answer.answer, tempAnswer.answer, 'C')">
                      <label><span class="subject-option-prefix">C.&nbsp;</span><span v-html="tempAnswer.subject.options[2].optionContent" class="subject-option-prefix"></span></label>
                    </li>
                    <li class="subject-option" :class="getClass(tempAnswer.subject.answer.answer, tempAnswer.answer, 'D')">
                      <label><span class="subject-option-prefix">D.&nbsp;</span><span v-html="tempAnswer.subject.options[3].optionContent" class="subject-option-prefix"></span></label>
                    </li>
                  </ul>
                </div>
                <!-- 简答题 -->
                <div v-if="tempAnswer.type === 1">
                  <p>
                    <span v-html="tempAnswer.answer"></span>
                  </p>
                </div>
              </div>
            </div>
            <el-row>
              <el-col :span="24">
                <el-form-item label="参考答案：">
                  <span v-html="tempAnswer.subject.answer.answer"></span>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="解析：">
                  <span v-html="tempAnswer.subject.analysis"></span>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="得分：">
                  <el-input v-model="tempAnswer.score"/>
                </el-form-item>
              </el-col>
            </el-row>
            <div class="subject-buttons" v-if="!markLoading && tempAnswer.subject.id !== ''">
              <el-button plain @click="last">上一题</el-button>
              <el-button plain @click="next">下一题</el-button>
              <el-button type="success" icon="el-icon-check" @click="completeMarking">批改完成</el-button>
            </div>
          </el-form>
        </el-col>
        <el-col :span="4">
          <span>选择题目</span>
          <div class="answer-number">
            <el-button class="number-btn" circle v-for="(value, index) in subjectIds" :key="index" @click="toSubject(index, value.subjectId, value.type)" >&nbsp;{{index + 1}}&nbsp;</el-button>
          </div>
        </el-col>
      </el-row>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogMarkingVisible = false">{{ $t('table.cancel') }}</el-button>
        <el-button type="primary" @click="dialogMarkingVisible = false">{{ $t('table.confirm') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { fetchExamRecordList, exportObj, completeMarking } from '@/api/exam/examRecord'
import { getAnswerByRecordId, putAnswer } from '@/api/exam/answer'
import { getSubjectIds } from '@/api/exam/exam'
import waves from '@/directive/waves'
import { mapGetters } from 'vuex'
import { exportExcel, messageFail, messageSuccess, formatDate } from '@/utils/util'
import SpinnerLoading from '@/components/SpinnerLoading'
import Tinymce from '@/components/Tinymce'

export default {
  name: 'ExamRecordManagement',
  components: {
    SpinnerLoading, Tinymce
  },
  directives: {
    waves
  },
  filters: {
    submitStatusFilter (type) {
      const typeMap = {
        0: '未提交',
        1: '已提交',
        2: '待批改',
        3: '统计完成'
      }
      return typeMap[type]
    },
    submitStatusTypeFilter (status) {
      const statusMap = {
        0: 'warning',
        1: 'warning',
        2: 'warning',
        3: 'success'
      }
      return statusMap[status]
    },
    timeFilter (time) {
      return formatDate(new Date(time), 'yyyy-MM-dd hh:mm')
    },
    consumingFilter(answer) {
      return (answer.endTime - answer.startTime) / 1000 + 'ms'
    }
  },
  data () {
    return {
      tableKey: 0,
      list: null,
      total: null,
      listLoading: true,
      listQuery: {
        pageNum: 1,
        pageSize: 10,
        examinationName: undefined,
        sort: 'id',
        order: 'descending'
      },
      temp: {
        id: '',
        examinationName: '',
        userName: '',
        deptName: '',
        startTime: '',
        endTime: '',
        status: '',
        totalScore: '',
        type: '',
        correctNumber: '',
        inCorrectNumber: '',
        duration: '',
        collegeId: ''
      },
      checkedKeys: [],
      exam_record_btn_export: false,
      // 多选
      multipleSelection: [],
      dialogVisible: false,
      // 批改
      dialogMarkingVisible: false,
      markLoading: false,
      subjectIds: [],
      answer: '',
      tempAnswer: {
        examRecordId: '',
        answer: '',
        subject: {
          id: '',
          examinationId: '',
          categoryId: 0,
          subjectName: '',
          type: 0,
          choicesType: 0,
          options: [
            { subjectChoicesId: '', optionName: 'A', optionContent: '' },
            { subjectChoicesId: '', optionName: 'B', optionContent: '' },
            { subjectChoicesId: '', optionName: 'C', optionContent: '' },
            { subjectChoicesId: '', optionName: 'D', optionContent: '' }
          ],
          answer: {
            subjectId: '',
            answer: '',
            answerType: '',
            score: '',
            type: 0
          },
          score: 5,
          analysis: '',
          level: 2
        }
      },
      labelPosition: 'right',
      currentRecord: {
        id: '',
        userName: '',
        deptName: ''
      },
      currentIndex: 1
    }
  },
  created () {
    this.getList()
    this.exam_record_btn_export = this.permissions['exam:examRecord:export']
  },
  computed: {
    ...mapGetters([
      'elements',
      'permissions'
    ])
  },
  methods: {
    getList () {
      this.listLoading = true
      fetchExamRecordList(this.listQuery).then(response => {
        this.list = response.data.list
        this.total = parseInt(response.data.total)
        setTimeout(() => {
          this.listLoading = false
        }, 500)
      })
    },
    handleFilter () {
      this.listQuery.pageNum = 1
      this.getList()
    },
    handleSizeChange (val) {
      this.listQuery.limit = val
      this.getList()
    },
    handleCurrentChange (val) {
      this.listQuery.pageNum = val
      this.getList()
    },
    handleSelectionChange (val) {
      this.multipleSelection = val
    },
    sortChange (column, prop, order) {
      this.listQuery.sort = column.prop
      this.listQuery.order = column.order
      this.getList()
    },
    // 点击选中
    handleRowClick (row) {
      this.$refs.multipleTable.toggleRowSelection(row, true)
    },
    resetTemp () {
      this.temp = {
        id: '',
        examinationName: ''
      }
    },
    handleExportExamRecord () {
      if (this.total > 0) {
        if (this.multipleSelection.length === 0) {
          this.$confirm('确定要导出全部成绩数据吗?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            exportObj({ idString: '' }).then(response => {
              // 导出Excel
              exportExcel(response)
            })
          }).catch(() => {})
        } else {
          let ids = ''
          for (let i = 0; i < this.multipleSelection.length; i++) {
            ids += this.multipleSelection[i].id + ','
          }
          exportObj({ idString: ids }).then(response => {
            // 导出Excel
            exportExcel(response)
          })
        }
      } else {
        this.$notify({
          title: '提示',
          message: '无数据导出',
          type: 'warn',
          duration: 2000
        })
      }
    },
    // 查看成绩详情
    handleUpdate (row) {
      this.temp = Object.assign({}, row)
      this.dialogVisible = true
    },
    // 批改
    handleMarking (row) {
      this.currentRecord = row
      // 加载答题信息
      getAnswerByRecordId(row.id, undefined, undefined).then(response => {
        if (response.data.data === null) {
          messageFail(this, '加载答题失败')
          return
        }
        this.tempAnswer = response.data.data
        // 获取考试的题目数量
        getSubjectIds(row.examinationId).then(response => {
          // 题目数
          this.subjectIds = response.data.data
        })
        this.dialogMarkingVisible = true
      }).catch(error => {
        console.log(error)
        messageFail(this, '加载答题失败')
        this.dialogMarkingVisible = false
      })
    },
    // 完成批改
    completeMarking () {
      this.saveCurrentAnswer()
      completeMarking({ id: this.currentRecord.id }).then(response => {
        if (response.data.data) {
          messageSuccess(this, '操作成功')
        }
        this.getList()
        this.dialogMarkingVisible = false
      })
    },
    // 上一题
    last () {
      this.saveCurrentAnswer()
      this.getAnswer(this.tempAnswer.subject.id, undefined, 1)
    },
    // 下一题
    next () {
      this.saveCurrentAnswer()
      this.getAnswer(this.tempAnswer.subject.id, undefined, 0)
    },
    // 跳到指定的题目
    toSubject (index, subjectId, subjectType) {
      this.currentIndex = index + 1
      this.saveCurrentAnswer()
      this.getAnswer(subjectId, subjectType, 2)
    },
    // 更新分数
    saveCurrentAnswer () {
      // 更新分数、批改状态
      const answer = {
        id: this.tempAnswer.id,
        score: this.tempAnswer.score,
        markStatus: 1
      }
      // 简答题，更新答题状态为正确
      if (this.tempAnswer.type === 1) {
        answer.answerType = 1
      }
      putAnswer(answer).then(response => {
        if (response.data) {
          console.log('保存成功.')
        }
      })
    },
    getAnswer (nextSubjectId, nextSubjectType, nextType) {
      this.markLoading = true
      getAnswerByRecordId(this.tempAnswer.examRecordId, nextSubjectId, nextSubjectType, nextType).then(response => {
        let msg = nextType === 0 ? '已经是最后一题了' : '已经是第一题了'
        if (response.data.data === null) {
          this.$notify({
            title: '提示',
            message: msg,
            type: 'warn',
            duration: 2000
          })
        } else {
          this.tempAnswer = response.data.data
        }
        setTimeout(() => {
          this.markLoading = false
        }, 500)
      }).catch(error => {
        console.log(error)
        messageFail(this, '加载答题失败')
        this.markLoading = false
      })
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
  /* 题目 */
  .subject-title {
    color: #333333;
    font-size: 18px;
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
    color: #666666;
    text-align: left;
  }
  .correct {
    color: #F56C6C;
  }
  .right {
    color: #67C23A;
  }
  .score-gray-box-title {
    text-align: center;
  }
  .subject-buttons {
    text-align: center;
  }
  .answer-number {
    padding: 12px;
    .number-btn {
      margin: 6px;
    }
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
