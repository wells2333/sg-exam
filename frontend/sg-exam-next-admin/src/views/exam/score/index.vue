<template>
  <div>
    <BasicTable @register="registerTable">
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: 'ant-design:eye-outlined',
              onClick: handleView.bind(null, record),
               tooltip: t('common.viewText'),
            },
            {
              icon: 'ant-design:align-left-outlined',
              onClick: handleAnalysis.bind(null, record),
               tooltip: t('common.modules.exam.scoreAnalysis'),
            }
          ]"
        />
      </template>
    </BasicTable>
    <ScoreListModal @register="registerModal" @success="handleSuccess"/>
    <ScoreAnalysisModal @register="registerScoreAnalysisModal" @success="handleScoreAnalysisModelSuccess"/>
  </div>
</template>
<script lang="ts">
import {useI18n} from '/@/hooks/web/useI18n';
import {defineComponent} from 'vue';
import {BasicTable, useTable, TableAction} from '/@/components/Table';
import {getExaminationList} from '/@/api/exam/examination';
import {useModal} from '/@/components/Modal';
import ScoreListModal from './ScoreListModal.vue';
import ScoreAnalysisModal from './ScoreAnalysisModal.vue';
import {examColumns, searchFormSchema} from './score.data';

export default defineComponent({
  name: 'ScoreManagement',
  components: {BasicTable, TableAction, ScoreListModal, ScoreAnalysisModal},
  setup() {
    const {t} = useI18n();
    const [registerModal, {openModal}] = useModal();
    const [registerScoreAnalysisModal, {openModal: openScoreAnalysisModal}] = useModal();
    const [registerTable, {reload}] = useTable({
      title: t('common.modules.exam.score') + t('common.list'),
      api: getExaminationList,
      columns: examColumns,
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
        title: t('common.operationText'),
        dataIndex: 'action',
        slots: {customRender: 'action'},
        fixed: undefined,
      },
    });

    function handleView(record: Recordable) {
      openModal(true, {
        id: record.id
      });
    }

    function handleAnalysis(record: Recordable) {
      openScoreAnalysisModal(true, {
        id: record.id
      });
    }

    async function handleSuccess() {
      await reload();
    }

    async function handleScoreAnalysisModelSuccess() {
      await reload();
    }

    return {
      t,
      registerTable,
      registerModal,
      registerScoreAnalysisModal,
      handleView,
      handleAnalysis,
      handleSuccess,
      handleScoreAnalysisModelSuccess
    };
  },
});
</script>
