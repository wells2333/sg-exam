<template>
  <div>
    <BasicTable @register="registerTable" @fetch-success="onFetchSuccess">
      <template #toolbar>
        <a-button v-if="hasPermission(['exam:category:add'])" type="primary" @click="handleCreate"> 新增分类 </a-button>
      </template>
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: 'clarity:note-edit-line',
              onClick: handleEdit.bind(null, record),
              auth: 'exam:category:edit'
            },
            {
              icon: 'ant-design:delete-outlined',
              color: 'error',
              auth: 'exam:category:del',
              popConfirm: {
                title: '是否确认删除',
                confirm: handleDelete.bind(null, record),
              },
            },
          ]"
        />
      </template>
    </BasicTable>
    <CategoryModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts">
import {defineComponent, nextTick} from 'vue';
import { BasicTable, useTable, TableAction } from '/@/components/Table';
import { getSubjectCategoryTree, deleteCategory } from '/@/api/exam/subjectCategory';
import { useModal } from '/@/components/Modal';
import CategoryModal from './CategoryModal.vue';
import { columns, searchFormSchema } from './category.data';
import { usePermission } from '/@/hooks/web/usePermission';
import {useMessage} from "/@/hooks/web/useMessage";
export default defineComponent({
  name: 'CategoryManagement',
  components: { BasicTable, CategoryModal, TableAction },
  setup() {
    const { hasPermission } = usePermission();
    const { createMessage } = useMessage();
    const [registerModal, { openModal }] = useModal();
    const [registerTable, { reload, expandAll}] = useTable({
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
        width: 80,
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
      await deleteCategory(record.id);
      createMessage.success('操作成功');
      await reload();
    }
    function handleSuccess() {
      createMessage.success('操作成功');
      reload();
    }
    function onFetchSuccess() {
      nextTick(expandAll);
    }
    return {
      hasPermission,
      registerTable,
      registerModal,
      handleCreate,
      handleEdit,
      handleDelete,
      handleSuccess,
      onFetchSuccess
    };
  },
});
</script>
