<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" width="60%" @ok="handleSubmit">
    <BasicForm @register="registerForm">
    </BasicForm>
  </BasicModal>
</template>
<script lang="ts">
import { defineComponent, ref, computed, unref } from 'vue';
import { BasicModal, useModalInner } from '/@/components/Modal';
import { BasicForm, useForm } from '/@/components/Form/index';
import { formSchema } from './gen.data';
import { createGen, updateGen } from '/@/api/sys/gen';

export default defineComponent({
  name: 'GenModal',
  components: { BasicModal, BasicForm },
  emits: ['success', 'register'],
  setup(_, { emit }) {
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
        setModalProps({ confirmLoading: true });
        if (id) {
          await updateGen(id, values);
        } else {
          await createGen(values);
        }
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
