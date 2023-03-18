<template>
  <view v-if="examInfo !== undefined" class="exam-detail">
    <image class="exam-detail-image" :src="examInfo.imageUrl"></image>
    <view class="exam-detail-info">
      <text>{{ examInfo.examinationName }}</text>
      <nut-tag v-if="examInfo.tags !== null && examInfo.tags !== ''" class="exam-type" type="success" size="small">{{ examInfo.tags }}
      </nut-tag>
      <view class="exam-detail-item-info-pane">
        <view>
          <view class="exam-detail-item-info">
            <text class="exam-desc-item">
              类型：{{ examInfo.typeLabel }}
            </text>
          </view>
          <view class="exam-detail-item-info">
            <text class="exam-desc-item">
              总分：{{ examInfo.totalScore }}
            </text>
          </view>
          <view class="exam-detail-item-info">
            <text class="exam-desc-item" v-if="examInfo.course !== null">
              课程：{{ examInfo.course.courseName }}
            </text>
          </view>
        </view>
        <view :span="6" class="exam-start-button">
          <nut-button type="primary" @click="handleClickStart">开始</nut-button>
        </view>
      </view>
    </view>
    <view>
      <view class="exam-pane">
        <text class="exam-pane-desc">考试介绍：</text>
        <view class="exam-pane-content">
          <wxparse :html="examInfo.remark" key={Math.random()} />
        </view>
        <view class="exam-pane-desc">
          <text>注意事项：</text>
        </view>
        <view class="exam-pane-content">
          <text>{{ examInfo.attention }}</text>
        </view>
      </view>
    </view>
  </view>
  <nut-dialog content="确定开始吗？" v-model:visible="isOpenedStartModal" @cancel="handleCancelStartModal" @ok="handleConfirmStartModal" />
</template>

<script lang="ts">
import Taro from "@tarojs/taro";
import {onMounted, ref} from 'vue';
import examApi from '../../../api/exam.api';
import api from '../../../api/api';
import {showLoading, hideLoading, warnMessage} from '../../../utils/util';

export default {
  setup() {
    const currentInstance = Taro.getCurrentInstance();
    const params = currentInstance.router.params;
    const examId = params.id;
    const examInfo = ref<any>(undefined);
    const isOpenedStartModal = ref<boolean>(false);

    async function fetch() {
      const res = await examApi.examinationDetail(examId);
      const {code, result} = res;
      if (code === 0) {
        examInfo.value = result;
      }
    }

    function handleClickStart() {
      isOpenedStartModal.value = true;
    }

    function handleCloseStartModal() {
      isOpenedStartModal.value = false;
    }

    function handleCancelStartModal() {
      isOpenedStartModal.value = false;
    }

    async function handleConfirmStartModal() {
      try {
        await showLoading();
        const {id} = api.getUserInfo();
        if (!id) {
          await warnMessage('请先登录', 1500);
          return;
        }
        const startResult = await examApi.startExam(examId, id);
        if (startResult && startResult.code === 1) {
          await warnMessage(startResult.message, 1500);
          return;
        }
        const {code, result, message} = startResult;
        if (code !== 0) {
          await warnMessage(message, 1500);
          return;
        }
        const {examination, examRecord, subjectDto, total, cards} = result;
        if (examination) {
          api.setExamination(examination);
        }
        if (subjectDto) {
          api.setSubject(subjectDto);
        }
        if (cards) {
          api.setCards(cards);
        }
        if (examRecord) {
          api.setExamRecord(examRecord);
        }
        const {answerType} = examination;
        if (answerType === 0) {
          // 展示所有题目
          Taro.navigateTo({url: "/pages/exam_pages/all_subject/index?recordId=" + examRecord.id + "&examinationId=" + examRecord.examinationId + "&total=" + total})
        } else if (answerType === 1) {
          // 上一题、下一题模式
          Taro.navigateTo({url: "/pages/exam_pages/next_subject/index?recordId=" + examRecord.id + "&examinationId=" + examRecord.examinationId + "&total=" + total})
        }
      } finally {
        handleCloseStartModal();
        await Taro.hideLoading();
      }
    }

    async function init() {
      try {
        await showLoading();
        await fetch();
      } finally {
        hideLoading();
      }
    }

    onMounted(() => {
      init();
    });

    return {
      examInfo,
      isOpenedStartModal,
      init,
      handleClickStart,
      handleCloseStartModal,
      handleCancelStartModal,
      handleConfirmStartModal
    }
  },
  onPullDownRefresh() {
    try {
      this.init();
    } finally {
      Taro.stopPullDownRefresh();
    }
  },
}

</script>

<style>
.exam-detail {
  margin-left: 22px;
  margin-right: 22px;
}

.exam-detail-image {
  width: 100%;
  height: 260px;
  border-radius: 6px;
}

.exam-detail-info {
  font-size: 32px;
  margin-top: 20px;
}

.exam-detail-item-info-pane {
  display: flex;
  justify-content: space-between;
}

.exam-detail-item-info {
  margin-top: 10px;
}

.exam-type {
  margin-left: 18px;
}

.exam-desc-item {
  font-size: 28px;
  color: gray;
}

.exam-start-button {
  margin-left: auto;
}

.exam-pane {
  margin-top: 50px;
  color: #2C405A;
}

.exam-pane-desc {
  font-size: 30px;
  font-weight: bold;
  margin-top: 50px;
  margin-bottom: 22px;
}

.exam-pane-content {
  margin-top: 16px;
  font-size: 30px;
}
</style>