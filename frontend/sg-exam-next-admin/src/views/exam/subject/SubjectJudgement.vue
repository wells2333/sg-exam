<template>
  <PageWrapper
    title=""
    contentBackground
    content=""
    contentClass="p-4"
  >
    <BasicForm @register="register" />
  </PageWrapper>
</template>
<script lang="ts">
import { BasicForm, useForm } from '/@/components/Form';
import {defineComponent, ref, unref} from 'vue';
import {
  subjectNameSchemas,
  basicSchemas,
  answerSchemas,
  subjectType, generateJudgementAnswer
} from './subject.data';
import { useMessage } from '/@/hooks/web/useMessage';
import { PageWrapper } from '/@/components/Page';
import {useGo} from "/@/hooks/web/usePage";
import {createSubject, updateSubject} from "/@/api/exam/subject";
import {useRouter} from "vue-router";

export default defineComponent({
  name: 'SubjectJudgementPage',
  components: { BasicForm, PageWrapper },
  props: {
    subjectData: { type: Object },
    categoryId: { type: String },
    examinationId: { type: String },
  },
  setup(props) {
    const go = useGo();
    const router = useRouter();
    const { query } = unref(router.currentRoute);
    const { createMessage } = useMessage();
    const subjectData = props.subjectData;
    const categoryId = props.categoryId;
    let examinationId = props.examinationId;
    // 优先使用URL上面的参数
    if (query.eId) {
      examinationId = query.eId;
    }
    const isUpdate = ref(true);
    let subjectId: string;
    if (subjectData !== null) {
      const { id } =  subjectData;
      subjectId = id;
    }
    if (subjectId) {
      const { answer } =  subjectData;
      if (answer) {
        subjectData.answer = answer.answer;
      }
    } else {
      isUpdate.value = false;
    }
    const schemas: any[] = [];
    schemas.push(...subjectNameSchemas);
    schemas.push(...basicSchemas);
    schemas.push(generateJudgementAnswer());
    schemas.push(...answerSchemas);
    const [register, { validate, setProps, setFieldsValue }] = useForm({
      labelWidth: 100,
      schemas: schemas,
      model: subjectData,
      submitButtonOptions: {
        text: '保存',
      },
      showResetButton: false,
      submitFunc: customSubmitFunc,
    });

    async function customSubmitFunc() {
      const values = await validate();
      setProps({
        submitButtonOptions: {
          loading: true,
        },
      });
      const value = {
        type: subjectType.SubjectJudgement,  // 判断题
        categoryId,
        examinationId
      };
      Object.assign(value, values);
      value.answer = {
        answer: value.answer
      };
      try {
        let result;
        if (unref(isUpdate)) {
          result = await updateSubject(subjectId, value);
        } else {
          result = await createSubject(value);
        }
        createMessage.success('保存成功');
        let url = '/exam/subject_detail/' + result.id;
        if (result.categoryId) {
          url += '&categoryId=' + result.categoryId;
        }
        go(url);
      } catch (error) {
        console.error(error);
      } finally {
        setProps({
          submitButtonOptions: {
            loading: false,
          },
        });
      }
    }
    setTimeout(() => {
      setFieldsValue(subjectData);
    }, 50);
    return { register };
  },
});
</script>
<style lang="less" scoped>
.form-wrap {
  padding: 24px;
  background-color: @component-background;
}
</style>
