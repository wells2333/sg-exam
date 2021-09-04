<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.examinationName" placeholder="考试名称" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter"/>
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">{{ $t('table.search') }}</el-button>
      <el-button v-if="exam_record_btn_export" class="filter-item" type="success" icon="el-icon-download" @click="handleExportExamRecord">{{ $t('table.export') }}</el-button>
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
      <el-table-column :label="$t('table.examRecord.examTime')" min-width="100">
        <template slot-scope="scope">
          <span>{{ scope.row.startTime | fmtDate('MM-dd hh:mm') }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.examRecord.submitStatus')" min-width="90">
        <template slot-scope="scope">
          <el-tag :type="scope.row.submitStatus | simpleTagStatusFilter(3)" effect="dark" size="small">{{ scope.row.submitStatus | submitStatusFilter }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.actions')" class-name="status-col" width="100px">
        <template slot-scope="scope">
          <el-dropdown>
            <span class="el-dropdown-link">
              操作<i class="el-icon-caret-bottom el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>
                <a @click="handleDetails(scope.row)">
                  <span><i class="el-icon-edit"></i>{{ $t('table.examRecord.details') }}</span>
                </a>
              </el-dropdown-item>
              <el-dropdown-item>
                <a @click="handleMarking(scope.row)">
                  <span><i class="el-icon-check"></i>{{ $t('table.examRecord.marking') }}</span>
                </a>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
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
            <h4>开始时间: <span type="success">{{temp.startTime | fmtDate('MM-dd hh:mm')}}</span></h4>
            <h4>结束时间: <span type="success">{{temp.endTime | fmtDate('MM-dd hh:mm')}}</span></h4>
          </div>
        </el-col>
      </el-row>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">{{ $t('table.cancel') }}</el-button>
        <el-button type="primary" @click="dialogVisible = false">{{ $t('table.confirm') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { fetchExamRecordList, exportObj } from '@/api/exam/examRecord'
import waves from '@/directive/waves'
import { mapGetters } from 'vuex'
import { exportExcel } from '@/utils/util'
import SpinnerLoading from '@/components/SpinnerLoading'

export default {
  name: 'ExamRecordManagement',
  components: {
    SpinnerLoading
  },
  directives: {
    waves
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
      markLoading: false,
      answer: '',
      labelPosition: 'right'
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
      }).catch(() => {
        this.listLoading = false
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
            exportObj([]).then(response => {
              // 导出Excel
              exportExcel(response)
            })
          }).catch(() => {})
        } else {
          let ids = []
          for (let i = 0; i < this.multipleSelection.length; i++) {
            ids.push(this.multipleSelection[i].id)
          }
          exportObj(ids).then(response => {
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
    handleDetails (row) {
      this.$router.push({
        path: `/exam/score/detail/${row.id}`
      })
    },
    // 批改
    handleMarking (row) {
      this.$router.push({
        path: `/exam/mark/${row.examinationId}-${row.id}`
      })
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" rel="stylesheet/scss" scoped>
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
  .subject-option {
    padding-bottom: 10px;
    padding-left: 10px;
  }
  .score {
    margin: 20px;
  }
  .subject-content {
    height: 300px;
    border-radius: 4px;
    padding: 12px 0 12px 22px;
    margin-bottom: 12px;
    position: relative;
    color: #666666;
    text-align: left;
    box-shadow: 0 2px 8px 0 rgba(0,0,0,.1);
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
  .user-info {

  }
  .answer {
    padding: 6px;
  }
</style>
