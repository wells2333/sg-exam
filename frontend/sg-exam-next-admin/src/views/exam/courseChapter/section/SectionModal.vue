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
              icon: 'ant-design:align-right-outlined',
              tooltip: '知识点管理',
              onClick: handlePointManage.bind(null, record),
              auth: 'exam:course:edit'
            },
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
    <SectionDataModal width="80%" @register="registerSectionDataModal" @success="handleSectionDataSuccess"></SectionDataModal>
    <PointModal width="70%" @register="registerPointModal" @success="handlePointSuccess"></PointModal>
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
import PointModal from '../point/PointModal.vue';
import {useMessage} from "/@/hooks/web/useMessage";

export default defineComponent({
  name: 'SectionModal',
  components: {
    BasicModal,
    BasicForm,
    BasicTable,
    TableAction,
    SectionDataModal,
    PointModal
  },
  emits: ['success', 'register'],
  setup(_) {
    const {hasPermission} = usePermission();
    const { createMessage } = useMessage();
    const chapterId = ref<object>();
    // 列表
    const [registerTable, {reload}] = useTable({
      title: '节列表',
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

    // 章节弹框
    const [registerSectionDataModal, { openModal: openSectionDataModal }] = useModal();
    // 知识点弹框
    const [registerPointModal, { openModal: openPointModal }] = useModal();

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
      createMessage.success('操作成功');
      await reload();
    }

    function handleSectionDataSuccess() {
      createMessage.success('操作成功');
      reload();
    }

    function handlePointManage(record: Recordable) {
      openPointModal(true, {
        record,
        isUpdate: true,
      });
    }

    function handlePointSuccess() {

    }

    return {
      hasPermission,
      registerModal,
      registerTable,
      registerSectionDataModal,
      registerPointModal,
      handleSubmit,
      handleCreate,
      handleEdit,
      handleDelete,
      handleSectionDataSuccess,
      handlePointManage,
      handlePointSuccess
    };
  },
});
</script>
