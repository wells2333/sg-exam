<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button v-if="hasPermission(['sys:role:add'])" type="primary" @click="handleCreate">
          {{ t('common.addText') }}
        </a-button>
      </template>
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: 'clarity:note-edit-line',
              onClick: handleEdit.bind(null, record),
              auth: 'sys:role:edit'
            },
            {
              icon: 'ant-design:delete-outlined',
              color: 'error',
               auth: 'sys:role:del',
              popConfirm: {
                title: t('common.confirmDelText'),
                confirm: handleDelete.bind(null, record),
              },
            },
          ]"
        />
      </template>
    </BasicTable>
    <RoleModal @register="registerModal" @success="handleSuccess"/>
  </div>
</template>
<script lang="ts">
import {useI18n} from '/@/hooks/web/useI18n';
import {defineComponent} from 'vue';
import {BasicTable, useTable, TableAction} from '/@/components/Table';
import {getRoleList, deleteRole} from '/@/api/sys/role';
import {useModal} from '/@/components/Modal';
import RoleModal from './RoleModal.vue';
import {columns, searchFormSchema} from './role.data';
import {useMessage} from "/@/hooks/web/useMessage";
import {usePermission} from '/@/hooks/web/usePermission';

export default defineComponent({
  name: 'RoleManagement',
  components: {BasicTable, RoleModal, TableAction},
  setup() {
    const {t} = useI18n();
    const {hasPermission} = usePermission();
    const {createMessage} = useMessage();
    const [registerModal, {openModal}] = useModal();
    const [registerTable, {reload}] = useTable({
      title: t('common.modules.sys.role') + t('common.list'),
      api: getRoleList,
      columns,
      formConfig: {
        labelWidth: 120,
        schemas: searchFormSchema,
      },
      useSearchForm: true,
      showTableSetting: true,
      bordered: true,
      showIndexColumn: false,
      actionColumn: {
        width: 80,
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
      await deleteRole(record.id);
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
