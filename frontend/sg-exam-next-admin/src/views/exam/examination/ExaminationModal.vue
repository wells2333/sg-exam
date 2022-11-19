<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit" width="60%">
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>
<script lang="ts">
import { defineComponent, ref, computed, unref } from 'vue';
import { BasicModal, useModalInner } from '/@/components/Modal';
import { BasicForm, useForm } from '/@/components/Form/index';
import { formSchema } from './examination.data';
import { createExamination, updateExamination } from "/@/api/exam/examination";
export default defineComponent({
  name: 'ExaminationModal',
  components: { BasicModal, BasicForm },
  emits: ['success', 'register'],
  setup(_, { emit }) {
    const isUpdate = ref(true);
    let id: string;
    const [registerForm, { resetFields, setFieldsValue, updateSchema, validate }] = useForm({
      labelWidth: 100,
      schemas: formSchema,
      showActionButtonGroup: false,
    });
    const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
      resetFields();
      setModalProps({ confirmLoading: false });
      isUpdate.value = !!data?.isUpdate;
      if (unref(isUpdate)) {
        data.record.examTime = [];
        if (data.record.startTime) {
          data.record.examTime.push(data.record.startTime);
        }
        if (data.record.endTime) {
          data.record.examTime.push(data.record.endTime);
        }
        setFieldsValue({
          ...data.record,
        });
      }
      id = data.record?.id || null;
    });
    const getTitle = computed(() => (!unref(isUpdate) ? '新增' : '编辑'));
    async function handleSubmit() {
      try {
        const values = await validate();
        setModalProps({ confirmLoading: true });
        if (values.examTime) {
          values.startTime = values.examTime[0];
          values.endTime = values.examTime[1];
          values.examTime = undefined;
        }
        let course = {
          id: ''
        };
        if (values.courseId) {
          course.id = values.courseId;
          values.course = course;
        }
        if (id) {
          await updateExamination(id, values);
        } else {
          await createExamination(values);
        }
        closeModal();
        emit('success');
      } finally {
        setModalProps({ confirmLoading: false });
      }
    }
    return { registerModal, registerForm, getTitle, handleSubmit };
  },
});
</script>
