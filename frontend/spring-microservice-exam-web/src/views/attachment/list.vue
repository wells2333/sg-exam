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
        action="api/user/v1/attachment/upload"
        :headers="headers"
        :data="params"
        class="upload-demo"
        multiple>
        <el-button v-waves type="primary" class="filter-item">上传<i class="el-icon-upload el-icon--right" style="margin-left: 10px;"/></el-button>
        <el-progress v-if="uploading === true" :percentage="percentage" :text-inside="true" :stroke-width="18" status="success"/>
      </el-upload>
    </div>

    <spinner-loading v-if="listLoading"/>
    <el-table
      :key="tableKey"
      :data="list"
      highlight-current-row
      style="width: 100%;"
      @sort-change="sortChange">
      <el-table-column type="selection" width="55"/>
      <el-table-column prop="id" label="流水号" min-width="100">
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
      <el-table-column :label="$t('table.uploader')" min-width="50">
        <template slot-scope="scope">
          <span>{{ scope.row.creator }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.uploadDate')" min-width="70">
        <template slot-scope="scope">
          <span>{{ scope.row.createDate | timeFilter }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.actions')" class-name="status-col" width="300px">
        <template slot-scope="scope">
          <el-button type="text" @click="handleDownload(scope.row)">{{ $t('table.download') }}</el-button>
          <el-button type="text" @click="handleDelete(scope.row)">{{ $t('table.delete') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-container">
      <el-pagination v-show="total>0" :current-page="listQuery.pageNum" :page-sizes="[10,20,30, 50]" :page-size="listQuery.pageSize" :total="total" background layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange" @current-change="handleCurrentChange"/>
    </div>
  </div>
</template>

<script>
import { fetchList, addObj, putObj, delAttachment, getDownloadUrl } from '@/api/admin/attachment'
import waves from '@/directive/waves'
import { getToken } from '@/utils/auth' // getToken from cookie
import { notifySuccess, messageSuccess, isNotEmpty, formatDate } from '@/utils/util'
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
    timeFilter (time) {
      return formatDate(new Date(time), 'yyyy-MM-dd hh:mm')
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
      percentage: 0
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
            notifySuccess(this, '创建成功')
          })
        }
      })
    },
    handleDownload (row) {
      getDownloadUrl(row.id).then(response => {
        if (isNotEmpty(response.data)) {
          window.open('http://' + response.data.data, '_blank')
        }
      })
    },
    updateData () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          putObj(tempData).then(() => {
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
        delAttachment(row.id).then(() => {
          this.getList()
          notifySuccess(this, '删除成功')
        })
      }).catch(() => {})
    },
    handleUploadSuccess () {
      this.uploading = false
      this.getList()
      notifySuccess(this, '上传成功')
    },
    handleUploadProgress (event, file, fileList) {
      this.uploading = true
      this.percentage = parseInt(file.percentage.toFixed(0))
    }
  }
}
</script>
