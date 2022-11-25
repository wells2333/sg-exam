<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button v-if="hasPermission(['sys:role:add'])" type="primary" @click="handleCreate"> 新增角色 </a-button>
      </template>
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: 'clarity:note-edit-line',
              onClick: handleEdit.bind(null, record),
              auth: 'sys:role:edit'
            },
            {
              icon: 'ant-design:delete-outlined',
              color: 'error',
               auth: 'sys:role:del',
              popConfirm: {
                title: '是否确认删除',
                confirm: handleDelete.bind(null, record),
              },
            },
          ]"
        />
      </template>
    </BasicTable>
    <RoleModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts">
import { defineComponent } from 'vue';
import { BasicTable, useTable, TableAction } from '/@/components/Table';
import { getRoleList, deleteRole } from '/@/api/sys/role';
import { useModal } from '/@/components/Modal';
import RoleModal from './RoleModal.vue';
import { columns, searchFormSchema } from './role.data';
import {useMessage} from "/@/hooks/web/useMessage";
import { usePermission } from '/@/hooks/web/usePermission';

export default defineComponent({
  name: 'RoleManagement',
  components: { BasicTable, RoleModal, TableAction },
  setup() {
    const { hasPermission } = usePermission();
    const { createMessage } = useMessage();
    const [registerModal, { openModal }] = useModal();
    const [registerTable, { reload }] = useTable({
      title: '角色列表',
      api: getRoleList,
      columns,
      formConfig: {
        labelWidth: 120,
        schemas: searchFormSchema,
      },
      useSearchForm: true,
      showTableSetting: true,
      bordered: true,
      showIndexColumn: false,
      actionColumn: {
        width: 80,
        title: '操作',
        dataIndex: 'action',
        slots: { customRender: 'action' },
        fixed: undefined,
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

    async function handleDelete(record: Recordable) {
      await deleteRole(record.id);
      createMessage.success('操作成功');
      await reload();
    }

    function handleSuccess() {
      createMessage.success('操作成功');
      reload();
    }

    return {
      hasPermission,
      registerTable,
      registerModal,
      handleCreate,
      handleEdit,
      handleDelete,
      handleSuccess,
    };
  },
});
</script>
