<template>
  <div class="score-detail">
    <Description
      size="middle"
      title="成绩详情"
      :bordered="false"
      :column="3"
      :data="score"
      :schema="scoreDetailSchema"
    />
    <div style="background-color: white" v-if="score.submitStatus < 3">
      <a-button type="primary" @click="handleMarkOk" class="mr-2" :loading="markOkLoading" style="margin-left: 10px;">批改完成</a-button>
    </div>
    <BasicTable @register="registerTable">
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: 'ant-design:align-left-outlined',
              tooltip: t('common.viewText') + '详情',
              onClick: handleView.bind(null, record),
            },
            {
              icon: 'ant-design:check-outlined',
              tooltip: '正确',
              onClick: handleRight.bind(null, record),
            },
            {
              icon: 'ant-design:close-outlined',
              tooltip: '错误',
              onClick: handleWrong.bind(null, record),
            },
          ]"
        />
      </template>
    </BasicTable>
    <ScoreDetailModal @register="registerModal" width="60%"></ScoreDetailModal>
  </div>
</template>
<script>
import { useI18n } from '/@/hooks/web/useI18n';
import {defineComponent, onMounted, ref, unref} from 'vue';
import {Divider} from 'ant-design-vue';
import {useRoute} from 'vue-router';
import {PageWrapper} from '/@/components/Page';
import {useGo} from '/@/hooks/web/usePage';
import {getScoreDetail} from '/@/api/exam/score';
import {markAnswer, markOk} from '/@/api/exam/answer';
import {Description} from '/@/components/Description/index';
import {BasicTable, TableAction, useTable} from '/@/components/Table';
import {answerColumns, scoreDetailSchema} from './detail.data';
import {useMessage} from "/@/hooks/web/useMessage";
import ScoreDetailModal from './ScoreDetailModal.vue';
import {useModal} from '/@/components/Modal';

export default defineComponent({
  name: 'ScoreDetail',
  components: {
    PageWrapper,
    Description,
    BasicTable,
    TableAction,
    [Divider.name]: Divider,
    ScoreDetailModal
  },
  setup() {
    const { t } = useI18n();
    const [registerModal, {openModal}] = useModal();
    const route = useRoute();
    const go = useGo();
    const {createMessage} = useMessage();
    const recordId = ref(route.params?.id);
    const currentKey = ref('detail');
    const score = ref({});
    const answers = ref([]);
    const markOkLoading = ref(false);
    const [registerTable, {reload}] = useTable({
      title: '题目列表',
      columns: answerColumns,
      pagination: true,
      dataSource: answers,
      showIndexColumn: false,
      scroll: {y: 300},
      actionColumn: {
        width: 150,
        title: t('common.operationText'),
        dataIndex: 'action',
        slots: {customRender: 'action'},
        fixed: undefined,
      },
    });

    function goBack() {
      go('/exam/score');
    }

    async function fetch() {
      const recordResult = await getScoreDetail(unref(recordId));
      score.value = recordResult.record;
      answers.value = recordResult.record.answers;
    }

    function handleView(record) {
      openModal(true, {
        record,
        isUpdate: true,
      });
    }

    async function handleRight(record) {
      const {id, score, answer} = record;
      const result = await markAnswer({
        id,
        score,
        answer,
        answerType: 0
      });
      if (result) {
        createMessage.success('保存成功');
      } else {
        createMessage.warn('保存失败');
      }
      await fetch();
    }

    async function handleWrong(record) {
      const {id, score, answer} = record;
      const result = await markAnswer({
        id,
        score,
        answer,
        answerType: 1
      });
      if (result) {
        createMessage.success('保存成功');
      } else {
        createMessage.warn('保存失败');
      }
      await fetch();
    }

    async function handleMarkOk() {
      try {
        markOkLoading.value = true;
        const res = await markOk(recordId.value);
        if (res) {
          createMessage.success('保存成功');
        } else {
          createMessage.warn('保存失败');
        }
      } finally {
        markOkLoading.value = false;
      }
    }

    onMounted(() => {
      fetch();
    });
    return {
      t,
      registerModal,
      scoreDetailSchema,
      score,
      recordId,
      currentKey,
      markOkLoading,
      registerTable,
      goBack,
      handleView,
      handleRight,
      handleWrong,
      handleMarkOk
    };
  },
});
</script>

<style>
.score-detail {
  margin: 10px;
}
</style>
