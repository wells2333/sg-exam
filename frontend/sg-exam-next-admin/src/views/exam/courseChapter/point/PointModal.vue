<template>
  <BasicModal v-bind="$attrs" @register="registerModal" title="知识点管理" @ok="handleSubmit">
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button v-if="hasPermission(['exam:course:edit'])" type="primary" @click="handleCreate"> {{ t('common.addText') }}</a-button>
      </template>
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: 'clarity:note-edit-line',
              tooltip: t('common.editText'),
              onClick: handleEdit.bind(null, record),
              auth: 'exam:course:edit'
            },
            {
              icon: 'ant-design:delete-outlined',
              color: 'error',
              tooltip: t('common.delText'),
              auth: 'exam:course:edit',
              popConfirm: {
                title: t('common.confirmDelText'),
                confirm: handleDelete.bind(null, record),
              },
            },
          ]"
        />
      </template>
    </BasicTable>
    <PointDataModal width="75%" @register="registerPointDataModal" @success="handleSuccess"/>
  </BasicModal>
</template>
<script lang="ts">
import { useI18n } from '/@/hooks/web/useI18n';
import {defineComponent, ref, unref} from 'vue';
import {BasicUpload} from '/@/components/Upload';
import {BasicTable, TableAction, useTable} from '/@/components/Table';
import {
  deleteExamCourseKnowledgePoint,
  getExamCourseKnowledgePointList
} from '/@/api/exam/knowledgePoint';
import PointDataModal from './PointDataModal.vue';
import {columns, searchFormSchema} from './point.data';
import {useMessage} from "/@/hooks/web/useMessage";
import {BasicModal, useModal, useModalInner} from '/@/components/Modal';
import {usePermission} from "/@/hooks/web/usePermission";

export default defineComponent({
  name: 'KnowledgePointManagement',
  components: {BasicModal, BasicTable, PointDataModal, TableAction, BasicUpload},
  setup() {
    const { t } = useI18n();
    const {hasPermission} = usePermission();
    const [registerPointDataModal, {openModal: openPointDataModal}] = useModal();
    const {createMessage} = useMessage();
    const sectionId = ref<string>('');
    const [registerTable, {reload}] = useTable({
      title: '知识点列表',
      api: (arg) => {
        if (sectionId.value === '') {
          return undefined;
        }
        const params = {sectionId: unref(sectionId)};
        Object.assign(params, arg);
        return getExamCourseKnowledgePointList(params);
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
        width: 150,
        title: t('common.operationText'),
        dataIndex: 'action',
        slots: {customRender: 'action'},
        fixed: undefined,
      },
    });

    const [registerModal, { setModalProps, closeModal }] = useModalInner( (data) => {
      setModalProps({ confirmLoading: false });
      sectionId.value = data.record?.id || null;
      reload();
    });

    function handleCreate() {
      openPointDataModal(true, {
        sectionId: sectionId.value,
        isUpdate: false,
      });
    }

    function handleEdit(record: Recordable) {
      openPointDataModal(true, {
        record,
        sectionId: sectionId.value,
        isUpdate: true,
      });
    }

    async function handleDelete(record: Recordable) {
      await deleteExamCourseKnowledgePoint(record.id);
      createMessage.success(t('common.operationSuccessText'));
      await reload();
    }

    function handleSuccess() {
      createMessage.success(t('common.operationSuccessText'));
      reload();
    }

    function handleSubmit() {
      closeModal();
    }
    return {
      t,
      hasPermission,
      registerTable,
      registerModal,
      registerPointDataModal,
      handleCreate,
      handleEdit,
      handleDelete,
      handleSuccess,
      handleSubmit
    };
  },
});
</script>

<style scoped lang="less">

</style>
