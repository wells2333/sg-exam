<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.knowledgeName" placeholder="知识名称" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter"/>
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">{{ $t('table.search') }}</el-button>
      <el-button class="filter-item" type="primary" style="margin-left: 10px;" icon="el-icon-check" @click="handleCreate">{{ $t('table.add') }}</el-button>
      <el-button class="filter-item" type="danger" icon="el-icon-delete" @click="handleDeletes">{{ $t('table.del') }}</el-button>
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
      <el-table-column :label="$t('table.knowledge.knowledgeName')" sortable prop="knowledge_name" min-width="90">
        <template slot-scope="scope">
          <span>{{ scope.row.knowledgeName }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.knowledge.knowledgeDesc')" min-width="90">
        <template slot-scope="scope">
          <span>{{ scope.row.knowledgeDesc }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.knowledge.status')" min-width="90">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status | statusTypeFilter" effect="dark" size="small">{{ scope.row.status | publicStatusFilter }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.modifyDate')" min-width="80">
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
              <el-dropdown-item>
                <a @click="handleUpdate(scope.row)">
                  <span><i class="el-icon-edit"></i>{{ $t('table.edit') }}</span>
                </a>
              </el-dropdown-item>
              <el-dropdown-item v-if="scope.row.status == 1">
                <a @click="handlePublic(scope.row, 0)">
                  <span><i class="el-icon-check"></i>{{ $t('table.public') }}</span>
                </a>
              </el-dropdown-item>
              <el-dropdown-item v-if="scope.row.status == 0">
                <a @click="handlePublic(scope.row, 1)">
                  <span><i class="el-icon-close"></i>{{ $t('table.withdraw') }}</span>
                </a>
              </el-dropdown-item>
              <el-dropdown-item>
                <a @click="handleDelete(scope.row)">
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

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" width="60%" top="10vh">
      <el-form ref="dataForm" :rules="rules" :model="temp" :label-position="labelPosition" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('table.knowledge.knowledgeName')" prop="knowledgeName">
              <el-input v-model="temp.knowledgeName"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('table.knowledge.knowledgeDesc')">
              <el-input :autosize="{ minRows: 3, maxRows: 5}" :placeholder="$t('table.knowledge.knowledgeDesc')" v-model="temp.knowledgeDesc" type="textarea"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-upload
              v-show="temp.id !== undefined"
              :show-file-list="showFileList"
              :on-success="handleUploadSuccess"
              :on-exceed="handleExceed"
              :on-remove="handleRemove"
              action="api/user-service/v1/attachment/upload"
              :headers="headers"
              :data="params"
              :limit="1"
              :file-list="fileList"
              class="upload-demo"
              multiple>
              <el-button v-waves type="primary" class="filter-item">上传<i class="el-icon-upload el-icon--right" style="margin-left: 10px;"/></el-button>
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
  </div>
</template>

<script>
import { fetchKnowledgeList, addObj, putObj, delObj, delAllObj } from '@/api/exam/knowledge'
import waves from '@/directive/waves'
import { mapGetters, mapState } from 'vuex'
import { isNotEmpty, checkMultipleSelect, notifySuccess, notifyFail, messageSuccess } from '@/utils/util'
import { getToken } from '@/utils/auth' // getToken from cookie
import { getObj, delAttachment } from '@/api/admin/attachment'
import SpinnerLoading from '@/components/SpinnerLoading'

export default {
  name: 'KnowledgeManagement',
  components: {
    SpinnerLoading
  },
  directives: {
    waves
  },
  filters: {
    statusTypeFilter (status) {
      const statusMap = {
        0: 'success',
        1: 'warning'
      }
      return statusMap[status]
    },
    statusFilter (status) {
      return status === '0' ? '启用' : '禁用'
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
        roleName: undefined,
        sort: 'id',
        order: 'descending'
      },
      temp: {
        id: undefined,
        knowledgeName: '',
        knowledgeDesc: '',
        attachmentId: '',
        status: '0'
      },
      checkedKeys: [],
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '新建'
      },
      rules: {
        knowledgeName: [{ required: true, message: '请输入知识名称', trigger: 'change' }]
      },
      downloadLoading: false,
      labelPosition: 'right',
      // 多选
      multipleSelection: [],
      uploading: false,
      showFileList: true,
      headers: {
        Authorization: 'Bearer ' + getToken()
      },
      params: {
        busiType: '2'
      },
      fileList: []
    }
  },
  created () {
    this.getList()
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
    getList () {
      this.listLoading = true
      fetchKnowledgeList(this.listQuery).then(response => {
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
        id: undefined,
        knowledgeName: '',
        knowledgeDesc: '',
        attachmentId: '',
        status: '0'
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
      this.temp = Object.assign({}, row)
      this.temp.status = parseInt(this.temp.status)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
      // 获取附件列表
      this.getFileList(this.temp.attachmentId)
    },
    updateData () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          putObj(tempData).then(() => {
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
          delAllObj(ids).then(() => {
            this.dialogFormVisible = false
            this.getList()
            notifySuccess(this, '删除成功')
          })
        }).catch(() => {})
      }
    },
    handleUploadSuccess (response) {
      this.uploading = false
      // 关联知识
      this.temp.attachmentId = response.data.id
      this.updateData()
    },
    // 查询附件列表
    getFileList (attachmentId) {
      this.fileList = []
      if (isNotEmpty(attachmentId)) {
        getObj(attachmentId).then(response => {
          const data = response.data.data
          const attachment = {
            name: data.attachName,
            url: ''
          }
          this.fileList = []
          this.fileList.push(attachment)
        }).catch(() => {
          this.fileList = []
        })
      }
    },
    // 限制
    handleExceed (files, fileList) {
      this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`)
    },
    // 删除附件
    handleRemove (file, fileList) {
      delAttachment(this.temp.attachmentId).then(() => {
        notifySuccess(this, '删除成功')
      }).catch(() => {
        notifyFail(this, '删除失败')
      })
    },
    // 启用知识
    handlePublic (row, status) {
      const tempData = Object.assign({}, row)
      tempData.status = status
      putObj(tempData).then(() => {
        this.getList()
        notifySuccess(this, '更新成功')
      })
    }
  }
}
</script>
