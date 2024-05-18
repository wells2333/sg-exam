<template>
   <div class="answer-box" v-if="!loading">
     <div class="overview-container">
       <div class="overview-container-btn">
        <nut-button shape="round" type="success" size="large">
          <template #icon>
            <IconFont name="Check" size="40px"></IconFont>
          </template>
        </nut-button>
       </div>
       <div class="answer-exam-name">
        <text>提交成功</text>
       </div>
       <div class="overview-item">
        <text>你的得分：</text>
        <text class="overview-item-detail">{{ score }}</text>
       </div>
     </div>
     <div class="answer-box-item">
       <div class="answer-subject-content">
        <nut-divider>答题列表</nut-divider>
         <div v-for="(item, index) in detail.answers">
           <div class="subject-title">
              <text class="subject-title-content">{{ index + 1 }}. &nbsp;</text>
              <div class="subject-title-content" v-html="item.subject.subjectName"></div>
           </div>
           <div>
             <div>
               <div v-if="item.subject.type === 0 || item.subject.type === 3">
                <choice :subject="item.subject" :answer="item.answer" :standAnswer="item.subject.answer.answer"
                        :disabled="true"></choice>
               </div>
               <div v-else-if="item.subject.type === 1">
                <short-answer :subject="item.subject" :answer="item.answer" :standAnswer="item.subject.answer.answer"
                              :disabled="true"></short-answer>
               </div>
               <div v-else-if="item.subject.type === 2">
                <judgement :subject="item.subject" :answer="item.answer" :standAnswer="item.subject.answer.answer" :disabled="true"></judgement>
               </div>
             </div>
           </div>
           <div class="answer-text" v-if="item.subject.answer.answer !== undefined && item.subject.answer.answer !== null">
            <text class="answer-text-title">答案：</text>
            <text class="answer-text-value">
              {{ item.subject.answer.answer }}
            </text>
           </div>
           <div class="answer-text answer-text-analysis" v-if="item.subject.analysis !== undefined && item.subject.analysis !== null">
             <div>
              <text class="answer-text-title">解析：</text>
             </div>
             <div>
              <div class="answer-text-value" v-html="item.subject.analysis"></div>
             </div>
           </div>
         </div>
         <div class="answer-try-again-btn">
          <nut-button type="primary" @click="handleTryAgain">再试一次</nut-button>
         </div>
       </div>
     </div>
   </div>
</template>

<script lang="ts">
import {onMounted, ref} from 'vue';
import {IconFont} from '@nutui/icons-vue-taro';
import recordApi from '../../../api/record.api';
import Taro from '@tarojs/taro';
import {Choice} from '../../../components/subject/choice/index';
import {Judgement} from '../../../components/subject/judgement/index';
import {ShortAnswer} from '../../../components/subject/shortAnswer/index';
import {showLoading, hideLoading} from '../../../utils/util';

export default {
  components: {
    IconFont,
    'choice': Choice,
    'judgement': Judgement,
    'short-answer': ShortAnswer
  },
  setup() {
    const currentInstance = Taro.getCurrentInstance();
    let detail = ref<any>({
      duration: '-'
    });
    const recordId = ref<string>('');
    const examinationId = ref<string>('');
    const score = ref<string>('-');
    const rate = ref<string>('-');
    const loading = ref<boolean>(true);

    async function fetch() {
      try {
        loading.value = true;
        await showLoading();
        const params = currentInstance.router.params;
        recordId.value = params.recordId;
        examinationId.value = params.examinationId;
        const detailResult = await recordApi.recordDetails(recordId.value);
        const {code, result} = detailResult
        if (code === 0) {
          const {record} = result;
          detail.value = record;
          // 统计完成
          if (record.submitStatus === 3) {
            rate.value = Math.round(record.correctNumber / (record.correctNumber + record.inCorrectNumber) * 10000) / 100 + '%';
            score.value = record.score;
          }
        }
      } finally {
        hideLoading();
        loading.value = false;
      }
    }

    function handleTryAgain() {
      Taro.navigateTo({url: "/pages/exam_pages/exam_detail/index?id=" + examinationId.value})
    }

    function getOptionColor(answer, subject) {
      if (answer === subject.answer.answer) {
        return '#07c160';
      }
      return '#ff7618';
    }

    onMounted(() => {
      fetch();
    });
    return {
      loading,
      detail,
      score,
      rate,
      handleTryAgain
    }
  }
}
</script>

<style>
page {
  background-color: rgba(242, 244, 248, 1);
}

.overview-container-btn {
  width: 100px;
  margin: 30px auto 10px;
}
</style>