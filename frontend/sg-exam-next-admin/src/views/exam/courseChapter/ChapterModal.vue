<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>
<script lang="ts">
import { defineComponent, ref, computed, unref } from 'vue';
import { BasicModal, useModalInner } from '/@/components/Modal';
import { BasicForm, useForm } from '/@/components/Form/index';
import { formSchema } from './chapter.data';
import { createChapter, updateChapter} from "/@/api/exam/chapter";
import {useRoute} from "vue-router";
export default defineComponent({
  name: 'ChapterModal',
  components: { BasicModal, BasicForm },
  emits: ['success', 'register'],
  setup(_, { emit }) {
    const route = useRoute();
    const courseId = ref(route.params?.id);
    const isUpdate = ref(true);
    let id: string;
    const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
      labelWidth: 100,
      schemas: formSchema,
      showActionButtonGroup: false,
    });
    const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
      resetFields();
      setModalProps({ confirmLoading: false });
      isUpdate.value = !!data?.isUpdate;
      if (unref(isUpdate)) {
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
        values.courseId = unref(courseId);
        setModalProps({ confirmLoading: true });
        if (id) {
          await updateChapter(id, values);
        } else {
          await createChapter(values);
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
