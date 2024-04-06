<template>
  <div>
    <BasicTable @register="registerTable"
    :rowSelection="{ type: 'checkbox'}">
      <template #toolbar>
        <a-button type="primary" @click="handleCreate"> {{ t('common.addText') }} </a-button>
        <a-button type="primary" @click="handleSelectSubjects"> 从题库选择</a-button>
        <a-button type="primary" @click="handleRandomSelectSubjects"> 随机组题</a-button>
        <PopConfirmButton
          v-if="hasPermission(['exam:subject:bank:del'])"
          title="确定删除么？"
          okText="确认"
          cancelText="取消"
          @confirm="handleDeleteBatch"
          type="danger"
          >删除
        </PopConfirmButton>
      </template>
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: 'clarity:note-edit-line',
              auth: 'exam:subject:bank:edit',
              onClick: handleEdit.bind(null, record),
            },
            {
              icon: 'ant-design:delete-outlined',
              color: 'error',
              auth: 'exam:subject:bank:del',
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
    <SelectSubjectModal
      @register="registerSelectSubjectModal"
      @success="handleSelectSubjectSuccess"
    ></SelectSubjectModal>
    <RandomSubjectModal
      @register="registerRandomSubjectModal"
      @success="handleRandomSubjectSuccess"
    ></RandomSubjectModal>
  </div>
</template>
<script lang="ts">
  import { useI18n } from '/@/hooks/web/useI18n';
  import { defineComponent, ref, unref } from 'vue';
  import { BasicTable, TableAction, useTable } from '/@/components/Table';
  import { useModal } from '/@/components/Modal';
  import { columns, searchFormSchema } from '../subject/subject.data';
  import { useRoute } from 'vue-router';
  import { getMaterialSubjectList } from '/@/api/exam/material';
  import SubjectModal from './SubjectModal.vue';
  import SelectSubjectModal from './SelectSubjectModal.vue';
  import RandomSubjectModal from './RandomSubjectModal.vue';
  import { deleteSubject,deleteBatchSubject } from '/@/api/exam/subject';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { exportSubjects } from '/@/api/exam/subject';
  import { PopConfirmButton } from '/@/components/Button';
  import { usePermission } from '/@/hooks/web/usePermission';
  export default defineComponent({
    name: 'SubjectManagement',
    components: {
      BasicTable,
      TableAction,
      SubjectModal,
      SelectSubjectModal,
      RandomSubjectModal,
      PopConfirmButton
    },
    setup() {
      const { t } = useI18n();
      const [registerModal, { openModal }] = useModal();
      const [registerSelectSubjectModal, { openModal: openSelectSubjectModal }] = useModal();
      const [registerRandomSubjectModal, { openModal: openRandomSubjectModal }] = useModal();
      const { createMessage } = useMessage();
      const route = useRoute();
      const materialId = ref<any>(route.params?.materialId);
      const examinationId = ref<any>(route.params?.examinationId);
      const { hasPermission } = usePermission();
      const [registerTable, { reload, getPaginationRef,getSelectRows, clearSelectedRowKeys }] = useTable({
        title: t('common.modules.exam.subject') + t('common.list'),
        api: getMaterialSubjectList,
        searchInfo: {
          materialId,
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
        clickToRowSelect: false,
        actionColumn: {
          width: 180,
          title: t('common.operationText'),
          dataIndex: 'action',
          slots: { customRender: 'action' },
          fixed: undefined,
        },
      });

      function handleCreate() {
      
        openModal(true, {
          isUpdate: false,
          materialId,
          examinationId
        });
      }

      function handleEdit(record: Recordable) {
        openModal(true, {
          record,
          isUpdate: true,
          materialId,
          examinationId,
          type: record.type,
        });
      }

      async function handleDelete(record: Recordable) {
        await deleteSubject(record.id, unref(materialId),unref(examinationId));
        createMessage.success(t('common.operationSuccessText'));
        await reload();
      }
      async function handleDeleteBatch() {
        const ids = getSelectedRowIds();
        if (!ids || ids.length === 0) {
          return;
        }
        await deleteBatchSubject(ids);
        createMessage.success(t('common.operationSuccessText'));
        clearSelectedRowKeys()
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
          materialId,
          examinationId
        });
      }

      function handleRandomSelectSubjects() {
        const paginationRef = getPaginationRef();
        openRandomSubjectModal(true, {
          isUpdate: false,
          materialId,
          examinationId,
          currentCnt: paginationRef.total,
        });
      }

      function handleSelectSubjectSuccess() {
        reload();
      }

      function handleRandomSubjectSuccess() {
        reload();
      }
      function handleExport(record: Recordable) {
        const ids = getSelectedRowIds();
        if (!ids || ids.length === 0) {
          return;
        }
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
          clearSelectedRowKeys()
        });
      }
      function getSelectedRowIds() {
        const rows = getSelectRows();
        if (!rows || rows.length === 0) {
          return undefined;
        }
        console.log("test")
        console.log(rows)
        const ids = [];
        rows.forEach(e => {
          ids.push(e.id);
        });
        return ids;
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
        handleExport,
        handleDeleteBatch,
        hasPermission,
        getSelectRows,
        clearSelectedRowKeys
      };
    },
  });
</script>

<style lang="less" scoped></style>
