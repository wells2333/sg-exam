<template>
  <view v-if="courseInfo !== undefined" class="course-detail ml-22 mr-22">
    <image class="course-detail-image" :src="courseInfo.imageUrl"></image>
    <view class="course-detail-info">
      <view class="course-detail-name">
        <text>{{ courseInfo.courseName }}</text>
        <nut-tag class="course-charge-type" type="success" size="small">{{
            courseInfo.chargeType === 0 ? '免费' : '$' + courseInfo.chargePrice
          }}
        </nut-tag>
      </view>
      <nut-rate class="course-detail-rate course-detail-item-info" v-model="courseInfo.level" size="small" spacing="3" readonly></nut-rate>
      <view class="course-detail-items course-detail-item-info">
        <view>
          <view class="course-detail-item-info">
            <text class="course-teacher" v-if="courseInfo.memberCount > 0">
              报名人数：{{ courseInfo.memberCount }}
            </text>
          </view>
          <view class="course-detail-item-info">
            <text class="course-teacher" v-if="courseInfo.teacher !== null">
              {{ courseInfo.teacher }}
            </text>
          </view>
        </view>
        <view>
          <nut-button v-if="joinText !== undefined" type="primary" :loading="joining" @click="handleClickJoin">
            {{ joinText }}
          </nut-button>
        </view>
      </view>
    </view>
    <view>
      <nut-tabs v-model="currentTag">
        <nut-tab-pane title="课程介绍">
          <view class="course-pane">
            <wxparse :html="courseInfo.courseDescription" key={Math.random()}></wxparse>
          </view>
        </nut-tab-pane>
        <nut-tab-pane title="课程章节">
          <view class="course-pane">
            <view v-for="chapter in chapters">
              <p class="course-chapter">{{ chapter.chapter.title }}</p>
              <view v-for="section in chapter.sections">
                <p class="course-section" @click="handleClickSection(section.section)">{{ section.section.title }}</p>
                <view v-for="point in section.points">
                  <p class="course-point" @click="handleClickPoint(point)">{{ point.title }}</p>
                </view>
              </view>
            </view>
          </view>
        </nut-tab-pane>
        <nut-tab-pane title="课程评价">
          <view class="course-pane">
            <view>
              <view>
                <view>
                  <nut-textarea
                      v-model="evaluateValue"
                      :maxLength="100"
                      height="80px"
                      placeholder='请输入评价内容'
                  />
                </view>
                <nut-rate class="course-evaluate-input-rate" v-model="evaluateRateValue" size="small" spacing="3"></nut-rate>
                <nut-button class="course-evaluate-input-btn" type='primary' block :loading="submitting" @click="handleSubmitEvaluate">提交
                </nut-button>
              </view>
              <view>
                <view v-for="item in evaluates" class="course-evaluates">
                  <view class="course-evaluate-operator">
                    <view>
                      <view>{{ item.operatorName }}</view>
                      <view>
                        <nut-rate class="course-flex-item" v-model="item.evaluateLevel" size="small" spacing="3"></nut-rate>
                      </view>
                    </view>
                    <view class="course-flex-item course-evaluate-time">{{ item.evaluateTime }}</view>
                  </view>
                  <view class="course-evaluate-content">
                    {{ item.evaluateContent }}
                  </view>
                </view>
              </view>
            </view>
          </view>
        </nut-tab-pane>
      </nut-tabs>
    </view>
    <nut-dialog v-model:visible="isOpenedJoinModal" :content="`确定${joinText}吗？`"
                @cancel="handleCloseJoinModal"
                @ok="handleConfirmJoinModal"></nut-dialog>
  </view>
</template>
<script lang="ts">
import Taro from '@tarojs/taro';
import {onMounted, ref} from 'vue';
import examApi from '../../../api/exam.api';
import {showLoading, hideLoading, successMessage, warnMessage} from '../../../utils/util';

export default {
  setup() {
    const currentInstance = Taro.getCurrentInstance();
    const params = currentInstance.router.params;
    const courseId = params.courseId;
    const courseDetail = ref<any>(undefined);
    const courseInfo = ref<any>(undefined);
    const chapters = ref<any>(undefined);
    const evaluates = ref<any>(undefined);
    const currentTag = ref<number>(0);
    const evaluateValue = ref<String>('');
    const evaluateRateValue = ref<number>(5);
    const isSubmitted = ref<boolean>(false);
    const submitting = ref<boolean>(false);
    const isOpenedJoinModal = ref<boolean>(false);
    const isUserJoin = ref<boolean>(false);
    const joinText = ref<string>(undefined);
    const joining = ref<boolean>(false);

    async function fetch() {
      const res = await examApi.courseDetail(courseId);
      const {code, result} = res;
      if (code === 0) {
        courseDetail.value = result;
        courseInfo.value = result.course;
        chapters.value = result.chapters;
        isUserJoin.value = result.isUserJoin;
        joinText.value = result.isUserJoin === true ? '取消报名' : '报名';
      }
    }

    async function fetchEvaluate() {
      const res = await examApi.courseEvaluate(courseId, {pageSize: 5});
      const {code, result} = res;
      if (code === 0) {
        evaluates.value = result.list;
      }
    }

    const tagsList = [{title: '课程介绍'}, {title: '课程章节'}, {title: '课程评价'}];

    async function handleSubmitEvaluate() {
      if (!await checkIsHasJoin()) {
        return;
      }
      if (!isSubmitted.value) {
        let value = evaluateValue.value;
        if (value === '') {
          value = '用户默认好评';
        }
        try {
          const data = {
            courseId,
            evaluateContent: value,
            evaluateLevel: evaluateRateValue.value
          };
          submitting.value = true;
          isSubmitted.value = true;
          const res = await examApi.addEvaluate(data);
          const {code} = res;
          if (code === 0) {
            await successMessage('提交成功');
            await fetchEvaluate();
            setTimeout(() => {
              isSubmitted.value = false;
            }, 3000);
          } else {
            await warnMessage('提交失败');
            isSubmitted.value = false;
          }
        } finally {
          submitting.value = false;
        }
      } else {
        await warnMessage('请勿频繁提交');
      }
    }

    function handleClickSection(section) {
      toCourseSection("sectionId=" + section.id)
    }

    function handleClickPoint(point) {
      toCourseSection("pointId=" + point.id)
    }

    function toCourseSection(params: string) {
      if (!checkIsHasJoin()) {
        return;
      }
      Taro.navigateTo({url: "/pages/exam_pages/course_section/index?courseId=" + courseId + "&" + params})
    }

    function handleClickJoin() {
      isOpenedJoinModal.value = true;
    }

    function handleCloseJoinModal() {
      isOpenedJoinModal.value = false;
    }

    async function handleConfirmJoinModal() {
      try {
        joining.value = true;
        const type = isUserJoin.value ? '0' : '1';
        const res = await examApi.joinCourse(courseId, type);
        const {code} = res;
        if (code === 0) {
          await successMessage(joinText.value + '成功');
        } else {
          await warnMessage(joinText.value + '失败');
        }
        handleCloseJoinModal();
        await fetch();
      } finally {
        joining.value = false;
      }
    }

    async function checkIsHasJoin() {
      if (!isUserJoin.value) {
        await warnMessage('请先报名');
        return false;
      }
      return true;
    }

    async function init() {
      try {
        await showLoading();
        evaluateValue.value = '';
        evaluateRateValue.value = 5;
        await fetch();
        await fetchEvaluate();
      } finally {
        hideLoading();
      }
    }

    onMounted(() => {
      init();
    });

    return {
      courseDetail,
      courseInfo,
      chapters,
      evaluates,
      tagsList,
      currentTag,
      evaluateValue,
      evaluateRateValue,
      isOpenedJoinModal,
      joinText,
      submitting,
      joining,
      init,
      handleSubmitEvaluate,
      handleClickSection,
      handleClickPoint,
      handleClickJoin,
      handleCloseJoinModal,
      handleConfirmJoinModal
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
.course-detail {

}

.course-detail-image {
  width: 100%;
  height: 300px;
  border-radius: 6px;
}

.course-detail-info {
  padding: 8px;
  margin-bottom: 12px;
}

.course-detail-item-info {
  margin-top: 8px;
  display: flex;
  align-items: center;
}

.course-teacher {
  font-size: 30px;
  color: gray;
}

.course-pane {
  margin-left: 8px;
  margin-top: 20px;
}

.course-section {
  margin-left: 20px;
  font-size: 28px;
}

.course-point {
  margin-left: 50px;
  font-size: 26px;
}

.course-chapter {
  font-size: 30px;
}

.course-chapter:hover, .course-section:hover, .course-point:hover {
  color: #6190E8;
}

.course-evaluate-time {
  font-size: 22px;
  font-weight: 300;
  color: gray;
}

.course-flex-item {
  padding: 2px;
}

.course-evaluate-input {
  display: flex;
  align-items: center;
}

.course-evaluate-input-rate, .course-evaluate-input-btn {
  margin-top: 10px;
  margin-bottom: 10px;
}

.course-evaluates {
  margin-top: 26px;
  border-bottom: 1px solid rgb(238 238 238);
}

.course-evaluate-operator {
  display: flex;
  justify-content: space-between;
  overflow: hidden;
  text-overflow: ellipsis;
  margin-right: 5px;
}

.course-chapter, .course-section, .course-point {

}

.join-button {
  margin-left: auto;
}

.course-charge-type {
  margin-left: 20px;
}

.course-detail-name {
  display: flex;
  align-items: center;
}

.course-detail-rate {
  margin-top: 8px;
  margin-bottom: 8px;
}

.course-detail .nut-tab-pane {
  padding: 0;
}

.course-detail .nut-textarea {
  padding: 0;
}

.course-evaluate-content {
  margin-bottom: 20px;
}

.course-detail-items {
  display: flex;
  justify-content: space-between;
}
</style>