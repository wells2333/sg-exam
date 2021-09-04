<template>
  <div class="app-container">
    <el-row>
      <el-col :span="4">
        <div class="category-header">
          题目分类
        </div>
        <el-row>
          <el-button v-if="subject_category_btn_add" class="category-btn" icon="el-icon-plus" size="mini" type="primary" @click="handleAddSuperCategory">主分类</el-button>
          <el-button v-if="subject_category_btn_add" class="category-btn" icon="el-icon-plus" size="mini" type="primary" @click="handleAddCategory">子分类</el-button>
          <el-button v-if="subject_category_btn_edit" class="category-btn" icon="el-icon-edit" size="mini" type="primary" @click="handleUpdateCategory">{{ $t('table.edit') }}</el-button>
          <el-button v-if="subject_category_btn_del" class="category-btn" icon="el-icon-delete" size="mini" type="danger" @click="handleDeleteCategory">{{ $t('table.del') }}</el-button>
        </el-row>
        <el-row>
          <div class="tree-container">
            <el-tree
              :data="treeData"
              :filter-node-method="filterNode"
              :props="defaultProps"
              class="filter-tree"
              node-key="id"
              highlight-current
              accordion
              @node-click="getNodeData"
              @node-expand="nodeExpand"
              @node-collapse="nodeCollapse"
            />
          </div>
        </el-row>
      </el-col>
      <el-col :span="20">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>题目管理</span>
          </div>
          <div class="filter-container">
            <el-input v-model="listQuery.subjectName" placeholder="题目名称" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter"/>
            <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">{{ $t('table.search') }}</el-button>
            <el-button v-if="subject_bank_btn_add" class="filter-item" type="primary" style="margin-left: 10px;" icon="el-icon-check" @click="handleCreateSubject">{{ $t('table.add') }}</el-button>
            <el-button v-if="subject_bank_btn_del" class="filter-item" type="danger" icon="el-icon-delete" @click="handleDeletesSubject">{{ $t('table.del') }}</el-button>
            <el-button v-if="subject_bank_btn_import" class="filter-item" type="success" icon="el-icon-upload2" @click="handleImportSubject">{{ $t('table.import') }}</el-button>
            <el-button v-if="subject_bank_btn_export" class="filter-item" type="success" icon="el-icon-download" @click="handleExportSubject">{{ $t('table.export') }}</el-button>
          </div>
          <spinner-loading v-if="listLoading"/>
          <el-table
            :data="list"
            highlight-current-row
            style="width: 100%;"
            @selection-change="handleSubjectSelectionChange"
            @sort-change="sortSubjectChange">
            <el-table-column type="selection" width="55"/>
            <el-table-column :label="$t('table.subjectName')" min-width="120">
              <template slot-scope="scope">
                <span v-html="scope.row.subjectName"></span>
              </template>
            </el-table-column>
            <el-table-column :label="$t('table.subject.type')" width="120">
              <template slot-scope="scope">
                <el-tag :type="scope.row.type | subjectTypeTagFilter" effect="dark" size="small">{{ scope.row.type | subjectTypeFilter }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column :label="$t('table.subject.score')" width="120">
              <template slot-scope="scope">
                <span>{{ scope.row.score }}</span>
              </template>
            </el-table-column>
            <el-table-column :label="$t('table.modifier')" property="modifier" width="120">
              <template slot-scope="scope">
                <span>{{ scope.row.modifier }}</span>
              </template>
            </el-table-column>
            <el-table-column :label="$t('table.modifyDate')" property="updateTime" width="150">
              <template slot-scope="scope">
                <span>{{ scope.row.modifyDate | fmtDate('yyyy-MM-dd hh:mm') }}</span>
              </template>
            </el-table-column>
            <el-table-column :label="$t('table.actions')" class-name="status-col" width="100px">
              <template slot-scope="scope">
                <el-dropdown>
                  <span class="el-dropdown-link">
                    操作<i class="el-icon-caret-bottom el-icon--right"></i>
                  </span>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item v-if="subject_bank_btn_edit">
                      <a @click="handleUpdateSubject(scope.row)">
                        <span><i class="el-icon-edit"></i>{{ $t('table.edit') }}</span>
                      </a>
                    </el-dropdown-item>
                    <el-dropdown-item>
                      <a @click="handleViewSubject(scope.row)">
                        <span><i class="el-icon-view"></i>{{ $t('table.preview') }}</span>
                      </a>
                    </el-dropdown-item>
                    <el-dropdown-item v-if="subject_bank_btn_del">
                      <a @click="handleDeleteSubject(scope.row)">
                        <span><i class="el-icon-delete"></i>{{ $t('table.delete') }}</span>
                      </a>
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
               </template>
            </el-table-column>
          </el-table>
          <div class="pagination-container">
            <el-pagination v-show="total>0" :current-page="listQuery.pageNum" :page-sizes="[10,20,30, 50]" :page-size="listQuery.pageSize" :total="total" background layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange" @current-change="handleCurrentChange"/>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!--题目分类信息表单-->
    <el-dialog :title="textMap[categoryFormStatus]" :visible.sync="dialogCategoryFormVisible" width="30%" top="20vh">
      <el-form ref="dataCategoryForm" :rules="categoryRules" :model="tempCategory" :label-position="labelPosition" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('table.categoryName')" prop="categoryName">
              <el-input :placeholder="$t('table.categoryName')" v-model="tempCategory.categoryName"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('table.sort')" prop="categoryName">
              <el-input :placeholder="$t('table.sort')" v-model="tempCategory.sort"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('table.categoryDesc')" prop="categoryDesc">
              <el-input :placeholder="$t('table.categoryDesc')" :autosize="{ minRows: 4, maxRows: 6}" v-model="tempCategory.categoryDesc" type="textarea"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogCategoryFormVisible = false">{{ $t('table.cancel') }}</el-button>
        <el-button v-if="categoryFormStatus === 'create'" type="primary" @click="createCategory">{{ $t('table.save') }}</el-button>
        <el-button v-else type="primary" @click="updateCategory">{{ $t('table.save') }}</el-button>
      </div>
    </el-dialog>

    <!--题目信息表单-->
    <el-dialog :title="textMap[subjectFormStatus]" :visible.sync="dialogSubjectFormVisible" width="80%" top="5vh">
      <el-form ref="dataSubjectForm" :rules="subjectRules" :model="tempSubject" :label-position="labelPosition" label-width="100px">
        <el-tabs v-model="activeName" @tab-click="handleTabChange">
          <!-- 单选题 -->
          <el-tab-pane label="单选题" name="0" :disabled="tempSubject.type !== 0 && subjectFormStatus !== 'create'">
            <choices ref="choices" subjectInfo="tempSubject"/>
          </el-tab-pane>
          <!-- 多选题 -->
          <el-tab-pane label="多选题" name="3" :disabled="tempSubject.type !== 3 && subjectFormStatus !== 'create'">
            <multiple-choices ref="multipleChoices" subjectInfo="tempSubject"/>
          </el-tab-pane>
          <!-- 简答题 -->
          <el-tab-pane label="简答题" name="1" :disabled="tempSubject.type !== 1 && subjectFormStatus !== 'create'">
            <short-answer ref="shortAnswer" subjectInfo="tempSubject"/>
          </el-tab-pane>
        </el-tabs>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogSubjectFormVisible = false">{{ $t('table.cancel') }}</el-button>
        <el-button v-if="subjectFormStatus === 'create'" type="primary" @click="createSubjectData">{{ $t('table.save') }}</el-button>
        <el-button v-else type="primary" @click="updateSubjectData">{{ $t('table.save') }}</el-button>
        <el-button type="primary" @click="updateAndAddSubjectData">{{ $t('table.saveAndAdd') }}</el-button>
      </div>
    </el-dialog>

    <!-- 导入题目 -->
    <el-dialog :visible.sync="dialogImportVisible" :title="$t('table.import')">
      <el-row>
        <el-col :span="24">
          <el-upload
            drag
            :multiple="false"
            :auto-upload="true"
            :before-upload="beforeUploadSubjectUpload"
            :on-progress="handleUploadSubjectProgress"
            :on-success="handleUploadSubjectSuccess"
            :action="importUrl"
            :headers="headers"
            :data="params"
            style="text-align: center;">
            <i class="el-icon-upload"/>
            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            <div slot="tip" class="el-upload__tip">只能上传xlsx文件</div>
          </el-upload>
        </el-col>
      </el-row>
    </el-dialog>

    <!-- 预览题目 -->
    <el-dialog title="预览题目" :visible.sync="dialogViewVisible" width="60%" top="10vh">
      <div class="subject-title">
        <span class="subject-title-content" v-html="tempSubject.subjectName"/>
        <span class="subject-title-content">&nbsp;({{tempSubject.score}})分</span>
      </div>
      <ul v-if="tempSubject.type === 0 || tempSubject.type === 3" class="subject-options">
        <li class="subject-option" v-for="(option) in tempSubject.options" :key="option.id">
          <input class="toggle" type="checkbox">
          <label><span class="subject-option-prefix">{{option.optionName}}&nbsp;</span><span v-html="option.optionContent" class="subject-option-prefix"></span></label>
        </li>
      </ul>
      <ul v-if="tempSubject.type === 2" class="subject-options">
        <li class="subject-option">
          <input class="toggle" type="checkbox">
          <label><span class="subject-option-prefix">正确</span></label>
        </li>
        <li class="subject-option">
          <input class="toggle" type="checkbox">
          <label><span class="subject-option-prefix">错误</span></label>
        </li>
      </ul>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogViewVisible = false">{{ $t('table.confirm') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { fetchCategoryTree, getCategory, addCategory, delCategory, putCategory } from '@/api/exam/subjectCategory'
import { fetchSubjectList, addSubject, getSubject, putSubject, delSubject, delAllSubject, exportSubject } from '@/api/exam/subject'
import { mapGetters } from 'vuex'
import { getToken } from '@/utils/auth'
import { checkMultipleSelect, exportExcel, notifySuccess, isNotEmpty } from '@/utils/util'
import waves from '@/directive/waves'
import Tinymce from '@/components/Tinymce'
import SpinnerLoading from '@/components/SpinnerLoading'
import Choices from '@/components/Subjects/Choices'
import MultipleChoices from '@/components/Subjects/MultipleChoices'
import ShortAnswer from '@/components/Subjects/ShortAnswer'

export default {
  name: 'SubjectManagement',
  components: { Tinymce, SpinnerLoading, Choices, MultipleChoices, ShortAnswer },
  directives: {
    waves
  },
  data () {
    return {
      baseUrl: '/exam',
      list: null,
      total: null,
      formAdd: true,
      formStatus: '',
      showElement: false,
      typeOptions: ['0', '1'],
      listLoading: true,
      listQuery: {
        subjectName: undefined,
        categoryId: undefined,
        examinationId: undefined,
        sort: 'id',
        order: 'descending'
      },
      treeData: [],
      oExpandedKey: {
      },
      oTreeNodeChildren: {
      },
      defaultProps: {
        children: 'children',
        label: 'categoryName'
      },
      labelPosition: 'right',
      categoryFormStatus: '',
      subjectFormStatus: '',
      textMap: {
        update: '编辑',
        create: '新建'
      },
      category: undefined,
      tempCategory: {
        categoryName: undefined,
        categoryDesc: undefined,
        id: undefined,
        parentId: -1,
        sort: 30,
        type: 0
      },
      // 题目临时信息
      tempSubject: {
        id: '',
        examinationId: undefined,
        categoryId: undefined,
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
          score: ''
        },
        score: 5,
        analysis: '',
        level: 2
      },
      // 选择题类型
      tempChoiceType: [
        { type: 0, name: '单选题' },
        { type: 1, name: '简答题' },
        { type: 2, name: '判断题' },
        { type: 3, name: '多选题' }
      ],
      currentCategoryId: '',
      // 表单校验规则
      categoryRules: {
        categoryName: [{ required: true, message: '请输入分类名称', trigger: 'change' }]
      },
      // 表单校验规则
      subjectRules: {
        subjectName: [{ required: true, message: '请输入题目名称', trigger: 'change' }],
        score: [{ required: true, message: '请输入题目分值', trigger: 'change' }],
        answer: [{ required: true, message: '请输入答案', trigger: 'change' }],
        serialNumber: [{ required: true, message: '请输入题目序号', trigger: 'change' }]
      },
      // 按钮权限
      subject_category_btn_add: false,
      subject_category_btn_edit: false,
      subject_category_btn_del: false,
      subject_bank_btn: false,
      subject_bank_btn_add: false,
      subject_bank_btn_edit: false,
      subject_bank_btn_del: false,
      subject_bank_btn_import: false,
      subject_bank_btn_export: false,
      // 分类窗口状态
      dialogCategoryFormVisible: false,
      // 题目
      dialogSubjectFormVisible: false,
      // 导入窗口状态
      dialogImportVisible: false,
      // 导出窗口状态
      dialogExportVisible: false,
      // 预览窗口状态
      dialogViewVisible: false,
      // 选择的菜单
      multipleSelection: [],
      importUrl: '/api/exam/v1/subject/import',
      // 多选题目
      multipleSubjectSelection: [],
      uploading: false,
      uploadingSubject: false,
      percentageSubject: 0,
      params: {
        categoryId: undefined
      },
      headers: {
        Authorization: 'Bearer ' + getToken()
      },
      tinymce: {
        type: 1, // 类型 0：题目名称，1：选项A，2：选择B，3：选项C，4：选项D
        dialogTinymceVisible: false,
        tempValue: '',
        currentEdit: -1
      },
      activeName: '0',
      choicesContent: '',
      shortAnswerEditorContent: '',
      // 编辑对象
      tinymceEdit: {
        subjectName: -1,
        optionA: 0,
        optionB: 1,
        optionC: 2,
        optionD: 3,
        answer: 4,
        analysis: 5
      }
    }
  },
  created () {
    this.getList()
    this.handleSubjectManagement()
    this.subject_category_btn_add = this.permissions['exam:subject:category:add']
    this.subject_category_btn_edit = this.permissions['exam:subject:category:edit']
    this.subject_category_btn_del = this.permissions['exam:subject:category:del']
    this.subject_bank_btn = this.permissions['exam:subject:bank']
    this.subject_bank_btn_add = this.permissions['exam:subject:bank:add']
    this.subject_bank_btn_del = this.permissions['exam:subject:bank:del']
    this.subject_bank_btn_edit = this.permissions['exam:subject:bank:edit']
    this.subject_bank_btn_import = this.permissions['exam:subject:bank:import']
    this.subject_bank_btn_export = this.permissions['exam:subject:bank:export']
  },
  computed: {
    ...mapGetters([
      'elements',
      'permissions'
    ])
  },
  methods: {
    getList () {
      fetchCategoryTree(this.listQuery).then(response => {
        this.treeData = response.data
      })
    },
    filterNode (value, data) {
      if (!value) return true
      return data.label.indexOf(value) !== -1
    },
    nodeExpand (data) {
      const aChildren = data.children
      if (aChildren.length > 0) {
        this.oExpandedKey[data.id] = true
        this.oTreeNodeChildren[data.id] = aChildren
      }
      this.setExpandedKeys()
    },
    nodeCollapse (data) {
      this.oExpandedKey[data.id] = false
      // 如果有子节点
      this.treeRecursion(this.oTreeNodeChildren[data.id], (oNode) => {
        this.oExpandedKey[oNode.id] = false
      })
      this.setExpandedKeys()
    },
    setExpandedKeys () {
      const oTemp = this.oExpandedKey
      this.aExpandedKeys = []
      for (const sKey in oTemp) {
        if (oTemp[sKey]) {
          this.aExpandedKeys.push(parseInt(sKey))
        }
      }
    },
    treeRecursion (aChildren, fnCallback) {
      if (aChildren) {
        for (let i = 0; i < aChildren.length; ++i) {
          const oNode = aChildren[i]
          fnCallback && fnCallback(oNode)
          this.treeRecursion(oNode.children, fnCallback)
        }
      }
    },
    // 点击分类
    getNodeData (data) {
      // 获取分类信息
      getCategory(data.id).then(response => {
        this.category = response.data.data
      })
      this.currentCategoryId = data.id
      this.listQuery.categoryId = this.currentCategoryId
      this.params.categoryId = this.currentCategoryId
      // 获取题目信息
      this.handleSubjectManagement()
    },
    handleFilter () {
      this.listQuery.pageNum = 1
      this.handleSubjectManagement()
    },
    handleSizeChange (val) {
      this.listQuery.limit = val
      this.handleSubjectManagement()
    },
    handleCurrentChange (val) {
      this.listQuery.pageNum = val
      this.handleSubjectManagement()
    },
    handleSubjectSelectionChange (val) {
      this.multipleSubjectSelection = val
    },
    // 切换题目类型
    changeSubjectType (value) {
      console.log(value)
    },
    // 修改分类
    handleUpdateCategory () {
      if (this.currentCategoryId === '') {
        this.$message({
          message: '请选择分类',
          type: 'warning'
        })
        return
      }
      this.tempCategory = Object.assign({}, this.category) // copy obj
      this.categoryFormStatus = 'update'
      this.dialogCategoryFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataCategoryForm'].clearValidate()
      })
    },
    // 点击主分类按钮
    handleAddSuperCategory () {
      this.resetCategoryForm()
      this.tempCategory.parentId = -1
      this.categoryFormStatus = 'create'
      this.dialogCategoryFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataCategoryForm'].clearValidate()
      })
    },
    // 点击子分类按钮
    handleAddCategory () {
      if (this.currentCategoryId === '') {
        this.$message({
          message: '请选择分类',
          type: 'warning'
        })
        return
      }
      this.resetCategoryForm()
      this.categoryFormStatus = 'create'
      this.dialogCategoryFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataCategoryForm'].clearValidate()
      })
    },
    // 删除分类
    handleDeleteCategory () {
      if (this.currentCategoryId === '') {
        this.$message({
          message: '请选择要删除的记录',
          type: 'warning'
        })
        return
      }
      // TODO 检查是否有子分类
      // 检查分类下是否有题目
      fetchSubjectList(this.listQuery).then(response => {
        if (response.data.list.length > 0) {
          this.$message({
            message: '该分类下还有题目',
            type: 'warning'
          })
          return
        }
        this.$confirm('确定要删除吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          delCategory(this.currentCategoryId).then(() => {
            notifySuccess(this, '删除成功')
            this.getList()
            this.resetForm()
            this.onCancel()
          })
        })
      })
    },
    // 新建分类
    createCategory () {
      this.$refs['dataCategoryForm'].validate((valid) => {
        if (valid) {
          addCategory(this.tempCategory).then(() => {
            this.dialogCategoryFormVisible = false
            this.getList()
            notifySuccess(this, '创建成功')
          })
        }
      })
    },
    // 更新分类
    updateCategory () {
      this.$refs['dataCategoryForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.tempCategory)
          putCategory(tempData).then(() => {
            this.dialogCategoryFormVisible = false
            this.getList()
            notifySuccess(this, '更新成功')
          })
        }
      })
    },
    onCancel () {
      this.formStatus = ''
    },
    resetCategoryForm () {
      this.tempCategory = {
        categoryName: undefined,
        categoryDesc: undefined,
        id: undefined,
        parentId: this.currentCategoryId,
        sort: 30
      }
    },
    resetTempSubject (score) {
      this.tempSubject = {
        id: '',
        examinationId: undefined,
        categoryId: undefined,
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
          score: ''
        },
        score: score,
        analysis: '',
        level: 2
      }
      this.$refs['choices'].resetTempSubject(score)
      this.$refs['shortAnswer'].resetTempSubject(score)
      this.$refs['multipleChoices'].resetTempSubject(score)
    },
    // 加载题目
    handleSubjectManagement () {
      this.listLoading = true
      fetchSubjectList(this.listQuery).then(response => {
        if (isNotEmpty(response.data) && response.data.list.length > 0) {
          for (let i = 0; i < response.data.list.length; i++) {
            const subject = response.data.list[i]
            subject.type = parseInt(subject.type)
            subject.level = parseInt(subject.level)
          }
        }
        this.list = response.data.list
        this.total = parseInt(response.data.total)
        this.listLoading = false
      }).catch(() => {
        this.listLoading = false
      })
    },
    // 点击新建题目
    handleCreateSubject () {
      if (this.currentCategoryId === '') {
        this.$message({
          message: '请选择分类',
          type: 'warning'
        })
        return
      }
      this.$router.push({
        path: `/exam/subjects/detail/${this.currentCategoryId}-undefined-0-1`
      })
    },
    // 修改题目
    handleUpdateSubject (row) {
      this.$router.push({
        path: `/exam/subjects/detail/${this.currentCategoryId}-${row.id}-${row.type}-1`
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
          this.dialogSubjectFormVisible = false
          this.handleSubjectManagement()
          notifySuccess(this, '删除成功')
        })
      })
    },
    // 保存题目
    createSubjectData () {
      const ref = this.getSubjectRef()
      if (ref.validate()) {
        let subjectInfo = ref.getSubjectInfo()
        subjectInfo.categoryId = this.currentCategoryId
        addSubject(subjectInfo).then(() => {
          this.dialogSubjectFormVisible = false
          notifySuccess(this, '创建成功')
        })
      }
    },
    // 更新题目
    updateSubjectData () {
      const ref = this.getSubjectRef()
      if (ref.validate()) {
        const subjectInfo = ref.getSubjectInfo()
        putSubject(subjectInfo).then(() => {
          this.dialogSubjectFormVisible = false
          notifySuccess(this, '更新成功')
        })
      }
    },
    // 更新并添加题目
    updateAndAddSubjectData () {
      const ref = this.getSubjectRef()
      if (ref.validate()) {
        const subjectInfo = ref.getSubjectInfo()
        if (isNotEmpty(this.currentCategoryId)) {
          subjectInfo.categoryId = this.currentCategoryId
        }
        // 创建
        if (this.subjectFormStatus === 'create') {
          addSubject(subjectInfo).then(() => {
            this.resetTempSubject(subjectInfo.score)
            this.subjectFormStatus = 'create'
            ref.clearValidate()
            notifySuccess(this, '创建成功')
          })
        } else {
          // 更新
          putSubject(subjectInfo).then(() => {
            this.resetTempSubject(subjectInfo.score)
            this.subjectFormStatus = 'create'
            // 绑定分类ID
            this.tempSubject.categoryId = this.currentCategoryId
            ref.clearValidate()
            notifySuccess(this, '更新成功')
          })
        }
      }
    },
    // 批量删除
    handleDeletesSubject () {
      if (checkMultipleSelect(this.multipleSubjectSelection, this)) {
        let ids = []
        for (let i = 0; i < this.multipleSubjectSelection.length; i++) {
          ids.push(this.multipleSubjectSelection[i].id)
        }
        this.$confirm('确定要删除吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          delAllSubject(ids).then(() => {
            this.handleSubjectManagement()
            notifySuccess(this, '删除成功')
          })
        }).catch(() => {})
      }
    },
    // 查看题目
    handleViewSubject (row) {
      // 加载题目信息
      getSubject(row.id, { type: row.type }).then(response => {
        this.tempSubject = response.data.data
        this.dialogViewVisible = true
      })
    },
    // 点击排序按钮
    sortSubjectChange (column, prop, order) {
      this.listQuery.sort = column.prop
      this.listQuery.order = column.order
      this.handleSubjectManagement()
    },
    // 导出
    handleExportSubject () {
      // 没选择题目，导出所有
      if (this.multipleSubjectSelection.length === 0 || this.currentCategoryId === '') {
        this.$confirm('是否导出所有题目?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'success'
        }).then(() => {
          exportSubject([], '', this.currentCategoryId).then(response => {
            // 导出Excel
            exportExcel(response)
          })
        }).catch(() => {

        })
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
          exportSubject(ids, '', '').then(response => {
            // 导出Excel
            exportExcel(response)
          })
        }).catch(() => {})
      }
    },
    // 导入
    handleImportSubject () {
      if (this.currentCategoryId === '') {
        this.$message({
          message: '请选择分类',
          type: 'warning'
        })
        return
      }
      this.dialogImportVisible = true
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
    handleUploadSubjectProgress (event, file, fileList) {
      this.uploadingSubject = true
      this.percentageSubject = parseInt(file.percentage.toFixed(0))
    },
    // 上传成功
    handleUploadSubjectSuccess () {
      this.dialogImportVisible = false
      this.handleSubjectManagement()
      notifySuccess(this, '导入成功')
      this.uploadingSubject = false
    },
    // 切换题型
    handleTabChange (tab, event) {
      this.tempSubject.type = parseInt(tab.name)
      // 更新组件里的题目信息
      this.updateComponentSubjectInfo()
    },
    updateCurrentTag (type) {
      this.activeName = type + ''
    },
    resetActiveName () {
      // 重置选项卡至单选题
      this.activeName = '0'
    },
    // 更新组件里的题目信息
    updateComponentSubjectInfo () {
      // 单选题
      const ref = this.getSubjectRef()
      if (isNotEmpty(ref)) {
        ref.setSubjectInfo(this.tempSubject)
      }
    },
    getSubjectRef () {
      let ref
      switch (this.activeName) {
        case '0':
          ref = this.$refs['choices']
          break
        case '1':
          ref = this.$refs['shortAnswer']
          break
        case '3':
          ref = this.$refs['multipleChoices']
          break
      }
      return ref
    }
  }
}
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
  .category-header {
    margin: 12px;
  }

  .tree-container {
    padding-top: 10px;
  }

  .category-btn {
    margin: 5px;
    padding: 6px 13px;
  }

  .filter-tree {
    overflow: hidden;
  }

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

      .toggle + label {
        background-image: url('data:image/svg+xml;utf8,%3Csvg%20xmlns%3D%22http%3A//www.w3.org/2000/svg%22%20width%3D%2240%22%20height%3D%2240%22%20viewBox%3D%22-10%20-18%20100%20135%22%3E%3Ccircle%20cx%3D%2250%22%20cy%3D%2250%22%20r%3D%2250%22%20fill%3D%22none%22%20stroke%3D%22%23ededed%22%20stroke-width%3D%223%22/%3E%3C/svg%3E');
        background-repeat: no-repeat;
        background-position: center left;
        background-size: 30px;
      }

      .toggle:checked + label {
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
