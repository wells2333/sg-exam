<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit"
              width="60%">
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
      <DeptModal @register="registerDeptModal" @success="handleSuccess" />
    </div>
  </BasicModal>
</template>
<script lang="ts">
import {computed, defineComponent} from 'vue';
import {useI18n} from '/@/hooks/web/useI18n';
import {BasicModal, useModal, useModalInner} from '/@/components/Modal';
import {BasicForm} from '/@/components/Form/index';
import {useMessage} from "/@/hooks/web/useMessage";
import {BasicTable, TableAction, useTable} from "/@/components/Table";
import DeptModal from './DeptModal.vue';
import { usePermission } from '/@/hooks/web/usePermission';
import {deleteDept, getDeptList} from "/@/api/sys/dept";
import {columns, searchFormSchema} from "./dept.data";
export default defineComponent({
  name: 'DeptListModal',
  components: {
    BasicTable,
    DeptModal,
    TableAction,
    BasicModal,
    BasicForm
  },
  emits: ['success', 'register'],
  setup(_) {
    const {t} = useI18n();
    const { hasPermission } = usePermission();
    const {createMessage} = useMessage();
    const [registerModal, {setModalProps, closeModal}] = useModalInner(async (data) => {
      setModalProps({confirmLoading: false});
    })
    const getTitle = computed(() => t('routes.sys.dept'));

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
    const [registerDeptModal, { openModal:openDeptModal }] = useModal();
    function handleSubmit() {
      closeModal();
    }
    function handleCreate() {
      openDeptModal(true, {
        isUpdate: false,
      });
    }
    function handleEdit(record: Recordable) {
      openDeptModal(true, {
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
      registerModal,
      registerTable,
      registerDeptModal,
      handleSubmit,
      getTitle,
      handleCreate,
      handleEdit,
      handleDelete,
      handleSuccess,
    };
  },
});
</script>

<style lang="less">
.ant-modal-wrap .ant-modal {
  top: 20px;
}

// 按钮居中
.ant-modal-footer {
  text-align: center !important;
}
</style>
