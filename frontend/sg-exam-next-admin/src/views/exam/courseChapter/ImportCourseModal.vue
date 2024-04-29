<template>
  <BasicModal v-bind="$attrs" @register="registerModal" title="批量导入" @ok="handleSubmit"
              width="50%">
    <div>支持两种导入方式：</div>
    <div>1. 单个PDF 文件：后台将 PDF 内容提取出来，自动生成课程章节</div>
    <div style="margin-bottom: 20px;">2. 将多个课程视频（MP4）打包成一个 zip 文件：后台为每个视频生成一个章节，章节标题为文件名</div>
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
      setFieldsValue({courseId: data?.courseId || null, file: null});
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
