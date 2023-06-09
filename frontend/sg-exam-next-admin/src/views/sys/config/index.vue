<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button v-if="hasPermission(['sys:config:add'])" type="primary" @click="handleCreate"> {{ t('common.addText') }} </a-button>
      </template>
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: 'clarity:note-edit-line',
               auth: 'sys:menu:edit',
              onClick: handleEdit.bind(null, record),
            },
            {
              icon: 'ant-design:delete-outlined',
              color: 'error',
               auth: 'sys:config:del',
              popConfirm: {
                title: t('common.confirmDelText'),
                confirm: handleDelete.bind(null, record),
              },
            },
          ]"
        />
      </template>
    </BasicTable>
    <ConfigModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts">
import {useI18n} from '/@/hooks/web/useI18n';
import { defineComponent } from 'vue';
import { BasicUpload } from '/@/components/Upload';
import { BasicTable, useTable, TableAction } from '/@/components/Table';
import { getSysConfigList, deleteSysConfig } from '/@/api/sys/config';
import { useModal } from '/@/components/Modal';
import ConfigModal from './ConfigModal.vue';
import { columns, searchFormSchema } from './config.data';
import {useMessage} from "/@/hooks/web/useMessage";
import {usePermission} from "/@/hooks/web/usePermission";
export default defineComponent({
  name: 'SysConfigManagement',
  components: { BasicTable, ConfigModal, TableAction, BasicUpload },
  setup() {
    const {t} = useI18n();
    const { hasPermission } = usePermission();
    const [registerModal, { openModal }] = useModal();
    const { createMessage } = useMessage();
    const [registerTable, { reload }] = useTable({
      title: t('common.modules.sys.config') + t('common.list'),
      api: getSysConfigList,
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

    async function handleDelete(record: Recordable) {
      await deleteSysConfig(record.id);
      createMessage.success(t('common.operationSuccessText'));
      await reload();
    }
    function handleSuccess() {
      createMessage.success(t('common.operationSuccessText'));
      reload();
    }
    return {
      t,
      hasPermission,
      registerTable,
      registerModal,
      handleCreate,
      handleEdit,
      handleDelete,
      handleSuccess
    };
  },
});
</script>
