<template>
  <AtMessage/>
  <view class="fixed-top">
    <view class="subject-exam-title bg-white">
      <view>{{ examination.examinationName }}({{ examination.totalScore }}分)</view>
      <view class="subject-exam-progress">
        <at-progress :percent="percentage" size="small" strokeWidth="8"/>
      </view>
    </view>
  </view>
  <view class="fixed-top-next subject-box">
    <view class="subject-content bg-white" v-for="(item, index) in subjects" :key="index">
      <view class="subject-type-label">
        <at-tag size="small" type="primary">{{ item.typeLabel }}</at-tag>
        <at-icon v-if="examination.type !== 0" @click="handleFavSubject(item)" value="star-2" size='10' :color="item.favorite === true ? '#FFC82C': '#AAAAAA'"></at-icon>
      </view>
      <view class="subject-title">
        <text class="subject-title-content">{{ item.sort }}.&nbsp;</text>
        <wxparse class="subject-title-content" :html="item.subjectName" key={Math.random()}></wxparse>
      </view>
      <view>
        <view v-if="item.type === 0">
          <choice :ref="refs[index]" :subject="refItem(item)" @update-selected="handleChoiceSelectedChange"></choice>
        </view>
        <view v-else-if="item.type === 3">
          <choice :ref="refs[index]" :subject="refItem(item)" :multi="true"
                  @update-selected="handleChoiceSelectedChange"></choice>
        </view>
        <view v-else-if="item.type === 1">
          <short-answer :ref="refs[index]" :subject="refItem(item)"
                        @update-selected="handleChoiceSelectedChange"></short-answer>
        </view>
        <view v-else-if="item.type === 4">
          <judgement :ref="refs[index]" :subject="refItem(item)"
                     @update-selected="handleChoiceSelectedChange"></judgement>
        </view>
      </view>
    </view>
    <view class="all-subject-submit-btn">
      <view class="submit-btn-item">
        <at-button type="primary" :circle="true" @click="handleSubmit">提交</at-button>
      </view>
    </view>
  </view>
  <at-modal :isOpened="isOpenedSubmitModal" title="确定提交吗？" cancelText="取消"
            confirmText="确认"
            @close="handleCloseSubmitModal"
            @cancel="handleCloseSubmitModal"
            @confirm="handleConfirmSubmitModal"></at-modal>
</template>
<script lang="ts">
import {onMounted, ref, unref} from 'vue';
import Taro from "@tarojs/taro";
import examApi from '../../api/exam.api';
import api from "../../api/api";
import {Choice} from '../../components/subject/choice/index';
import {Judgement} from '../../components/subject/judgement/index';
import {ShortAnswer} from '../../components/subject/shortAnswer/index';
import {showLoading, hideLoading, successMessage} from '../../utils/util';

export default {
  components: {
    'choice': Choice,
    'judgement': Judgement,
    'short-answer': ShortAnswer,
  },
  setup() {
    const currentInstance = Taro.getCurrentInstance();
    const subjects = ref<any>([]);
    const radioArr = ref<any>([]);
    const visibleSubmitDialog = ref(false);
    const recordId = ref<string>("");
    const examinationId = ref<string>("");
    const percentage = ref<number>(0);
    const examination = ref<any>({});
    const isOpenedSubmitModal = ref<boolean>(false);
    const refs = ref<any>([]);

    async function fetch() {
      try {
        await showLoading();
        examination.value = api.getExamination();
        const params = currentInstance.router.params;
        recordId.value = params.recordId;
        examinationId.value = params.examinationId;
        const subjectResult = await examApi.allSubjects(examinationId.value);
        if (subjectResult && subjectResult.code === 0) {
          subjects.value = subjectResult.result;
        }
      } finally {
        hideLoading();
      }
    }

    function handleStart(item) {
      console.log(radioArr.value, 'radioArr')
    }

    function handleSingleOptionChange($event, item) {
      const {detail} = $event;
      unref(item).answer = detail;
      unref(item).checked = true;
      refreshProgress();
    }

    function handleMultiOptionChange($event, item) {
      const {detail} = $event;
      unref(item).answer = detail;
      unref(item).checked = true;
      refreshProgress();
    }

    function handleShortAnswerChange($event, item) {
      const {detail} = $event;
      unref(item).answer = detail;
      unref(item).checked = true;
      refreshProgress();
    }

    function handleJudgementChange($event, item) {
      const {detail} = $event;
      unref(item).answer = detail;
      unref(item).checked = true;
      refreshProgress();
    }

    function refreshProgress() {
      let count = 0;
      unref(subjects).forEach(subject => {
        if (unref(subject).checked) {
          count++;
        }
      });
      percentage.value = Math.round(count / unref(subjects).length * 100);
    }

    function handleSubmit() {
      isOpenedSubmitModal.value = true;
    }

    function onCancelSubmit() {

    }

    function handleChoiceSelectedChange() {
      refreshProgress();
    }

    function handleCloseSubmitModal() {
      isOpenedSubmitModal.value = false;
    }

    async function handleConfirmSubmitModal() {
      try {
        await showLoading('提交中');
        const data = ref<any>([]);
        unref(subjects).forEach(subject => {
          let {id, answerValue} = subject;
          data.value.push({examRecordId: recordId.value, subjectId: id, answer: answerValue});
        })
        await examApi.submitAllSubjects(data.value);
        await successMessage('提交成功');
      } finally {
        hideLoading();
        handleCloseSubmitModal();
        setTimeout(() => {
          Taro.redirectTo({url: "/pages/record/index?type=" + examination.value.type})
        }, 500);
      }
    }

    function refItem(item) {
      return ref<any>(item);
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
      fetch();
    });
    return {
      subjects,
      percentage,
      examination,
      radioArr,
      isOpenedSubmitModal,
      refs,
      handleStart,
      handleSingleOptionChange,
      handleMultiOptionChange,
      handleShortAnswerChange,
      handleJudgementChange,
      visibleSubmitDialog,
      handleSubmit,
      onCancelSubmit,
      handleChoiceSelectedChange,
      handleCloseSubmitModal,
      handleConfirmSubmitModal,
      refItem,
      handleFavSubject
    }
  }
}
</script>

<style>
page {
  background-color: rgba(242, 244, 248, 1);
}

.subject-exam-title {
  font-size: 18px;
  line-height: 25px;
}

.all-subject-submit-btn {
  display: flex;
  justify-content: center;
  padding: 10px;
  background-color: white;
}

.at-checkbox::after {
  display: none;
}

.subject-type-label {
  display: flex;
  justify-content: space-between;
}
</style>