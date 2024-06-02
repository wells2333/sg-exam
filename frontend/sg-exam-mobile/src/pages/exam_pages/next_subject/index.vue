<template>
   <div class="fixed-top">
     <div class="subject-exam-title bg-white">
       <div style="display: flex; justify-content: space-between">
        <span>{{ subject.sort }}/{{ subjectTotalCount }}</span>
        <nut-tag type="primary" plain class="duration-info" :circle="true">{{ duration }}</nut-tag>
       </div>
       <div class="subject-exam-progress">
        <nut-progress :percentage="percentage" :show-text="false" size="small" strokeWidth="8"/>
       </div>
     </div>
   </div>
   <div class="fixed-top-next subject-box">
     <div class="subject-content bg-white">
       <div class="subject-type-label">
        <nut-tag size="small" type="primary">{{ subject.typeLabel }}</nut-tag>
        <IconFont v-if="examination.type !== 0" @click="handleFavSubject(subject)" name="star-2" size='10'
                 :color="subject.favorite === true ? '#FFC82C': '#AAAAAA'"></IconFont>
       </div>
       <div class="subject-title">
        <span class="subject-title-content">{{ subject.sort }}.&nbsp;</span>
        <div class="subject-title-content" v-html="subject.subjectName"></div>
       </div>
       <div>
         <div v-if="subject.type === 0">
          <choice ref="choiceRef" :subject="subject" @update-selected="handleChoiceSelectedChange"></choice>
         </div>
         <div v-else-if="subject.type === 1">
          <short-answer ref="shortAnswerRef" :subject="subject"
                        @update-selected="handleChoiceSelectedChange"></short-answer>
         </div>
         <div v-else-if="subject.type === 2">
          <judgement ref="judgementRef" :subject="subject"
                     @update-selected="handleChoiceSelectedChange"></judgement>
         </div>
         <div v-else-if="subject.type === 3" class="subject-options">
            <choice ref="multiChoiceRef" :subject="subject" :multi="true"
                    @update-selected="handleChoiceSelectedChange"></choice>
         </div>
       </div>
     </div>

     <div class="submit-btn">
       <div class="submit-btn-item">
        <nut-button type="primary" :circle="true" @click="handleSaveAndNext('1')" :loading="preLoading">上一题</nut-button>
       </div>
       <div class="submit-btn-item">
        <nut-button type="primary" :circle="true" @click="handleSaveAndNext('0')" :loading="nextLoading">下一题</nut-button>
       </div>
       <div class="submit-btn-item">
        <nut-button type="primary" :circle="true" @click="handleOpenCards">答题卡</nut-button>
       </div>
       <div class="submit-btn-item" v-if="!hasMore">
        <nut-button type="primary" :circle="true" @click="handleSubmit('0')" :loading="submitLoading">提交</nut-button>
       </div>
     </div>
   </div>

  <nut-dialog v-model:visible="isOpenedCardModal" title="答题卡"
              @cancel="handleCloseCards"
              @ok="handleCloseCards">
     <div class="card-box">
       <div class="card-item" v-for="card in cards">
        <nut-button class="card-item-btn" :type="getCardItemType(card)" :circle="true"
                   @click="handleClickCardSubject(card.sort)">
          {{ card.sort }}
        </nut-button>
       </div>
     </div>
  </nut-dialog>

  <nut-dialog v-model:visible="isOpenedSubmitModal" title="确定提交吗？"
            @cancel="handleCloseSubmitModal"
            @ok="handleConfirmSubmitModal">
  </nut-dialog>
</template>
<script lang="ts">
import {onMounted, ref, unref} from 'vue';
import Taro from "@tarojs/taro";
import {IconFont} from '@nutui/icons-vue-taro';
import examApi from '../../../api/exam.api';
import api from '../../../api/api';
import {initRecorderManager, playAudio, pleaseStartRecord, startRecordAudio, stopRecordAudio} from "./audio";
import {Choice} from '../../../components/subject/choice/index';
import {Judgement} from '../../../components/subject/judgement/index';
import {ShortAnswer} from '../../../components/subject/shortAnswer/index';
import {getDuration, parseRes, successMessage, warnMessage, showLoading, hideLoading, showNoMoreData} from '../../../utils/util';

export default {
  components: {
    'choice': Choice,
    'judgement': Judgement,
    'short-answer': ShortAnswer,
    IconFont
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
          Taro.redirectTo({url: "/pages/exam_pages/record/index?type=" + examination.value.type})
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
.nut-tag--primary.nut-tag--plain {
  border: none;
}

.subject-exam-progress {
  margin-top: 12px;
}

.subject-type-label {
  display: flex;
  justify-content: space-between;
}
</style>