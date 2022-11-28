<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button v-if="hasPermission(['exam:exam:add'])" type="primary" @click="handleCreate"> 新增
        </a-button>
      </template>
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: 'ant-design:align-left-outlined',
              tooltip: '题目管理',
              onClick: handleSubjects.bind(null, record),
              auth: 'exam:exam:subject:add'
            },
            {
              icon: 'clarity:note-edit-line',
              tooltip: '编辑',
              onClick: handleEdit.bind(null, record),
              auth: 'exam:exam:edit'
            },
            {
              icon: 'ant-design:delete-outlined',
              tooltip: '删除',
              color: 'error',
              auth: 'exam:exam:del',
              popConfirm: {
                title: '是否确认删除',
                confirm: handleDelete.bind(null, record),
              },
            },
          ]"
        />
      </template>
    </BasicTable>
    <ExaminationModal width="80%" @register="registerModal" @success="handleSuccess"/>
  </div>
</template>
<script lang="ts">
import {defineComponent} from 'vue';
import {BasicTable, TableAction, useTable} from '/@/components/Table';
import {deleteExamination, getExaminationList} from '/@/api/exam/examination';
import {useModal} from '/@/components/Modal';
import ExaminationModal from './ExaminationModal.vue';
import ExaminationDetailDrawer from './ExaminationDetail.vue';
import {columns, searchFormSchema} from './examination.data';
import {useGo} from "/@/hooks/web/usePage";
import {usePermission} from '/@/hooks/web/usePermission';
import {useMessage} from "/@/hooks/web/useMessage";

export default defineComponent({
  name: 'ExaminationManagement',
  components: {BasicTable, ExaminationModal, ExaminationDetailDrawer, TableAction},
  setup() {
    const {hasPermission} = usePermission();
    const { createMessage } = useMessage();
    const [registerModal, {openModal}] = useModal();
    const [registerExamImageModal] = useModal();
    const go = useGo();
    const [registerTable, {reload}] = useTable({
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
      await deleteExamination(record.id);
      createMessage.success('操作成功');
      await reload();
    }

    function handleSuccess() {
      createMessage.success('操作成功');
      reload();
    }

    function handleSubjects(record: Recordable) {
      go('/exam/examination_subjects/' + record.id);
    }

    return {
      hasPermission,
      registerTable,
      registerModal,
      registerExamImageModal,
      handleCreate,
      handleEdit,
      handleSubjects,
      handleDelete,
      handleSuccess
    };
  },
});
</script>
