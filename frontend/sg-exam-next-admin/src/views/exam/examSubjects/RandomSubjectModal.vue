<template>
  <BasicModal v-bind="$attrs" @register="registerModal" title="组卷配置" @ok="handleSubmit"
              width="60%">
    <PageWrapper dense contentFullHeight fixedHeight contentClass="flex">
      <SubjectCategoryTree class="w-1/5 xl:w-1/6" @select="handleSelectCategory"/>
      <BasicForm @register="registerForm"/>
    </PageWrapper>
  </BasicModal>
</template>
<script lang="ts">
import {defineComponent, ref} from 'vue';
import {BasicModal, useModalInner} from '/@/components/Modal';
import {BasicForm, useForm} from '/@/components/Form/index';
import {useMessage} from "/@/hooks/web/useMessage";
import {formSchema} from "./random.data";
import {PageWrapper} from '/@/components/Page';
import SubjectCategoryTree from '../subject/SubjectCategoryTree.vue';
import {randomAddSubjects} from "/@/api/exam/examination";

export default defineComponent({
  name: 'RandomSubjectModal',
  components: {
    BasicModal,
    BasicForm,
    PageWrapper,
    SubjectCategoryTree
  },
  emits: ['success', 'register'],
  setup(_, {emit}) {
    const {createMessage} = useMessage();
    const examinationId = ref<string>('');
    const [registerForm, {resetFields, validate, setFieldsValue}] = useForm({
      labelWidth: 100,
      schemas: formSchema,
      showActionButtonGroup: false,
    });

    const [registerModal, {setModalProps, closeModal}] = useModalInner(async (data) => {
      resetFields();
      setModalProps({confirmLoading: false});
      examinationId.value = data?.examinationId || null;
    });

    async function handleSubmit() {
      try {
        const values = await validate();
        setModalProps({confirmLoading: true});
        const res = await randomAddSubjects(examinationId.value, values);
        if (res) {
          createMessage.success('保存成功');
        }
        closeModal();
        emit('success');
      } catch (error) {
        console.error(error);
        createMessage.error('保存失败，请选择分类和输入题目数量');
      } finally {
        setModalProps({confirmLoading: false});
      }
    }

    function handleSelectCategory(categoryId = '') {
      setFieldsValue({categoryId});
    }

    return {
      registerForm,
      registerModal,
      handleSubmit,
      handleSelectCategory
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
