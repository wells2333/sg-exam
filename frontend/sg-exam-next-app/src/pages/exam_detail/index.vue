<template>
  <AtMessage/>
  <view v-if="examInfo !== undefined" class="exam-detail">
    <image class="exam-detail-image" :src="examInfo.imageUrl"></image>
    <view class="exam-detail-info">
      <text>{{ examInfo.examinationName }}</text>
      <at-tag v-if="examInfo.tags !== null && examInfo.tags !== ''" class="exam-type" type="primary" size="small">{{ examInfo.tags }}
      </at-tag>
      <view class="exam-detail-item-info">
        <at-flex>
          <at-flex-item :size="8">
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
              <text class="exam-desc-item">
                模式：{{ examInfo.answerType===0 ? '展示全部题目' : '顺序答题' }}
              </text>
            </view>
            <view class="exam-detail-item-info">
              <text class="exam-desc-item" v-if="examInfo.course !== null">
                课程：{{ examInfo.course.courseName }}
              </text>
            </view>
          </at-flex-item>
          <at-flex-item :size="3" class="exam-start-button">
            <at-button type="primary" size="small" @click="handleClickStart">开始</at-button>
          </at-flex-item>
        </at-flex>
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
  <at-modal :isOpened="isOpenedStartModal" title="" confirm-text="确定" cancel-text="取消"
            content="确定开始吗？"
            @close="handleCloseStartModal"
            @cancel="handleCancelStartModal"
            @confirm="handleConfirmStartModal"
  ></at-modal>
</template>

<script lang="ts">
import Taro from "@tarojs/taro";
import {onMounted, ref} from 'vue';
import examApi from '../../api/exam.api';
import api from "../../api/api";
import {showLoading, hideLoading, warnMessage} from '../../utils/util';

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
          await warnMessage('请先登录');
          return;
        }
        const startResult = await examApi.startExam(examId, id);
        if (startResult && startResult.code === 1) {
          await warnMessage(startResult.message);
          return;
        }
        const {code, result, message} = startResult;
        if (code !== 0) {
          await warnMessage(message);
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
          Taro.navigateTo({url: "/pages/all_subject/index?recordId=" + examRecord.id + "&examinationId=" + examRecord.examinationId + "&total=" + total})
        } else if (answerType === 1) {
          // 上一题、下一题模式
          Taro.navigateTo({url: "/pages/next_subject/index?recordId=" + examRecord.id + "&examinationId=" + examRecord.examinationId + "&total=" + total})
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
  margin-left: 8px;
  margin-right: 8px;
}

.exam-detail-image {
  width: 100%;
  height: 160px;
  border-radius: 6px;
}

.exam-detail-info {
  padding: 8px;
}

.exam-detail-item-info {
  margin-top: 3px;
}

.exam-type {
  margin-left: 12px;
}

.exam-desc-item {
  font-size: 14px;
  color: gray;
}

.exam-start-button {
  margin-left: auto;
}

.exam-pane {
  margin-left: 8px;
  margin-top: 10px;
  color: #2C405A;
}

.exam-pane-desc {
  font-size: 14px;
  font-weight: bold;
  margin-top: 10px;
  margin-bottom: 10px;
}

.exam-pane-content {
  margin-top: 16px;
  font-size: 14px;
}
</style>