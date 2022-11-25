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
    <BasicTable @register="registerTable">
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: 'ant-design:align-left-outlined',
              tooltip: '查看详情',
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
import {defineComponent, onMounted, ref, unref} from 'vue';
import {Divider} from 'ant-design-vue';
import {useRoute} from 'vue-router';
import {PageWrapper} from '/@/components/Page';
import {useGo} from '/@/hooks/web/usePage';
import {getScoreDetail} from '/@/api/exam/score';
import {markAnswer} from '/@/api/exam/answer';
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
    const [registerModal, {openModal}] = useModal();
    const route = useRoute();
    const go = useGo();
    const {createMessage} = useMessage();
    const recordId = ref(route.params?.id);
    const currentKey = ref('detail');
    const score = ref({});
    const answers = ref([]);
    const [registerTable, {reload}] = useTable({
      title: '题目列表',
      columns: answerColumns,
      pagination: true,
      dataSource: answers,
      showIndexColumn: false,
      scroll: {y: 300},
      actionColumn: {
        width: 150,
        title: '操作',
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

    onMounted(() => {
      fetch();
    });
    return {
      registerModal,
      scoreDetailSchema, score, recordId, currentKey, goBack, registerTable,
      handleView, handleRight, handleWrong
    };
  },
});
</script>

<style>
.score-detail {
  margin: 10px;
}
</style>
