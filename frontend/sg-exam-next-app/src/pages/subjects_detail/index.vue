<template>
  <AtMessage/>
  <view>
    <view class="subject-box" v-if="subject !== undefined">
      <view class="subject-content bg-white">
        <view class="subject-detail-type-label subject-list-item-label">
          <at-tag size="small" type="primary">{{ subject.typeLabel }}</at-tag>
          <view>
            <AtIcon class="subject-views" value='eye' size='8' color="#AAAAAA"></AtIcon>
            <text class="subject-views-text">{{subject.views}}</text>
          </view>
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
            <short-answer ref="shortAnswerRef" :subject="subject" :showInput="false"></short-answer>
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
            <text class="answer-text-title">答案：</text>
            <text class="answer-text-value">
              {{ subject.answer !== undefined ? subject.answer.answer : '' }}
            </text>
          </view>
          <view class="answer-text answer-text-analysis" v-if="subject.analysis !== undefined && subject.analysis !== null">
            <view>
              <text class="answer-text-title">解析：</text>
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
import {onMounted, ref} from 'vue';
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
        const res = await examApi.getSubjectDetail(subjectId.value, {findFav: true, isView: true});
        const {code, result} = res;
        if (code === 0) {
          subject.value = result;
          updateAnswerValue();
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
        await successMessage(text + '成功');
      }
    }

    async function handleNext(nextType: string = '0') {
      const res = await examApi.getNextSubjectByCategoryId(subject.value.categoryId, subject.value.id, nextType);
      if (res.code === 0) {
        if (res.result !== null) {
          subject.value = res.result;
          updateAnswerValue();
          updateSubjectRef();
        } else {
          await warnMessage('无更多数据');
        }
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
          ref.value.update(subject);
        }
      }
    }

    function updateAnswerValue() {
      if (subject.value.answer !== undefined && subject.value.answer !== null) {
        subject.value.answerValue = subject.value.answer.answer;
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
  position: absolute;
  bottom: 200px;
  right: 0;
  width: 38px;
  height: 38px;
  margin-right: 10px;
}
.fav-fab .fav-fab-fav::before {
  color: #FFC82C;
}
</style>