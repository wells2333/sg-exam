<template>
  <div>
    <BasicTable @register="registerTable" :rowSelection="{ type: 'checkbox' }">
      <template #toolbar>
        <a-button v-if="hasPermission(['exam:exam:add'])" type="primary" @click="handleCreate">
          {{ t('common.addText') }}
        </a-button>
        <PopConfirmButton
          v-if="hasPermission(['exam:exam:del'])"
          title="确定删除么？"
          okText="确认"
          cancelText="取消"
          @confirm="handleDelete"
          color="error"
        >删除
        </PopConfirmButton>
      </template>
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: 'ant-design:align-left-outlined',
              tooltip: '题目管理',
              onClick: handleSubjects.bind(null, record),
              auth: 'exam:exam:subject:add'
            },
            {
              icon: 'ant-design:upload-outlined',
              tooltip: '修改发布状态',
              auth: 'exam:exam:edit',
              popConfirm: {
                title: '确定修改发布状态吗？',
                confirm: handlePublic.bind(null, record),
              },
            },
            {
              icon: 'clarity:note-edit-line',
              tooltip: t('common.editText'),
              onClick: handleEdit.bind(null, record),
              auth: 'exam:exam:edit'
            },
            {
                icon: 'ant-design:arrow-down-outlined',
                tooltip: t('common.modules.exam.export'),
                auth: 'exam:exam:edit',
                popConfirm: {
                  title: t('common.confirmExportText'),
                  confirm: handleExport.bind(null, record),
                },
            },
            {
                icon: 'ant-design:qrcode-outlined',
                tooltip: t('common.modules.exam.qrCode'),
                auth: 'exam:exam:edit',
                popConfirm: {
                  title: t('common.confirmGenerateQrCodeText'),
                  confirm: handleGenerateQrCode.bind(null, record),
                },
            },
          ]"
        />
      </template>
    </BasicTable>
    <ExaminationModal width="80%" @register="registerModal" @success="handleSuccess"/>
    <QrCodeModal width="40%" @register="registerQrCodeModal"/>
  </div>
</template>
<script lang="ts">
import {useI18n} from '/@/hooks/web/useI18n';
import {defineComponent, unref} from 'vue';
import {BasicTable, TableAction, useTable} from '/@/components/Table';
import {
  getExaminationList,
  generateQrCodeMessage,
  deleteBatchExamination,
  updateExamination
} from '/@/api/exam/examination';
import {useModal} from '/@/components/Modal';
import ExaminationModal from './ExaminationModal.vue';
import QrCodeModal from './QrCodeModal.vue';
import ExaminationDetailDrawer from './ExaminationDetail.vue';
import {columns, searchFormSchema} from './examination.data';
import {useGo} from "/@/hooks/web/usePage";
import {usePermission} from '/@/hooks/web/usePermission';
import {useMessage} from "/@/hooks/web/useMessage";
import {exportSubjects} from '/@/api/exam/subject';
import {PopConfirmButton} from "/@/components/Button";

export default defineComponent({
  name: 'ExaminationManagement',
  components: {
    PopConfirmButton,
    BasicTable, ExaminationModal, QrCodeModal, ExaminationDetailDrawer, TableAction},
  setup() {
    const {t} = useI18n();
    const {hasPermission} = usePermission();
    const {createMessage} = useMessage();
    const [registerModal, {openModal}] = useModal();
    const [registerQrCodeModal, {openModal: openQrCodeModal}] = useModal();
    const [registerExamImageModal] = useModal();
    const go = useGo();
    const [registerTable, {reload, getSelectRows, clearSelectedRowKeys}] = useTable({
      title: t('common.modules.exam.examination') + t('common.list'),
      api: getExaminationList,
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
        width: 250,
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

    async function handleDelete() {
      const ids = getSelectedRowIds();
      if (!ids || ids.length === 0) {
        return;
      }

      await deleteBatchExamination(ids);
      createMessage.success(t('common.operationSuccessText'));
      clearSelectedRowKeys();
      await reload();
    }

    function handleSuccess(isUpdate) {
      let msg = t('common.operationSuccessText');
      if (isUpdate && !unref(isUpdate)) {
        msg = '新增成功，状态未未发布，需要发布后才生效';
      }
      createMessage.success(msg);
      reload();
    }

    function handleSubjects(record: Recordable) {
      go('/exam/examination_subjects/' + record.id);
    }

    async function handlePublic(record: Recordable) {
      const data = {...record}
      if (data.status === 0) {
        data.status = 1;
      } else {
        data.status = 0;
      }

      await updateExamination(data.id, data);
      createMessage.success(t('common.operationSuccessText'));
      await reload();
    }

    function handleExport(record: Recordable) {
      let url = '?examinationId=' + record.id;
      exportSubjects(url).then((res) => {
        const url = window.URL.createObjectURL(
          new Blob([res], {type: 'application/octet-stream'}),
        );
        let link = document.createElement('a');
        link.style.display = 'none';
        link.href = url;
        link.setAttribute('download', '题目.xlsx');
        document.body.appendChild(link);
        link.click();
        window.URL.revokeObjectURL(link.href);
        document.body.removeChild(link);
      });
    }

    async function handleGenerateQrCode(record: Recordable) {
      if (record.type !== 2) {
        createMessage.warn(t('common.operationNotSupportText'));
        return;
      }

      const res = await generateQrCodeMessage(record.id);
      if (res && res !== null) {
        openQrCodeModal(true, res);
      }
    }

    function getSelectedRowIds() {
      const rows = getSelectRows();
      if (!rows || rows.length === 0) {
        return undefined;
      }

      const ids = [];
      rows.forEach(e => {
        ids.push(e.id);
      });
      return ids;
    }

    return {
      t,
      hasPermission,
      registerTable,
      registerModal,
      registerQrCodeModal,
      registerExamImageModal,
      handleCreate,
      handleEdit,
      handleSubjects,
      handlePublic,
      handleDelete,
      handleSuccess,
      handleExport,
      handleGenerateQrCode
    };
  },
});
</script>
