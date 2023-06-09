<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" :defaultFullscreen="true" @ok="handleSubmit">
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>
<script lang="ts">
import {useI18n} from '/@/hooks/web/useI18n';
import { defineComponent, ref, computed, unref } from 'vue';
import { BasicModal, useModalInner } from '/@/components/Modal';
import { BasicForm, useForm } from '/@/components/Form/index';
import { formSchema } from './section.data';
import { createSection, updateSection} from "/@/api/exam/section";
export default defineComponent({
  name: 'SectionDataModal',
  components: { BasicModal, BasicForm },
  emits: ['success', 'register'],
  setup(_, { emit }) {
    const {t} = useI18n();
    const isUpdate = ref(true);
    let id: string;
    let chapterId: string;
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
      chapterId = data?.chapterId || null;
    });
    const getTitle = computed(() => (!unref(isUpdate) ? t('common.addText') :
      t('common.editText')));
    async function handleSubmit() {
      try {
        const values = await validate();
        Object.assign(values, {chapterId: unref(chapterId)});
        setModalProps({ confirmLoading: true });
        if (id) {
          await updateSection(id, values);
        } else {
          await createSection(values);
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
