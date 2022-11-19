<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" @click="handleCreate"> 新增语音 </a-button>
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
            }
          ]"
        />
      </template>
    </BasicTable>
    <SynthesisModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts">
import { defineComponent } from 'vue';
import { BasicTable, useTable, TableAction } from '/@/components/Table';
import { getSpeechSynthesisList, deleteSpeech } from '/@/api/speech/synthesis';
import { useModal } from '/@/components/Modal';
import SynthesisModal from './SynthesisModal.vue';
import { columns, searchFormSchema } from './synthesis.data';
import {useMessage} from "/@/hooks/web/useMessage";
export default defineComponent({
  name: 'SynthesisManagement',
  components: { BasicTable, SynthesisModal, TableAction },
  setup() {
    const [registerModal, { openModal }] = useModal();
    const { createMessage } = useMessage();
    const [registerTable, { reload }] = useTable({
      title: '语音列表',
      api: getSpeechSynthesisList,
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
      await deleteSpeech(record.id);
      await reload();
    }
    function handleSuccess() {
      createMessage.success('操作成功');
      reload();
    }
    return {
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
