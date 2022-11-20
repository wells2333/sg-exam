<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" width="60%" @ok="handleSubmit">
    <BasicForm @register="registerForm">
    </BasicForm>
  </BasicModal>
</template>
<script lang="ts">
import { defineComponent, computed } from 'vue';
import { BasicModal, useModalInner } from '/@/components/Modal';
import { BasicForm, useForm } from '/@/components/Form/index';
import { formSchema } from './log.data';

export default defineComponent({
  name: 'LogModal',
  components: { BasicModal, BasicForm },
  emits: ['success', 'register'],
  setup(_, { emit }) {
    const [registerForm, { resetFields }] = useForm({
      labelWidth: 100,
      schemas: formSchema,
      showActionButtonGroup: false,
    });
    const [registerModal, { setModalProps, closeModal }] = useModalInner(async () => {
      resetFields();
      setModalProps({ confirmLoading: false });
    });
    const getTitle = computed(() => '查看');
    async function handleSubmit() {
      try {
        setModalProps({ confirmLoading: true });
        closeModal();
        emit('success');
      } finally {
        setModalProps({ confirmLoading: false });
      }
    }
    return {
      registerModal,
      registerForm,
      getTitle,
      handleSubmit
    };
  },
});
</script>
