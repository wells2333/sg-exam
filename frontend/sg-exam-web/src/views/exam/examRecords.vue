<template>
  <div class="my-content-container">
    <el-row>
      <el-col :span="22" :offset="1">
        <el-table
          border
          :key="tableKey"
          :data="examRecodeList"
          :default-sort="{ prop: 'id', order: 'descending' }"
          @cell-dblclick="handleDetail"
          highlight-current-row
          height="560"
          style="width: 100%;">
          <el-table-column :label="$t('exam.examinationName')" align="center" width="300">
            <template slot-scope="scope">
              <span :title="scope.row.examinationName">{{ scope.row.examinationName | simpleStrFilter }}</span>
            </template>
          </el-table-column>
          <el-table-column :label="$t('exam.examinationType')" min-width="90" align="center">
            <template slot-scope="scope">
              <span>{{ transformExaminationType(scope.row.type) }}</span>
            </template>
          </el-table-column>
          <el-table-column :label="$t('exam.examinationTime')" sortable prop="start_time" min-width="90" align="center">
            <template slot-scope="scope">
              <span>{{ scope.row.startTime | fmtDate('yyyy-MM-dd hh:mm') }}</span>
            </template>
          </el-table-column>
          <el-table-column :label="$t('exam.status')" min-width="90" align="center">
            <template slot-scope="scope">
              <el-tag :type="transformStatusTag(scope.row.submitStatus, 3)" size="mini">{{ transformSubmitStatus(scope.row.submitStatus)}}</el-tag>
            </template>
          </el-table-column>
          <el-table-column :label="$t('exam.score')" prop="score" align="center" width="120px">
            <template slot-scope="scope">
              <span>{{ scope.row.score }}</span>
            </template>
          </el-table-column>
          <el-table-column :label="$t('operation')" align="center">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="handleDetail(scope.row)" :disabled="scope.row.submitStatus !== 3">{{$t('exam.scoreDetail')}}</el-button>
              <el-button type="text" size="small" @click="handleScore(scope.row)">{{$t('exam.scoreRank')}}</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-row class="list-pagination" v-show="examRecodeList && examRecodeList.length > 0">
            <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="listQuery.page"
              :page-sizes="[20, 50, 100]"
              :page-size="20"
              layout="total, sizes, prev, pager, next, jumper"
              :total="total">
            </el-pagination>
        </el-row>
      </el-col>
    </el-row>
    <el-dialog :title="$t('exam.scoreRank')" :visible.sync="dialogTableVisible" width="35%">
      <el-table :data="gridData">
        <el-table-column :label="$t('exam.exams.rank')" width="120">
          <template slot-scope="scope">
            <div style="display: flex; align-items: center;">
              <div style="width: 26px; height: 26px; margin-right: 10px; cursor: pointer;">
                <i class="iconfont icon-guanjun rank-num" v-if="scope.row.rankNum === 1" style="color: #f24f09"></i>
                <i class="iconfont icon-yajun1 rank-num" v-if="scope.row.rankNum === 2" style="color: #eeb173"></i>
                <i class="iconfont icon-jijun1 rank-num" v-if="scope.row.rankNum === 3" style="color: #707070"></i>
              </div>
              <span>{{ scope.row.rankNum }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column :label="$t('exam.exams.account')" width="260">
          <template slot-scope="scope">
            <div style="display: flex; align-items: center; cursor: pointer;">
              <img  v-if="scope.row.avatarUrl" class="account" :src="scope.row.avatarUrl">
              <i class="iconfont icon-user account account-icon" v-else></i>
              <span >{{ scope.row.name }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column property="score" :label="$t('exam.exams.score')"></el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>
<script>
import { mapState } from 'vuex'
import { fetchList } from '@/api/exam/examRecord'
import { examRankInfo } from '@/api/exam/answer'

import store from '@/store'
import { messageWarn } from '@/utils/util'

export default {
  data () {
    return {
      examRecodeList: [],
      isLastPage: false,
      total: 0,
      listLoading: true,
      tableKey: 0,
      listQuery: {
        page: 1,
        pageSize: 20,
        courseId: '',
        sort: 'id',
        order: 'descending',
        userId: ''
      },
      tempScore: {
        score: '',
        correctNumber: '',
        inCorrectNumber: ''
      },
      dialogTableVisible: false,
      gridData: []
    }
  },
  computed: {
    ...mapState({
      userInfo: state => state.user.userInfo
    })
  },
  created () {
    this.listQuery.userId = this.userInfo.id
    this.getList()
  },
  methods: {
    getList () {
      fetchList(this.listQuery).then(response => {
        const { total, isLastPage, list } = response.data.result
        this.examRecodeList = list
        this.total = total
        this.isLastPage = isLastPage
        this.listLoading = false
      }).catch(() => {
        messageWarn(this, this.$t('load.noMoreData'))
        this.listLoading = false
      })
    },
    handleDetail (row) {
      if (row.submitStatus === 3) {
        store.dispatch('SetIncorrectRecord', { id: row.id }).then(() => {
          this.$router.push({ name: 'incorrect', query: {recordId: row.id} })
        }).catch((error) => {
          console.error(error)
        })
      }
    },
    handleSizeChange(val) {
      this.listQuery.pageSize = val
      if (this.listLoading) {
        messageWarn(this, this.$t('load.loading'))
        return
      }
      this.getList()
    },
    handleCurrentChange(val) {
      this.listQuery.page = val
      if (this.listLoading) {
        messageWarn(this, this.$t('load.loading'))
        return
      }
      this.getList()
    },
    handleScore (row) {
      const { examinationId } = row
      if (examinationId) {
        examRankInfo({
          examinationId
        }).then(response => {
          console.log(response, 'OK')
          const { result } = response.data
          this.gridData = result
          this.dialogTableVisible = true
        }).catch((error) => {
          console.error(error)
        })
      }
    },
    transformExaminationType (type) {
      const examType = {
        0: this.$t('status.examination'),
        1: this.$t('status.practice'),
        2: this.$t('status.questionnaire'),
        3: this.$t('status.interview')
      }
      return examType[type]
    },
    transformStatusTag (status, expectStatus) {
      return status === expectStatus ? 'success' : 'warning'
    },
    transformSubmitStatus (status) {
      const typeMap = {
        0: this.$t('status.todoMark'),
        1: this.$t('status.todoMark'),
        2: this.$t('status.markComplete'),
        3: this.$t('status.complete')
      }
      return typeMap[status]
    }
  },
  beforeRouteLeave(to, from, next) {
    const { name, query } = to;
    if (name === "start-exam-a" || name === "start-exam-b") {
      this.$router.push({path: 'exams', query})
    }
    next()
  }
}
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
  .my-content-container {
    margin-top: 2rem;
    margin-left: 20px;
    margin-right: 20px;
  }
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
    margin-top: 50px;
  }
  .score-gray-box-title {
    text-align: center;
  }
  .list-pagination {
    display: flex;
    justify-content: flex-end;
    margin: 10px 0 50px 0;
  }
  .rank-num {
    font-size: 22px;
  }
  .account {
    margin-right: 10px;
    width: 30px;
    height: 30px;
     border-radius: 50%;
  }
  .account-icon {
    font-size: 30px;
    color: #5a5a5a;
    display: flex;
    align-items: center;
  }
</style>
