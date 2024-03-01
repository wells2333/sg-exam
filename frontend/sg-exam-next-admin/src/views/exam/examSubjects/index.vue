<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" @click="handleCreate"> {{ t('common.addText') }} </a-button>
        <a-button type="primary" @click="handleSelectSubjects"> 从题库选择</a-button>
        <a-button type="primary" @click="handleRandomSelectSubjects"> 随机组题</a-button>
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
    <SubjectModal @register="registerModal" @success="handleSubjectDataSuccess"></SubjectModal>
    <SelectSubjectModal @register="registerSelectSubjectModal" @success="handleSelectSubjectSuccess"></SelectSubjectModal>
    <RandomSubjectModal @register="registerRandomSubjectModal" @success="handleRandomSubjectSuccess"></RandomSubjectModal>
  </div>
</template>
<script lang="ts">
import { useI18n } from '/@/hooks/web/useI18n';
import {defineComponent, ref, unref} from 'vue';
import {BasicTable, TableAction, useTable} from '/@/components/Table';
import {useModal} from '/@/components/Modal';
import {columns, searchFormSchema} from '../subject/subject.data';
import {useRoute} from "vue-router";
import {getExaminationSubjectList} from "/@/api/exam/examination";
import SubjectModal from "./SubjectModal.vue";
import SelectSubjectModal from "./SelectSubjectModal.vue";
import RandomSubjectModal from "./RandomSubjectModal.vue";
import {deleteSubject} from '/@/api/exam/subject';
import {useMessage} from "/@/hooks/web/useMessage";
import { exportSubjects } from '/@/api/exam/subject';
export default defineComponent({
  name: 'SubjectManagement',
  components: {BasicTable, TableAction, SubjectModal, SelectSubjectModal, RandomSubjectModal},
  setup() {
    const { t } = useI18n();
    const [registerModal, {openModal}] = useModal();
    const [registerSelectSubjectModal, {openModal: openSelectSubjectModal}] = useModal();
    const [registerRandomSubjectModal, {openModal: openRandomSubjectModal}] = useModal();
    const {createMessage} = useMessage();
    const route = useRoute();
    const examinationId = ref<any>(route.params?.id);
    const [registerTable, {reload, getPaginationRef}] = useTable({
      title: t('common.modules.exam.subject') + t('common.list'),
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
        title: t('common.operationText'),
        dataIndex: 'action',
        slots: {customRender: 'action'},
        fixed: undefined,
      }
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
      await deleteSubject(record.id, unref(examinationId));
      createMessage.success(t('common.operationSuccessText'));
      await reload();
    }

    function handleSuccess() {
      createMessage.success(t('common.operationSuccessText'));
      reload();
    }

    function handleSubjectDataSuccess() {
      reload();
    }

    function handleSelectSubjects() {
      openSelectSubjectModal(true, {
        examinationId
      });
    }

    function handleRandomSelectSubjects() {
      const paginationRef = getPaginationRef();
      openRandomSubjectModal(true, {
        isUpdate: false,
        examinationId,
        currentCnt: paginationRef.total
      });
    }

    function handleSelectSubjectSuccess() {
      reload();
    }

    function handleRandomSubjectSuccess() {
      reload();
    }
    function handleExport(record: Recordable) {
        let url = '?ids=' + record.id;
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
      registerTable,
      registerModal,
      registerSelectSubjectModal,
      registerRandomSubjectModal,
      handleCreate,
      handleEdit,
      handleDelete,
      handleSuccess,
      handleSubjectDataSuccess,
      handleSelectSubjects,
      handleRandomSelectSubjects,
      handleSelectSubjectSuccess,
      handleRandomSubjectSuccess,
      handleExport
    };
  },
});
</script>

<style lang="less" scoped>

</style>
