<template>
  <div class="app-container">
    <el-row class="knowledge-msg">
      <el-col :span="24" style="color: black;">
        <h1>知识库</h1>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="20" :offset="2">
        <el-table
          v-loading="listLoading"
          :key="tableKey"
          :data="knowledgeList"
          :default-sort="{ prop: 'id', order: 'descending' }"
          highlight-current-row
          style="width: 100%;">
          <el-table-column label="名称" min-width="90" align="center">
            <template slot-scope="scope">
              <span>{{ scope.row.knowledgeName }}</span>
            </template>
          </el-table-column>
          <el-table-column label="附件" min-width="90" align="center">
            <template slot-scope="scope">
               <span>{{scope.row.attachName}}</span>
            </template>
          </el-table-column>
          <el-table-column label="大小" min-width="90" align="center">
            <template slot-scope="scope">
              <span>{{ scope.row.attachSize | attachSizeFilter }}</span>
            </template>
          </el-table-column>
          <el-table-column label="上传时间" min-width="90" align="center">
            <template slot-scope="scope">
              <span>{{ scope.row.createDate }}</span>
            </template>
          </el-table-column>
          <el-table-column label="上传者" min-width="90" align="center">
            <template slot-scope="scope">
              <span>{{ scope.row.creator }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="90" align="center">
            <template slot-scope="scope">
              <el-button type="primary" size="mini" @click="handleDownload(scope.row)">下载</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="pagination-container">
          <el-pagination v-show="total>0" :current-page="listQuery.pageNum" :page-sizes="[10,20,30, 50]" :page-size="listQuery.pageSize" :total="total" background layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange" @current-change="handleCurrentChange"/>
        </div>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import { fetchKnowledgeList } from '@/api/exam/knowledge'
export default {
  filters: {
    attachSizeFilter (attachSize) {
      attachSize = parseInt(attachSize)
      // kb
      attachSize = attachSize / 1024
      // 少于100kb，单位用kb
      if (attachSize < 100) {
        return attachSize + 'kb'
      } else {
        // mb
        attachSize = attachSize / 1024
      }
      attachSize = attachSize + ''
      if (attachSize.length > 5) {
        attachSize = attachSize.substring(0, 4)
      }
      attachSize = attachSize + 'M'
      return attachSize
    }
  },
  data () {
    return {
      knowledgeList: [],
      total: 0,
      listLoading: true,
      tableKey: 0,
      listQuery: {
        pageNum: 1,
        pageSize: 10,
        sort: 'id',
        order: 'descending'
      }
    }
  },
  created () {
    this.getKnowledgeList()
  },
  methods: {
    // 加载课程列表
    getKnowledgeList () {
      fetchKnowledgeList().then(response => {
        this.knowledgeList = response.data.list
        this.total = response.data.total
        this.listLoading = false
      }).catch(() => {
        this.$notify({
          title: '失败',
          message: '加载知识库失败',
          type: 'error',
          duration: 2000
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
    // 下载
    handleDownload (row) {
      if (row.attachmentId !== undefined && row.attachmentId !== '') {
        window.location.href = '/user-service/v1/attachment/download?id=' + row.attachmentId
      }
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" rel="stylesheet/scss" scoped>
</style>
