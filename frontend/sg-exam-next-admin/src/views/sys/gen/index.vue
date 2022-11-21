<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button v-if="hasPermission(['sys:gen:add'])" type="primary" @click="handleCreate"> 新增 </a-button>
      </template>
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: 'ant-design:arrow-down-outlined',
              auth: 'sys:gen:edit',
              popConfirm: {
                title: '生成代码',
                confirm: handleGen.bind(null, record),
                auth: 'sys:gen:edit'
              },
           },
            {
              icon: 'ant-design:delete-outlined',
              color: 'error',
              auth: 'sys:gen:del',
              popConfirm: {
                title: '是否确认删除',
                confirm: handleDelete.bind(null, record),
                auth: 'sys:gen:del',
              },
            },
          ]"
        />
      </template>
    </BasicTable>
    <GenModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts">
import { defineComponent } from 'vue';
import { BasicTable, useTable, TableAction } from '/@/components/Table';
import {getGenList, genCode, deleteGen} from '/@/api/sys/gen';
import { useModal } from '/@/components/Modal';
import GenModal from './GenModal.vue';
import { columns, searchFormSchema } from './gen.data';
import {useMessage} from "/@/hooks/web/useMessage";
import { usePermission } from '/@/hooks/web/usePermission';
export default defineComponent({
  name: 'GenManagement',
  components: { BasicTable, GenModal, TableAction },
  setup() {
    const { hasPermission } = usePermission();
    const [registerModal, { openModal }] = useModal();
    const { createMessage } = useMessage();
    const [registerTable, { reload }] = useTable({
      title: 'Table列表',
      api: getGenList,
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
    function handleCreate() {
      openModal(true, {
        isUpdate: false,
      });
    }
    function handleGen(record: Recordable) {
      genCode(record.tableName);
    }
    async function handleDelete(record: Recordable) {
      await deleteGen(record.tableId);
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
      handleGen,
      handleDelete,
      handleSuccess,
    };
  },
});
</script>
