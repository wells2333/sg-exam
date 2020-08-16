<template>
  <div class="tab-container">
    <div class="filter-container">
      <el-button v-if="menu_btn_add" icon="el-icon-check" type="primary" @click="handlerAddSuper">添加顶级菜单</el-button>
      <el-button v-if="menu_btn_add" icon="el-icon-check" type="primary" @click="handlerAdd">添加子菜单</el-button>
      <el-button v-if="menu_btn_del" icon="el-icon-delete" type="danger" @click="handleDelete">{{ $t('table.del') }}</el-button>
      <el-button v-if="menu_btn_import" icon="el-icon-upload2" type="success" @click="handleImport">{{ $t('table.import') }}</el-button>
      <el-button v-if="menu_btn_export" icon="el-icon-download" type="success" @click="handleExport">{{ $t('table.export') }}</el-button>

      <el-row>
        <el-col :span="4" style ="margin-top:10px;">
          <el-tree
            ref="tree"
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
        </el-col>
        <el-col :span="20" style="margin-top:10px;">
          <el-card class="box-card">
            <el-form ref="form" :rules="rules" :label-position="labelPosition" :model="form" label-width="100px" style="width: 90%;">
              <el-row>
                <el-col :span="12">
                  <el-form-item label="菜单名称" prop="name">
                    <el-input v-model="form.name" placeholder="请输入菜单名称"/>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="菜单标识" prop="permission">
                    <el-input v-model="form.permission" placeholder="请输入菜单标识"/>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="菜单URL" prop="url">
                    <el-input v-model="form.url" placeholder="请输入菜单URL"/>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="类型" prop="type">
                    <el-select v-model="form.type" class="filter-item" placeholder="请输入资源请求类型">
                      <el-option v-for="item in typeOptions" :key="item" :label="item | typeFilter" :value="item"/>
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="排序号" prop="sort">
                    <el-input v-model="form.sort" placeholder="请输入排序号"/>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="菜单图标" prop="icon">
                    <el-input v-model="form.icon" placeholder="请选择"/>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="组件名称" prop="component">
                    <el-input v-model="form.component" placeholder="请输入组件名称"/>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="组件地址" prop="component">
                    <el-input v-model="form.path" placeholder="组件地址"/>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                  <el-form-item :label="$t('table.remark')">
                    <el-input :autosize="{ minRows: 4, maxRows: 9}" v-model="form.remark" type="textarea" placeholder="备注"/>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-form-item>
                <el-button v-if="menu_btn_edit" type="primary" @click="create">保存</el-button>
                <el-button @click="onCancel">取消</el-button>
              </el-form-item>
            </el-form>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 导出菜单 -->
    <el-dialog :visible.sync="dialogExportVisible" :title="$t('table.export')">
      <el-tree
        ref="menuTree"
        :data="treeData"
        :props="defaultProps"
        show-checkbox
        class="filter-tree"
        node-key="id"
        highlight-current
        @node-click="getNodeData"
      />
      <div slot="footer" class="dialog-footer">
        <el-button v-if="menu_btn_export" type="primary" @click="handleExportMenu()">导出</el-button>
      </div>
    </el-dialog>

    <!-- 导入菜单 -->
    <el-dialog :visible.sync="dialogImportVisible" :title="$t('table.import')">
      <el-row>
        <el-col :span="24">
          <el-upload
            drag
            :multiple="false"
            :auto-upload="true"
            :show-file-list="true"
            :before-upload="beforeMenuUpload"
            :on-progress="handleUploadProgress"
            :on-success="handleUploadMenuSuccess"
            :action="importUrl"
            :headers="headers"
            style="text-align: center;">
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            <div slot="tip" class="el-upload__tip">只能上传xlsx文件</div>
          </el-upload>
        </el-col>
      </el-row>
    </el-dialog>
  </div>
</template>

<script>
import { fetchTree, getObj, addObj, delObj, putObj, exportObj } from '@/api/admin/menu'
import { mapGetters } from 'vuex'
import { getToken } from '@/utils/auth'
import { exportExcel, notifySuccess } from '@/utils/util'

export default {
  name: 'MenuManagement',
  components: {},
  filters: {
    typeFilter (type) {
      const typeMap = {
        0: '菜单',
        1: '按钮'
      }
      return typeMap[type]
    }
  },
  data () {
    return {
      list: null,
      total: null,
      formAdd: true,
      formStatus: '',
      showElement: false,
      typeOptions: [0, 1],
      listQuery: {
        name: undefined
      },
      treeData: [],
      oExpandedKey: {
      },
      oTreeNodeChildren: {
      },
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      labelPosition: 'right',
      form: {
        permission: undefined,
        name: undefined,
        id: undefined,
        parentId: -1,
        url: undefined,
        icon: '',
        sort: 30,
        component: undefined,
        type: 0,
        path: undefined,
        remark: undefined
      },
      currentId: '',
      // 表单校验规则
      rules: {
        name: [{ required: true, message: '请输入菜单名称', trigger: 'change' }],
        permission: [{ required: true, message: '请输入菜单标识', trigger: 'change' }]
      },
      // 按钮权限
      menu_btn_add: false,
      menu_btn_edit: false,
      menu_btn_del: false,
      menu_btn_import: false,
      menu_btn_export: false,
      // 导入窗口状态
      dialogImportVisible: false,
      // 导出窗口状态
      dialogExportVisible: false,
      // 选择的菜单
      multipleSelection: [],
      importUrl: '/user/v1/menu/import',
      headers: {
        Authorization: 'Bearer ' + getToken()
      },
      uploading: false,
      percentage: 0
    }
  },
  created () {
    this.getList()
    this.menu_btn_add = this.permissions['sys:menu:add']
    this.menu_btn_edit = this.permissions['sys:menu:edit']
    this.menu_btn_del = this.permissions['sys:menu:del']
    this.menu_btn_import = this.permissions['sys:menu:import']
    this.menu_btn_export = this.permissions['sys:menu:export']
  },
  computed: {
    ...mapGetters([
      'elements',
      'permissions'
    ])
  },
  methods: {
    getList () {
      fetchTree(this.listQuery).then(response => {
        this.treeData = response.data
        // 加载后默认选中第一个菜单
        if (response.data && response.data.length > 0) {
          const firstNode = response.data[0]
          this.$refs.tree.setCheckedNodes([].concat(firstNode))
          this.getNodeData(firstNode)
        }
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
    getNodeData (data) {
      this.formStatus = 'update'
      getObj(data.id).then(response => {
        this.form = response.data
      })
      this.currentId = data.id
      this.showElement = true
    },
    handlerEdit () {
      if (this.form.id) {
        this.formStatus = 'update'
      }
    },
    handlerAddSuper () {
      this.resetForm()
      this.form.parentId = -1
      this.form.component = 'Layout'
      this.formStatus = 'create'
    },
    handlerAdd () {
      this.resetForm()
      this.formStatus = 'create'
    },
    handleDelete () {
      if (this.currentId === '') {
        this.$message({
          message: '请选择要删除的记录',
          type: 'warning'
        })
        return
      }
      this.$confirm('确定要删除吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delObj(this.currentId).then(() => {
          this.getList()
          this.resetForm()
          this.onCancel()
          notifySuccess(this, '删除成功')
        })
      }).catch(() => {})
    },
    create () {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          if (this.form.id) {
            putObj(this.form).then(() => {
              this.getList()
              notifySuccess(this, '更新成功')
            })
          } else {
            addObj(this.form).then(() => {
              this.getList()
              notifySuccess(this, '创建成功')
            })
          }
          this.getList()
        }
      })
    },
    onCancel () {
      this.formStatus = ''
    },
    resetForm () {
      this.form = {
        permission: undefined,
        name: undefined,
        id: undefined,
        parentId: this.currentId,
        url: undefined,
        icon: '',
        sort: 30,
        component: undefined,
        type: 0,
        path: undefined,
        remark: undefined
      }
    },
    // 导入
    handleImport () {
      this.dialogImportVisible = true
    },
    // 显示导出弹窗
    handleExport () {
      this.dialogExportVisible = true
    },
    // 导出
    handleExportMenu () {
      // 获取选中节点
      const keys = this.$refs.menuTree.getCheckedKeys(true).concat(this.$refs.menuTree.getHalfCheckedKeys())
      let ids = []
      if (keys.length === 0) {
        this.$confirm('是否导出所有菜单?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'success'
        }).then(() => {
          exportObj(ids).then(response => {
            // 导出Excel
            exportExcel(response)
          })
        }).catch(() => {})
      } else {
        this.$confirm('是否导出选中的菜单?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'success'
        }).then(() => {
          for (let i = 0; i < keys.length; i++) {
            ids.push(keys[i])
          }
          exportObj(ids).then(response => {
            // 导出Excel
            exportExcel(response)
          })
        }).catch(() => {})
      }
    },
    // 上传前
    beforeMenuUpload (file) {
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
    handleUploadProgress (event, file, fileList) {
      this.uploading = true
      this.percentage = parseInt(file.percentage.toFixed(0))
    },
    // 上传成功
    handleUploadMenuSuccess () {
      notifySuccess(this, '导入成功')
      this.dialogImportVisible = false
      this.getList()
      this.uploading = false
    }
  }
}
</script>

<style scoped>
  .tab-container{
    margin: 30px;
  }
  .filter-tree {
    overflow: hidden;
  }
</style>
