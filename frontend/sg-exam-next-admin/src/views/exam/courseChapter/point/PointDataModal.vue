<template>
  <BasicModal width="60%" v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>
<script lang="ts">
import { defineComponent, ref, computed, unref } from 'vue';
import { BasicModal, useModalInner } from '/@/components/Modal';
import { BasicForm, useForm } from '/@/components/Form/index';
import { formSchema } from './point.data';
import { createExamCourseKnowledgePoint, updateExamCourseKnowledgePoint } from '/@/api/exam/knowledgePoint';
export default defineComponent({
  name: 'PointDataModal',
  components: { BasicModal, BasicForm },
  emits: ['success', 'register'],
  setup(_, { emit }) {
    const isUpdate = ref(true);
    let id: string;
    let sectionId: string;
    const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
      labelWidth: 100,
      schemas: formSchema,
      showActionButtonGroup: false,
    });
    const [registerModal, { setModalProps, closeModal }] = useModalInner( (data) => {
      resetFields();
      setModalProps({ confirmLoading: false });
      isUpdate.value = !!data?.isUpdate;
      if (unref(isUpdate)) {
        setFieldsValue({
          ...data.record,
        });
      }
      id = data.record?.id || null;
      sectionId = data?.sectionId || null;
    });
    const getTitle = computed(() => (!unref(isUpdate) ? '新增' : '编辑'));
    async function handleSubmit() {
      try {
        const values = await validate();
        values.sectionId = sectionId;
        setModalProps({ confirmLoading: true });
        if (id) {
          await updateExamCourseKnowledgePoint(id, values);
        } else {
          await createExamCourseKnowledgePoint(values);
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

<style scoped lang="less">

</style>
