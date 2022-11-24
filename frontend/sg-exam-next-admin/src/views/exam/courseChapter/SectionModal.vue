<template>
  <BasicModal v-bind="$attrs" @register="registerModal" title="节管理" @ok="handleSubmit">
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button v-if="hasPermission(['exam:course:edit'])" type="primary" @click="handleCreate">
          新增
        </a-button>
      </template>
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: 'clarity:note-edit-line',
              tooltip: '编辑',
              onClick: handleEdit.bind(null, record),
              auth: 'exam:course:edit'
            },
            {
              icon: 'ant-design:delete-outlined',
              tooltip: '删除',
              color: 'error',
              auth: 'exam:course:del',
              popConfirm: {
                title: '是否确认删除',
                confirm: handleDelete.bind(null, record),
              },
            },
          ]"
        />
      </template>
    </BasicTable>
    <SectionDataModal width="60%" @register="registerSectionDataModal" @success="handleSectionDataSuccess"></SectionDataModal>
  </BasicModal>
</template>
<script lang="ts">
import {defineComponent, ref, unref} from 'vue';
import {BasicModal, useModal, useModalInner} from '/@/components/Modal';
import {BasicForm} from '/@/components/Form/index';
import {columns, searchFormSchema} from './section.data';
import {getSectionList, deleteSection} from "/@/api/exam/section";
import {BasicTable, TableAction, useTable} from '/@/components/Table';
import {usePermission} from '/@/hooks/web/usePermission';
import SectionDataModal from './SectionDataModal.vue';

export default defineComponent({
  name: 'SectionModal',
  components: {
    BasicModal,
    BasicForm,
    BasicTable,
    TableAction,
    SectionDataModal
  },
  emits: ['success', 'register'],
  setup(_) {
    const {hasPermission} = usePermission();
    const chapterId = ref<object>();
    // 列表
    const [registerTable, {reload}] = useTable({
      title: '章列表',
      api: (arg) => {
        const params = {chapterId: unref(chapterId)};
        Object.assign(params, arg);
        return getSectionList(params);
      },
      columns: columns,
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
      immediate: false, // 关闭立即请求，由reload触发
      actionColumn: {
        width: 120,
        title: '操作',
        dataIndex: 'action',
        slots: {customRender: 'action'},
        fixed: undefined,
      },
    });
    // 弹框
    const [registerModal, {setModalProps, closeModal}] = useModalInner(async (data) => {
      chapterId.value = data.record.id;
      reload();
      setModalProps({confirmLoading: false});
    });

    const [registerSectionDataModal, { openModal: openSectionDataModal }] = useModal();

    async function handleSubmit() {
      closeModal();
    }

    function handleCreate(record: Recordable) {
      openSectionDataModal(true, {
        record,
        chapterId,
        isUpdate: false,
      });
    }

    function handleEdit(record: Recordable) {
      openSectionDataModal(true, {
        record,
        chapterId,
        isUpdate: true,
      });
    }

    async function handleDelete(record: Recordable) {
      await deleteSection(record.id);
      await reload();
    }

    function handleSectionDataSuccess() {
      reload();
    }

    return {
      hasPermission,
      registerModal,
      registerTable,
      registerSectionDataModal,
      handleSubmit,
      handleCreate,
      handleEdit,
      handleDelete,
      handleSectionDataSuccess
    };
  },
});
</script>
