<template>
  <el-row>
    <el-col :span="4">
      <el-card class="tree-box-card" style="margin-right: 5px;">
        <div slot="header">
          <span>题目分类</span>
        </div>
        <el-row>
          <div class="tree-container">
            <el-tree
              :data="treeData"
              :props="defaultProps"
              class="filter-tree"
              node-key="id"
              highlight-current
              accordion
              @node-click="getNodeData"
            />
          </div>
        </el-row>
      </el-card>
    </el-col>

    <el-col :span="20">
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>题目列表</span>
        </div>
        <el-table
          v-loading="listLoading"
          :data="list"
          :default-sort="{ prop: 'id', order: 'ascending' }"
          highlight-current-row
          style="width: 100%;">
          <el-table-column :label="$t('table.subjectName')" prop="subject_name" property="subjectName" min-width="120">
            <template slot-scope="scope">
              <span>{{ scope.row.subjectName | simpleStrFilter }}</span>
            </template>
          </el-table-column>
          <el-table-column :label="$t('table.subject.type')">
            <template slot-scope="scope">
              <el-tag type="success">{{ scope.row.type | subjectTypeFilter }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column :label="$t('table.subject.score')">
            <template slot-scope="scope">
              <span>{{ scope.row.score }}</span>
            </template>
          </el-table-column>
          <el-table-column :label="$t('table.actions')" width="100">
            <template slot-scope="scope">
              <el-button type="text" @click="handleSelectSubject(scope.row)" icon="el-icon-check">{{ $t('table.select') }}</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="pagination-container">
          <el-pagination v-show="total>0" :current-page="listQuery.pageNum" :page-sizes="[10,20,30, 50]" :page-size="listQuery.pageSize" :total="total" background layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange" @current-change="handleCurrentChange"/>
        </div>
      </el-card>
    </el-col>
  </el-row>
</template>

<script>
import Tinymce from '@/components/Tinymce'
import { fetchCategoryTree } from '@/api/exam/subjectCategory'
import { fetchSubjectListById } from '@/api/exam/exam'
import { getSubject } from '@/api/exam/subject'

export default {
  name: 'CategorySubjects',
  components: {
    Tinymce
  },
  data () {
    return {
      listLoading: false,
      list: [],
      total: 0,
      dialogVisible: false,
      // 题目列表查询参数
      listQuery: {
        subjectName: undefined,
        categoryId: undefined,
        examinationId: undefined,
        sort: 'id',
        order: 'ascending'
      },
      // 分类树数据
      treeData: [],
      // 题目分类数据
      defaultProps: {
        children: 'children',
        label: 'categoryName'
      },
      tempSubject: undefined
    }
  },
  created () {
    this.handleCreateSubjectFromSubjectBank()
  },
  methods: {
    getList () {
      this.listLoading = true
      fetchSubjectListById(this.listQuery).then(response => {
        if (response.data.list.length > 0) {
          response.data.list.map(subject => {
            subject.type = parseInt(subject.type)
            subject.level = parseInt(subject.level)
          })
          this.list = response.data.list
        } else {
          this.list = []
        }
        this.total = parseInt(response.data.total)
        setTimeout(() => {
          this.listLoading = false
        }, 500)
      })
    },
    handleSizeChange (val) {
      this.limit = val
      this.handleSubjectManagement()
    },
    handleCurrentChange (val) {
      this.pageNum = val
      this.handleSubjectManagement()
    },
    // 从题库新增
    handleCreateSubjectFromSubjectBank () {
      // 加载分类树
      fetchCategoryTree(this.listQuery).then(response => {
        this.treeData = response.data
      })
      this.dialogVisible = true
      // 加载题目列表
    },
    // 点击分类
    getNodeData (data) {
      // 获取分类ID
      this.listQuery.categoryId = data.id
      // 获取题目信息
      this.getList()
    },
    // 选择题目
    handleSelectSubject (selected) {
      // 加载题目信息
      getSubject(selected.id, { type: selected.type }).then(response => {
        this.tempSubject = response.data.data
        this.$emit('selected', this.tempSubject)
      })
    },
    getSubjectInfo () {
      return this.tempSubject
    }
  }
}
</script>

<style lang="scss" scoped>
  @import "../../../styles/subject.scss";
</style>
