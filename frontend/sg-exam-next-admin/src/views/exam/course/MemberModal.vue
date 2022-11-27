<template>
  <BasicModal v-bind="$attrs" @register="registerModal" title="学员列表" @ok="handleSubmit" width="90%">
    <BasicTable @register="registerTable">
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: 'ant-design:delete-outlined',
               tooltip: '删除',
              color: 'error',
              auth: 'exam:course:del',
              popConfirm: {
                title: '是否确认删除',
                confirm: handleDeleteMember.bind(null, record),
              },
            },
          ]"
        />
      </template>
    </BasicTable>
  </BasicModal>
</template>
<script lang="ts">
import {defineComponent, ref} from 'vue';
import {BasicTable, TableAction, useTable} from '/@/components/Table';
import {BasicModal, useModalInner} from '/@/components/Modal';
import {deleteExamCourseMember, getExamCourseMemberList} from "/@/api/exam/member";
import {memberColumns, memberSearchFormSchema} from "/@/views/exam/course/course.data";
import {useMessage} from "/@/hooks/web/useMessage";

export default defineComponent({
  name: 'MemberModal',
  components: {BasicModal, BasicTable, TableAction},
  emits: ['success', 'register'],
  setup(_) {
    const {createMessage} = useMessage();
    const courseId = ref<string>(undefined);
    let id: string;
    const [registerTable, {reload}] = useTable({
      title: '学员列表',
      api: (arg) => {
        if (courseId.value === undefined) {
          return undefined;
        }
        const params = {courseId: courseId.value};
        Object.assign(params, arg);
        return getExamCourseMemberList(params);
      },
      columns: memberColumns,
      formConfig: {
        labelWidth: 120,
        schemas: memberSearchFormSchema,
      },
      pagination: false,
      striped: false,
      useSearchForm: true,
      showTableSetting: true,
      bordered: true,
      showIndexColumn: false,
      canResize: false,
      actionColumn: {
        width: 120,
        title: '操作',
        dataIndex: 'action',
        slots: {customRender: 'action'},
        fixed: undefined,
      },
    });

    const [registerModal, {setModalProps, closeModal}] = useModalInner(async (data) => {
      setModalProps({confirmLoading: false});
      id = data.record?.id || null;
      courseId.value = data.courseId;
      reload();
    });

    async function handleDeleteMember(record: Recordable) {
      await deleteExamCourseMember(record.id);
      createMessage.success('操作成功');
      await reload();
    }

    async function handleSubmit() {
      closeModal();
    }

    return {registerTable, registerModal, handleDeleteMember, handleSubmit};
  },
});
</script>
