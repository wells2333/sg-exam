<template>
  <BasicModal v-bind="$attrs" @register="registerModal" title="节管理" @ok="handleSubmit">
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button v-if="hasPermission(['exam:course:edit'])" type="primary" @click="handleCreate">
          {{ t('common.addText') }}
        </a-button>
        <a-button v-if="hasPermission(['exam:course:edit'])" type="primary" @click="handleImport">
          {{ t('common.batchImportText') }}
        </a-button>
      </template>
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: 'ant-design:align-right-outlined',
              tooltip: '知识点管理',
              onClick: handlePointManage.bind(null, record),
              auth: 'exam:course:edit'
            },
            {
              icon: 'clarity:note-edit-line',
              tooltip: t('common.editText'),
              onClick: handleEdit.bind(null, record),
              auth: 'exam:course:edit'
            },
            {
              icon: 'ant-design:delete-outlined',
              tooltip: t('common.delText'),
              color: 'error',
              auth: 'exam:course:del',
              popConfirm: {
                title: t('common.confirmDelText'),
                confirm: handleDelete.bind(null, record),
              },
            },
          ]"
        />
      </template>
    </BasicTable>
    <SectionDataModal width="85%" @register="registerSectionDataModal" @success="handleSectionDataSuccess"></SectionDataModal>
    <PointModal width="80%" @register="registerPointModal" @success="handlePointSuccess"></PointModal>
    <ImportSectionModal width="50%" @register="registerImportSectionModal" @success="handleImportSectionSuccess"/>
  </BasicModal>
</template>
<script lang="ts">
import { useI18n } from '/@/hooks/web/useI18n';
import {defineComponent, ref, unref} from 'vue';
import {BasicModal, useModal, useModalInner} from '/@/components/Modal';
import {BasicForm} from '/@/components/Form/index';
import {columns, searchFormSchema} from './section.data';
import {getSectionList, deleteSection} from "/@/api/exam/section";
import {BasicTable, TableAction, useTable} from '/@/components/Table';
import {usePermission} from '/@/hooks/web/usePermission';
import SectionDataModal from './SectionDataModal.vue';
import ImportSectionModal from './ImportSectionModal.vue';
import PointModal from '../point/PointModal.vue';
import {useMessage} from "/@/hooks/web/useMessage";
import ImportChapterModal from "/@/views/exam/courseChapter/ImportChapterModal.vue";

export default defineComponent({
  name: 'SectionModal',
  components: {
    ImportChapterModal,
    BasicModal,
    BasicForm,
    BasicTable,
    TableAction,
    SectionDataModal,
    PointModal,
    ImportSectionModal
  },
  emits: ['success', 'register'],
  setup(_) {
    const { t } = useI18n();
    const {hasPermission} = usePermission();
    const { createMessage } = useMessage();
    const chapterId = ref<object>();
    const [registerTable, {reload}] = useTable({
      title: '节列表',
      api: (arg) => {
        const params = {chapterId: unref(chapterId)};
        Object.assign(params, arg);
        return getSectionList(params);
      },
      columns: columns,
      formConfig: {
        labelWidth: 120,
        schemas: searchFormSchema,
      },
      pagination: false,
      striped: false,
      useSearchForm: true,
      showTableSetting: true,
      bordered: true,
      showIndexColumn: false,
      canResize: false,
      immediate: false, // 关闭立即请求，由 reload 触发
      actionColumn: {
        width: 120,
        title: t('common.operationText'),
        dataIndex: 'action',
        slots: {customRender: 'action'},
        fixed: undefined,
      },
    });
    // 弹框
    const [registerModal, {setModalProps, closeModal}] = useModalInner(async (data) => {
      chapterId.value = data.record.id;
      reload();
      setModalProps({confirmLoading: false});
    });

    // 章节弹框
    const [registerSectionDataModal, { openModal: openSectionDataModal }] = useModal();

    // 知识点弹框
    const [registerPointModal, { openModal: openPointModal }] = useModal();

    // 导入弹框
    const [registerImportSectionModal, {openModal: openImportSectionModal}] = useModal();

    async function handleSubmit() {
      closeModal();
    }

    function handleCreate(record: Recordable) {
      openSectionDataModal(true, {
        record,
        chapterId,
        isUpdate: false,
      });
    }

    function handleImport() {
      openImportSectionModal(true, {
        chapterId
      });
    }

    function handleEdit(record: Recordable) {
      openSectionDataModal(true, {
        record,
        chapterId,
        isUpdate: true,
      });
    }

    async function handleDelete(record: Recordable) {
      await deleteSection(record.id);
      createMessage.success(t('common.operationSuccessText'));
      await reload();
    }

    function handleSectionDataSuccess() {
      createMessage.success(t('common.operationSuccessText'));
      reload();
    }

    function handlePointManage(record: Recordable) {
      openPointModal(true, {
        record,
        isUpdate: true,
      });
    }

    function handlePointSuccess() {

    }

    function handleImportSectionSuccess() {
      createMessage.success(t('common.operationSuccessText'));
      reload();
    }

    return {
      t,
      hasPermission,
      registerModal,
      registerTable,
      registerSectionDataModal,
      registerPointModal,
      registerImportSectionModal,
      handleSubmit,
      handleCreate,
      handleImport,
      handleEdit,
      handleDelete,
      handleSectionDataSuccess,
      handlePointManage,
      handlePointSuccess,
      handleImportSectionSuccess
    };
  },
});
</script>
