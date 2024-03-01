<template>
  <div>
    <BasicModal
      width="80%"
      v-bind="$attrs"
      @register="registerCategoryModal"
      title="题目分类管理"
      @ok="handleSubmitCategory"
    >
      <BasicTable @register="registerTable" @fetch-success="onFetchSuccess">
        <template #toolbar>
          <a-button
            v-if="hasPermission(['exam:subject:category:add'])"
            type="primary"
            @click="handleCreate"
          >
            {{ t('common.addText') }}
          </a-button>
        </template>
        <template #action="{ record }">
          <TableAction
            :actions="[
              {
                icon: 'clarity:note-edit-line',
                onClick: handleEdit.bind(null, record),
                auth: 'exam:subject:category:edit',
              },
              {
                icon: 'ant-design:delete-outlined',
                color: 'error',
                auth: 'exam:subject:category:del',
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
    </BasicModal>
    <CategoryDataModal @register="registerCategoryDataModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts">
  import { useI18n } from '/@/hooks/web/useI18n';
  import { defineComponent, nextTick } from 'vue';
  import { BasicTable, TableAction, useTable } from '/@/components/Table';
  import { deleteCategory, getSubjectCategoryTree } from '/@/api/exam/subjectCategory';
  import { BasicModal, useModal, useModalInner } from '/@/components/Modal';
  import CategoryDataModal from './CategoryDataModal.vue';
  import { columns, searchFormSchema } from './category.data';
  import { usePermission } from '/@/hooks/web/usePermission';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { exportSubjects } from '/@/api/exam/subject';
  export default defineComponent({
    name: 'CategoryManagement',
    components: { BasicTable, CategoryDataModal, TableAction, BasicModal },
    emits: ['success', 'register'],
    setup(_, { emit }) {
      const { t } = useI18n();
      const { hasPermission } = usePermission();
      const { createMessage } = useMessage();
      // 新增、编辑弹框
      const [registerCategoryDataModal, { openModal: openCategoryDataModal }] = useModal();
      const [registerCategoryModal, { setModalProps, closeModal }] = useModalInner(() => {
        setModalProps({ confirmLoading: false });
      });

      // 分类列表
      const [registerTable, { reload, expandAll }] = useTable({
        title: '分类列表',
        api: getSubjectCategoryTree,
        columns,
        formConfig: {
          labelWidth: 120,
          schemas: searchFormSchema,
        },
        isTreeTable: true,
        pagination: false,
        striped: false,
        useSearchForm: true,
        showTableSetting: true,
        bordered: true,
        showIndexColumn: false,
        canResize: false,
        actionColumn: {
          width: 100,
          title: t('common.operationText'),
          dataIndex: 'action',
          slots: { customRender: 'action' },
          fixed: undefined,
        },
      });

      function handleCreate() {
        openCategoryDataModal(true, {
          isUpdate: false,
        });
      }

      function handleEdit(record: Recordable) {
        openCategoryDataModal(true, {
          record,
          isUpdate: true,
        });
      }

      async function handleDelete(record: Recordable) {
        await deleteCategory(record.id);
        createMessage.success(t('common.operationSuccessText'));
        await reload();
      }

      function handleSuccess() {
        createMessage.success(t('common.operationSuccessText'));
        reload();
      }

      function onFetchSuccess() {
        nextTick(expandAll);
      }

      function handleSubmitCategory() {
        try {
          setModalProps({ confirmLoading: true });
          closeModal();
          emit('success');
        } finally {
          setModalProps({ confirmLoading: false });
        }
      }

      function handleExport(record: Recordable) {
        let url = '?categoryId=' + record.id;
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
        registerCategoryModal,
        registerTable,
        registerCategoryDataModal,
        handleCreate,
        handleEdit,
        handleDelete,
        handleSuccess,
        onFetchSuccess,
        handleSubmitCategory,
        handleExport,
      };
    },
  });
</script>
