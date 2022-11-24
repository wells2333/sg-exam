<template>
  <PageWrapper
    title=""
    contentBackground
    content=""
    contentClass="p-4"
  >
    <BasicForm @register="register" ref="formElRef"/>
  </PageWrapper>
</template>
<script lang="ts">
import {BasicForm, FormActionType, useForm} from '/@/components/Form';
import {defineComponent, ref, unref} from 'vue';
import {SubjectSubmitData, subjectType} from './subject.constant';
import {PageWrapper} from '/@/components/Page';
import {emptySubject, genVideoSchemas} from "./subject.gen";

export default defineComponent({
  name: 'SubjectVideoPage',
  components: {BasicForm, PageWrapper},
  setup() {
    const categoryId = ref<string>();
    const examinationId = ref<string>();
    const formElRef = ref<Nullable<FormActionType>>(null);
    const [register, {resetFields, validate, clearValidate, setFieldsValue, setProps}] = useForm({
      labelWidth: 100,
      schemas: [],
      showResetButton: false,
      showSubmitButton: false,
    });

    async function getSubjectValue() {
      const values = await validate();
      const value: SubjectSubmitData = {
        type: subjectType.SubjectVideo,
        categoryId: unref(categoryId),
        examinationId: unref(examinationId),
        options: [],
        answer: {}
      };
      Object.assign(value, values);
      value.answer = {
        answer: value.answer
      };
      return value;
    }

    function resetSchemas(data) {
      setParams(data);
      const schemas = genVideoSchemas();
      setProps({schemas});
      setTimeout(() => {
        setFieldsValue(empty(data));
        clearValidate();
      }, 10);
    }

    function empty(data) {
      const empty = emptySubject();
      Object.assign(empty, {sort: unref(data?.nextSubjectNo)});
      return empty;
    }

    function setSubjectValue(data) {
      setParams(data);
      const subjectData = unref(data?.subjectData);
      const {answer} = subjectData;
      if (answer) {
        subjectData.answer = answer.answer;
      }
      resetSchemas(data);
      resetFields();
      setTimeout(() => {
        setValue(data, subjectData);
      }, 10);
    }

    function setParams(data) {
      categoryId.value = data?.categoryId;
      examinationId.value = data?.examinationId;
    }

    function setValue(data, subjectData) {
      setFieldsValue(emptySubject());
      setNextSubjectNo(data);
      if (Object.keys(subjectData).length !== 0) {
        setFieldsValue(subjectData);
      }
      clearValidate();
    }

    function setNextSubjectNo(data) {
      setFieldsValue({sort: unref(data?.nextSubjectNo)})
    }

    return {formElRef, getSubjectValue, setSubjectValue, resetSchemas, register};
  },
});
</script>
<style lang="less" scoped>
.form-wrap {
  padding: 24px;
  background-color: @component-background;
}
</style>
