<template>
  <BasicModal v-bind="$attrs" @register="registerModal" title="导入题目" @ok="handleSubmit"
              width="50%">
    <BasicForm @register="registerForm"/>
  </BasicModal>
</template>
<script lang="ts">
import {defineComponent, ref} from 'vue';
import {BasicModal, useModalInner} from '/@/components/Modal';
import {BasicForm, useForm} from '/@/components/Form/index';
import {useMessage} from "/@/hooks/web/useMessage";
import {formSchema} from "./import.data";

export default defineComponent({
  name: 'RandomSubjectModal',
  components: {
    BasicModal,
    BasicForm
  },
  emits: ['success', 'register'],
  setup(_, {emit}) {
    const {createMessage} = useMessage();
    const [registerForm, {resetFields, setFieldsValue}] = useForm({
      labelWidth: 100,
      schemas: formSchema,
      showActionButtonGroup: false,
    });

    const [registerModal, {setModalProps, closeModal}] = useModalInner(async (data) => {
      resetFields();
      setFieldsValue({categoryId: data?.categoryId || null, file: null});
      setModalProps({confirmLoading: false});
    });

    async function handleSubmit() {
      try {
        setModalProps({confirmLoading: true});
        closeModal();
        emit('success');
      } catch (error) {
        console.error(error);
        createMessage.error('导入失败');
      } finally {
        setModalProps({confirmLoading: false});
      }
    }

    return {
      registerForm,
      registerModal,
      handleSubmit
    };
  },
});
</script>

<style lang="less">
.ant-modal-wrap .ant-modal {
  top: 20px;
}

// 按钮居中
.ant-modal-footer {
  text-align: center !important;
}
</style>
