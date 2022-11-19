<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <BasicUpload
          :maxSize="20"
          :maxNumber="10"
          @change="handleChange"
          :api="uploadApi"
          class="my-5"
          :accept="['image/*', 'png', 'jpeg', 'doc', 'docx', 'xml']"
        />
      </template>
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: 'ant-design:copy-filled',
              onClick: handleGetDownloadUrl.bind(null, record),
              tooltip: '复制下载地址'
            },
            {
              icon: 'ant-design:arrow-down-outlined',
              tooltip: '下载',
              popConfirm: {
                title: '是否确认下载',
                confirm: handleDownload.bind(null, record),
              },
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
    <AttachmentModal @register="registerModal" @success="handleSuccess" />
    <AttachmentInfoModal @register="registerInfoModal" width="60%" :height="80" :footer="null"/>
  </div>
</template>
<script lang="ts">
import { defineComponent } from 'vue';
import { BasicUpload } from '/@/components/Upload';
import { BasicTable, useTable, TableAction } from '/@/components/Table';
import { getAttachmentList, uploadApi, getDownloadUrl, download, deleteAttachment } from '/@/api/attachment/attach';
import { useModal } from '/@/components/Modal';
import AttachmentModal from './AttachmentModal.vue';
import AttachmentInfoModal from './AttachmentInfoModal.vue';
import { columns, searchFormSchema } from './attach.data';
import {useMessage} from "/@/hooks/web/useMessage";
export default defineComponent({
  name: 'AttachmentManagement',
  components: { BasicTable, AttachmentModal, AttachmentInfoModal, TableAction, BasicUpload },
  setup() {
    const [registerModal, { openModal }] = useModal();
    const [registerInfoModal, { openModal: openInfoModal }] = useModal();
    const { createMessage } = useMessage();
    const [registerTable, { reload }] = useTable({
      title: '附件列表',
      api: getAttachmentList,
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
    async function handleGetDownloadUrl(record: Recordable) {
      const result = await getDownloadUrl(record.id);
      openInfoModal(true, {
        result
      });
    }

    async function handleDownload(record: Recordable) {
      const result = await download(record.id);
      window.open(result);
    }

    async function handleDelete(record: Recordable) {
      const result = await deleteAttachment(record.id);
      if (result) {
        createMessage.success('删除成功');
        await reload();
      } else {
        createMessage.error('删除失败');
      }
    }
    function handleSuccess() {
      reload();
    }
    return {
      registerTable,
      registerModal,
      registerInfoModal,
      handleCreate,
      handleEdit,
      handleGetDownloadUrl,
      handleDownload,
      handleDelete,
      handleSuccess,
      uploadApi,
      handleChange: (list: string[]) => {
        reload();
      },
    };
  },
});
</script>
