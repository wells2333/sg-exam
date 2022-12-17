<template>
  <AtMessage/>
  <view v-if="courseInfo !== undefined" class="course-detail">
    <image class="course-detail-image" :src="courseInfo.imageUrl"></image>
    <view class="course-detail-info">
      <text>{{ courseInfo.courseName }}</text>
      <at-tag class="course-charge-type" type="primary" size="small">{{
          courseInfo.chargeType === 0 ? '免费' : '收费'
        }}
      </at-tag>
      <at-rate class="course-detail-item-info" :value="courseInfo.level" size="small"></at-rate>
      <view class="course-detail-item-info">
        <at-flex>
          <at-flex-item :size="8">
            <view class="course-detail-item-info">
              <text class="course-teacher" v-if="courseInfo.memberCount > 0">
                报名人数：{{courseInfo.memberCount}}
              </text>
            </view>
            <view class="course-detail-item-info">
              <text class="course-teacher" v-if="courseInfo.teacher !== null">
                {{ courseInfo.teacher }}
              </text>
            </view>
          </at-flex-item>
          <at-flex-item :size="3" class="join-button">
            <at-button v-if="joinText !== undefined" type="primary" size="small" @click="handleClickJoin">{{joinText}}</at-button>
          </at-flex-item>
        </at-flex>
      </view>
    </view>
    <view>
      <at-tabs :tab-list="tagsList" :current="currentTag" @click="handleTabClick" color="rgb(7, 193, 96)">
        <at-tabs-pane title="课程介绍">
          <view class="course-pane">
            <wxparse :html="courseInfo.courseDescription" key={Math.random()} />
          </view>
        </at-tabs-pane>
        <at-tabs-pane title="课程章节">
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
        </at-tabs-pane>
        <at-tabs-pane title="课程评价">
          <view class="course-pane">
            <view>
              <view class="course-evaluate-input">
                <at-textarea
                    :value="evaluateValue"
                    @change="handleEvaluateValueChange"
                    :maxLength="100"
                    :height="50"
                    placeholder='请输入评价内容'
                />
                <at-rate class="course-evaluate-input-rate" :value="evaluateRateValue"
                         @change="handleEvaluateRateValueChange"></at-rate>
                <at-button class="course-evaluate-input-btn" type='primary' @click="handleSubmitEvaluate">提交</at-button>
              </view>
              <view class="course-evaluates">
                <view v-for="item in evaluates">
                  <at-flex class="course-flex">
                    <at-flex-item :size="3" class="course-evaluate-operator">{{ item.operatorName }}</at-flex-item>
                    <at-flex-item :size="10">
                      <at-rate class="course-flex-item" :value="item.evaluateLevel" size="small"></at-rate>
                      <text class="course-flex-item">{{ item.evaluateContent }}</text>
                      <p class="course-flex-item course-evaluate-time">{{ item.updateTime }}</p>
                    </at-flex-item>
                  </at-flex>
                </view>
              </view>
            </view>
          </view>
        </at-tabs-pane>
      </at-tabs>
    </view>

    <at-modal :isOpened="isOpenedJoinModal" :title="`确定${joinText}吗？`" cancelText="取消"
              confirmText="确认"
              @close="handleCloseJoinModal"
              @cancel="handleCloseJoinModal"
              @confirm="handleConfirmJoinModal"></at-modal>
  </view>
</template>
<script lang="ts">
import Taro from "@tarojs/taro";
import {onMounted, ref} from 'vue';
import examApi from '../../api/exam.api';

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
    const isOpenedJoinModal = ref<boolean>(false);
    const isUserJoin = ref<boolean>(false);
    const joinText = ref<string>(undefined);

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

    function handleTabClick(value) {
      currentTag.value = value;
    }

    function handleEvaluateValueChange(value) {
      evaluateValue.value = value;
    }

    function handleEvaluateRateValueChange(value) {
      evaluateRateValue.value = value;
    }

    async function handleSubmitEvaluate() {
      if (!checkIsHasJoin()) {
        return;
      }
      if (!isSubmitted.value) {
        let value = evaluateValue.value;
        if (value === '') {
          value = '用户默认好评';
        }
        const data = {
          courseId,
          evaluateContent: value,
          evaluateLevel: evaluateRateValue.value
        };
        isSubmitted.value = true;
        const res = await examApi.addEvaluate(data);
        const {code} = res;
        if (code === 0) {
          Taro.atMessage({
            message: '提交成功',
            type: 'success',
          });
          await fetchEvaluate();
          setTimeout(() => {
            isSubmitted.value = false;
          }, 3000);
        } else {
          Taro.atMessage({
            message: '提交失败',
            type: 'warning',
          });
          isSubmitted.value = false;
        }
      } else {
        Taro.atMessage({
          message: '请勿频繁提交',
          type: 'warning',
        });
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
      Taro.navigateTo({url: "/pages/course_section/index?courseId=" + courseId + "&" + params})
    }

    function handleClickJoin() {
      isOpenedJoinModal.value = true;
    }

    function handleCloseJoinModal() {
      isOpenedJoinModal.value = false;
    }

    async function handleConfirmJoinModal() {
      const type = isUserJoin.value ? '0' : '1';
      const res = await examApi.joinCourse(courseId, type);
      const {code} = res;
      if (code === 0) {
        Taro.atMessage({
          message: joinText.value + '成功',
          type: 'success',
        });
      } else {
        Taro.atMessage({
          message: joinText.value + '失败',
          type: 'warning',
        });
      }
      handleCloseJoinModal();
      await fetch();
    }

    function checkIsHasJoin() {
      if (!isUserJoin.value) {
        Taro.atMessage({
          message: '请先报名',
          type: 'warning',
        });
        return false;
      }
      return true;
    }

    onMounted(() => {
      fetch();
      fetchEvaluate();
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
      handleTabClick,
      handleEvaluateValueChange,
      handleEvaluateRateValueChange,
      handleSubmitEvaluate,
      handleClickSection,
      handleClickPoint,
      handleClickJoin,
      handleCloseJoinModal,
      handleConfirmJoinModal
    }
  }
}
</script>

<style>
.course-detail {
  margin-left: 8px;
  margin-right: 8px;
}

.course-detail-image {
  width: 100%;
  height: 160px;
  border-radius: 6px;
}

.course-detail-info {
  padding: 8px;
}

.course-detail-item-info {
  margin-top: 3px;
}

.course-teacher {
  font-size: 14px;
  color: gray;
}

.course-pane {
  margin-left: 8px;
  margin-top: 10px;
  color: #2C405A;
}

.course-section {
  margin-left: 20px;
  font-size: 15px;
}

.course-point {
  margin-left: 50px;
  font-size: 15px;
}

.course-chapter {
  font-size: 15px;
}

.course-chapter:hover, .course-section:hover, .course-point:hover {
  color: #6190E8;
}

.course-flex {
  margin-bottom: 18px;
}

.course-evaluate-time {
  font-size: 13px;
  color: gray;
}

.course-flex-item {
  padding: 2px;
}

.course-evaluate-input-rate, .course-evaluate-input-btn {
  margin-top: 10px;
  margin-bottom: 10px;
}

.course-evaluates {
  margin-top: 20px;
}

.course-evaluate-operator {
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
  margin-left: 12px;
}
</style>