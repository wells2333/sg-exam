<template>
  <div>
    <BasicTable @register="registerTable">
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: 'clarity:note-edit-line',
              onClick: handleEdit.bind(null, record),
              auth: 'sys:log:edit',
            },
            {
              icon: 'ant-design:delete-outlined',
              color: 'error',
              auth: 'sys:log:del',
              popConfirm: {
                title: '是否确认删除',
                confirm: handleDelete.bind(null, record),
              },
            }
          ]"
        />
      </template>
    </BasicTable>
    <LogModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts">
import { defineComponent } from 'vue';
import { BasicTable, useTable, TableAction } from '/@/components/Table';
import { getLogList, deleteLog } from '/@/api/sys/log';
import { useModal } from '/@/components/Modal';
import LogModal from './LogModal.vue';
import { columns, searchFormSchema } from './log.data';
import {useMessage} from "/@/hooks/web/useMessage";
export default defineComponent({
  name: 'LogManagement',
  components: { BasicTable, LogModal, TableAction },
  setup() {
    const [registerModal, { openModal }] = useModal();
    const { createMessage } = useMessage();
    const [registerTable, { reload }] = useTable({
      title: '操作日志',
      api: getLogList,
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
    function handleEdit(record: Recordable) {
      openModal(true, {
        record,
        isUpdate: true,
      });
    }
    async function handleDelete(record: Recordable) {
      await deleteLog(record.id);
      createMessage.success('操作成功');
      await reload();
    }
    function handleSuccess() {
      createMessage.success('操作成功');
      reload();
    }
    return {
      registerTable,
      registerModal,
      handleEdit,
      handleDelete,
      handleSuccess,
    };
  },
});
</script>
