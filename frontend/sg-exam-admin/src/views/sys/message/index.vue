<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button v-if="hasPermission(['sys:message:add'])" type="primary" @click="handleCreate">
          {{ t('common.addText') }}
        </a-button>
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
                title: t('common.confirmDelText'),
                confirm: handleDelete.bind(null, record),
              },
            }
          ]"
        />
      </template>
    </BasicTable>
    <MessageModal @register="registerModal" @success="handleSuccess"/>
  </div>
</template>
<script lang="ts">
import {useI18n} from '/@/hooks/web/useI18n';
import {defineComponent} from 'vue';
import {BasicTable, useTable, TableAction} from '/@/components/Table';
import {getMessageList, deleteMessage} from '/@/api/sys/message';
import {useModal} from '/@/components/Modal';
import MessageModal from './MessageModal.vue';
import {columns, searchFormSchema} from './message.data';
import {useMessage} from "/@/hooks/web/useMessage";
import {usePermission} from "/@/hooks/web/usePermission";

export default defineComponent({
  name: 'MessageManagement',
  components: {BasicTable, MessageModal, TableAction},
  setup() {
    const {t} = useI18n();
    const {hasPermission} = usePermission();
    const [registerModal, {openModal}] = useModal();
    const {createMessage} = useMessage();
    const [registerTable, {reload}] = useTable({
      title: t('common.modules.sys.message') + t('common.list'),
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
        title: t('common.operationText'),
        dataIndex: 'action',
        slots: {customRender: 'action'},
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
      createMessage.success(t('common.operationSuccessText'));
      await reload();
    }

    function handleSuccess() {
      createMessage.success(t('common.operationSuccessText'));
      reload();
    }

    return {
      t,
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
