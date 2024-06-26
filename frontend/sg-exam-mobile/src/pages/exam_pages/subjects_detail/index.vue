<template>
   <div>
     <div class="subject-box" v-if="subject !== undefined">
       <div class="subject-content bg-white">
         <div class="subject-detail-type-label subject-list-item-label">
          <nut-tag size="small" type="success">{{ subject.typeLabel }}</nut-tag>
           <div class="align-items-center">
            <IconFont class="subject-views" font-class-name="iconfont" class-prefix="icon" name='eye' color='#AAAAAA'
                      size="18"></IconFont>
            <text class="subject-views-text">{{ subject.views }}</text>
           </div>
         </div>
         <div class="subject-title">
          <div class="subject-title-content" v-html="subject.subjectName"></div>
         </div>
         <div>
           <div v-if="subject.type === 0">
            <choice ref="choiceRef" :subject="subject" @update-selected="handleAnswered"></choice>
           </div>
           <div v-else-if="subject.type === 3" class="subject-options">
            <choice ref="multiChoiceRef" :subject="subject" :multi="true" @update-selected="handleAnswered"></choice>
           </div>
           <div v-else-if="subject.type === 1">
            <short-answer ref="shortAnswerRef" :subject="subject" :showInput="false"></short-answer>
           </div>
           <div v-else-if="subject.type === 2">
            <judgement ref="judgementRef" :subject="subject" @update-selected="handleAnswered"></judgement>
           </div>
           <div v-else-if="subject.type === 4" class="audio-btn-group">
           </div>
           <div v-else-if="subject.type === 5">
            <subject-video ref="videoRef" :subject="subject"></subject-video>
           </div>
           <div
              v-if="showAnswerAndAnalysis && subject.answer !== undefined && subject.answer !== null && subject.answer !== ''">
            <text class="answer-text-title">答案：</text>
             <div v-if="subject.type === 2">
              <text>{{ subject.answer.answer === '0' ? '正确' : '错误' }}</text>
             </div>
             <div v-else>
              <div class="answer-text-value" v-html="subject.answer.answer"></div>
             </div>
           </div>
           <div
              v-if="showAnswerAndAnalysis && subject.analysis !== undefined && subject.analysis !== null && subject.analysis !== ''">
             <div>
              <text class="answer-text-title">解析：</text>
             </div>
             <div>
              <div class="answer-text-value" v-html="subject.analysis"></div>
             </div>
           </div>
         </div>
       </div>
     </div>
     <div class="submit-btn-previous">
     </div>
     <div class="submit-btn">
       <div class="submit-btn-item" v-if="mode !== '3'">
        <nut-button type="primary" :circle="true" :loading="loadingPrevious" @click="handleNext('1')">上一题
        </nut-button>
       </div>
       <div class="submit-btn-item">
        <nut-button type="primary" :circle="true" :loading="loadingNext" @click="handleNext('0')">下一题</nut-button>
       </div>
     </div>
     <div class="fav-fab" v-if="subject !== undefined" @click="handleFavSubject(subject)">
      <nut-button class="fav-fab-btn" size="large" :circle="true" :color="subject.favorite ? '#FFC82C' : '#6190e8ab'">
        <template #icon>
          <Star size="22px"/>
        </template>
      </nut-button>
     </div>
   </div>
  <nut-notify type="success" duration="500" v-model:visible="showNotify" :msg="notifyMsg"/>
</template>

<script lang="ts">
import {IconFont} from '@nutui/icons-vue-taro';
import Taro from "@tarojs/taro";
import {onMounted, ref} from 'vue';
import examApi from '../../../api/exam.api';
import {Choice} from '../../../components/subject/choice/index';
import {Judgement} from '../../../components/subject/judgement/index';
import {ShortAnswer} from '../../../components/subject/shortAnswer/index';
import api from '../../../api/api';
import {showNoMoreData, showLoading, hideLoading} from '../../../utils/util';
import {StarFill, Star} from '@nutui/icons-vue-taro';

export default {
  components: {
    IconFont,
    StarFill,
    Star,
    'choice': Choice,
    'judgement': Judgement,
    'short-answer': ShortAnswer
  },
  setup() {
    const params = Taro.getCurrentInstance().router?.params;
    const subjectId = ref<any>(undefined);
    const subject = ref<any>(undefined);
    const choiceRef = ref<any>(undefined);
    const multiChoiceRef = ref<any>(undefined);
    const judgementRef = ref<any>(undefined);
    const shortAnswerRef = ref<any>(undefined);
    const videoRef = ref<any>(undefined);
    const loadingNext = ref<boolean>(false);
    const loadingPrevious = ref<boolean>(false);
    // 1：预览模式，2：顺序刷题，3：随机刷题
    const mode = ref<string>('1');
    const showAnswerAndAnalysis = ref<boolean>(false);
    const realTimeIds = [];
    if (params) {
      subjectId.value = params.id;
      mode.value = params.mode + '';
      showAnswerAndAnalysis.value = params.mode === '1';
    }
    const showNotify = ref<boolean>(false);
    const notifyMsg = ref<string>('');

    async function fetch() {
      await showLoading();
      try {
        const res = await examApi.getSubjectDetail(subjectId.value, {findFav: true, isView: true});
        const {code, result} = res;
        if (code === 0) {
          subject.value = result;
          updateAnswerValue();
        }
      } finally {
        hideLoading();
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
        notifyMsg.value = text + '成功';
        showNotify.value = true;
      }
    }

    async function handleNext(nextType: string = '0') {
      const loadingRef = nextType === '0' ? loadingNext : loadingPrevious;
      // 不是预览模式，隐藏答案和分析
      if (!isPreviewMode()) {
        hideAnswerAndAnalysis();
      }
      try {
        loadingRef.value = true;
        const random = isRandomMode();
        const res = await examApi.getNextSubjectByCategoryId(subject.value.categoryId, subject.value.id, nextType, random, realTimeIds);
        if (res.code === 0) {
          if (res.result !== null) {
            subject.value = res.result;
            addRealTimeIds(res.result.sort);
            updateAnswerValue();
            updateSubjectRef();
          } else {
            await showNoMoreData();
          }
        }
      } finally {
        loadingRef.value = false;
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
        if (ref != undefined && ref.value !== undefined) {
          ref.value.update(subject);
        }
      }
    }

    function isPreviewMode() {
      return mode.value === '1';
    }

    function isRandomMode() {
      return mode.value === '3';
    }

    function updateAnswerValue() {
      if (isPreviewMode() && subject.value.answer !== undefined && subject.value.answer !== null) {
        subject.value.answerValue = subject.value.answer.answer;
      }
    }

    async function handleAnswered(s) {
      showAnswerAndAnalysis.value = true;
      // 单选题自动下一题
      if (subject.value.type === 0 && subject.value.answer.answer === s.value.answerValue) {
        await handleNext('0');
      }
    }

    function hideAnswerAndAnalysis() {
      showAnswerAndAnalysis.value = false;
    }

    function addRealTimeIds(id) {
      if (realTimeIds.length < 200) {
        realTimeIds.push(id);
      }
    }

    onMounted(() => {
      init();
    });

    return {
      mode,
      showAnswerAndAnalysis,
      subject,
      choiceRef,
      multiChoiceRef,
      judgementRef,
      shortAnswerRef,
      videoRef,
      loadingPrevious,
      loadingNext,
      showNotify,
      notifyMsg,
      init,
      handleFavSubject,
      handleNext,
      handleAnswered
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
  position: fixed;
  bottom: 500px;
  right: 50px;
}

.fav-fab-btn {
  padding: 26px;
}
</style>