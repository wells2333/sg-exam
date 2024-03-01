<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button v-if="hasPermission(['exam:exam:add'])" type="primary" @click="handleCreate">
          {{ t('common.addText') }}
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
              tooltip: t('common.editText'),
              onClick: handleEdit.bind(null, record),
              auth: 'exam:exam:edit'
            },
            {
              icon: 'ant-design:delete-outlined',
              tooltip: t('common.delText'),
              color: 'error',
              auth: 'exam:exam:del',
              popConfirm: {
                title: t('common.confirmDelText'),
                confirm: handleDelete.bind(null, record),
              },
            },
            {
                icon: 'ant-design:export-outlined',
                tooltip: t('common.modules.exam.export'),
                popConfirm: {
                  title: t('common.confirmExportText'),
                  confirm: handleExport.bind(null, record),
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
import { useI18n } from '/@/hooks/web/useI18n';
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
import { exportSubjects } from '/@/api/exam/subject';
export default defineComponent({
  name: 'ExaminationManagement',
  components: {BasicTable, ExaminationModal, ExaminationDetailDrawer, TableAction},
  setup() {
    const { t } = useI18n();
    const {hasPermission} = usePermission();
    const { createMessage } = useMessage();
    const [registerModal, {openModal}] = useModal();
    const [registerExamImageModal] = useModal();
    const go = useGo();
    const [registerTable, {reload}] = useTable({
      title: t('common.modules.exam.examination') + t('common.list'),
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
      await deleteExamination(record.id);
      createMessage.success(t('common.operationSuccessText'));
      await reload();
    }

    function handleSuccess() {
      createMessage.success(t('common.operationSuccessText'));
      reload();
    }

    function handleSubjects(record: Recordable) {
      go('/exam/examination_subjects/' + record.id);
    }
    function handleExport(record: Recordable) {
        let url = '?examinationId=' + record.id;
        exportSubjects(url).then((res) => {
          const url = window.URL.createObjectURL(
            new Blob([res], { type: 'application/octet-stream' }),
          );
          let link = document.createElement('a');
          link.style.display = 'none';
          link.href = url;
          link.setAttribute('download', '题目.xlsx');
          document.body.appendChild(link);
          link.click();
          window.URL.revokeObjectURL(link.href);
          document.body.removeChild(link);
        });
      }

    return {
      t,
      hasPermission,
      registerTable,
      registerModal,
      registerExamImageModal,
      handleCreate,
      handleEdit,
      handleSubjects,
      handleDelete,
      handleSuccess,
      handleExport
    };
  },
});
</script>
