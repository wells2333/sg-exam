<template>
  <AtMessage/>
  <view>
    <view class="subject-box" v-if="subject !== undefined">
      <view class="subject-content bg-white">
        <view class="subject-detail-type-label subject-list-item-label">
          <at-tag size="small" type="primary">{{ subject.typeLabel }}</at-tag>
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

    <view class="submit-btn">
      <view class="submit-btn-item">
        <AtButton type="primary" :circle="true" @click="handleNext('1')">上一题</AtButton>
      </view>
      <view class="submit-btn-item">
        <AtButton type="primary" :circle="true" @click="handleNext('0')">下一题</AtButton>
      </view>
    </view>
    <AtFab class="fav-fab" v-if="subject !== undefined" @click="handleFavSubject(subject)">
      <text class="at-fab__icon at-icon at-icon-star-2" :class="subject.favorite ? 'fav-fab-fav' : ''"></text>
    </AtFab>
  </view>
</template>

<script lang="ts">
import Taro from "@tarojs/taro";
import {onMounted, ref, unref} from 'vue';
import examApi from '../../api/exam.api';
import {Choice} from '../../components/subject/choice/index';
import {Judgement} from '../../components/subject/judgement/index';
import {ShortAnswer} from '../../components/subject/shortAnswer/index';
import {SubjectVideo} from '../../components/subject/video/index';
import api from "../../api/api";
import {successMessage, warnMessage} from "../../utils/util";

export default {
  components: {
    'choice': Choice,
    'judgement': Judgement,
    'short-answer': ShortAnswer,
    'subject-video': SubjectVideo
  },
  setup() {
    const params = Taro.getCurrentInstance().router.params;
    const subjectId = ref<string>(params.id);
    const subject = ref<object>(undefined);
    const choiceRef = ref<any>(undefined);
    const multiChoiceRef = ref<any>(undefined);
    const judgementRef = ref<any>(undefined);
    const shortAnswerRef = ref<any>(undefined);
    const videoRef = ref<any>(undefined);

    async function fetch() {
      await Taro.showLoading();
      try {
        const res = await examApi.getSubjectDetail(subjectId.value, true);
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

    async function handleNext(nextType: string = '0') {
      await Taro.showLoading();
      try {
        const res = await examApi.getNextSubjectByCategoryId(subject.value.categoryId, subject.value.id, nextType);
        if (res.code === 0) {
          if (res.result !== null) {
            subject.value = res.result;
            updateSubjectRef();
          } else {
            warnMessage('无更多数据');
          }
        }
      } finally {
        Taro.hideLoading();
      }
    }

    function updateSubjectRef() {
      if (subject !== undefined) {
        const type = subject.value.type;
        let ref = undefined;
        if (type === 0) {
          ref = choiceRef;
        } else if (type === 1) {
          ref = shortAnswerRef;
        } else if (type === 2) {
          ref = judgementRef;
        } else if (type === 3) {
          ref = multiChoiceRef;
        } else if (type === 5) {
          ref = videoRef;
        }
        if (ref != undefined) {
          setTimeout(() => {
            ref.value.update(subject);
          }, 50);
        }
      }
    }

    onMounted(() => {
      init();
    });

    return {
      subject,
      choiceRef,
      multiChoiceRef,
      judgementRef,
      shortAnswerRef,
      videoRef,
      init,
      handleFavSubject,
      handleNext
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
.fav-fab {
  float: right;
  margin-right: 10px;
}
.fav-fab .fav-fab-fav::before {
  color: #FFC82C;
}
</style>