<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" @click="handleCreate"> 手动添加</a-button>
        <a-button type="primary" @click="handleSelectSubjects"> 从题库选择</a-button>
        <a-button type="primary" @click="handleRandomSelectSubjects"> 随机组题</a-button>
      </template>
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: 'clarity:note-edit-line',
              onClick: handleEdit.bind(null, record),
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
    <SubjectModal @register="registerModal" @success="handleSubjectDataSuccess"></SubjectModal>
    <SelectSubjectModal @register="registerSelectSubjectModal" @success="handleSelectSubjectSuccess"></SelectSubjectModal>
    <RandomSubjectModal @register="registerRandomSubjectModal" @succecc="handleRandomSubjectSuccess"></RandomSubjectModal>
  </div>
</template>
<script lang="ts">
import {defineComponent, ref} from 'vue';
import {BasicTable, TableAction, useTable} from '/@/components/Table';
import {useModal} from '/@/components/Modal';
import {columns, searchFormSchema} from '../subject/subject.data';
import {useRoute} from "vue-router";
import {getExaminationSubjectList} from "/@/api/exam/examination";
import SubjectModal from "./SubjectModal.vue";
import SelectSubjectModal from "./SelectSubjectModal.vue";
import RandomSubjectModal from "./RandomSubjectModal.vue";
import {deleteSubject} from '/@/api/exam/subject';
import {useMessage} from "/@/hooks/web/useMessage";

export default defineComponent({
  name: 'SubjectManagement',
  components: {BasicTable, TableAction, SubjectModal, SelectSubjectModal, RandomSubjectModal},
  setup() {
    const [registerModal, {openModal}] = useModal();
    const [registerSelectSubjectModal, {openModal: openSelectSubjectModal}] = useModal();
    const [registerRandomSubjectModal, {openModal: openRandomSubjectModal}] = useModal();
    const {createMessage} = useMessage();
    const route = useRoute();
    const examinationId = ref<any>(route.params?.id);
    const [registerTable, {reload}] = useTable({
      title: '题目列表',
      api: getExaminationSubjectList,
      searchInfo: {
        examinationId
      },
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
        width: 180,
        title: '操作',
        dataIndex: 'action',
        slots: {customRender: 'action'},
        fixed: undefined,
      },
    });

    function handleCreate() {
      openModal(true, {
        isUpdate: false,
        examinationId
      });
    }

    function handleEdit(record: Recordable) {
      openModal(true, {
        record,
        isUpdate: true,
        examinationId,
        type: record.type,
      });
    }

    async function handleDelete(record: Recordable) {
      await deleteSubject(record.id);
      createMessage.success('操作成功');
      await reload();
    }

    function handleSuccess() {
      createMessage.success('操作成功');
      reload();
    }

    function handleSubjectDataSuccess() {
      reload();
    }

    function handleSelectSubjects() {
      openSelectSubjectModal(true, {
        examinationId
      });
    }

    function handleRandomSelectSubjects() {
      openRandomSubjectModal(true, {
        isUpdate: false,
        examinationId
      });
    }

    function handleSelectSubjectSuccess() {
      reload();
    }

    function handleRandomSubjectSuccess() {
      reload();
    }

    return {
      registerTable,
      registerModal,
      registerSelectSubjectModal,
      registerRandomSubjectModal,
      handleCreate,
      handleEdit,
      handleDelete,
      handleSuccess,
      handleSubjectDataSuccess,
      handleSelectSubjects,
      handleRandomSelectSubjects,
      handleSelectSubjectSuccess,
      handleRandomSubjectSuccess
    };
  },
});
</script>

<style lang="less">


</style>
