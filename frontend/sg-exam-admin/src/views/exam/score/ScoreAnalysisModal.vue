<template>
  <div>
    <BasicModal v-bind="$attrs" @register="registerModal" title="成绩分析" @ok="handleSubmit"
                width="90%" :defaultFullscreen="true">
      <div style="margin: 16px;">
        <div class="md:flex enter-y">
          <div class="p-2 px-4 flex">
            <span>考试名称：{{analysisResult.examinationName}}</span>
          </div>
        </div>
        <div class="md:flex enter-y">
          <div class="p-2 px-4 flex">
            <span>考试人数：</span>
            <CountTo :startVal="0" :endVal="analysisResult.userCnt" class="text-l" />
          </div>
          <div class="p-2 px-4 flex">
            <span>平均分：</span>
            <CountTo :startVal="0" :endVal="analysisResult.avgScore" class="text-l" />
          </div>
          <div class="p-2 px-4 flex">
            <span>最高分：</span>
            <CountTo :startVal="0" :endVal="analysisResult.highestScore" class="text-l" />
          </div>
          <div class="p-2 px-4 flex">
            <span>最低分：</span>
            <CountTo :startVal="0" :endVal="analysisResult.lowestScore" class="text-l" />
          </div>
        </div>
        <div class="md:flex enter-y">
          <div class="md:w-1/3 w-full" style="margin-right: 20px;">
            <BasicTable @register="registerTable">
            </BasicTable>
          </div>
          <div class="w-full">
            <div class="md:flex enter-y" style="margin-top: 30px;" v-if="analysisResult.userCnt">
              <Distribution :value="analysisResult" />
            </div>
            <div class="md:flex enter-y" style="margin-top: 30px;" v-if="analysisResult.userCnt">
              <DistributionPercent class="md:w-1/3 !md:mx-4 !md:my-0 !my-4 w-full" :value="analysisResult" />
              <!-- <VisitRadar class="md:w-1/3 w-full"/>
              <SalesProductPie class="md:w-1/3 w-full"/> -->
            </div>
          </div>
        </div>
      </div>
    </BasicModal>
  </div>
</template>
<script lang="ts">
import {useI18n} from '/@/hooks/web/useI18n';
import {defineComponent, computed, ref} from 'vue';
import {BasicTable, TableAction, useTable} from '/@/components/Table';
import {BasicModal, useModalInner} from '/@/components/Modal';
import {BasicForm} from '/@/components/Form/index';
import { CountTo } from '/@/components/CountTo/index';
import VisitRadar from './analysis/VisitRadar.vue';
import DistributionPercent from './analysis/DistributionPercent.vue';
import SalesProductPie from './analysis/SalesProductPie.vue';
import Distribution from './analysis/Distribution.vue';
import {getScoreAnalysis} from "/@/api/exam/examination";
import {rankColumns} from './analysis/rank.data';

export default defineComponent({
  name: 'ScoreAnalysisModal',
  components: {BasicTable, TableAction, BasicModal, BasicForm, CountTo, VisitRadar, DistributionPercent, SalesProductPie, Distribution},
  emits: ['success', 'register'],
  setup(_) {
    const {t} = useI18n();
    const examinationId = ref<any>(undefined);
    const analysisResult = ref<any>({
      userCnt: 0,
      avgScore: 0,
      highestScore: 0,
      lowestScore: 0
    });
    const [registerTable, {reload}] = useTable({
      title: t('common.modules.exam.examination') + t('common.list'),
      api: async (arg) => {
        if (examinationId.value === undefined) {
          return undefined;
        }
        const res = await getScoreAnalysis(examinationId.value);
        if (res) {
          analysisResult.value = res;
          return res.scoreItems;
        }
        return [];
      },
      columns: rankColumns,
      formConfig: {
        labelWidth: 50
      },
      pagination: false,
      striped: false,
      useSearchForm: false,
      showTableSetting: false,
      bordered: true,
      showIndexColumn: false,
      canResize: false,
    });
    const [registerModal, {setModalProps, closeModal}] = useModalInner(async (data) => {
      examinationId.value = data?.id || undefined;
      await reload();
      setModalProps({confirmLoading: false});
    });

    const getTitle = computed(() => t('common.viewText'));

    function handleSubmit() {
      closeModal();
    }

    return {t, registerModal, registerTable, handleSubmit, getTitle, analysisResult};
  },
});
</script>
