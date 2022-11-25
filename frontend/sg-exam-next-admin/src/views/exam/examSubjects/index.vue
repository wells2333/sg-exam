<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" @click="handleCreate"> 新增题目 </a-button>
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
    <SubjectModal @register="registerModal" @success="handleSubjectDataSuccess"></SubjectModal>
  </div>
</template>
<script lang="ts">
import {defineComponent, ref} from 'vue';
import { BasicTable, useTable, TableAction } from '/@/components/Table';
import { useModal } from '/@/components/Modal';
import { columns, searchFormSchema } from '../subject/subject.data';
import {useRoute} from "vue-router";
import {getExaminationSubjectList} from "/@/api/exam/examination";
import SubjectModal from "./SubjectModal.vue";
import {deleteSubject} from '/@/api/exam/subject';
import {useMessage} from "/@/hooks/web/useMessage";

export default defineComponent({
  name: 'SubjectManagement',
  components: { BasicTable, TableAction, SubjectModal },
  setup() {
    const [registerModal, { openModal }] = useModal();
    const { createMessage } = useMessage();
    const route = useRoute();
    const examinationId = ref<any>(route.params?.id);
    const [registerTable, { reload }] = useTable({
      title: '题目列表',
      api: getExaminationSubjectList,
      searchInfo: {
        examinationId
      },
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
        width: 180,
        title: '操作',
        dataIndex: 'action',
        slots: { customRender: 'action' },
        fixed: undefined,
      },
    });
    function handleCreate() {
      openModal(true, {
        isUpdate: false,
        examinationId
      });
    }
    function handleEdit(record: Recordable) {
      openModal(true, {
        record,
        isUpdate: true,
        examinationId,
        type: record.type,
      });
    }
    async function handleDelete(record: Recordable) {
      await deleteSubject(record.id);
      createMessage.success('操作成功');
      await reload();
    }
    function handleSuccess() {
      createMessage.success('操作成功');
      reload();
    }
    function handleSubjectDataSuccess() {
      reload();
    }

    return {
      registerTable,
      registerModal,
      handleCreate,
      handleEdit,
      handleDelete,
      handleSuccess,
      handleSubjectDataSuccess
    };
  },
});
</script>

<style lang="less">


</style>
