<template>
  <BasicModal v-bind="$attrs" @register="registerModal" title="组卷配置" @ok="handleSubmit"
              width="60%">
    <PageWrapper dense contentClass="flex">
      <SubjectCategoryTree ref="cateTreeRef" class="w-1/3 xl:w-1/3" @select="handleSelectCategory"/>
      <div>
        <div>
          <p>该分类下的题目数量：{{subjectCnt}}</p>
        </div>
        <BasicForm @register="registerForm"/>
      </div>
    </PageWrapper>
  </BasicModal>
</template>
<script lang="ts">
import {defineComponent, ref, unref} from 'vue';
import {BasicModal, useModalInner} from '/@/components/Modal';
import {BasicForm, useForm} from '/@/components/Form/index';
import {useMessage} from "/@/hooks/web/useMessage";
import {formSchema} from "./random.data";
import {PageWrapper} from '/@/components/Page';
import SubjectCategoryTree from '../subject/SubjectCategoryTree.vue';
import {randomAddSubjects} from "/@/api/exam/examination";
import {getSubjectCountByCategoryId} from "/@/api/exam/subject";

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
    const cateTreeRef = ref<any>(undefined);
    const examinationId = ref<string>('');
    const subjectCnt = ref<number>(0);
    const [registerForm, {resetFields, validate, setFieldsValue}] = useForm({
      labelWidth: 100,
      schemas: formSchema,
      showActionButtonGroup: false,
    });

    const [registerModal, {setModalProps, closeModal}] = useModalInner(async (data) => {
      resetFields();
      setModalProps({confirmLoading: false});
      examinationId.value = data?.examinationId || null;
      if (cateTreeRef.value !== undefined) {
        unref(cateTreeRef).resetSelectedKeys();
      }
    });

    async function handleSubmit() {
      try {
        const values = await validate();
        const {subjectCount} = values;
        if (subjectCount > subjectCnt.value) {
          createMessage.error('题目数量超过' + subjectCnt.value);
          return;
        }
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

    async function handleSelectCategory(categoryId = '') {
      await setFieldsValue({categoryId});
      // 根据分类ID查询题目数量
      if (categoryId !== '') {
        const res = await getSubjectCountByCategoryId(categoryId);
        subjectCnt.value = Number(res);
      } else {
        subjectCnt.value = 0;
      }
    }

    return {
      cateTreeRef,
      subjectCnt,
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
