<template>
  <AtMessage/>
  <view class="fixed-top">
    <view class="subject-exam-title bg-white">
      <view style="display: flex; justify-content: space-between">
        <text>{{ subject.sort }}/{{ subjectTotalCount }}</text>
        <at-tag type="primary" class="duration-info" :circle="true">{{ duration }}</at-tag>
      </view>
      <view class="subject-exam-progress">
        <at-progress :percent="percentage" size="small" strokeWidth="8"/>
      </view>
    </view>
  </view>
  <view class="fixed-top-next subject-box">
    <view class="subject-content bg-white">
      <view class="subject-type-label">
        <at-tag size="small" type="primary">{{ subject.typeLabel }}</at-tag>
        <at-icon v-if="examination.type !== 0" @click="handleFavSubject(subject)" value="star-2" size='10'
                 :color="subject.favorite === true ? '#FFC82C': '#AAAAAA'"></at-icon>
      </view>
      <view class="subject-title">
        <text class="subject-title-content">{{ subject.sort }}.&nbsp;</text>
        <wxparse class="subject-title-content" :html="subject.subjectName" key={Math.random()}></wxparse>
      </view>
      <view>
        <view v-if="subject.type === 0">
          <choice ref="choiceRef" :subject="subject" @update-selected="handleChoiceSelectedChange"></choice>
        </view>
        <view v-else-if="subject.type === 3" class="subject-options">
          <choice ref="multiChoiceRef" :subject="subject" :multi="true"
                  @update-selected="handleChoiceSelectedChange"></choice>
        </view>
        <view v-else-if="subject.type === 1">
          <short-answer ref="shortAnswerRef" :subject="subject"
                        @update-selected="handleChoiceSelectedChange"></short-answer>
        </view>
        <view v-else-if="subject.type === 2">
          <judgement ref="judgementRef" :subject="subject"
                     @update-selected="handleChoiceSelectedChange"></judgement>
        </view>
        <view v-else-if="subject.type === 4" class="audio-btn-group">
          <view class="audio-btn">
            <at-button type="primary" :icon="recordBtnIcon" size="small" @click="handleClickRecordAudio">
              {{ recordBtnText }}
            </at-button>
          </view>
          <view class="audio-btn">
            <at-button type="primary" :icon="recordCompleteBtnIcon" size="small" @click="handleRecordAudioComplete">
              {{ recordCompleteBtnText }}
            </at-button>
          </view>
          <view class="audio-btn">
            <at-button type="primary" icon="checked" size="small" @click="handleUploadAudio">上传</at-button>
          </view>
        </view>
        <view v-else-if="subject.type === 5">
          <subject-video ref="videoRef" :subject="subject"
                         @update-selected="handleChoiceSelectedChange"></subject-video>
        </view>
      </view>
    </view>

    <view class="submit-btn">
      <view class="submit-btn-item">
        <AtButton type="primary" :circle="true" @click="handleSaveAndNext('1')" :loading="preLoading">上一题</AtButton>
      </view>
      <view class="submit-btn-item">
        <AtButton type="primary" :circle="true" @click="handleSaveAndNext('0')" :loading="nextLoading">下一题</AtButton>
      </view>
      <view class="submit-btn-item">
        <AtButton type="primary" :circle="true" @click="handleOpenCards">答题卡</AtButton>
      </view>
      <view class="submit-btn-item" v-if="!hasMore">
        <AtButton type="primary" :circle="true" @click="handleSubmit('0')" :loading="submitLoading">提交</AtButton>
      </view>
    </view>
  </view>

  <at-modal :isOpened="isOpenedCardModal" @click="handleCloseCards" @cancel="handleCloseCards">
    <at-modal-header>答题卡</at-modal-header>
    <at-modal-content>
      <view class="card-box">
        <view class="card-item" v-for="card in cards">
          <at-button class="card-item-btn" :type="getCardItemType(card)" :circle="true"
                     @click="handleClickCardSubject(card.sort)">
            {{ card.sort }}
          </at-button>
        </view>
      </view>
    </at-modal-content>
    <at-modal-action>
      <button @click="handleCloseCards">关闭</button>
    </at-modal-action>
  </at-modal>

  <at-modal :isOpened="isOpenedSubmitModal" title="确定提交吗？" cancelText="取消"
            confirmText="确认"
            @close="handleCloseSubmitModal"
            @cancel="handleCloseSubmitModal"
            @confirm="handleConfirmSubmitModal"></at-modal>
</template>
<script lang="ts">
import {onMounted, ref, unref} from 'vue';
import Taro from "@tarojs/taro";
import examApi from '../../../api/exam.api';
import api from '../../../api/api';
import {initRecorderManager, playAudio, pleaseStartRecord, startRecordAudio, stopRecordAudio} from "./audio";
import {Choice} from '../../../components/subject/choice/index';
import {Judgement} from '../../../components/subject/judgement/index';
import {ShortAnswer} from '../../../components/subject/shortAnswer/index';
import {SubjectVideo} from '../../../components/subject/video/index';
import {getDuration, parseRes, successMessage, warnMessage, showLoading, hideLoading, showNoMoreData} from '../../../utils/util';

export default {
  components: {
    'choice': Choice,
    'judgement': Judgement,
    'short-answer': ShortAnswer,
    'subject-video': SubjectVideo
  },
  setup() {
    const choiceRef = ref<any>(undefined);
    const multiChoiceRef = ref<any>(undefined);
    const judgementRef = ref<any>(undefined);
    const shortAnswerRef = ref<any>(undefined);
    const videoRef = ref<any>(undefined);
    const currentInstance = Taro.getCurrentInstance();
    const radioArr = ref<any>([]);
    const visibleSubmitDialog = ref(false);
    const recordId = ref<string>("");
    const examinationId = ref<string>("");
    const percentage = ref<number>(0);
    const examination = ref<any>(api.getExamination());
    const subject = ref<any>(api.getSubject());
    const subjectTotalCount = ref<number>(0);
    const duration = ref<string>("");
    const preLoading = ref<boolean>(false);
    const nextLoading = ref<boolean>(false);
    const submitLoading = ref<boolean>(false);
    const recorderManagerRef = ref<any>(undefined);
    // 开始录音，0：待开始，1：正在录音，2：暂停
    const recordFlag = ref<number>(0);
    const recordBtnText = ref<string>("开始录音");
    const recordBtnIcon = ref<string>("play-circle");
    const recordRes = ref<any>(undefined);

    const recordCompleteBtnText = ref<string>('完成');
    const recordCompleteBtnIcon = ref<string>('stop-circle');

    const hasMore = ref<boolean>(true);

    const isOpenedCardModal = ref<boolean>(false);
    const cards = ref<object>(api.getCards());

    const isOpenedSubmitModal = ref<boolean>(false);

    api.setAnsweredCount(0);

    async function fetch() {
      await showLoading();
      try {
        handleFirstSubject();
        const params = currentInstance.router?.params;
        if (params !== undefined) {
          recordId.value = params.recordId;
          examinationId.value = params.examinationId;
          if (params.total && params.total > 0) {
            api.setSubjectTotalCount(params.total);
            subjectTotalCount.value = params.total;
          }
        }
        updateSubjectRef();
      } finally {
        hideLoading();
      }
    }

    function handleFirstSubject() {
      // 语音题目则直接播放语音
      const obj = unref(subject);
      if (obj.type === 4) {
        if (recorderManagerRef.value === undefined) {
          recorderManagerRef.value = initRecorderManager(recordFlag, recordBtnText, recordBtnIcon, recordRes, recordCompleteBtnText, recordCompleteBtnIcon);
        } else {
          stopRecordAudio(recorderManagerRef);
        }
        const {speechUrl} = obj;
        if (speechUrl) {
          //playAudio(speechUrl);
        }
      }
    }

    function handleClickRecordAudio() {
      const recorderManager = unref(recorderManagerRef);
      startRecordAudio(recorderManager, recordFlag, recordCompleteBtnText, recordCompleteBtnIcon);
    }

    // 停止录音
    function handleRecordAudioComplete() {
      const text = unref(recordCompleteBtnText);
      if (text === '完成') {
        stopRecordAudio(recorderManagerRef);
      } else if (text === '播放') {
        handlePlayAudio();
      }
    }

    function handlePlayAudio() {
      const res = unref(recordRes);
      if (res && res.tempFilePath) {
        playAudio(res.tempFilePath);
      }
    }

    function handleUploadAudio() {
      stopRecordAudio(recorderManagerRef);
      const res = unref(recordRes);
      if (res && res.tempFilePath) {
        //uploadAudioFile(res.tempFilePath);
        successMessage('上传成功');
      } else {
        pleaseStartRecord();
      }
    }

    function handleStart(item) {
      console.log(radioArr.value, 'radioArr')
    }

    function handleChoiceSelectedChange(item, selected) {
      refreshProgress();
    }

    function handleShortAnswerChange($event, item) {
      const {detail} = $event;
      const itemObj = unref(item);
      itemObj.answer = detail;
      itemObj.checked = true;
      refreshProgress();
    }

    function refreshProgress() {
      let count = api.getAnsweredCount();
      let total = api.getSubjectTotalCount();
      let counted = subject.value.counted;
      if (counted === undefined && subject.value.checked && count < total) {
        subject.value.counted = true;
        count++;
        api.setAnsweredCount(count)
        updateCheckedCard(subject);
      }
      if (total > 0) {
        percentage.value = Math.round(count / total * 100);
      }
    }

    async function refreshDuration() {
      const record = api.getExamRecord();
      if (record && record.startTime) {
        duration.value = getDuration(new Date(), new Date(record.startTime.replace(/-/g, '/')));
      }
    }

    async function handleSaveAndNext(type: string = '0', nextSubjectSortNo: string = '') {
      // 停止语音录制
      const obj = unref(subject);
      if (obj.type === 4) {
        stopRecordAudio(recorderManagerRef);
      }
      let loading = type === '1' ? preLoading : nextLoading;
      loading.value = true;
      try {
        const {userId} = api.getUserInfo();
        const res = await examApi.saveAndNext({
          userId: userId,
          examRecordId: recordId.value,
          subjectId: subject.value.id,
          answer: subject.value.answerValue
        }, type, nextSubjectSortNo);
        const result = parseRes(res);
        if (result) {
          subject.value = result;
          subject.value.answerValue = result.answer.answer;
          hasMore.value = result.hasMore;
          updateSubjectRef();
        } else {
          hasMore.value = false;
          await showNoMoreData();
        }
      } finally {
        loading.value = false;
      }
    }

    function updateSubjectRef() {
      if (subject !== undefined) {
        const type = unref(subject).type;
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
        setTimeout(() => {
          if (ref != undefined && ref.value !== undefined) {
            ref.value.update(subject);
          }
        }, 50);
      }
    }

    async function handleSubmit(type: string = '0') {
      const {userId} = api.getUserInfo();
      await examApi.saveAndNext({
        userId: userId,
        examRecordId: recordId.value,
        subjectId: subject.value.id,
        answer: subject.value.answerValue
      }, type);
      isOpenedSubmitModal.value = true;
    }

    function onCancelSubmit() {

    }

    function handleOpenCards() {
      isOpenedCardModal.value = true;
    }

    function handleCloseCards() {
      isOpenedCardModal.value = false;
    }

    function handleClickCardSubject(sort) {
      handleCloseCards();
      handleSaveAndNext('0', sort);
    }

    function getCardItemType(card) {
      if (card && card.checked) {
        return 'primary'
      }
      return 'default';
    }

    function updateCheckedCard(subject) {
      const sort = subject.value.sort;
      for (let e in cards.value) {
        if (cards.value[e].sort === sort) {
          cards.value[e].checked = true;
          break;
        }
      }
    }

    function handleCloseSubmitModal() {
      isOpenedSubmitModal.value = false;
    }

    function handleConfirmSubmitModal() {
      const {userId} = api.getUserInfo();
      submitLoading.value = true;
      examApi.submit({
        userId,
        examinationId: examinationId.value,
        examRecordId: recordId.value
      }).then(() => {
        submitLoading.value = false;
        successMessage('提交成功');
        setTimeout(() => {
          Taro.redirectTo({url: "/pages/record/index?type=" + examination.value.type})
        }, 1000);
      }).catch(() => {
        warnMessage('提交失败');
      });
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

    onMounted(() => {
      refreshDuration();
      fetch();

      setInterval(() => {
        refreshDuration();
      }, 1000);
    })
    return {
      choiceRef,
      multiChoiceRef,
      judgementRef,
      shortAnswerRef,
      videoRef,
      subject,
      percentage,
      examination,
      radioArr,
      subjectTotalCount,
      duration,
      preLoading,
      nextLoading,
      submitLoading,
      recordBtnText,
      recordBtnIcon,
      hasMore,
      recordCompleteBtnText,
      recordCompleteBtnIcon,
      visibleSubmitDialog,
      isOpenedCardModal,
      cards,
      isOpenedSubmitModal,
      handleStart,
      handleShortAnswerChange,
      handleSaveAndNext,
      handleSubmit,
      onCancelSubmit,
      handleClickRecordAudio,
      handleUploadAudio,
      handlePlayAudio,
      handleRecordAudioComplete,
      handleOpenCards,
      handleCloseCards,
      handleChoiceSelectedChange,
      handleClickCardSubject,
      getCardItemType,
      handleCloseSubmitModal,
      handleConfirmSubmitModal,
      handleFavSubject
    }
  },
  toback() {

  }
}
</script>

<style>
page {
  background-color: rgba(242, 244, 248, 1);
}

.at-tag--primary {
  color: #E65D05;
  border-color: #E65D05;
  background-color: #FFF;
  font-weight: bold;
}

.at-checkbox::after {
  display: none;
}

.subject-type-label {
  display: flex;
  justify-content: space-between;
}
</style>