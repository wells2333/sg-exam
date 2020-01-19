<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input :placeholder="$t('table.subjectName')" v-model="subject.listQuery.subjectName" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilterSubject"/>
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilterSubject">{{ $t('table.search') }}</el-button>
      <el-button v-if="exam_btn_subject_add" class="filter-item" icon="el-icon-check" plain @click="handleCreateSubject">{{ $t('table.add') }}</el-button>
      <el-button v-if="exam_btn_subject_add" class="filter-item" icon="el-icon-check" plain @click="handleCreateSubjectFromSubjectBank">{{ $t('table.addFromSubjectBank') }}</el-button>
      <el-button v-if="exam_btn_subject_import" class="filter-item" icon="el-icon-upload2" plain @click="handleImportSubject">{{ $t('table.import') }}</el-button>
      <el-button v-if="exam_btn_subject_export" class="filter-item" icon="el-icon-download" plain @click="handleExportSubject">{{ $t('table.export') }}</el-button>
    </div>
    <spinner-loading v-if="subject.listLoading"/>
    <el-table
      :data="subject.list"
      highlight-current-row
      style="width: 100%;"
      @selection-change="handleSubjectSelectionChange"
      @cell-dblclick="handleUpdateSubject"
      @sort-change="sortSubjectChange">
      <el-table-column type="selection" width="55"/>
      <el-table-column :label="$t('table.subjectName')" min-width="120">
        <template slot-scope="scope">
          <span>{{ scope.row.subjectName | simpleStrFilter }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.subject.type')" width="120">
        <template slot-scope="scope">
          <el-tag type="success">{{ scope.row.type | subjectTypeFilter }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.subject.score')" property="score" width="120">
        <template slot-scope="scope">
          <span>{{ scope.row.score }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.subject.modifyDate')" property="updateTime" width="150">
        <template slot-scope="scope">
          <span>{{ scope.row.modifyDate | fmtDate('yyyy-MM-dd hh:mm') }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.subject.modifier')" property="modifier" width="120">
        <template slot-scope="scope">
          <span>{{ scope.row.modifier }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.actions')" class-name="status-col" width="300px">
        <template slot-scope="scope">
          <el-button type="text" @click="handleViewSubject(scope.row)" icon="el-icon-view">{{ $t('table.view') }}</el-button>
          <el-button v-if="exam_btn_subject" type="text" @click="handleUpdateSubject(scope.row)" icon="el-icon-edit">{{ $t('table.edit') }}</el-button>
          <el-button v-if="exam_btn_del" type="text" @click="handleDeleteSubject(scope.row)" icon="el-icon-delete">{{ $t('table.delete') }}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-container">
      <el-pagination v-show="subject.total>0" :current-page="subject.listQuery.pageNum" :page-sizes="[10,20,30,50]" :page-size="subject.listQuery.pageSize" :total="subject.total" background layout="total, sizes, prev, pager, next, jumper" @size-change="handleSubjectSizeChange" @current-change="handleSubjectCurrentChange"/>
    </div>

    <!-- 导入题目 -->
    <el-dialog :visible.sync="dialogImportVisible" :title="$t('table.import')">
      <el-row>
        <el-col :span="24">
          <el-upload
            drag
            :multiple="false"
            :auto-upload="true"
            :show-file-list="true"
            :before-upload="beforeUploadSubjectUpload"
            :on-progress="handleUploadSubjectProgress"
            :on-success="handleUploadSubjectSuccess"
            :action="importUrl"
            :headers="headers"
            :data="params"
            style="text-align: center;">
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            <div slot="tip" class="el-upload__tip">只能上传xlsx文件</div>
          </el-upload>
        </el-col>
      </el-row>
    </el-dialog>

    <!-- 题库列表 -->
    <el-dialog title="选择题目" :visible.sync="category.dialogVisible" width="80%" top="10vh">
      <el-row>
        <el-col :span="4">
          <el-card class="tree-box-card" style="margin-right: 5px;">
            <div slot="header">
              <span>题目分类</span>
            </div>
            <el-row>
              <div class="tree-container">
                <el-tree
                  :data="category.treeData"
                  :props="category.defaultProps"
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
              <span>选择题目</span>
            </div>
            <el-table
              v-loading="category.listLoading"
              :data="category.list"
              :default-sort="{ prop: 'id', order: 'ascending' }"
              highlight-current-row
              style="width: 100%;"
              @row-click = "handleSingleSubjectSelection"
              @current-change="handleSingleSubjectCurrentChange">
              <el-table-column align="center" width="55" label="" >
                <template slot-scope="scope">
                  <el-radio :label="scope.$index" v-model="category.tempRadio" @change.native="handleSingleSubjectSelectionChange(scope.$index, scope.row)">&nbsp;</el-radio>
                </template>
              </el-table-column>
              <el-table-column :label="$t('table.subjectName')" sortable prop="subject_name" property="subjectName" min-width="120">
                <template slot-scope="scope">
                  <span>{{ scope.row.subjectName }}</span>
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
              <el-table-column :label="$t('table.subject.level')">
                <template slot-scope="scope">
                  <el-rate v-model="scope.row.level" :max="4"/>
                </template>
              </el-table-column>
            </el-table>
            <div class="pagination-container">
              <el-pagination v-show="category.total>0" :current-page="category.listQuery.pageNum" :page-sizes="[10,20,30, 50]" :page-size="category.listQuery.pageSize" :total="category.total" background layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange" @current-change="handleCurrentChange"/>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <div slot="footer" class="dialog-footer">
        <el-button @click="category.dialogVisible = false">{{ $t('table.cancel') }}</el-button>
        <el-button type="primary" @click="handleSelectSubject">{{ $t('table.confirm') }}</el-button>
      </div>
    </el-dialog>

    <!-- 查看题目 -->
    <el-dialog title="查看题目" :visible.sync="dialogViewVisible" width="60%" top="10vh">
      <div class="subject-title">
        <span class="subject-title-content" v-html="tempSubject.subjectName"/>
        <span class="subject-title-content">&nbsp;({{tempSubject.score}})分</span>
      </div>
      <ul v-if="tempSubject.type === 0 || tempSubject.type === 3" class="subject-options">
        <li class="subject-option" v-for="(option) in tempSubject.options">
          <input class="toggle" type="checkbox">
          <label><span class="subject-option-prefix">{{option.optionName}}&nbsp;</span><span v-html="option.optionContent" class="subject-option-prefix"></span></label>
        </li>
      </ul>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogViewVisible = false">{{ $t('table.confirm') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

import { fetchSubjectListById } from '@/api/exam/exam'
import { getSubject, delSubject, exportSubject } from '@/api/exam/subject'
import { fetchCategoryTree } from '@/api/exam/subjectCategory'
import { getToken } from '@/utils/auth'
import waves from '@/directive/waves'
import { mapGetters } from 'vuex'
import { checkMultipleSelect, notifySuccess, messageSuccess, exportExcel, isNotEmpty } from '@/utils/util'
import SpinnerLoading from '@/components/SpinnerLoading'

export default {
  name: 'ExamSubjectsManagement',
  components: { SpinnerLoading },
  directives: {
    waves
  },
  data () {
    return {
      headers: {
        Authorization: 'Bearer ' + getToken()
      },
      params: {
        busiType: '1'
      },
      tableKey: 0,
      list: null,
      total: null,
      listLoading: true,
      listQuery: {
        pageNum: 1,
        pageSize: 10,
        sort: 'id',
        order: 'descending'
      },
      // 导入题目的url
      importUrl: '/api/exam/v1/subject/import',
      // 题目
      subject: {
        listQuery: {
          pageNum: 1,
          pageSize: 10,
          examinationId: '',
          categoryId: '',
          sort: 'id',
          order: 'ascending'
        },
        list: null,
        total: null,
        listLoading: true,
        categoryId: ''
      },

      exam_btn_add: false,
      exam_btn_edit: false,
      exam_btn_del: false,
      exam_btn_subject: false,
      exam_btn_subject_add: false,
      exam_btn_subject_del: false,
      exam_btn_subject_import: false,
      exam_btn_subject_export: false,
      // 导入弹窗状态
      dialogImportVisible: false,
      // 预览弹窗状态
      dialogViewVisible: false,
      uploadingSubject: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '新建'
      },
      // 题目分类数据
      category: {
        dialogVisible: false,
        // 题目列表查询参数
        listQuery: {
          subjectName: undefined,
          categoryId: '',
          sort: 'id',
          order: 'ascending'
        },
        // 题目列表数据
        list: [],
        // 分类树数据
        treeData: [],
        // 题目分类数据
        defaultProps: {
          children: 'children',
          label: 'categoryName'
        },
        // 列表加载状态
        listLoading: false,
        tempRadio: ''
      },
      // 多选题目
      multipleSubjectSelection: [],
      // 单选题目
      singleSubjectSelection: [],
      percentageSubject: '',
      tempSubject: {
        id: null,
        examinationId: null,
        categoryId: null,
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
          subjectId: null,
          answer: '',
          answerType: '',
          score: '',
          type: 0
        },
        score: 5,
        analysis: '',
        level: 2
      }
    }
  },
  created () {
    this.getList()
    this.exam_btn_add = this.permissions['exam:exam:add']
    this.exam_btn_edit = this.permissions['exam:exam:edit']
    this.exam_btn_del = this.permissions['exam:exam:del']
    this.exam_btn_subject = this.permissions['exam:exam:subject']
    this.exam_btn_subject_add = this.permissions['exam:exam:subject:add']
    this.exam_btn_subject_del = this.permissions['exam:exam:subject:del']
    this.exam_btn_subject_import = this.permissions['exam:exam:subject:import']
    this.exam_btn_subject_export = this.permissions['exam:exam:subject:export']
  },
  computed: {
    ...mapGetters([
      'elements',
      'permissions'
    ])
  },
  methods: {
    handleSizeChange (val) {
      this.listQuery.limit = val
      this.getList()
    },
    handleCurrentChange (val) {
      this.listQuery.pageNum = val
      this.getList()
    },
    handleSubjectSizeChange (val) {
      this.subject.listQuery.limit = val
      this.getList()
    },
    handleSubjectCurrentChange (val) {
      this.subject.listQuery.pageNum = val
      this.getList()
    },
    sortSubjectChange (column, prop, order) {
      this.subject.listQuery.sort = column.prop
      this.subject.listQuery.order = column.order
      this.getList()
    },
    handleFilterSubject () {
      this.subject.listQuery.pageNum = 1
      this.getList()
    },
    // 题库里选择题目
    handleSingleSubjectSelectionChange (index, row) {
      this.category.singleSubjectSelection = row
    },
    // 点击行时选择题目
    handleSingleSubjectSelection (row) {
      this.category.tempRadio = this.category.list.indexOf(row)
    },
    // 表格变化
    handleSingleSubjectCurrentChange (row) {
      this.category.singleSubjectSelection = row
    },
    // 选择题目
    handleSelectSubject () {
      // 加载题目信息
      getSubject(this.category.singleSubjectSelection.id, { type: this.category.singleSubjectSelection.type }).then(response => {
        this.tempSubject = response.data.data
        // 隐藏弹框
        this.category.dialogVisible = false
        // 清空题目ID
        this.tempSubject.id = ''
        // 清空分类ID
        this.tempSubject.categoryId = ''
        // 清空选项ID
        this.tempSubject.options.forEach(option => {
          option.id = ''
        })
        // 绑定考试ID
        this.tempSubject.examinationId = this.subject.examinationId
        this.dialogStatus = 'create'
        // 切换到对应的题型选项卡
        this.updateCurrentTag(this.tempSubject.type)
        // 更新组件里的题目信息
        setTimeout(() => {
          this.updateComponentSubjectInfo()
        }, 200)
      })
    },
    getList() {
      this.subject.listLoading = true
      this.subject.listQuery.examinationId = this.$route.params.id
      fetchSubjectListById(this.subject.listQuery).then(response => {
        if (response.data.list.length > 0) {
          response.data.list.map(subject => {
            subject.type = parseInt(subject.type)
            subject.level = parseInt(subject.level)
          })
          this.subject.list = response.data.list
        } else {
          this.subject.list = []
        }
        this.subject.total = parseInt(response.data.total)
        setTimeout(() => {
          this.subject.listLoading = false
        }, 500)
      })
    },
    // 新建题目
    handleCreateSubject () {
      this.$router.push({
        path: `/exam/${this.subject.listQuery.examinationId}/subjects/undefined/0`,
      })
    },
    // 从题库新增
    handleCreateSubjectFromSubjectBank () {
      // 加载分类树
      fetchCategoryTree(this.category.listQuery).then(response => {
        this.category.treeData = response.data
      })
      this.category.dialogVisible = true
      // 加载题目列表
    },
    // 导入
    handleImportSubject () {
      this.dialogImportVisible = true
    },
    // 导出
    handleExportSubject () {
      // 没选择题目，导出所有
      if (this.multipleSubjectSelection.length === 0) {
        this.$confirm('是否导出所有题目?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'success'
        }).then(() => {
          exportSubject([], this.subject.listQuery.examinationId).then(response => {
            // 导出Excel
            exportExcel(response)
          })
        }).catch(() => {})
      } else {
        // 导出选中
        this.$confirm('是否导出选中的题目?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'success'
        }).then(() => {
          let ids = []
          for (let i = 0; i < this.multipleSubjectSelection.length; i++) {
            ids.push(this.multipleSubjectSelection[i].id)
          }
          exportSubject(ids, '').then(response => {
            // 导出Excel
            exportExcel(response)
          })
        }).catch(() => {})
      }
    },
    handleSubjectSelectionChange (val) {
      this.multipleSubjectSelection = val
    },
    // 修改题目
    handleUpdateSubject (row) {
      let examinationId = this.subject.listQuery.examinationId
      this.$router.push({
        path: `/exam/${examinationId}/subjects/${row.id}/${row.type}`,
      })
    },
    // 查看题目
    handleViewSubject (row) {
      // 加载题目信息
      getSubject(row.id, { type: row.type }).then(response => {
        this.tempSubject = response.data.data
        this.dialogViewVisible = true
      })
    },
    // 删除题目
    handleDeleteSubject (row) {
      this.$confirm('确定要删除吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delSubject(row.id, { type: row.type }).then(() => {
          this.getList()
          notifySuccess(this, '删除成功')
        })
      }).catch(() => {})
    },
    // 点击分类
    getNodeData (data) {
      // 获取分类ID
      this.category.listQuery.categoryId = data.id
      // 获取题目信息
      this.getList()
    },
    handleUploadSubjectProgress (event, file, fileList) {
      this.uploadingSubject = true
      this.percentageSubject = parseInt(file.percentage.toFixed(0))
    },
    // 上传成功
    handleUploadSubjectSuccess () {
      this.dialogImportVisible = false
      this.getList()
      notifySuccess(this, '导入成功')
      this.uploadingSubject = false
    },
    // 上传前
    beforeUploadSubjectUpload (file) {
      const isExcel = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
      const isLt10M = file.size / 1024 / 1024 < 10
      if (!isExcel) {
        this.$message.error('上传附件只能是 excel 格式!')
      }
      if (!isLt10M) {
        this.$message.error('上传附件大小不能超过 10MB!')
      }
      return isExcel && isLt10M
    },
  }
}
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
  /* 题目 */
  .subject-title {
    font-size: 18px;
    line-height: 22px;
    .subject-title-number {
      display: inline-block;
      line-height: 22px;
    }
    .subject-title-content {
      display: inline-block;
    }
  }
  .subject-options {
    margin: 0;
    padding: 0;
    list-style: none;
    > li {
      position: relative;
      font-size: 24px;
      .toggle {
        opacity: 0;
        text-align: center;
        width: 35px;
        /* auto, since non-WebKit browsers doesn't support input styling */
        height: auto;
        position: absolute;
        top: 0;
        bottom: 0;
        margin: auto 0;
        border: none;
        /* Mobile Safari */
        -webkit-appearance: none;
        appearance: none;
      }
      .toggle+label {
        background-image: url('data:image/svg+xml;utf8,%3Csvg%20xmlns%3D%22http%3A//www.w3.org/2000/svg%22%20width%3D%2240%22%20height%3D%2240%22%20viewBox%3D%22-10%20-18%20100%20135%22%3E%3Ccircle%20cx%3D%2250%22%20cy%3D%2250%22%20r%3D%2250%22%20fill%3D%22none%22%20stroke%3D%22%23ededed%22%20stroke-width%3D%223%22/%3E%3C/svg%3E');
        background-repeat: no-repeat;
        background-position: center left;
        background-size: 30px;
      }
      .toggle:checked+label {
        background-size: 30px;
        background-image: url('data:image/svg+xml;utf8,%3Csvg%20xmlns%3D%22http%3A//www.w3.org/2000/svg%22%20width%3D%2240%22%20height%3D%2240%22%20viewBox%3D%22-10%20-18%20100%20135%22%3E%3Ccircle%20cx%3D%2250%22%20cy%3D%2250%22%20r%3D%2250%22%20fill%3D%22none%22%20stroke%3D%22%23bddad5%22%20stroke-width%3D%223%22/%3E%3Cpath%20fill%3D%22%235dc2af%22%20d%3D%22M72%2025L42%2071%2027%2056l-4%204%2020%2020%2034-52z%22/%3E%3C/svg%3E');
      }
      label {
        word-break: break-all;
        padding: 10px 10px 10px 45px;
        display: block;
        line-height: 1.0;
        transition: color 0.4s;
      }
      /* 选项名称 */
      .subject-option-prefix {
        font-size: 16px;
        display: inline-block
      }
    }
  }
</style>
