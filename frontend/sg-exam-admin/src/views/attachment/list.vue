<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input :placeholder="$t('table.attachName')" v-model="listQuery.attachName" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter"/>
      <el-select v-model="listQuery.busiType" :placeholder="$t('table.type')" clearable class="filter-item" style="width: 130px">
        <el-option v-for="item in attachmentTypeOptions" :key="item.key" :label="item.display_name" :value="item.key"/>
      </el-select>
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">{{ $t('table.search') }}</el-button>
      <el-upload
        :show-file-list="showFileList"
        :on-success="handleUploadSuccess"
        :on-progress="handleUploadProgress"
        action="api/user-service/v1/attachment/upload"
        :headers="headers"
        :data="params"
        class="upload-demo"
        multiple>
        <el-button v-waves type="success" class="filter-item">上传<i class="el-icon-upload el-icon--right" style="margin-left: 10px;"/></el-button>
      </el-upload>
    </div>
    <el-progress v-if="uploading === true" :percentage="percentage" :text-inside="true" :stroke-width="18" status="success"/>

    <spinner-loading v-if="listLoading"/>
    <el-table
      :key="tableKey"
      :data="list"
      highlight-current-row
      style="width: 100%;"
      @sort-change="sortChange">
      <el-table-column type="selection" width="55"/>
      <el-table-column prop="流水号" label="id" min-width="100">
        <template slot-scope="scope">
          <span>{{ scope.row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.attachName')" prop="attach_name" min-width="120">
        <template slot-scope="scope">
          <span>{{ scope.row.attachName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="附件类型" min-width="90">
        <template slot-scope="scope">
          <span>{{ scope.row.busiType | attachmentTypeFilter }}</span>
        </template>
      </el-table-column>
      <el-table-column label="附件大小" min-width="90">
        <template slot-scope="scope">
          <span>{{ scope.row.attachSize | attachmentSizeFilter }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.uploader')" min-width="50">
        <template slot-scope="scope">
          <span>{{ scope.row.creator }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.uploadDate')" min-width="70">
        <template slot-scope="scope">
          <span>{{ scope.row.createDate | fmtDate('yyyy-MM-dd hh:mm') }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.actions')" class-name="status-col" width="100">
        <template slot-scope="scope">
          <el-dropdown>
            <span class="el-dropdown-link">
              操作<i class="el-icon-caret-bottom el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>
                <a @click="handlePreview(scope.row)">
                  <span><i class="el-icon-view"></i>{{ $t('table.preview') }}</span>
                </a>
              </el-dropdown-item>
              <el-dropdown-item>
                <a @click="handleDownloadUrl(scope.row)">
                  <span><i class="el-icon-document-copy"></i>{{ $t('table.downloadUrl') }}</span>
                </a>
              </el-dropdown-item>
              <el-dropdown-item>
                <a @click="handleDownload(scope.row)">
                  <span><i class="el-icon-download"></i>{{ $t('table.download') }}</span>
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

    <!-- 预览 -->
    <el-dialog :visible.sync="dialogPreviewVisible" title="预览" width="50%" top="12vh">
      <div class="preview">
        <img :src="previewUrl" alt="二维码">
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { fetchList, addObj, putObj, delAttachment, canPreview } from '@/api/admin/attachment'
import waves from '@/directive/waves'
import { getToken } from '@/utils/auth'
import { messageSuccess, messageWarn } from '@/utils/util'
import SpinnerLoading from '@/components/SpinnerLoading'

export default {
  name: 'AttachmentManagement',
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
        1: 'danger'
      }
      return statusMap[status]
    },
    statusFilter (status) {
      return status === '0' ? '启用' : '禁用'
    },
    attachmentTypeFilter (type) {
      let attachType
      if (type === '1') {
        attachType = '用户头像'
      } else if (type === '2') {
        attachType = '知识库附件'
      } else {
        attachType = '普通附件'
      }
      return attachType
    },
    attachmentSizeFilter (attachSize) {
      let fileSizeByte = attachSize
      let fileSizeMsg = ''
      if (fileSizeByte < 1048576) fileSizeMsg = (fileSizeByte / 1024).toFixed(2) + 'KB'
      else if (fileSizeByte === 1048576) fileSizeMsg = '1MB'
      else if (fileSizeByte > 1048576 && fileSizeByte < 1073741824) fileSizeMsg = (fileSizeByte / (1024 * 1024)).toFixed(2) + 'MB'
      else if (fileSizeByte > 1048576 && fileSizeByte === 1073741824) fileSizeMsg = '1GB'
      else if (fileSizeByte > 1073741824 && fileSizeByte < 1099511627776) fileSizeMsg = (fileSizeByte / (1024 * 1024 * 1024)).toFixed(2) + 'GB'
      else fileSizeMsg = '文件超过1TB'
      return fileSizeMsg
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
        sort: 'create_date',
        order: 'descending',
        busiType: '0'
      },
      temp: {
        id: '',
        attachName: '',
        attachSize: ''
      },
      downloadLoading: false,
      labelPosition: 'right',
      showFileList: false,
      headers: {
        Authorization: 'Bearer ' + getToken()
      },
      params: {
        busiType: '0'
      },
      attachmentTypeOptions: [
        {
          display_name: '普通附件',
          key: '0'
        },
        {
          display_name: '用户头像',
          key: '1'
        },
        {
          display_name: '知识库文件',
          key: '2'
        }
      ],
      uploading: false,
      percentage: 0,
      dialogPreviewVisible: false,
      previewUrl: ''
    }
  },
  created () {
    this.getList()
  },
  methods: {
    getList () {
      this.listLoading = true
      fetchList(this.listQuery).then(response => {
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
    handleModifyStatus (row, status) {
      row.status = status
      putObj(row).then(() => {
        messageSuccess(this, '操作成功')
      })
    },
    sortChange (column, prop, order) {
      this.listQuery.sort = column.prop
      this.listQuery.order = column.order
      this.getList()
    },
    resetTemp () {
      this.temp = {
        id: '',
        attachName: '',
        attachSize: ''
      }
    },
    createData () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          addObj(this.temp).then(() => {
            this.list.unshift(this.temp)
            this.getList()
            messageSuccess(this, '创建成功')
          })
        }
      })
    },
    handleDownload (row) {
      window.location.href = '/user-service/v1/attachment/download?id=' + row.id
    },
    handlePreview (row) {
      this.previewUrl = ''
      canPreview(row.id).then(response => {
        if (response.data.data) {
          this.previewUrl = '/user-service/v1/attachment/preview?id=' + row.id
          this.dialogPreviewVisible = true
        } else {
          messageWarn(this, '暂不支持预览该格式的附件')
        }
      }).catch(error => {
        console.error(error)
      })
    },
    handleDownloadUrl (row) {
      const url = 'http://' + window.location.host + '/user-service/v1/attachment/download?id=' + row.id
      this.$alert(url, '下载链接', { confirmButtonText: '确定' })
    },
    updateData () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          putObj(tempData).then(() => {
            this.getList()
            messageSuccess(this, '更新成功')
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
        delAttachment(row.id).then(() => {
          this.getList()
          messageSuccess(this, '删除成功')
        })
      }).catch(() => {})
    },
    handleUploadSuccess () {
      this.uploading = false
      this.getList()
      messageSuccess(this, '上传成功')
    },
    handleUploadProgress (event, file, fileList) {
      this.uploading = true
      this.percentage = parseInt(file.percentage.toFixed(0))
    }
  }
}
</script>

<style lang="scss" scoped>
  .upload-demo {
    display: inline-block;
  }
  .preview {
    text-align: center;
    overflow: hidden;
  }
</style>
