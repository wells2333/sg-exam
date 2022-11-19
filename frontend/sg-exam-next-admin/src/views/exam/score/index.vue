<template>
  <div>
    <BasicTable @register="registerTable">
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: 'ant-design:align-left-outlined',
              onClick: handleView.bind(null, record),
            }
          ]"
        />
      </template>
    </BasicTable>
    <ScoreModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts">
import { defineComponent } from 'vue';
import { BasicTable, useTable, TableAction } from '/@/components/Table';
import { getScoreList } from '/@/api/exam/score';
import { useModal } from '/@/components/Modal';
import ScoreModal from './ScoreModal.vue';
import { columns, searchFormSchema } from './score.data';
import {useGo} from "/@/hooks/web/usePage";
export default defineComponent({
  name: 'ScoreManagement',
  components: { BasicTable, ScoreModal, TableAction },
  setup() {
    const go = useGo();
    const [registerModal, { openModal }] = useModal();
    const [registerTable, { reload }] = useTable({
      title: '成绩列表',
      api: getScoreList,
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
        width: 160,
        title: '操作',
        dataIndex: 'action',
        slots: { customRender: 'action' },
        fixed: undefined,
      },
    });
    function handleView(record: Recordable) {
      go('/exam/score_detail/' + record.id);
    }
    function handleEdit(record: Recordable) {
      openModal(true, {
        record,
        isUpdate: true,
      });
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
      handleView,
      handleEdit,
      handleDelete,
      handleSuccess
    };
  },
});
</script>
