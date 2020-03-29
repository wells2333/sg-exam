<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input :placeholder="$t('table.client.clientId')" v-model="listQuery.clientId" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter"/>
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">{{ $t('table.search') }}</el-button>
      <el-button v-if="client_btn_add" type="primary" class="filter-item" style="margin-left: 10px;" icon="el-icon-check" @click="handleCreate">{{ $t('table.add') }}</el-button>
      <el-button v-if="client_btn_del" type="danger" class="filter-item" icon="el-icon-delete" @click="handleDeletes">{{ $t('table.del') }}</el-button>
    </div>
    <spinner-loading v-if="listLoading"/>
    <el-table
      :key="tableKey"
      :data="list"
      :default-sort="{ prop: 'id', order: 'descending' }"
      highlight-current-row
      style="width: 100%;"
      @selection-change="handleSelectionChange"
      @sort-change="sortChange">
      <el-table-column type="selection" width="55"/>
      <el-table-column :label="$t('table.client.clientId')" prop="client_id">
        <template slot-scope="scope">
          <span>{{ scope.row.clientId }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.client.clientSecretPlainText')" min-width="100">
        <template slot-scope="scope">
          <span>{{ scope.row.clientSecretPlainText }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.client.accessTokenValidity')">
        <template slot-scope="scope">
          <span>{{ scope.row.accessTokenValidity }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.client.refreshTokenValidity')" min-width="100">
        <template slot-scope="scope">
          <span>{{ scope.row.refreshTokenValidity }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.actions')" class-name="status-col" width="100px">
        <template slot-scope="scope">
          <el-dropdown>
            <span class="el-dropdown-link">
              操作<i class="el-icon-caret-bottom el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item v-if="client_btn_edit">
                <a @click="handleUpdate(scope.row)">
                  <span><i class="el-icon-edit"></i>{{ $t('table.edit') }}</span>
                </a>
              </el-dropdown-item>
              <el-dropdown-item v-if="client_btn_del">
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
            <el-form-item :label="$t('table.client.clientId')" prop="clientId">
              <el-input v-model="temp.clientId"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('table.client.clientSecretPlainText')" prop="clientSecretPlainText">
              <el-input v-model="temp.clientSecretPlainText"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('table.client.clientSecret')" prop="clientSecret">
              <el-input v-model="temp.clientSecret" :readonly="true" placeholder="密钥密文，自动生成"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('table.client.scope')" prop="scope">
            <el-checkbox-group v-model="temp.scope">
              <el-checkbox label="read"></el-checkbox>
              <el-checkbox label="write"></el-checkbox>
            </el-checkbox-group>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('table.client.authorizedGrantTypes')">
              <el-checkbox-group v-model="temp.authorizedGrantTypes">
                <el-checkbox label="password"></el-checkbox>
                <el-checkbox label="authorization_code"></el-checkbox>
                <el-checkbox label="refresh_token"></el-checkbox>
                <el-checkbox label="implicit"></el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('table.client.accessTokenValidity')">
              <el-input v-model="temp.accessTokenValidity"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('table.client.refreshTokenValidity')" prop="refreshTokenValidity">
              <el-input v-model="temp.refreshTokenValidity"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">{{ $t('table.cancel') }}</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="createData">{{ $t('table.confirm') }}</el-button>
        <el-button v-else type="primary" @click="updateData">{{ $t('table.confirm') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { fetchList, addObj, putObj, delObj, delAllObj } from '@/api/admin/client'
import waves from '@/directive/waves'
import { mapGetters } from 'vuex'
import { checkMultipleSelect, notifySuccess, messageSuccess } from '@/utils/util'
import SpinnerLoading from '@/components/SpinnerLoading'

export default {
  name: 'ClientManagement',
  components: {
    SpinnerLoading
  },
  directives: {
    waves
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
        clientId: '',
        sort: 'id',
        order: 'descending'
      },
      temp: {
        id: '',
        clientId: '',
        resourceIds: '',
        clientSecretPlainText: '',
        clientSecret: '',
        scope: '',
        authorizedGrantTypes: '',
        webServerRedirectUri: '',
        authorities: '',
        accessTokenValidity: '',
        refreshTokenValidity: '',
        additionalInformation: '',
        autoapprove: ''
      },
      checkedKeys: [],
      multipleSelection: [],
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '新建'
      },
      rules: {
        clientId: [{ required: true, message: '请输入客户端ID', trigger: 'change' }],
        clientSecretPlainText: [{ required: true, message: '请输入客户端密钥', trigger: 'change' }]
      },
      downloadLoading: false,
      defaultProps: {
        children: 'children',
        label: 'deptName'
      },
      permissionProps: {
        children: 'children',
        lable: 'name'
      },
      labelPosition: 'right',
      client_btn_add: false,
      client_btn_edit: false,
      client_btn_del: false
    }
  },
  created () {
    this.getList()
    this.client_btn_add = this.permissions['sys:client:add']
    this.client_btn_edit = this.permissions['sys:client:edit']
    this.client_btn_del = this.permissions['sys:client:del']
  },
  computed: {
    ...mapGetters([
      'elements',
      'permissions'
    ])
  },
  methods: {
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
    resetTemp () {
      this.temp = {
        id: '',
        clientId: '',
        resourceIds: '',
        clientSecretPlainText: '',
        clientSecret: '',
        scope: '',
        authorizedGrantTypes: '',
        webServerRedirectUri: '',
        authorities: '',
        accessTokenValidity: '',
        refreshTokenValidity: '',
        additionalInformation: '',
        autoapprove: ''
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
      this.temp = Object.assign({}, row) // copy obj
      if (this.temp.scope) {
        this.temp.scope = this.temp.scope.split(',')
      }
      if (this.temp.authorizedGrantTypes) {
        this.temp.authorizedGrantTypes = this.temp.authorizedGrantTypes.split(',')
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
          if (tempData.scope) {
            tempData.scope = tempData.scope.join(',')
          }
          if (tempData.authorizedGrantTypes) {
            tempData.authorizedGrantTypes = tempData.authorizedGrantTypes.join(',')
          }
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
    }
  }
}
</script>
