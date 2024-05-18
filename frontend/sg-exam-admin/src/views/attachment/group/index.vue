<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" @click="handleCreate"> {{ t('common.addText') }}</a-button>
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
          ]"
        />
      </template>
    </BasicTable>
    <AttachGroupModal @register="registerModal" @success="handleSuccess"/>
  </div>
</template>
<script lang="ts">
import {useI18n} from '/@/hooks/web/useI18n';
import {defineComponent} from 'vue';
import {BasicUpload} from '/@/components/Upload';
import {BasicTable, useTable, TableAction} from '/@/components/Table';
import {getAttachGroupList, deleteAttachGroup} from '/@/api/attachment/group';
import {useModal} from '/@/components/Modal';
import AttachGroupModal from './AttachGroupModal.vue';
import {columns, searchFormSchema} from './group.data';
import {useMessage} from "/@/hooks/web/useMessage";

export default defineComponent({
  name: 'AttachGroupManagement',
  components: {BasicTable, AttachGroupModal, TableAction, BasicUpload},
  setup() {
    const {t} = useI18n();
    const [registerModal, {openModal}] = useModal();
    const {createMessage} = useMessage();
    const [registerTable, {reload}] = useTable({
      title: t('common.modules.attachment.group') + t('common.list'),
      api: getAttachGroupList,
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
        width: 150,
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
      const result = await deleteAttachGroup(record.id);
      if (result) {
        createMessage.success(t('common.delSuccessText'));
        await reload();
      } else {
        createMessage.error(t('common.delFailedText'));
      }
    }

    function handleSuccess() {
      reload();
    }

    return {
      t,
      registerTable,
      registerModal,
      handleCreate,
      handleEdit,
      handleDelete,
      handleSuccess
    };
  },
});
</script>
