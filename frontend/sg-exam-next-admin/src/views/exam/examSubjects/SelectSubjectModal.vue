<template>
  <BasicModal v-bind="$attrs" @register="registerModal" title="选择题目" @ok="handleSubmit"
              width="90%">
    <PageWrapper dense contentClass="flex">
      <SubjectCategoryTree class="w-1/5 xl:w-1/6" @select="handleSelectCategory"/>
      <BasicTable @register="registerTable" class="w-3/4 xl:w-4/5"></BasicTable>
    </PageWrapper>
  </BasicModal>
</template>
<script lang="ts">
import {defineComponent, reactive, ref} from 'vue';
import {BasicTable, TableAction, useTable} from '/@/components/Table';
import SubjectChoices from '/@/components/Subjects/SubjectChoices.vue';
import SubjectShortAnswer from '/@/components/Subjects/SubjectShortAnswer.vue';
import SubjectJudgement from '/@/components/Subjects/SubjectJudgement.vue';
import SubjectSpeech from "/@/components/Subjects/SubjectSpeech.vue";
import SubjectVideo from "/@/components/Subjects/SubjectVideo.vue";
import SubjectCategoryTree from '../subject/SubjectCategoryTree.vue';
import {getSubjectList} from '/@/api/exam/subject';
import {batchAddSubjects} from "/@/api/exam/examination";
import {BasicModal, useModalInner} from '/@/components/Modal';
import {useMessage} from "/@/hooks/web/useMessage";
import {PageWrapper} from '/@/components/Page';
import {columns, searchFormSchema} from "/@/views/exam/subject/subject.data";

export default defineComponent({
  name: 'SelectSubjectModal',
  components: {
    BasicModal,
    SubjectChoices,
    SubjectShortAnswer,
    SubjectJudgement,
    SubjectSpeech,
    SubjectVideo,
    SubjectCategoryTree,
    PageWrapper,
    BasicTable, TableAction
  },
  emits: ['success', 'register'],
  setup(_, {emit}) {
    const {createMessage} = useMessage();
    const examinationId = ref<string>('');
    const subjectSearchInfo = reactive<Recordable>({});
    const [registerTable, {reload, getSelectRows}] = useTable({
      title: '',
      api: (arg) => {
        const {categoryId} = subjectSearchInfo;
        if (categoryId === undefined) {
          return undefined;
        }
        const params = {categoryId};
        Object.assign(params, arg);
        return getSubjectList(params);
      },
      columns,
      formConfig: {
        labelWidth: 120,
        schemas: searchFormSchema,
      },
      pagination: true,
      striped: false,
      useSearchForm: true,
      showTableSetting: false,
      bordered: true,
      handleSearchInfoFn(info) {
        return info;
      },
      showIndexColumn: false,
      canResize: false,
      rowSelection: {
        type: 'checkbox',
        selectWay: 'onSelect'
      }
    });

    const [registerModal, {setModalProps, closeModal}] = useModalInner(async (data) => {
      setModalProps({confirmLoading: false});
      examinationId.value = data?.examinationId || null;
      await reload();
    });

    async function handleSubmit() {
      try {
        // 获取选中的题目
        const rows: Recordable[] = getSelectRows();
        console.log(rows);
        if (rows && rows.length > 0) {
          const res = await batchAddSubjects(examinationId.value, rows);
          if (res) {
            createMessage.success('保存成功');
          }
        } else {
          createMessage.warn('未选择题目');
        }
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

    function handleSelectCategory(categoryId = '') {
      subjectSearchInfo.categoryId = categoryId;
      reload();
    }

    return {
      prefixCls: 'subject',
      examinationId,
      registerModal,
      registerTable,
      handleSubmit,
      handleSelectCategory,
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
