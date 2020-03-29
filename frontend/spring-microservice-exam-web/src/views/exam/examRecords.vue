<template>
  <div class="my-content-container">
    <el-row>
      <el-col :span="20" :offset="2">
        <el-table
          v-loading="listLoading"
          :key="tableKey"
          :data="examRecodeList"
          :default-sort="{ prop: 'id', order: 'descending' }"
          @cell-dblclick="handleDetail"
          highlight-current-row
          style="width: 100%;">
          <el-table-column label="考试名称" align="center">
            <template slot-scope="scope">
              <span :title="scope.row.examinationName">{{ scope.row.examinationName }}</span>
            </template>
          </el-table-column>
          <el-table-column label="考试类型" min-width="90" align="center">
            <template slot-scope="scope">
              <span>{{ scope.row.type | examTypeFilter }}</span>
            </template>
          </el-table-column>
          <el-table-column label="考试时间" sortable prop="start_time" min-width="90" align="center">
            <template slot-scope="scope">
              <span>{{ scope.row.startTime | fmtDate('yyyy-MM-dd hh:mm') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="状态" min-width="90" align="center">
            <template slot-scope="scope">
              <el-tag :type="scope.row.submitStatus | simpleTagStatusFilter(3)">{{ scope.row.submitStatus | submitStatusFilter }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="成绩" sortable prop="score" align="center" width="120px">
            <template slot-scope="scope">
              <span>{{ scope.row.score }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center">
            <template slot-scope="scope">
              <el-button type="success" size="mini" @click="handleDetail(scope.row)" :disabled="scope.row.submitStatus !== 3">成绩详情</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-row style="text-align: center; margin-bottom: 50px;">
          <el-col :span="24">
            <el-button type="default" @click="scrollList" :loading="listLoading" style="margin-top:20px; margin-bottom: 100px;">加载更多</el-button>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import { mapState } from 'vuex'
import { fetchList } from '@/api/exam/examRecord'
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
        pageNum: 1,
        pageSize: 10,
        courseId: '',
        sort: 'id',
        order: 'descending',
        userId: ''
      },
      tempScore: {
        score: '',
        correctNumber: '',
        inCorrectNumber: ''
      }
    }
  },
  computed: {
    // 获取用户信息
    ...mapState({
      userInfo: state => state.user.userInfo
    })
  },
  created () {
    this.listQuery.userId = this.userInfo.id
    this.getList()
  },
  methods: {
    // 加载考试记录
    getList () {
      fetchList(this.listQuery).then(response => {
        const { total, isLastPage, list } = response.data
        this.updateList(list)
        this.total = total
        this.isLastPage = isLastPage
        this.listLoading = false
      }).catch(() => {
        messageWarn(this, '暂无更多数据！')
        this.listLoading = false
      })
    },
    scrollList () {
      if (this.isLastPage) {
        messageWarn(this, '暂无更多数据！')
        return
      }
      if (this.listLoading) {
        messageWarn(this, '正在拼命加载！')
        return
      }
      this.listLoading = true
      setTimeout(() => {
        this.listQuery.pageNum++
        fetchList(this.listQuery).then(response => {
          const { total, isLastPage, list } = response.data
          this.updateList(list)
          this.total = total
          this.isLastPage = isLastPage
          this.listLoading = false
        }).catch(() => {
          messageWarn(this, '加载数据失败！')
          this.listLoading = false
        })
      }, 1000)
    },
    updateList (list) {
      if (list === undefined || list.length === 0) {
        return list
      }
      if (this.examRecodeList.length === 0) {
        this.examRecodeList = list
        return
      }
      list.forEach(item => {
        let exist = false
        for (let i = 0; i < this.examRecodeList.length; i++) {
          if (this.examRecodeList[i].id === item.id) {
            exist = true
          }
        }
        if (!exist) {
          this.examRecodeList.push(item)
        }
      })
    },
    // 查看成绩详情
    handleDetail (row) {
      store.dispatch('SetIncorrectRecord', { id: row.id }).then(() => {
        this.$router.push({ name: 'incorrect' })
      }).catch((error) => {
        console.error(error)
      })
    }
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
    margin-top: 50px;
  }
  .score-gray-box-title {
    text-align: center;
  }

</style>
