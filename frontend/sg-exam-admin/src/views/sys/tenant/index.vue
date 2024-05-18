<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button v-if="hasPermission(['tenant:tenant:add'])" type="primary" @click="handleCreate">
          {{ t('common.addText') }}
        </a-button>
      </template>
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: 'clarity:note-edit-line',
              onClick: handleEdit.bind(null, record),
              auth: 'tenant:tenant:edit'
            },
            {
              icon: 'ant-design:delete-outlined',
              color: 'error',
              auth: 'tenant:tenant:del',
              popConfirm: {
                title: t('common.confirmDelText'),
                confirm: handleDelete.bind(null, record),
                auth: 'tenant:tenant:del'
              },
            }
          ]"
        />
      </template>
    </BasicTable>
    <TenantModal @register="registerModal" @success="handleSuccess"/>
  </div>
</template>
<script lang="ts">
import {useI18n} from '/@/hooks/web/useI18n';
import {defineComponent} from 'vue';
import {BasicTable, useTable, TableAction} from '/@/components/Table';
import {getTenantList, deleteTenant} from '/@/api/sys/tenant';
import {useModal} from '/@/components/Modal';
import TenantModal from './TenantModal.vue';
import {columns, searchFormSchema} from './tenant.data';
import {useMessage} from "/@/hooks/web/useMessage";
import {usePermission} from '/@/hooks/web/usePermission';

export default defineComponent({
  name: 'TenantManagement',
  components: {BasicTable, TenantModal, TableAction},
  setup() {
    const {t} = useI18n();
    const {hasPermission} = usePermission();
    const [registerModal, {openModal}] = useModal();
    const {createMessage} = useMessage();
    const [registerTable, {reload}] = useTable({
      title: t('common.modules.sys.tenant') + t('common.list'),
      api: getTenantList,
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

    async function handleDelete(record: Recordable) {
      await deleteTenant(record.id);
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
      handleSuccess,
    };
  },
});
</script>
