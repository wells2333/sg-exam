<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input :placeholder="$t('table.examinationName')" v-model="listQuery.examinationName" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter"/>
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">{{ $t('table.search') }}</el-button>
      <el-button v-if="exam_btn_add" class="filter-item" style="margin-left: 10px;" icon="el-icon-check" plain @click="handleCreate">{{ $t('table.add') }}</el-button>
      <el-button v-if="exam_btn_del" class="filter-item" icon="el-icon-delete" plain @click="handleDeletes">{{ $t('table.del') }}</el-button>
    </div>
    <spinner-loading v-if="listLoading"/>
    <!--考试列表-->
    <el-table
      :key="tableKey"
      :data="list"
      :default-sort="{ prop: 'id', order: 'descending' }"
      highlight-current-row
      style="width: 100%;"
      @cell-dblclick="handleUpdate"
      @selection-change="handleSelectionChange"
      @sort-change="sortChange">
      <el-table-column type="selection" width="55"/>
      <el-table-column :label="$t('table.examinationName')">
        <template slot-scope="scope">
          <span>{{ scope.row.examinationName }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.type')">
        <template slot-scope="scope">
          <span>{{ scope.row.type | typeFilter }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.course')">
        <template slot-scope="scope">
          <span>{{ scope.row | courseFilter }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.startTime')">
        <template slot-scope="scope">
          <span>{{ scope.row.startTime | timeFilter }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.endTime')">
        <template slot-scope="scope">
          <span>{{ scope.row.endTime | timeFilter }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.totalScore')">
        <template slot-scope="scope">
          <span>{{ scope.row.totalScore }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.status')">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status | statusTypeFilter">{{ scope.row.status | statusFilter }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.actions')" class-name="status-col" width="300">
        <template slot-scope="scope">
          <el-button v-if="exam_btn_edit" type="text" @click="handleUpdate(scope.row)" icon="el-icon-edit">{{ $t('table.edit') }}</el-button>
          <el-button v-if="exam_btn_edit && scope.row.status == 1" type="text" @click="handlePublic(scope.row, 0)" icon="el-icon-check">{{ $t('table.public') }}</el-button>
          <el-button v-if="exam_btn_edit && scope.row.status == 0" type="text" @click="handlePublic(scope.row, 1)" icon="el-icon-remove-outline">{{ $t('table.retrieve') }}</el-button>
          <el-button v-if="exam_btn_subject" type="text" @click="handleSubjectManagement(scope.row)" icon="el-icon-document">{{ $t('table.subjectManagement') }}</el-button>
          <el-button v-if="exam_btn_del" type="text" @click="handleDelete(scope.row)" icon="el-icon-delete">{{ $t('table.delete') }}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-container">
      <el-pagination v-show="total>0" :current-page="listQuery.pageNum" :page-sizes="[10,20,30, 50]" :page-size="listQuery.pageSize" :total="total" background layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange" @current-change="handleCurrentChange"/>
    </div>

    <!--考试信息表单-->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" width="70%" top="10vh">
      <el-form ref="dataForm" :rules="rules" :model="temp" :label-position="labelPosition" label-width="100px">
        <el-row>
          <el-col :span="18">
            <el-row>
              <el-col :span="24">
                <el-form-item :label="$t('table.examinationName')" prop="examinationName">
                  <el-input v-model="temp.examinationName" :readonly="temp.readonly"/>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item :label="$t('table.totalScore')" prop="totalScore">
                  <el-input v-model="temp.totalScore"/>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('table.course')" prop="course.id">
                  <el-input v-model="temp.course.courseName" @focus="selectCourse"/>
                  <input v-model="temp.course.id" type="hidden">
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item :label="$t('table.startTime')" prop="startTime">
                  <el-date-picker v-model="temp.startTime" :placeholder="$t('table.startTime')" type="datetime" format="yyyy-MM-dd HH:mm" value-format="timestamp"/>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('table.endTime')" prop="endTime">
                  <el-date-picker v-model="temp.endTime" :placeholder="$t('table.endTime')" type="datetime" format="yyyy-MM-dd HH:mm" value-format="timestamp"/>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item :label="$t('table.examinationType')" prop="type">
                  <el-radio-group v-model="temp.type">
                    <el-radio :label="0">正式考试</el-radio>
                    <el-radio :label="1">模拟考试</el-radio>
                    <el-radio :label="2">在线练习</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('table.status')">
                  <el-radio-group v-model="temp.status">
                    <el-radio :label="0">已发布</el-radio>
                    <el-radio :label="1">未发布</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item :label="$t('table.attention')">
                  <el-input :autosize="{ minRows: 3, maxRows: 5}" :placeholder="$t('table.attention')" v-model="temp.attention" type="textarea"/>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item :label="$t('table.remark')">
                  <el-input :autosize="{ minRows: 3, maxRows: 5}" v-model="temp.remark" type="textarea" placeholder="备注"/>
                </el-form-item>
              </el-col>
            </el-row>
          </el-col>
          <el-col :span="5" :offset="1">
            <el-upload
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload"
              :action="sysConfig.uploadUrl"
              :headers="headers"
              :data="params"
              class="avatar-uploader">
              <img v-if="temp.avatarId !== null" :src="avatar" class="avatar">
              <i v-else class="el-icon-plus avatar-uploader-icon"/>
            </el-upload>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">{{ $t('table.cancel') }}</el-button>
        <el-button v-if="dialogStatus === 'create'" type="primary" @click="createData">{{ $t('table.confirm') }}</el-button>
        <el-button v-else type="primary" @click="updateData">{{ $t('table.confirm') }}</el-button>
      </div>
    </el-dialog>

    <!--课程选择弹窗-->
    <el-dialog :visible.sync="dialogCourseVisible" :title="$t('table.course')">
      <el-table v-loading="course.listLoading" :data="course.list" @row-dblclick="selectedCourse">
        <el-table-column :label="$t('table.courseName')" property="courseName" width="150">
          <template slot-scope="scope">
            <span>{{ scope.row.courseName }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('table.college')" property="college" width="200">
          <template slot-scope="scope">
            <span>{{ scope.row.college }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('table.major')" property="major">
          <template slot-scope="scope">
            <span>{{ scope.row.major }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('table.teacher')" property="teacher">'
          <template slot-scope="scope">
            <span>{{ scope.row.teacher }}</span>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <!--题目管理列表-->
    <el-dialog :visible.sync="dialogSubjectVisible" :title="$t('table.subjectManagement')" width="80%" top="5vh">
      <div class="filter-container">
        <el-input :placeholder="$t('table.subjectName')" v-model="subject.listQuery.subjectName" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilterSubject"/>
        <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilterSubject">{{ $t('table.search') }}</el-button>
        <el-button v-if="exam_btn_subject_add" class="filter-item" icon="el-icon-check" plain @click="handleCreateSubject">{{ $t('table.add') }}</el-button>
        <el-button v-if="exam_btn_subject_add" class="filter-item" icon="el-icon-check" plain @click="handleCreateSubjectFromSubjectBank">{{ $t('table.addFromSubjectBank') }}</el-button>
        <el-button v-if="exam_btn_subject_import" class="filter-item" icon="el-icon-upload2" plain @click="handleImportSubject">{{ $t('table.import') }}</el-button>
        <el-button v-if="exam_btn_subject_export" class="filter-item" icon="el-icon-download" plain @click="handleExportSubject">{{ $t('table.export') }}</el-button>
      </div>
      <el-table
        :data="subject.list"
        v-loading="subject.listLoading"
        highlight-current-row
        style="width: 100%;"
        @selection-change="handleSubjectSelectionChange"
        @cell-dblclick="handleUpdateSubject"
        @sort-change="sortSubjectChange">
        <el-table-column type="selection" width="55"/>
        <el-table-column :label="$t('table.subjectName')" min-width="120">
          <template slot-scope="scope">
            <span>{{ scope.row.subjectName | subjectNameFilter }}</span>
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
        <el-table-column :label="$t('table.subject.level')" property="level" width="120">
          <template slot-scope="scope">
            <el-rate v-model="scope.row.level" :max="4"/>
          </template>
        </el-table-column>
        <el-table-column :label="$t('table.actions')" class-name="status-col" width="300px">
          <template slot-scope="scope">
            <el-button v-if="exam_btn_subject" type="text" @click="handleUpdateSubject(scope.row)" icon="el-icon-edit">{{ $t('table.edit') }}</el-button>
            <el-button v-if="exam_btn_del" type="text" @click="handleDeleteSubject(scope.row)" icon="el-icon-delete">{{ $t('table.delete') }}</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination-container">
        <el-pagination v-show="subject.total>0" :current-page="subject.listQuery.pageNum" :page-sizes="[10,20,30, 50]" :page-size="subject.listQuery.pageSize" :total="subject.total" background layout="total, sizes, prev, pager, next, jumper" @size-change="handleSubjectSizeChange" @current-change="handleSubjectCurrentChange"/>
      </div>
    </el-dialog>

    <!--题目信息表单-->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogSubjectFormVisible" width="80%" top="5vh">
      <el-tabs v-model="activeName" @tab-click="handleTabChange">
        <!-- 单选题 -->
        <el-tab-pane label="单选题" name="0" :disabled="tempSubject.type !== 0 && dialogStatus !== 'create'">
          <choices ref="choices" subjectInfo="tempSubject"></choices>
        </el-tab-pane>
        <!-- 多选题 -->
        <el-tab-pane label="多选题" name="3" :disabled="tempSubject.type !== 3 && dialogStatus !== 'create'">
          <multiple-choices ref="multipleChoices" subjectInfo="tempSubject"></multiple-choices>
        </el-tab-pane>
        <!-- 简答题 -->
        <el-tab-pane label="简答题" name="1" :disabled="tempSubject.type !== 1 && dialogStatus !== 'create'">
          <short-answer ref="shortAnswer" subjectInfo="tempSubject"></short-answer>
        </el-tab-pane>
      </el-tabs>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogSubjectFormVisible = false">{{ $t('table.cancel') }}</el-button>
        <el-button v-if="dialogStatus === 'create'" type="primary" @click="createSubjectData">{{ $t('table.save') }}</el-button>
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
  </div>
</template>

<script>
import { fetchList, fetchSubjectListById, addObj, putObj, delObj, delAllObj } from '@/api/exam/exam'
import { fetchCourseList } from '@/api/exam/course'
import { fetchSubjectList, getSubject, addSubject, putSubject, delSubject, delAllSubject, exportSubject } from '@/api/exam/subject'
import { fetchCategoryTree } from '@/api/exam/subjectCategory'
import waves from '@/directive/waves'
import { mapGetters, mapState } from 'vuex'
import { getToken } from '@/utils/auth'
import { checkMultipleSelect, exportExcel, isNotEmpty, notifySuccess, notifyFail, messageSuccess, formatDate } from '@/utils/util'
import { delAttachment, preview } from '@/api/admin/attachment'
import Tinymce from '@/components/Tinymce'
import SpinnerLoading from '@/components/SpinnerLoading'
import Choices from '@/components/Subjects/Choices'
import MultipleChoices from '@/components/Subjects/MultipleChoices'
import ShortAnswer from '@/components/Subjects/ShortAnswer'

export default {
  name: 'ExamManagement',
  directives: {
    waves
  },
  components: { Tinymce, SpinnerLoading, Choices, MultipleChoices, ShortAnswer },
  filters: {
    statusTypeFilter (status) {
      const statusMap = {
        0: 'success',
        1: 'warning'
      }
      return statusMap[status]
    },
    statusFilter (status) {
      return status === 0 ? '已发布' : '未发布'
    },
    typeFilter (type) {
      const typeMap = {
        0: '正式考试',
        1: '模拟考试',
        2: '在线练习'
      }
      return typeMap[type]
    },
    subjectTypeFilter (type) {
      const typeMap = {
        0: '单选题',
        1: '简答题',
        3: '多选题'
      }
      return typeMap[type]
    },
    subjectNameFilter (subjectName) {
      if (subjectName.length > 50) {
        return subjectName.substring(0, 50) + '...'
      }
      return subjectName
    },
    courseFilter (row) {
      if (isNotEmpty(row.course) && isNotEmpty(row.course.courseName)) {
        return row.course.courseName
      }
      return ''
    },
    timeFilter (time) {
      return formatDate(new Date(time), 'yyyy-MM-dd hh:mm')
    }
  },
  data () {
    return {
      headers: {
        Authorization: 'Bearer ' + getToken()
      },
      params: {
        busiType: '1'
      },
      baseUrl: '/exam',
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
      // 课程
      course: {
        listQuery: {
          pageNum: 1,
          pageSize: 10,
          sort: 'id',
          order: 'descending'
        },
        list: null,
        total: null,
        listLoading: true
      },
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
        examinationId: '',
        categoryId: ''
      },
      // 考试临时信息
      temp: {
        id: '',
        examinationName: '',
        type: 0,
        attention: '',
        startTime: '',
        endTime: '',
        duration: '',
        totalScore: '',
        totalSubject: '0',
        status: 0,
        avatarId: null,
        collegeId: '',
        majorId: '',
        course: {
          id: '',
          courseName: ''
        },
        remark: ''
      },
      avatar: null,
      // 题目临时信息
      tempSubject: {
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
          score: ''
        },
        score: 5,
        analysis: '',
        level: 2
      },
      tempSubjectTypeList: [
        { name: '单选题', type: 0 },
        { name: '简答题', type: 1 },
        { name: '多选题', type: 3 }
      ],
      // 选择题类型
      tempChoiceType: [
        { type: 0, name: '单选题' },
        { type: 1, name: '简答题' },
        { type: 2, name: '判断题' },
        { type: 3, name: '多选题' }
      ],
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '新建'
      },
      // 校验规则
      rules: {
        examinationName: [{ required: true, message: '请输入考试名称', trigger: 'change' }],
        courseId: [{ required: true, message: '请输入考试所属课程', trigger: 'change' }],
        startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
        endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }],
        totalScore: [{ required: true, message: '请输入总分', trigger: 'change' }]
      },
      downloadLoading: false,
      labelPosition: 'right',
      // 按钮权限
      exam_btn_add: false,
      exam_btn_edit: false,
      exam_btn_del: false,
      exam_btn_subject: false,
      exam_btn_subject_import: false,
      exam_btn_subject_export: false,
      dialogCourseVisible: false,
      courseData: [],
      dialogSubjectVisible: false,
      subjectData: [],
      dialogSubjectFormVisible: false,
      // 题目类型
      subjectTypeData: [
        { id: 0, subjectTypeName: '单选题' },
        { id: 1, subjectTypeName: '简答题' },
        { id: 3, subjectTypeName: '多选题' }
      ],
      // 多选考试
      multipleSelection: [],
      // 多选题目
      multipleSubjectSelection: [],
      // 单选题目
      singleSubjectSelection: [],
      // 导入弹窗状态
      dialogImportVisible: false,
      // 导入题目的url
      importUrl: '/api/exam/v1/subject/import',
      uploading: false,
      percentage: 0,
      uploadingSubject: false,
      percentageSubject: 0,
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
      activeName: '0',
      choicesContent: '',
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
    // 加载考试列表
    this.getList()
    // 获取课程列表
    fetchCourseList(this.course.listQuery).then(response => {
      this.course.list = [{ id: '', courseName: '请选择' }].concat(response.data.list)
      this.course.total = parseInt(response.data.total)
      this.course.listLoading = false
    })
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
    ]),
    ...mapState({
      sysConfig: state => state.sysConfig.sysConfig
    })
  },
  methods: {
    // 加载考试列表
    getList () {
      this.listLoading = true
      fetchList(this.listQuery).then(response => {
        this.list = response.data.list
        this.total = parseInt(response.data.total)
        // Just to simulate the time of the request
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
    handleSubjectSizeChange (val) {
      this.subject.listQuery.limit = val
      this.handleSubjectManagement()
    },
    handleSubjectCurrentChange (val) {
      this.subject.listQuery.pageNum = val
      this.handleSubjectManagement()
    },
    handleModifyStatus (row, status) {
      row.status = status
      putObj(row).then(() => {
        this.dialogFormVisible = false
        messageSuccess(this, '操作成功')
      })
    },
    handleSelectionChange (val) {
      this.multipleSelection = val
    },
    handleSubjectSelectionChange (val) {
      this.multipleSubjectSelection = val
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
        // 状态为新建
        this.dialogStatus = 'create'
        // 显示题目信息表单
        this.dialogSubjectFormVisible = true
        // 切换到对应的题型选项卡
        this.updateCurrentTag(this.tempSubject.type)
        // 更新组件里的题目信息
        setTimeout(() => {
          this.updateComponentSubjectInfo()
        }, 200)
      })
    },
    // 排序事件
    sortChange (column, prop, order) {
      this.listQuery.sort = column.prop
      this.listQuery.order = column.order
      this.getList()
    },
    sortSubjectChange (column, prop, order) {
      this.subject.listQuery.sort = column.prop
      this.subject.listQuery.order = column.order
      this.handleSubjectManagement()
    },
    // 点击分类
    getNodeData (data) {
      // 获取分类ID
      this.category.listQuery.categoryId = data.id
      // 获取题目信息
      this.handleSubjectBankManagement()
    },
    resetTemp () {
      this.temp = {
        id: '',
        examinationName: '',
        type: 0,
        attention: '',
        startTime: '',
        endTime: '',
        duration: '',
        totalScore: '',
        status: 0,
        avatar: '',
        collegeId: '',
        majorId: '',
        course: {
          id: '',
          courseName: ''
        },
        remark: ''
      }
    },
    handleCreate () {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.temp.totalScore = parseInt(this.temp.totalScore)
          addObj(this.temp).then(() => {
            this.list.unshift(this.temp)
            this.dialogFormVisible = false
            this.getList()
            notifySuccess(this, '创建成功')
          })
        }
      })
    },
    handleUpdate (row) {
      this.temp = Object.assign({}, row) // copy obj
      if (!isNotEmpty(this.temp.course)) {
        this.temp.course = {
          id: '',
          courseName: ''
        }
      }
      // 获取图片的预览地址
      if (isNotEmpty(this.temp.avatarId)) {
        preview(this.temp.avatarId).then(response => {
          this.avatar = response.data.data
        })
      }
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          putObj(tempData).then(() => {
            for (const v of this.list) {
              if (v.id === this.temp.id) {
                const index = this.list.indexOf(v)
                this.list.splice(index, 1, this.temp)
                break
              }
            }
            this.dialogFormVisible = false
            this.getList()
            notifySuccess(this, '更新成功')
          })
        }
      })
    },
    // 删除
    handleDelete (row) {
      this.$confirm('确定要删除吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delObj(row.id).then(() => {
          this.dialogFormVisible = false
          this.getList()
          notifySuccess(this, '删除成功')
        })
        const index = this.list.indexOf(row)
        this.list.splice(index, 1)
      }).catch(() => {})
    },
    // 批量删除
    handleDeletes () {
      if (checkMultipleSelect(this.multipleSelection, this)) {
        let ids = []
        for (let i = 0; i < this.multipleSelection.length; i++) {
          ids.push(this.multipleSelection[i].id)
        }
        this.$confirm('确定要删除吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          delAllObj({ ids: ids }).then(() => {
            this.getList()
            notifySuccess(this, '删除成功')
          })
        }).catch(() => {})
      }
    },
    // 选择课程
    selectCourse () {
      this.course.listLoading = true
      fetchCourseList(this.course.listQuery).then(response => {
        this.course.list = response.data.list
        this.course.total = parseInt(response.data.total)
        this.course.listLoading = false
      })
      this.dialogCourseVisible = true
    },
    // 双击选择课程
    selectedCourse (row) {
      this.temp.course.id = row.id
      this.temp.course.courseName = row.courseName
      this.dialogCourseVisible = false
    },
    // 加载题目
    handleSubjectManagement (row) {
      this.subject.listLoading = true
      // 保存当前题目列表的考试ID
      if (row !== undefined) {
        this.subject.examinationId = row.id
        this.subject.listQuery.examinationId = row.id
        this.params.examinationId = row.id
      }
      fetchSubjectListById(this.subject.listQuery).then(response => {
        if (response.data.list.length > 0) {
          for (let i = 0; i < response.data.list.length; i++) {
            const subject = response.data.list[i]
            subject.type = parseInt(subject.type)
            subject.level = parseInt(subject.level)
          }
        }
        this.subject.list = response.data.list
        this.subject.total = parseInt(response.data.total)
        setTimeout(() => {
          this.subject.listLoading = false
        }, 500)
      })
      this.dialogSubjectVisible = true
    },
    // 加载题库列表
    handleSubjectBankManagement () {
      this.category.listLoading = true
      fetchSubjectList(this.category.listQuery).then(response => {
        if (response.data.list.length > 0) {
          for (let i = 0; i < response.data.list.length; i++) {
            const subject = response.data.list[i]
            subject.type = parseInt(subject.type)
            subject.level = parseInt(subject.level)
          }
        }
        this.category.list = response.data.list
        this.category.total = parseInt(response.data.total)
        this.category.listLoading = false
      })
    },
    handleFilterSubject () {
      this.subject.listQuery.pageNum = 1
      this.handleSubjectManagement()
    },
    // 新建题目
    handleCreateSubject () {
      this.resetTempSubject()
      this.dialogStatus = 'create'
      this.dialogSubjectFormVisible = true
      this.resetActiveName()
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
    resetTempSubject (serialNumber, score) {
      const ref = this.getSubjectRef()
      if (isNotEmpty(ref)) {
        ref.resetTempSubject(serialNumber, score)
      }
    },
    // 修改题目
    handleUpdateSubject (row) {
      // 加载选项信息
      getSubject(row.id, { type: row.type }).then(response => {
        const subjectInfo = response.data.data
        this.dialogStatus = 'update'
        this.dialogSubjectFormVisible = true
        // 切换到对应的题型选项卡
        this.updateCurrentTag(subjectInfo.type)
        setTimeout(() => {
          const ref = this.getSubjectRef()
          if (isNotEmpty(ref)) {
            // 初始化单选题
            this.$nextTick(() => {
              ref.clearValidate()
              ref.setSubjectInfo(subjectInfo)
            })
          }
        }, 200)
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
      }).catch(() => {})
    },
    // 保存题目
    createSubjectData () {
      const ref = this.getSubjectRef()
      if (ref.validate()) {
        let subjectInfo = ref.getSubjectInfo()
        // 绑定考试ID
        subjectInfo.examinationId = this.subject.examinationId
        addSubject(subjectInfo).then(() => {
          this.subject.list.unshift(subjectInfo)
          this.dialogSubjectFormVisible = false
          this.handleSubjectManagement()
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
          this.handleSubjectManagement()
          notifySuccess(this, '更新成功')
        })
      }
    },
    // 更新并添加题目
    updateAndAddSubjectData () {
      const ref = this.getSubjectRef()
      if (ref.validate()) {
        const subjectInfo = ref.getSubjectInfo()
        // 绑定考试ID
        subjectInfo.examinationId = this.subject.examinationId
        // 创建
        if (this.dialogStatus === 'create') {
          addSubject(subjectInfo).then(() => {
            this.resetTempSubject(parseInt(subjectInfo.serialNumber) + 1, subjectInfo.score)
            this.dialogStatus = 'create'
            ref.clearValidate()
            this.handleSubjectManagement()
            notifySuccess(this, '创建成功')
          })
        } else {
          // 修改
          putSubject(subjectInfo).then(() => {
            this.resetTempSubject(parseInt(subjectInfo.serialNumber) + 1, subjectInfo.score)
            this.dialogStatus = 'create'
            ref.clearValidate()
            this.handleSubjectManagement()
            notifySuccess(this, '更新成功')
          })
        }
      }
    },
    // 切换题目类型
    changeSubjectType (value) {
      console.log(value)
    },
    // 发布考试
    handlePublic (row, status) {
      const tempData = Object.assign({}, row)
      tempData.status = status
      putObj(tempData).then(() => {
        this.getList()
        notifySuccess(this, '更新成功')
      })
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
    // 导出
    handleExportSubject () {
      // 没选择题目，导出所有
      if (this.multipleSubjectSelection.length === 0) {
        this.$confirm('是否导出所有题目?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'success'
        }).then(() => {
          exportSubject([], this.subject.examinationId).then(response => {
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
    // 导入
    handleImportSubject () {
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
    // 图片上传前
    beforeAvatarUpload (file) {
      const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isJPG) {
        this.$message.error('上传头像图片只能是 jpg/png 格式!')
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!')
      }
      return isJPG && isLt2M
    },
    // 上传成功
    handleAvatarSuccess (res, file) {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          if (isNotEmpty(this.temp.avatarId)) {
            // 删除旧头像
            delAttachment(this.temp.avatarId).then(() => {
              // 更新头像信息
              this.temp.avatarId = res.data.id
              putObj(Object.assign({}, this.temp)).then(() => {
                notifySuccess(this, '上传成功')
                this.dialogFormVisible = false
                this.getList()
              }).catch(() => {
                notifyFail(this, '上传失败')
              })
            })
          } else {
            // 更新头像信息
            this.temp.avatarId = res.data.id
            putObj(Object.assign({}, this.temp)).then(() => {
              notifySuccess(this, '上传成功')
              this.dialogFormVisible = false
              this.getList()
            }).catch(() => {
              notifyFail(this, '上传失败')
            })
          }
        }
      })
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

<style rel="stylesheet/scss" lang="scss" scoped>
  @import "../../styles/subject.scss";
</style>
