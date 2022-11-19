<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" @click="handleCreate"> 新增 </a-button>
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
    <BannerModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts">
import {defineComponent} from 'vue';
import { BasicTable, useTable, TableAction } from '/@/components/Table';
import { getBannerList, deleteBanner } from '/@/api/operation/banner';
import { useModal } from '/@/components/Modal';
import BannerModal from './BannerModal.vue';
import { columns, searchFormSchema } from './banner.data';

export default defineComponent({
  name: 'BannerManagement',
  components: { BasicTable, BannerModal, TableAction },
  setup() {
    const [registerModal, { openModal }] = useModal();
    const [registerImageModal, { openModal: openImageModal }] = useModal();
    const [registerTable, { reload }] = useTable({
      title: '运营位列表',
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
    function handleUpload(record: Recordable) {
      openImageModal(true, record);
    }
    async function handleDelete(record: Recordable) {
      await deleteBanner(record.id);
      await reload();
    }
    function handleSuccess() {
      reload();
    }
    function handleUploadSuccess() {
      reload();
    }
    return {
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
