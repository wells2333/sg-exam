<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button v-if="hasPermission(['sys:gen:add'])" type="primary" @click="handleCreate"> 新增 </a-button>
      </template>
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: 'ant-design:arrow-down-outlined',
              auth: 'sys:gen:edit',
              popConfirm: {
                title: '生成代码',
                confirm: handleGen.bind(null, record),
                auth: 'sys:gen:edit'
              },
           },
            {
              icon: 'ant-design:delete-outlined',
              color: 'error',
              auth: 'sys:gen:del',
              popConfirm: {
                title: t('common.confirmDelText'),
                confirm: handleDelete.bind(null, record),
                auth: 'sys:gen:del',
              },
            },
          ]"
        />
      </template>
    </BasicTable>
    <GenModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts">
import {useI18n} from '/@/hooks/web/useI18n';
import { defineComponent } from 'vue';
import { BasicTable, useTable, TableAction } from '/@/components/Table';
import {getGenList, genCode, deleteGen} from '/@/api/sys/gen';
import { useModal } from '/@/components/Modal';
import GenModal from './GenModal.vue';
import { columns, searchFormSchema } from './gen.data';
import {useMessage} from "/@/hooks/web/useMessage";
import { usePermission } from '/@/hooks/web/usePermission';
export default defineComponent({
  name: 'GenManagement',
  components: { BasicTable, GenModal, TableAction },
  setup() {
    const {t} = useI18n();
    const { hasPermission } = usePermission();
    const [registerModal, { openModal }] = useModal();
    const { createMessage } = useMessage();
    const [registerTable, { reload }] = useTable({
      title: t('common.modules.sys.codeGen') + t('common.list'),
      api: getGenList,
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
        width: 120,
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
    function handleGen(record: Recordable) {
      genCode(record.tableName);
    }
    async function handleDelete(record: Recordable) {
      await deleteGen(record.tableId);
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
      handleGen,
      handleDelete,
      handleSuccess,
    };
  },
});
</script>
