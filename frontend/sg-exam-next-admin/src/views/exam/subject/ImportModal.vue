<template>
  <BasicModal v-bind="$attrs" @register="registerModal" title="导入题目" @ok="handleSubmit"
              width="50%">
    <p style="color: red">1. 下载模板</p>
    <p style="color: red">2. 按模板填写题目内容</p>
    <div>
      <a-button class="template-btn" type="primary" preIcon="carbon:cloud-download"
                @click="handleDownloadJSONTemplate">JSON模板下载
      </a-button>
      <a-button class="template-btn" type="primary" preIcon="carbon:cloud-download"
                @click="handleDownloadEXCELTemplate">EXCEL模板下载
      </a-button>
    </div>
    <p style="color: red">3. 上传</p>
    <div>
      <BasicForm @register="registerForm"/>
    </div>
  </BasicModal>
</template>
<script lang="ts">
import {defineComponent} from 'vue';
import {BasicModal, useModalInner} from '/@/components/Modal';
import {BasicForm, useForm} from '/@/components/Form/index';
import {useMessage} from "/@/hooks/web/useMessage";
import {formSchema} from "./import.data";
import AButton from "/@/components/Button/src/BasicButton.vue";
import {SubjectsApi} from "/@/api/api";

export default defineComponent({
  name: 'RandomSubjectModal',
  components: {
    AButton,
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
      setFieldsValue({categoryId: data?.categoryId || null, jsonFile: null, excelFile: null});
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

    function handleDownloadJSONTemplate() {
        window.open(SubjectsApi.JsonTemplate);
    }

    function handleDownloadEXCELTemplate() {
      window.open(SubjectsApi.ExcelTemplate);
    }

    return {
      registerForm,
      registerModal,
      handleSubmit,
      handleDownloadJSONTemplate,
      handleDownloadEXCELTemplate
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

.template-btn {
  margin-left: 20px;
  margin-bottom: 10px;
}
</style>
