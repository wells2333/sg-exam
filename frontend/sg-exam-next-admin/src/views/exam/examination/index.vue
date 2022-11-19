<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" @click="handleCreate"> 新增 </a-button>
      </template>
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: 'ant-design:eye-outlined',
              tooltip: '查看详情',
              onClick: handleView.bind(null, record),
            },
            {
              icon: 'clarity:note-edit-line',
              tooltip: '编辑',
              onClick: handleEdit.bind(null, record),
            },
            {
              icon: 'ant-design:upload-outlined',
               tooltip: '上传图片',
              onClick: handleUpload.bind(null, record),
            },
            {
              icon: 'ant-design:align-left-outlined',
              tooltip: '题目管理',
              onClick: handleSubjects.bind(null, record),
            },
            {
              icon: 'ant-design:delete-outlined',
               tooltip: '删除',
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
    <ExaminationDetailDrawer @register="registerDetailDrawer" />
    <ExaminationModal @register="registerModal" @success="handleSuccess" />
    <ExamImageModal @register="registerExamImageModal" @success="handleSuccess"/>
  </div>
</template>
<script lang="ts">
import { defineComponent } from 'vue';
import { BasicTable, useTable, TableAction } from '/@/components/Table';
import { deleteExamination, getExaminationList } from '/@/api/exam/examination';
import { useModal } from '/@/components/Modal';
import ExaminationModal from './ExaminationModal.vue';
import ExaminationDetailDrawer from './ExaminationDetail.vue';
import ExamImageModal from "./ExamImageModal.vue";
import { columns, searchFormSchema } from './examination.data';
import { useDrawer } from "/@/components/Drawer";
import {useGo} from "/@/hooks/web/usePage";
export default defineComponent({
  name: 'ExaminationManagement',
  components: { BasicTable, ExaminationModal, ExaminationDetailDrawer, TableAction, ExamImageModal },
  setup() {
    const [registerDetailDrawer, { openDrawer: openDetailDrawer }] = useDrawer();
    const [registerModal, { openModal }] = useModal();
    const [registerExamImageModal, { openModal: openExamImageModal }] = useModal();
    const go = useGo();
    const [registerTable, { reload }] = useTable({
      title: '考试列表',
      api: getExaminationList,
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
        width: 200,
        title: '操作',
        dataIndex: 'action',
        slots: { customRender: 'action' },
        fixed: undefined,
      },
    });
    function handleView(record: Recordable) {
      openDetailDrawer(true, { record });
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
      await deleteExamination(record.id);
      await reload();
    }
    function handleSuccess() {
      reload();
    }

    function handleSubjects(record: Recordable) {
      go('/exam/examination_subjects/' + record.id);
    }
    function handleUpload(record: Recordable) {
      openExamImageModal(true, record);
    }
    return {
      registerTable,
      registerModal,
      registerDetailDrawer,
      registerExamImageModal,
      handleView,
      handleCreate,
      handleEdit,
      handleSubjects,
      handleDelete,
      handleSuccess,
      handleUpload,
    };
  },
});
</script>
