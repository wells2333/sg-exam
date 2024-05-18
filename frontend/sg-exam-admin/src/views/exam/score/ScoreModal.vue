<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <BasicForm @register="registerForm"/>
  </BasicModal>
</template>
<script lang="ts">
import {useI18n} from '/@/hooks/web/useI18n';
import {defineComponent, ref, computed, unref} from 'vue';
import {BasicModal, useModalInner} from '/@/components/Modal';
import {BasicForm, useForm} from '/@/components/Form/index';
import {formSchema} from './score.data';

export default defineComponent({
  name: 'ScoreModal',
  components: {BasicModal, BasicForm},
  emits: ['success', 'register'],
  setup(_, {emit}) {
    const {t} = useI18n();
    const isUpdate = ref(true);
    const [registerForm, {resetFields, setFieldsValue, validate}] = useForm({
      labelWidth: 100,
      schemas: formSchema,
      showActionButtonGroup: false,
    });
    const [registerModal, {setModalProps, closeModal}] = useModalInner(async (data) => {
      resetFields();
      setModalProps({confirmLoading: false});
      isUpdate.value = !!data?.isUpdate;
      if (unref(isUpdate)) {
        setFieldsValue({
          ...data.record,
        });
      }
    });
    const getTitle = computed(() => (!unref(isUpdate) ? t('common.addText') :
      t('common.editText')));

    async function handleSubmit() {
      try {
        const values = await validate();
        setModalProps({confirmLoading: true});
        closeModal();
        emit('success');
      } finally {
        setModalProps({confirmLoading: false});
      }
    }

    return {t, registerModal, registerForm, getTitle, handleSubmit};
  },
});
</script>
