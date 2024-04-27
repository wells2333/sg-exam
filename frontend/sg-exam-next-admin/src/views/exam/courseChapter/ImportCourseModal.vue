<template>
  <BasicModal v-bind="$attrs" @register="registerModal" title="导入" @ok="handleSubmit"
              width="50%">
    <BasicForm @register="registerForm"/>
  </BasicModal>
</template>
<script lang="ts">
import { useI18n } from '/@/hooks/web/useI18n';
import {defineComponent, reactive, ref} from 'vue';
import {BasicModal, useModalInner} from '/@/components/Modal';
import {useMessage} from "/@/hooks/web/useMessage";
import {BasicForm, useForm} from "/@/components/Form";
import {formSchema} from "./import.data";

export default defineComponent({
  name: 'ImportCourseModal',
  components: {
    BasicForm,
    BasicModal,
  },
  emits: ['success', 'register'],
  setup(_, {emit}) {
    const { t } = useI18n();
    const {createMessage} = useMessage();
    const [registerForm, {resetFields, setFieldsValue}] = useForm({
      labelWidth: 100,
      schemas: formSchema,
      showActionButtonGroup: false,
    });
    const [registerModal, {setModalProps, closeModal}] = useModalInner(async (data) => {
      resetFields();
      setFieldsValue({courseId: data?.courseId || null, pdfFile: null});
      setModalProps({confirmLoading: false});
    });

    async function handleSubmit() {
      try {
        setModalProps({confirmLoading: true});
        closeModal();
        emit('success');
      } catch (error) {
        console.error(error);
        createMessage.error('保存失败');
      } finally {
        setModalProps({confirmLoading: false});
      }
    }

    return {
      t,
      registerModal,
      registerForm,
      handleSubmit
    };
  },
});
</script>

<style lang="less">
.ant-modal-wrap .ant-modal {
  top: 20px;
}

.ant-modal-footer {
  text-align: center !important;
}
</style>
