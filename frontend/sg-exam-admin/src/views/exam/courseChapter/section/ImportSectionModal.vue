<template>
  <BasicModal v-bind="$attrs" @register="registerModal" title="批量导入" @ok="handleSubmit"
              width="50%">
    <div>支持通过 zip 文件导入：</div>
    <div>1. zip 文件内容为 PDF 文件：后台根据 PDF 标题自动生成课程章节</div>
    <div style="margin-bottom: 20px;">2. zip 文件内容为 MP4 文件：后台根据 MP4 文件名自动生成课程章节</div>
    <BasicForm @register="registerForm"/>
  </BasicModal>
</template>
<script lang="ts">
import {useI18n} from '/@/hooks/web/useI18n';
import {defineComponent} from 'vue';
import {BasicModal, useModalInner} from '/@/components/Modal';
import {useMessage} from "/@/hooks/web/useMessage";
import {BasicForm, useForm} from "/@/components/Form";
import {formSchema} from "./import.data";

export default defineComponent({
  name: 'ImportSectionModal',
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
      setFieldsValue({chapterId: data?.chapterId || null, file: null});
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
