<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button v-if="hasPermission(['sys:dept:add'])" type="primary" @click="handleCreate"> {{ t('common.addText') }} </a-button>
      </template>
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: 'clarity:note-edit-line',
              onClick: handleEdit.bind(null, record),
              auth: 'sys:dept:edit'
            },
            {
              icon: 'ant-design:delete-outlined',
              color: 'error',
               auth: 'sys:dept:del',
              popConfirm: {
                title: t('common.confirmDelText'),
                confirm: handleDelete.bind(null, record),
              },
            },
          ]"
        />
      </template>
    </BasicTable>
    <DeptModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts">
import {useI18n} from '/@/hooks/web/useI18n';
import { defineComponent } from 'vue';
import { BasicTable, useTable, TableAction } from '/@/components/Table';
import { getDeptList, deleteDept } from '/@/api/sys/dept';
import { useModal } from '/@/components/Modal';
import DeptModal from './DeptModal.vue';
import { columns, searchFormSchema } from './dept.data';
import { usePermission } from '/@/hooks/web/usePermission';
import {useMessage} from "/@/hooks/web/useMessage";
export default defineComponent({
  name: 'DeptManagement',
  components: { BasicTable, DeptModal, TableAction },
  setup() {
    const {t} = useI18n();
    const { hasPermission } = usePermission();
    const { createMessage } = useMessage();
    const [registerModal, { openModal }] = useModal();
    const [registerTable, { reload }] = useTable({
      title: t('common.modules.sys.dept') + t('common.list'),
      api: getDeptList,
      columns,
      formConfig: {
        labelWidth: 120,
        schemas: searchFormSchema,
      },
      isTreeTable: true,
      pagination: false,
      striped: false,
      useSearchForm: true,
      showTableSetting: true,
      bordered: true,
      showIndexColumn: false,
      canResize: false,
      actionColumn: {
        width: 80,
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
      await deleteDept([record.id]);
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
