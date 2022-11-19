<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" @click="handleCreate"> 新增租户 </a-button>
      </template>
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: 'clarity:note-edit-line',
              onClick: handleEdit.bind(null, record),
            },
            {
              icon: 'ant-design:delete-outlined',
              color: 'error',
              popConfirm: {
                title: '是否确认删除',
                confirm: handleDelete.bind(null, record),
              },
            },
            {
              icon: 'ant-design:reload-outlined',
              popConfirm: {
                title: '是否重新初始化',
                confirm: handleInit.bind(null, record),
              },
           },
          ]"
        />
      </template>
    </BasicTable>
    <TenantModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts">
import { defineComponent } from 'vue';
import { BasicTable, useTable, TableAction } from '/@/components/Table';
import { getTenantList, deleteTenant, initTenant } from '/@/api/sys/tenant';
import { useModal } from '/@/components/Modal';
import TenantModal from './TenantModal.vue';
import { columns, searchFormSchema } from './tenant.data';
import {useMessage} from "/@/hooks/web/useMessage";
export default defineComponent({
  name: 'TenantManagement',
  components: { BasicTable, TenantModal, TableAction },
  setup() {
    const [registerModal, { openModal }] = useModal();
    const { createMessage } = useMessage();
    const [registerTable, { reload }] = useTable({
      title: '租户列表',
      api: getTenantList,
      columns,
      formConfig: {
        labelWidth: 120,
        schemas: searchFormSchema,
      },
      pagination: true,
      striped: false,
      useSearchForm: true,
      showTableSetting: true,
      bordered: true,
      showIndexColumn: false,
      canResize: false,
      actionColumn: {
        width: 120,
        title: '操作',
        dataIndex: 'action',
        slots: { customRender: 'action' },
        fixed: undefined,
      },
    });
    async function handleInit(record: Recordable) {
      await initTenant(record.id);
      await reload();
    }
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
      await deleteTenant(record.id);
      await reload();
    }
    function handleSuccess() {
      createMessage.success('操作成功');
      reload();
    }
    return {
      registerTable,
      registerModal,
      handleInit,
      handleCreate,
      handleEdit,
      handleDelete,
      handleSuccess,
    };
  },
});
</script>
