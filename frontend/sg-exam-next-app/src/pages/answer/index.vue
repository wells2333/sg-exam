<template>
  <view class="answer-box">
    <view class="overview-container">
      <view class="answer-exam-name">
        <text>{{ detail.examinationName }}</text>
      </view>
      <view class="flex-row">
        <view class="overview-item">
          <text>成绩</text>
          <view class="overview-item-detail">{{ score }}</view>
        </view>
        <view class="overview-item">
          <text>正确率</text>
          <view class="overview-item-detail"> {{ rate }}</view>
        </view>
        <view class="overview-item">
          <text>耗时</text>
          <view class="overview-item-detail">{{ detail.duration }}</view>
        </view>
      </view>

    </view>
    <view class="answer-box-item">
      <view class="answer-subject-content">
        <at-divider content="答题列表"/>
        <view v-for="(item, index) in detail.answers">
          <view class="subject-title">
              <text class="subject-title-content">{{ index + 1 }}. &nbsp;</text>
              <wxparse class="subject-title-content" :html="item.subject.subjectName" key={Math.random()} />
          </view>
          <view>
            <view>
              <view v-if="item.subject.type === 0 || item.subject.type === 3">
                <choice :subject="item.subject" :answer="item.answer" :standAnswer="item.subject.answer.answer"
                        :disabled="true"></choice>
              </view>
              <view v-else-if="item.subject.type === 1">
                <short-answer :subject="item.subject" :answer="item.answer" :standAnswer="item.subject.answer.answer"
                              :disabled="true"></short-answer>
              </view>
              <view v-else-if="item.subject.type === 2">
                <judgement :subject="item.subject" :answer="item.answer" :standAnswer="item.subject.answer.answer" :disabled="true"></judgement>
              </view>
              <view v-else-if="item.subject.type === 4">

              </view>
              <view v-else-if="item.subject.type === 5">
                <subject-video :subject="item.subject" :answer="item.answer" :standAnswer="item.subject.answer.answer"></subject-video>
              </view>
            </view>
          </view>
          <view class="answer-text" v-if="item.subject.answer.answer !== undefined && item.subject.answer.answer !== null">
            <text class="answer-text-title">答案：</text>
            <text class="answer-text-value">
              {{ item.subject.answer.answer }}
            </text>
          </view>
          <view class="answer-text answer-text-analysis" v-if="item.subject.analysis !== undefined && item.subject.analysis !== null">
            <view>
              <text class="answer-text-title">解析：</text>
            </view>
            <view>
              <wxparse class="answer-text-value" :html="item.subject.analysis" key={Math.random()} />
            </view>
          </view>
        </view>
        <view class="answer-try-again-btn">
          <at-button type="primary" :circle="true" @click="handleTryAgain">再试一次</at-button>
        </view>
      </view>
    </view>
  </view>
</template>

<script lang="ts">
import {onMounted, ref} from 'vue';
import recordApi from '../../api/record.api';
import Taro from "@tarojs/taro";
import {Choice} from '../../components/subject/choice/index';
import {Judgement} from '../../components/subject/judgement/index';
import {ShortAnswer} from '../../components/subject/shortAnswer/index';
import {SubjectVideo} from '../../components/subject/video/index';
import {showLoading, hideLoading} from '../../utils/util';

export default {
  components: {
    'choice': Choice,
    'judgement': Judgement,
    'short-answer': ShortAnswer,
    'subject-video': SubjectVideo
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

    async function fetch() {
      try {
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
      }
    }

    function handleTryAgain() {
      Taro.navigateTo({url: "/pages/exam_detail/index?id=" + examinationId.value})
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
    return {detail, score, rate, handleTryAgain, getOptionColor}
  }
}
</script>

<style>
page {
  background-color: rgba(242, 244, 248, 1);
}

.at-divider {
  height: 16px;
}

.at-divider__content {
  color: #555555;
}
</style>