<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button v-if="hasPermission(['sys:message:add'])" type="primary" @click="handleCreate"> 新增消息 </a-button>
      </template>
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: 'clarity:note-edit-line',
              onClick: handleEdit.bind(null, record),
              auth: 'sys:message:edit',
            },
            {
              icon: 'ant-design:delete-outlined',
              color: 'error',
              auth: 'sys:message:del',
              popConfirm: {
                title: '是否确认删除',
                confirm: handleDelete.bind(null, record),
              },
            }
          ]"
        />
      </template>
    </BasicTable>
    <MessageModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts">
import { defineComponent } from 'vue';
import { BasicTable, useTable, TableAction } from '/@/components/Table';
import { getMessageList, deleteMessage } from '/@/api/sys/message';
import { useModal } from '/@/components/Modal';
import MessageModal from './MessageModal.vue';
import { columns, searchFormSchema } from './message.data';
import {useMessage} from "/@/hooks/web/useMessage";
import {usePermission} from "/@/hooks/web/usePermission";
export default defineComponent({
  name: 'MessageManagement',
  components: { BasicTable, MessageModal, TableAction },
  setup() {
    const { hasPermission } = usePermission();
    const [registerModal, { openModal }] = useModal();
    const { createMessage } = useMessage();
    const [registerTable, { reload }] = useTable({
      title: '消息管理',
      api: getMessageList,
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

    function handleEdit(record: Recordable) {
      openModal(true, {
        record,
        isUpdate: true,
      });
    }
    async function handleDelete(record: Recordable) {
      await deleteMessage(record.id);
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
