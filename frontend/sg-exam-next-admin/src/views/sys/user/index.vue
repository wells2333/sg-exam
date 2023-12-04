<template>
  <PageWrapper dense contentClass="flex">
    <DeptTree class="w-1/4 xl:w-1/7" @select="handleSelect"/>
    <BasicTable @register="registerTable" class="w-3/4 xl:w-4/5" :searchInfo="searchInfo">
      <template #toolbar>
        <a-button v-if="hasPermission(['sys:dept:add'])" type="primary" @click="handleManageDept"> 部门管理
        </a-button>
        <a-button v-if="hasPermission(['sys:user:add'])" type="primary" @click="handleCreate">新增账号
        </a-button>
      </template>
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: 'ant-design:eye-outlined',
              tooltip: '查看用户详情',
              onClick: handleView.bind(null, record),
              auth: 'sys:user:view'
            },
            {
              icon: 'clarity:note-edit-line',
              tooltip: '编辑用户资料',
              onClick: handleEdit.bind(null, record),
              auth: 'sys:user:edit'
            },
            {
              icon: 'ant-design:retweet-outlined',
              tooltip: '重置密码',
              auth: 'sys:user:edit',
              popConfirm: {
                title: '是否确认重置密码',
                confirm: handleResetPassword.bind(null, record)
              }
            },
            {
              icon: 'ant-design:delete-outlined',
              color: 'error',
              auth: 'sys:user:del',
              tooltip: '删除此账号',
              popConfirm: {
                title: t('common.confirmDelText'),
                confirm: handleDelete.bind(null, record),
              },
            },
          ]"
        />
      </template>
    </BasicTable>
    <UserDetailDrawer @register="registerDetailDrawer"/>
    <UserModal @register="registerModal" @success="handleSuccess"/>
    <DeptListModal @register="registerDeptListModal" @success="handleSuccess"/>
  </PageWrapper>
</template>
<script lang="ts">
import {useI18n} from '/@/hooks/web/useI18n';
import {defineComponent, reactive} from 'vue';
import {BasicTable, TableAction, useTable} from '/@/components/Table';
import {deleteUser, getUserList, resetPassword} from '/@/api/sys/user';
import {PageWrapper} from '/@/components/Page';
import DeptTree from './DeptTree.vue';
import {useModal} from '/@/components/Modal';
import UserModal from './UserModal.vue';
import UserDetailDrawer from './UserDetail.vue';
import {useDrawer} from '/@/components/Drawer';
import {columns, searchFormSchema} from './user.data';
import {useMessage} from "/@/hooks/web/useMessage";
import {usePermission} from '/@/hooks/web/usePermission';
import {ApiRes} from "/@/api/constant";
import DeptListModal from './DeptListModal.vue';

export default defineComponent({
  name: 'UserManagement',
  components: {BasicTable, PageWrapper, DeptTree, UserModal, UserDetailDrawer, TableAction, DeptListModal},
  setup() {
    const {t} = useI18n();
    const {hasPermission} = usePermission();
    const {createMessage} = useMessage();
    const [registerDetailDrawer, {openDrawer: openDetailDrawer}] = useDrawer();
    const [registerModal, {openModal}] = useModal();
    const [registerDeptListModal, {openModal: openDeptListModal}] = useModal();

    const searchInfo = reactive<Recordable>({});
    const [registerTable, {reload}] = useTable({
      title: t('common.modules.sys.user') + t('common.list'),
      api: getUserList,
      rowKey: 'id',
      columns,
      formConfig: {
        labelWidth: 120,
        schemas: searchFormSchema,
        autoSubmitOnEnter: true,
      },
      useSearchForm: true,
      showTableSetting: true,
      bordered: true,
      handleSearchInfoFn(info) {
        return info;
      },
      actionColumn: {
        width: 160,
        title: t('common.operationText'),
        dataIndex: 'action',
        slots: {customRender: 'action'},
      },
    });

    function handleCreate() {
      openModal(true, {
        isUpdate: false,
      });
    }

    function handleEdit(record: Recordable) {
      openModal(true, {
        record,
        isUpdate: true,
      });
    }

    async function handleResetPassword(record: Recordable) {
      const result: ApiRes = await resetPassword(record);
      if (result) {
        createMessage.success('重置成功');
        await reload();
      }
    }

    async function handleDelete(record: Recordable) {
      await deleteUser(record.id);
      createMessage.success(t('common.operationSuccessText'));
      await reload();
    }

    function handleSuccess() {
      createMessage.success(t('common.operationSuccessText'));
      reload();
    }

    function handleSelect(deptId = '') {
      searchInfo.deptId = deptId;
      reload();
    }

    function handleView(record: Recordable) {
      openDetailDrawer(true, {record});
    }

    function handleManageDept() {
      openDeptListModal(true, {
      });
    }

    return {
      t,
      hasPermission,
      registerTable,
      registerModal,
      registerDeptListModal,
      registerDetailDrawer,
      handleCreate,
      handleEdit,
      handleResetPassword,
      handleDelete,
      handleSuccess,
      handleSelect,
      handleView,
      handleManageDept,
      searchInfo,
    };
  },
});
</script>
