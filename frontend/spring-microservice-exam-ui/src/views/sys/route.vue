<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input :placeholder="$t('table.route.routeId')" v-model="listQuery.routeId" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter"/>
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">{{ $t('table.search') }}</el-button>
      <el-button v-if="route_btn_add" class="filter-item" style="margin-left: 10px;" icon="el-icon-check" plain @click="handleCreate">{{ $t('table.add') }}</el-button>
      <el-button v-if="route_btn_del" class="filter-item" icon="el-icon-delete" plain @click="handleDeletes">{{ $t('table.del') }}</el-button>
      <el-button v-if="route_btn_refresh" class="filter-item" icon="el-icon-refresh" plain @click="handleRefreshRoute">{{ $t('table.route.refresh') }}</el-button>
    </div>
    <spinner-loading v-if="listLoading"/>
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
      <el-table-column :label="$t('table.route.routeId')" sortable prop="route_id">
        <template slot-scope="scope">
          <span>{{ scope.row.routeId }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.route.routeName')">
        <template slot-scope="scope">
          <span>{{ scope.row.routeName }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.route.uri')">
        <template slot-scope="scope">
          <span>{{ scope.row.uri }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.route.sort')">
        <template slot-scope="scope">
          <span>{{ scope.row.sort }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.route.status')">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status | statusTypeFilter">{{ scope.row.status | statusFilter }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.actions')" class-name="status-col" width="300px">
        <template slot-scope="scope">
          <el-button v-if="route_btn_edit" type="text" @click="handleUpdate(scope.row)" icon="el-icon-edit">{{ $t('table.edit') }}</el-button>
          <el-button v-if="route_btn_del" type="text" @click="handleDelete(scope.row)" icon="el-icon-delete">{{ $t('table.delete') }}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-container">
      <el-pagination v-show="total>0" :current-page="listQuery.pageNum" :page-sizes="[10,20,30, 50]" :page-size="listQuery.pageSize" :total="total" background layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange" @current-change="handleCurrentChange"/>
    </div>

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" width="60%" top="10vh">
      <el-form ref="dataForm" :rules="rules" :model="temp" :label-position="labelPosition" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('table.route.routeId')" prop="routeId">
              <el-input v-model="temp.routeId"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('table.route.routeName')" prop="routeName">
              <el-input v-model="temp.routeName"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('table.route.uri')">
              <el-input v-model="temp.uri"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('table.route.sort')">
              <el-input v-model="temp.sort"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('table.route.status')">
              <el-radio-group v-model="temp.status">
                <el-radio :label="0">启用</el-radio>
                <el-radio :label="1">禁用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('table.route.predicates')" prop="predicates">
              <json-editor ref="predicatesJsonEditor" v-model="temp.tempPredicates"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('table.route.filters')" prop="filters">
              <json-editor ref="filtersJsonEditor" v-model="temp.tempFilters"/>
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
import { fetchList, addObj, putObj, delObj, delAllObj, refresh } from '@/api/admin/route'
import waves from '@/directive/waves'
import { mapGetters } from 'vuex'
import { checkMultipleSelect, notifySuccess, notifyFail, messageSuccess } from '@/utils/util'
import JsonEditor from '@/components/JsonEditor'
import SpinnerLoading from '@/components/SpinnerLoading'

export default {
  name: 'ClientManagement',
  components: {
    SpinnerLoading,
    JsonEditor
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
        routeId: '',
        sort: 'sort',
        order: 'descending'
      },
      temp: {
        id: '',
        routeId: '',
        routeName: '',
        predicates: '',
        filters: '',
        tempPredicates: {},
        tempFilters: {},
        uri: '',
        sort: '',
        status: 0
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
        routeId: [{ required: true, message: '请输入路由ID', trigger: 'change' }],
        routeName: [{ required: true, message: '请输入路由名称', trigger: 'change' }]
      },
      downloadLoading: false,
      labelPosition: 'right',
      route_btn_add: false,
      route_btn_edit: false,
      route_btn_del: false,
      route_btn_refresh: false
    }
  },
  created () {
    this.getList()
    this.route_btn_add = this.permissions['sys:route:add']
    this.route_btn_edit = this.permissions['sys:route:edit']
    this.route_btn_del = this.permissions['sys:route:del']
    this.route_btn_refresh = this.permissions['sys:route:refresh']
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
        routeId: '',
        routeName: '',
        predicates: '',
        filters: '',
        tempPredicates: {},
        tempFilters: {},
        uri: '',
        sort: '',
        status: 0
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
          this.temp.predicates = this.$refs.predicatesJsonEditor.getValue()
          this.temp.filters = this.$refs.filtersJsonEditor.getValue()
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
      this.dialogStatus = 'update'
      this.temp.tempPredicates = JSON.parse(this.temp.predicates)
      this.temp.tempFilters = JSON.parse(this.temp.filters)
      this.temp.status = parseInt(this.temp.status)
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          tempData.predicates = this.$refs.predicatesJsonEditor.getValue()
          tempData.filters = this.$refs.filtersJsonEditor.getValue()
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
          delAllObj(ids).then(() => {
            this.dialogFormVisible = false
            this.getList()
            notifySuccess(this, '删除成功')
          })
        }).catch(() => {})
      }
    },
    handleRefreshRoute () {
      this.$confirm('确定要刷新路由吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        refresh().then(() => {
          notifySuccess(this, '刷新成功')
        }).catch(() => {
          notifyFail(this, '刷新失败')
        })
      }).catch(() => {})
    }
  }
}
</script>
