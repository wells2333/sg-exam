<template>
  <div>
    <BasicModal v-bind="$attrs" @register="registerModal" title="成绩列表" @ok="handleSubmit"
                width="90%">
      <BasicTable @register="registerTable">
        <template #action="{ record }">
          <TableAction
            :actions="[
            {
              icon: 'ant-design:align-left-outlined',
              onClick: handleView.bind(null, record),
               tooltip: t('common.viewText'),
            }
          ]"
          />
        </template>
      </BasicTable>
      <ScoreModal @register="registerScoreModal" @success="handleSuccess"/>
    </BasicModal>
  </div>
</template>
<script lang="ts">
import {useI18n} from '/@/hooks/web/useI18n';
import {defineComponent, computed, ref, unref} from 'vue';
import {BasicTable, useTable, TableAction} from '/@/components/Table';
import {BasicModal, useModal, useModalInner} from '/@/components/Modal';
import {BasicForm} from '/@/components/Form/index';
import ScoreModal from './ScoreModal.vue';
import {columns, searchFormSchema} from './score.data';
import {getScoreList} from '/@/api/exam/score';
import {useGo} from "/@/hooks/web/usePage";

export default defineComponent({
  name: 'ScoreListModal',
  components: {BasicTable, TableAction, BasicModal, BasicForm, ScoreModal},
  emits: ['success', 'register'],
  setup(_) {
    const {t} = useI18n();
    const go = useGo();
    const examinationId = ref<any>(undefined);
    const [registerTable, {reload}] = useTable({
      title: t('common.modules.exam.score') + t('common.list'),
      api: (arg) => {
        if (examinationId.value === undefined) {
          return undefined;
        }
        const params = {examinationId: unref(examinationId)};
        Object.assign(params, arg);
        return getScoreList(params);
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
        width: 160,
        title: t('common.operationText'),
        dataIndex: 'action',
        slots: {customRender: 'action'},
        fixed: undefined,
      },
    });
    const [registerModal, {setModalProps, closeModal}] = useModalInner(async (data) => {
      examinationId.value = data?.id || undefined;
      await reload();
      setModalProps({confirmLoading: false});
    });
    const [registerScoreModal, {openModal}] = useModal();
    const getTitle = computed(() => t('common.viewText'));

    function handleView(record: Recordable) {
      go('/exam/score_detail/' + record.id);
    }

    function handleSubmit() {
      closeModal();
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

    return {t, registerTable, registerModal, registerScoreModal, handleView, handleSubmit, handleSuccess, handleEdit, handleDelete, getTitle};
  },
});
</script>
