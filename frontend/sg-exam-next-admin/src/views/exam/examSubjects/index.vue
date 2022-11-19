<template>
  <div>
    <BasicTable @register="registerTable">
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
  </div>
</template>
<script lang="ts">
import {defineComponent, ref} from 'vue';
import { BasicTable, useTable, TableAction } from '/@/components/Table';
import { useModal } from '/@/components/Modal';
import { columns, searchFormSchema } from '../subject/subject.data';
import {useGo} from "/@/hooks/web/usePage";
import {useRoute} from "vue-router";
import {getExaminationSubjectList} from "/@/api/exam/examination";
export default defineComponent({
  name: 'ExaminationSubjectManagement',
  components: { BasicTable, TableAction },
  setup() {
    const [registerModal] = useModal();
    const go = useGo();
    const route = useRoute();
    const examinationId = ref<any>(route.params?.id);
    const [registerTable, { reload }] = useTable({
      title: '题目列表',
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
        width: 80,
        title: '操作',
        dataIndex: 'action',
        slots: { customRender: 'action' },
        fixed: undefined,
      },
    });
    function handleCreate() {
      go('/exam/subject_detail/0?examinationId=' + examinationId.value);
    }
    function handleEdit(record: Recordable) {
      go('/exam/subject_detail/' + record.id + '?type=' + record.type + '&eId=' + examinationId.value);
    }
    function handleDelete(record: Recordable) {
      console.log(record);
    }
    function handleSuccess() {
      reload();
    }
    return {
      registerTable,
      registerModal,
      handleCreate,
      handleEdit,
      handleDelete,
      handleSuccess,
    };
  },
});
</script>
