<template>
  <AtMessage/>
  <view>
    <view class="subject-box" v-if="subject !== undefined">
      <view class="subject-content bg-white">
        <view class="subject-detail-type-label subject-list-item-label">
          <at-tag size="small" type="primary">{{ subject.typeLabel }}</at-tag>
          <AtIcon value='star-2' size='8' :color="subject.favorite === true ? '#FFC82C': '#AAAAAA'" @click="handleFavSubject(subject)"></AtIcon>
        </view>
        <view class="subject-title">
          <wxparse class="subject-title-content" :html="subject.subjectName" key={Math.random()}></wxparse>
        </view>
        <view>
          <view v-if="subject.type === 0">
            <choice ref="choiceRef" :subject="subject"></choice>
          </view>
          <view v-else-if="subject.type === 3" class="subject-options">
            <choice ref="multiChoiceRef" :subject="subject" :multi="true"></choice>
          </view>
          <view v-else-if="subject.type === 1">
            <short-answer ref="shortAnswerRef" :subject="subject"></short-answer>
          </view>
          <view v-else-if="subject.type === 2">
            <judgement ref="judgementRef" :subject="subject"></judgement>
          </view>
          <view v-else-if="subject.type === 4" class="audio-btn-group">
          </view>
          <view v-else-if="subject.type === 5">
            <subject-video ref="videoRef" :subject="subject"></subject-video>
          </view>
          <view class="answer-text">
            <text>参考答案：</text>
            <text class="answer-text-value">
              {{ subject.answer !== undefined ? subject.answer.answer : '' }}
            </text>
          </view>
          <view class="answer-text answer-text-analysis">
            <view>
              <text>解析：</text>
            </view>
            <view>
              <wxparse class="answer-text-value" :html="subject.analysis" key={Math.random()} />
            </view>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script lang="ts">
import Taro from "@tarojs/taro";
import {onMounted, ref} from 'vue';
import examApi from '../../api/exam.api';
import {Choice} from '../../components/subject/choice/index';
import {Judgement} from '../../components/subject/judgement/index';
import {ShortAnswer} from '../../components/subject/shortAnswer/index';
import {SubjectVideo} from '../../components/subject/video/index';
import api from "../../api/api";
import {successMessage} from "../../utils/util";

export default {
  components: {
    'choice': Choice,
    'judgement': Judgement,
    'short-answer': ShortAnswer,
    'subject-video': SubjectVideo
  },
  setup() {
    const params = Taro.getCurrentInstance().router.params;
    const subjectId = params.id;
    const subject = ref<object>(undefined);

    async function fetch() {
      await Taro.showLoading();
      try {
        const res = await examApi.getSubjectDetail(subjectId, true);
        const {code, result} = res;
        if (code === 0) {
          subject.value = result;
        }
      } finally {
        Taro.hideLoading();
      }
    }

    async function init() {
      await fetch();
    }

    async function handleFavSubject(item) {
      item.favorite = !item.favorite;
      const type = item.favorite ? '1' : '0';
      const text = item.favorite ? '收藏' : '取消收藏';
      const {id} = await api.getUserInfo();
      const res = await examApi.favoriteSubject(id, item.id, type);
      const {code} = res;
      if (code === 0) {
        successMessage(text + '成功');
      }
    }

    onMounted(() => {
      init();
    });

    return {
      subject,
      init,
      handleFavSubject
    }
  },
  onPullDownRefresh() {
    try {
      this.init();
    } finally {
      Taro.stopPullDownRefresh();
    }
  }
}
</script>

<style>
.subject-detail-type-label {
  display: flex;
  justify-content: space-between;
}
</style>