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
              icon: 'ant-design:eye-outlined',
              tooltip: '查看详情',
              onClick: handleView.bind(null, record),
              auth: 'exam:exam:view'
            },
            {
              icon: 'clarity:note-edit-line',
              tooltip: '编辑',
              onClick: handleEdit.bind(null, record),
              auth: 'exam:exam:edit'
            },
            {
              icon: 'ant-design:align-left-outlined',
              tooltip: '题目管理',
              onClick: handleSubjects.bind(null, record),
              auth: 'exam:exam:subject:add'
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
    <ExaminationDetailDrawer @register="registerDetailDrawer"/>
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
import {useDrawer} from "/@/components/Drawer";
import {useGo} from "/@/hooks/web/usePage";
import {usePermission} from '/@/hooks/web/usePermission';

export default defineComponent({
  name: 'ExaminationManagement',
  components: {BasicTable, ExaminationModal, ExaminationDetailDrawer, TableAction},
  setup() {
    const {hasPermission} = usePermission();
    const [registerDetailDrawer, {openDrawer: openDetailDrawer}] = useDrawer();
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

    function handleView(record: Recordable) {
      openDetailDrawer(true, {record});
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

    return {
      hasPermission,
      registerTable,
      registerModal,
      registerDetailDrawer,
      registerExamImageModal,
      handleView,
      handleCreate,
      handleEdit,
      handleSubjects,
      handleDelete,
      handleSuccess
    };
  },
});
</script>
