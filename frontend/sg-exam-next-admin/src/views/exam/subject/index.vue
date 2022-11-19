<template>
  <PageWrapper dense contentFullHeight fixedHeight contentClass="flex">
    <SubjectCategoryTree class="w-1/5 xl:w-1/6" @select="handleSelect" />
    <BasicTable @register="registerTable" class="w-3/4 xl:w-4/5" :searchInfo="searchInfo">
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
  </PageWrapper>
</template>
<script lang="ts">
import {defineComponent, reactive} from 'vue';
import { BasicTable, useTable, TableAction } from '/@/components/Table';
import { getSubjectList, deleteSubject } from '/@/api/exam/subject';
import { PageWrapper } from '/@/components/Page';
import SubjectCategoryTree from './SubjectCategoryTree.vue';
import { useModal } from '/@/components/Modal';
import { columns, searchFormSchema } from './subject.data';
import { useGo } from "/@/hooks/web/usePage";
export default defineComponent({
  name: 'SubjectManagement',
  components: { BasicTable, PageWrapper, SubjectCategoryTree, TableAction },
  setup() {
    const [registerModal] = useModal();
    const searchInfo = reactive<Recordable>({});
    const go = useGo();
    const [registerTable, { reload }] = useTable({
      title: '题目列表',
      api: getSubjectList,
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
        slots: { customRender: 'action' },
        fixed: undefined,
      },
    });
    function handleCreate() {
      let url = '/exam/subject_detail/0?';
      const { categoryId } = searchInfo;
      if (categoryId && categoryId !== '') {
        url += 'categoryId=' + categoryId;
      }
      go(url);
    }
    function handleEdit(record: Recordable) {
      let url = '/exam/subject_detail/' + record.id + '?';
      const { categoryId } = searchInfo;
      if (categoryId) {
        url += 'categoryId=' + categoryId;
      }
      go(url);
    }
    async function handleDelete(record: Recordable) {
      await deleteSubject(record.id);
      reload();
    }
    function handleSuccess() {
      reload();
    }

    function handleSelect(categoryId = '') {
      searchInfo.categoryId = categoryId;
      reload();
    }
    return {
      registerTable,
      registerModal,
      handleCreate,
      handleEdit,
      handleDelete,
      handleSuccess,
      handleSelect,
      searchInfo
    };
  },
});
</script>
