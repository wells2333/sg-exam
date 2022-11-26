<template>
  <BasicModal v-bind="$attrs" @register="registerModal" title="评价列表" @ok="handleSubmit" width="90%">
    <BasicTable @register="registerTable">
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: 'ant-design:eye-outlined',
              tooltip: '查看',
              onClick: handleViewEvaluate.bind(null, record),
              auth: 'exam:course:view'
            },
            {
              icon: 'ant-design:delete-outlined',
               tooltip: '删除',
              color: 'error',
              auth: 'exam:course:del',
              popConfirm: {
                title: '是否确认删除',
                confirm: handleDeleteEvaluate.bind(null, record),
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
import {deleteExamCourseEvaluate, getExamCourseEvaluateList} from "/@/api/exam/courseEvaluate";
import {evaluateColumns, evaluateSearchFormSchema} from "/@/views/exam/course/course.data";
import {useMessage} from "/@/hooks/web/useMessage";

export default defineComponent({
  name: 'EvaluateModal',
  components: {BasicModal, BasicTable, TableAction},
  emits: ['success', 'register'],
  setup(_) {
    const {createMessage} = useMessage();
    const courseId = ref<string>(undefined);
    let id: string;
    const [registerTable, {reload}] = useTable({
      title: '评价列表',
      api: (arg) => {
        if (courseId.value === undefined) {
          return undefined;
        }
        const params = {courseId: courseId.value};
        Object.assign(params, arg);
        return getExamCourseEvaluateList(params);
      },
      columns: evaluateColumns,
      formConfig: {
        labelWidth: 120,
        schemas: evaluateSearchFormSchema,
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

    function handleViewEvaluate() {

    }

    async function handleDeleteEvaluate(record: Recordable) {
      await deleteExamCourseEvaluate(record.id);
      createMessage.success('操作成功');
      reload();
    }

    async function handleSubmit() {
      closeModal();
    }

    return {registerTable, registerModal, handleViewEvaluate, handleDeleteEvaluate, handleSubmit};
  },
});
</script>
