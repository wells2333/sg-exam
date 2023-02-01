<template>
  <PageWrapper
    title=""
    contentBackground
    content=""
    contentClass="p-4"
  >
    <BasicForm :actionColOptions="{ span: 24 }" ref="formElRef" @register="register">
      <template #add="{ field }">
        <Button @click="addOption(field)" style="margin-right: 12px;"> +</Button>
        <Button @click="delOption(field)"> -</Button>
      </template>
    </BasicForm>
    <component :is="currentModal" :optionField="optionField" v-model:visible="modalVisible"
               @success="handleOptionInputSuccess"/>
    <OptionModal @register="registerOptionModal"/>
  </PageWrapper>
</template>
<script lang="ts">
import {BasicForm, FormActionType, useForm} from '/@/components/Form';
import {useModal} from '/@/components/Modal';
import {ComponentOptions, defineComponent, nextTick, ref, shallowRef, unref} from 'vue';
import {OptionItem, SubjectSubmitData, subjectType} from './subject.constant';
import {PageWrapper} from '/@/components/Page';
import {Input} from 'ant-design-vue';
import {Button} from '/@/components/Button';
import OptionModal from './OptionModal.vue';
import {
  emptySubject,
  generateAddOption,
  generateAnswer,
  generateChoicesSchemas,
  generateTinymceField
} from './subject.gen';
import {addOptionBtnSlot, optionPrefix} from './subject.constant';

export default defineComponent({
  name: 'SubjectChoicesPage',
  components: {BasicForm, PageWrapper, OptionModal, [Input.name]: Input, Button},
  setup() {
    const categoryId = ref<string>();
    const examinationId = ref<string>();
    const isMulti = ref<boolean>(false);
    const formElRef = ref<Nullable<FormActionType>>(null);
    const [register, {
      validate,
      clearValidate,
      setProps,
      resetFields,
      updateSchema,
      setFieldsValue,
      appendSchemaByField,
      removeSchemaByFiled
    }] = useForm({
      labelWidth: 100,
      schemas: [],
      showResetButton: false,
      showSubmitButton: false,
    });

    const currentModal = shallowRef<Nullable<ComponentOptions>>(null);
    const [registerOptionModal, {openModal: openOptionModal}] = useModal();
    const modalVisible = ref<Boolean>(false);
    const optionField = ref<any>();

    function addOption(field) {
      currentModal.value = OptionModal;
      optionField.value = field;
      nextTick(() => {
        modalVisible.value = true;
      });
    }

    function delOption(field) {
      const optionName = field.split(addOptionBtnSlot)[1];
      const newAnswerOptions: any[] = [];
      const schema = unref(formElRef.value).getSchema;
      schema.forEach(fieldObj => {
        const tempField = fieldObj.field;
        if (tempField.startsWith(optionPrefix) && optionName !== tempField) {
          const name = tempField.split(optionPrefix)[1];
          newAnswerOptions.push({label: name, value: name});
        }
      });
      updateSchema(generateAnswer(newAnswerOptions, newAnswerOptions[0].value, unref(isMulti)));
      removeSchemaByFiled(field);
      removeSchemaByFiled(optionName);
    }

    function handleOptionInputSuccess(values) {
      const {optionName, field} = values;
      const fieldName = optionPrefix + optionName;
      appendSchemaByField(generateTinymceField(optionName, fieldName), field);
      appendSchemaByField(generateAddOption(addOptionBtnSlot + optionPrefix + optionName), fieldName);
      // update answer option
      const newAnswerOptions: any[] = [];
      const schema = unref(formElRef.value).getSchema;
      schema.forEach(fieldObj => {
        const tempField = fieldObj.field;
        if (tempField.startsWith(optionPrefix)) {
          const name = tempField.split(optionPrefix)[1];
          newAnswerOptions.push({label: name, value: name});
          if ((addOptionBtnSlot + tempField) === field) {
            newAnswerOptions.push({label: optionName, value: optionName});
          }
        }
      });
      updateSchema(generateAnswer(newAnswerOptions, newAnswerOptions[0].value, unref(isMulti)));
    }

    async function getSubjectValue() {
      const values = await validate();
      // 默认单选题
      let type = unref(isMulti) ? subjectType.SubjectMultiChoices : subjectType.SubjectChoices;
      const value: SubjectSubmitData = {
        type,
        categoryId: unref(categoryId),
        examinationId: unref(examinationId),
        options: [],
        answer: {}
      };
      const options: OptionItem[] = [];
      let sort = 0;
      Object.keys(values).forEach(key => {
        const index = key.indexOf(optionPrefix);
        if (index >= 0 && key.indexOf(addOptionBtnSlot) < 0) {
          const optionName = key.split(optionPrefix)[1];
          options.push({optionName, optionContent: values[key], sort: ++sort});
        }
      });
      Object.assign(value, values);
      value.options = options;
      let answer: object | string = value.answer;
      if (Array.isArray(value.answer)) {
        answer = value.answer.join(',');
      }
      value.answer = {answer};
      return value;
    }

    // 更新题目
    function setSubjectValue(data) {
      setParams(data);
      const subjectData = unref(data?.subjectData);
      // 重新生成schemas
      const isUpdate = subjectData.id != undefined;
      isMulti.value = data?.isMulti;
      const schemas = generateChoicesSchemas(unref(data?.subjectData), unref(data?.defaultOptions), data?.isMulti, isUpdate);
      setProps({schemas});
      setTimeout(() => {
        setValue(data, subjectData);
      }, 10);
    }

    function resetSchemas(data) {
      setParams(data);
      const schemas = generateChoicesSchemas(unref(data?.subjectData), unref(data?.defaultOptions), data?.isMulti, false);
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

    function setParams(data) {
      isMulti.value = data?.isMulti;
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

    return {
      formElRef,
      register,
      addOption,
      delOption,
      registerOptionModal,
      openOptionModal,
      currentModal,
      modalVisible,
      handleOptionInputSuccess,
      optionField,
      getSubjectValue,
      setSubjectValue,
      resetSchemas
    };
  },
});
</script>
<style lang="less" scoped>
.form-wrap {
  padding: 24px;
  background-color: @component-background;
}
</style>
