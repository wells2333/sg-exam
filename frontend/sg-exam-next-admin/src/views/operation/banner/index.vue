<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" @click="handleCreate"> {{ t('common.addText') }}</a-button>
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
                title: t('common.confirmDelText'),
                confirm: handleDelete.bind(null, record),
              },
            },
          ]"
        />
      </template>
    </BasicTable>
    <BannerModal @register="registerModal" @success="handleSuccess"/>
  </div>
</template>
<script lang="ts">
import {useI18n} from '/@/hooks/web/useI18n';
import {defineComponent} from 'vue';
import {BasicTable, useTable, TableAction} from '/@/components/Table';
import {getBannerList, deleteBanner} from '/@/api/operation/banner';
import {useModal} from '/@/components/Modal';
import BannerModal from './BannerModal.vue';
import {columns, searchFormSchema} from './banner.data';
import {useMessage} from "/@/hooks/web/useMessage";

export default defineComponent({
  name: 'BannerManagement',
  components: {BasicTable, BannerModal, TableAction},
  setup() {
    const {t} = useI18n();
    const [registerModal, {openModal}] = useModal();
    const {createMessage} = useMessage();
    const [registerImageModal, {openModal: openImageModal}] = useModal();
    const [registerTable, {reload}] = useTable({
      title: t('common.modules.banner.banner') + t('common.list'),
      api: getBannerList,
      columns,
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
      actionColumn: {
        width: 120,
        title: t('common.operationText'),
        dataIndex: 'action',
        slots: {customRender: 'action'},
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

    function handleUpload(record: Recordable) {
      openImageModal(true, record);
    }

    async function handleDelete(record: Recordable) {
      await deleteBanner(record.id);
      createMessage.success(t('common.operationSuccessText'));
      await reload();
    }

    function handleSuccess() {
      createMessage.success(t('common.operationSuccessText'));
      reload();
    }

    function handleUploadSuccess() {
      reload();
    }

    return {
      t,
      registerTable,
      registerModal,
      registerImageModal,
      handleCreate,
      handleEdit,
      handleUpload,
      handleDelete,
      handleSuccess,
      handleUploadSuccess,
    };
  },
});
</script>
