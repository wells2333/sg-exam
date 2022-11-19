<template>
  <PageWrapper
    title=""
    contentBackground
    content=""
    contentClass="p-4"
  >
    <BasicForm :actionColOptions="{ span: 24 }" ref="formElRef" @register="register">
      <template #add="{ field }">
        <Button @click="addOption(field)" style="margin-right: 12px;"> + </Button>
        <Button @click="delOption(field)"> - </Button>
      </template>
    </BasicForm>
    <component :is="currentModal" :optionField="optionField" v-model:visible="modalVisible" @success="handleOptionInputSuccess"/>
    <OptionModal @register="registerOptionModal" />
  </PageWrapper>
</template>
<script lang="ts">
import {BasicForm, FormSchema, FormActionType, useForm} from '/@/components/Form';
import { useModal } from '/@/components/Modal';
import {
  defineComponent,
  shallowRef,
  ComponentOptions,
  ref,
  unref,
  nextTick,
  onMounted,
  watch
} from 'vue';
import {
  subjectNameSchemas,
  answerSchemas,
  basicSchemas, generateAddOption,
  generateAnswer,
  generateTinymceField, optionDividerSchemas,
  subjectType
} from './subject.data';
import {useMessage} from '/@/hooks/web/useMessage';
import {PageWrapper} from '/@/components/Page';
import {createSubject, updateSubject} from '/@/api/exam/subject';
import {useGo} from "/@/hooks/web/usePage";
import { Input } from 'ant-design-vue';
import { Button } from '/@/components/Button';
import OptionModal from './OptionModal.vue';
import {isArray} from "/@/utils/is";
import {useRouter} from "vue-router";

export default defineComponent({
  name: 'SubjectChoicesPage',
  components: { BasicForm, PageWrapper, OptionModal, [Input.name]: Input, Button},
  props: {
    subjectData: { type: Object },
    categoryId: { type: String },
    examinationId: { type: String },
    defaultOptions: { type: Object },
    nextSubjectNo: { type: Number },
    isMulti: { type: Boolean }
  },
  setup(props) {
    const go = useGo();
    const router = useRouter();
    const { query } = unref(router.currentRoute);
    const formElRef = ref<Nullable<FormActionType>>(null);
    const { createMessage } = useMessage();
    const subjectData = props.subjectData;
    // 是否多选
    const isMulti = props.isMulti;
    const defaultOptions = props.defaultOptions;
    const nextSubjectNo = props.nextSubjectNo;
    const categoryId = props.categoryId;
    let examinationId = props.examinationId;
    // 优先使用URL上面的参数
    if (query.eId) {
      examinationId = query.eId;
    }
    const optionsSchemas: FormSchema[] = [];
    const optionPrefix = 'options.';
    const addOptionBtnSlot = 'slot_';
    const isUpdate = ref(true);
    let subjectId: string;
    const answerOptions = [];
    if (subjectData !== null) {
      const { id } =  subjectData;
      subjectId = id;
    }
    const schemas: any[] = [];
    if (subjectId) {
      const { options, answer } =  subjectData;
      initOptionsAndAnswer(subjectData, options, answerOptions, optionsSchemas, answer);
    } else {
      // 新增题目
      if (defaultOptions !== null ) {
        isUpdate.value = false;
        if (defaultOptions.length > 0) {
          defaultOptions.forEach(item => {
            const fieldName = optionPrefix + item.labelName;
            optionsSchemas.push(generateTinymceField(item.labelName, fieldName));
            optionsSchemas.push(generateAddOption(addOptionBtnSlot + fieldName));
            answerOptions.push({ label: item.labelName, value: item.labelName});
          });
        }
      }
    }
    schemas.push(...subjectNameSchemas);
    schemas.push(...basicSchemas);
    schemas.push(...optionDividerSchemas)
    optionsSchemas.push(generateAnswer(answerOptions, answerOptions[0].value, isMulti));
    schemas.push(...optionsSchemas);
    schemas.push(...answerSchemas);
    const [register, { validate, updateSchema, setProps, setFieldsValue, appendSchemaByField, removeSchemaByFiled }] = useForm({
      labelWidth: 100,
      schemas: schemas,
      model: subjectData,
      submitFunc: customSubmitFunc,
      submitButtonOptions: {
        text: '保存',
      },
      showResetButton: false,
    });

    const currentModal = shallowRef<Nullable<ComponentOptions>>(null);
    const [ registerOptionModal, { openModal: openOptionModal }] = useModal();
    const modalVisible = ref<Boolean>(false);
    const optionField = ref<any>();
    async function customSubmitFunc() {
        const values = await validate();
        setProps({
          submitButtonOptions: {
            loading: true,
          },
        });
        // 默认单选题
        let type = isMulti ? subjectType.SubjectMultiChoices : subjectType.SubjectChoices;
        const value = {
          type,
          categoryId,
          examinationId
        };
        const options = [];
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
        let answer = value.answer;
        if (Array.isArray(value.answer)) {
          answer = value.answer.join(',');
        }
        value.answer = { answer };
      try {
        let result;
        if (unref(isUpdate)) {
          result = await updateSubject(subjectId, value);
        } else {
          result = await createSubject(value);
        }
        createMessage.success('保存成功');
        let url = '/exam/subject_detail/' + result.id + '?';
        if (result.categoryId) {
          url += 'categoryId=' + result.categoryId;
        }
        go(url);
      } catch (error) {
        console.error(error);
        createMessage.error('保存失败');
      } finally {
        setProps({
          submitButtonOptions: {
            loading: false,
          },
        });
      }
    };

    function initOptionsAndAnswer(subjectData, options, answerOptions, optionsSchemas, answer) {
      if (options && options.length > 0) {
        if (optionsSchemas.length === 0) {
          options.forEach(item => {
            const fieldName = optionPrefix + item.optionName;
            optionsSchemas.push(generateTinymceField(item.optionName, fieldName));
            optionsSchemas.push(generateAddOption(addOptionBtnSlot + fieldName));
            subjectData[fieldName] = item.optionContent;
            answerOptions.push({ label: item.optionName, value: item.optionName});
          });
        }
      }
      if (answer) {
        if (isMulti) {
          subjectData.answer = answer.answer.split(',');
        } else {
          subjectData.answer = answer.answer;
        }
      }
    }

    function addOption(field) {
      currentModal.value = OptionModal;
      optionField.value = field;
      nextTick(() => {
        modalVisible.value = true;
      });
    }

    function delOption(field) {
      const optionName = field.split(addOptionBtnSlot)[1];
      // update answer option
      const newAnswerOptions: any[] = [];
      const schema = unref(formElRef.value).getSchema;
      schema.forEach(fieldObj => {
        const tempField = fieldObj.field;
        if (tempField.startsWith(optionPrefix) && optionName !== tempField) {
          const name = tempField.split(optionPrefix)[1];
          newAnswerOptions.push({ label: name, value: name });
        }
      });
      updateSchema(generateAnswer(newAnswerOptions, newAnswerOptions[0].value, isMulti));
      removeSchemaByFiled(field);
      removeSchemaByFiled(optionName);
    }

    function handleOptionInputSuccess(values) {
      const { optionName, field } = values;
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
          newAnswerOptions.push({ label: name, value: name });
          if ((addOptionBtnSlot + tempField) === field) {
            newAnswerOptions.push({label: optionName, value: optionName});
          }
        }
      });
      updateSchema(generateAnswer(newAnswerOptions, newAnswerOptions[0].value, isMulti));
    }

    onMounted(() => {
      if (!subjectId) {
        setFieldsValue(subjectData);
        setFieldsValue({ sort: nextSubjectNo });
      }
    });

    setTimeout(() => {
      setFieldsValue(subjectData);
    }, 50);

    return { formElRef, register, addOption, delOption, registerOptionModal, openOptionModal, currentModal, modalVisible, handleOptionInputSuccess, optionField };
  },
});
</script>
<style lang="less" scoped>
.form-wrap {
  padding: 24px;
  background-color: @component-background;
}
</style>
