<template>
  <PageWrapper dense contentClass="flex">
    <SubjectCategoryTree class="w-1/5 xl:w-1/6" @select="handleSelect"/>
    <BasicTable @register="registerTable" class="w-3/4 xl:w-4/5" :searchInfo="searchInfo">
      <template #toolbar>
        <a-button v-if="hasPermission(['exam:subject:bank:add'])" type="primary" @click="handleCreate"> 手动添加</a-button>
        <a-button v-if="hasPermission(['exam:subject:bank:import'])" type="primary" @click="handleImport"> 批量导入</a-button>
      </template>
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: 'clarity:note-edit-line',
              onClick: handleEdit.bind(null, record),
              auth: 'exam:subject:bank:edit',
            },
            {
              icon: 'ant-design:delete-outlined',
              color: 'error',
              auth: 'exam:subject:bank:del',
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
    <ImportModal @register="registerImportModal" @success="handleImportSuccess"></ImportModal>
  </PageWrapper>
</template>
<script lang="ts">
import {defineComponent, reactive, unref} from 'vue';
import {BasicTable, TableAction, useTable} from '/@/components/Table';
import {deleteSubject, getSubjectList} from '/@/api/exam/subject';
import {PageWrapper} from '/@/components/Page';
import SubjectCategoryTree from './SubjectCategoryTree.vue';
import {useModal} from '/@/components/Modal';
import {columns, searchFormSchema} from './subject.data';
import SubjectModal from "./SubjectModal.vue";
import ImportModal from "./ImportModal.vue";
import { useMessage } from '/@/hooks/web/useMessage';
import {usePermission} from "/@/hooks/web/usePermission";

export default defineComponent({
  name: 'SubjectManagement',
  components: {BasicTable, PageWrapper, SubjectCategoryTree, TableAction, SubjectModal, ImportModal},
  setup() {
    const {hasPermission} = usePermission();
    const [registerModal, { openModal }] = useModal();
    const [registerImportModal, {openModal: openImportModal}] = useModal();
    const {createMessage} = useMessage();
    const searchInfo = reactive<Recordable>({});
    const [registerTable, {reload}] = useTable({
      title: '题目列表',
      api: (arg) => {
        const {categoryId} = searchInfo;
        if (categoryId === undefined) {
          return undefined;
        }
        const params = {categoryId};
        Object.assign(params, arg);
        return getSubjectList(params);
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
      handleSearchInfoFn(info) {
        return info;
      },
      showIndexColumn: false,
      canResize: false,
      actionColumn: {
        width: 80,
        title: '操作',
        dataIndex: 'action',
        slots: {customRender: 'action'},
        fixed: undefined,
      },
    });

    function handleCreate() {
      const {categoryId} = searchInfo;
      if (categoryId === undefined) {
        createMessage.warning('请选择题目分类');
        return;
      }
      openModal(true, {
        isUpdate: false,
        categoryId
      });
    }

    function handleEdit(record: Recordable) {
      const {categoryId} = searchInfo;
      if (categoryId === undefined || categoryId === '') {
        createMessage.warning('请选择题目分类');
        return;
      }
      openModal(true, {
        record,
        isUpdate: true,
        categoryId,
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

    function handleSelect(categoryId = '') {
      searchInfo.categoryId = categoryId;
      reload();
    }

    function handleSubjectDataSuccess() {
      reload();
    }

    function handleImport() {
      const {categoryId} = searchInfo;
      if (categoryId === undefined || categoryId === '') {
        createMessage.warning('请选择题目分类');
        return;
      }
      openImportModal(true, {
        categoryId
      });
    }

    function handleImportSuccess() {
      reload();
    }

    return {
      hasPermission,
      registerTable,
      registerModal,
      registerImportModal,
      handleCreate,
      handleEdit,
      handleDelete,
      handleSuccess,
      handleSelect,
      searchInfo,
      handleSubjectDataSuccess,
      handleImport,
      handleImportSuccess
    };
  },
});
</script>
