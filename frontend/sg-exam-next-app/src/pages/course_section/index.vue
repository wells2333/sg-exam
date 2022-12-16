<template>
  <view>
    <view class="at-article">
      <view v-if="courseDetail !== undefined">
        <view class="at-article__h2">{{ courseDetail.course.courseName }}</view>
      </view>
      <view v-if="pointDetail !== undefined">
        <view class="at-article__h3">{{ pointDetail.title }}</view>
        <view class='at-article__info'>
          更新时间：{{ pointDetail.updateTime }}&nbsp;&nbsp;&nbsp;&nbsp;学习时长：{{ pointDetail.learnHour }}小时
        </view>
        <view class="at-article__content">
          <view class="section-content">
            <view class="section-video">
              <video class="section-video-content" v-if="videoUrl !== undefined" :src="videoUrl"></video>
            </view>
            <view class="at-article__section">
              <wxparse :html="pointDetail.content" key={Math.random()} />
            </view>
          </view>
        </view>
      </view>
      <view v-if="sectionDetail !== undefined">
        <view class="at-article__h3">{{ sectionDetail.section.title }}</view>
        <view class='at-article__info'>
          更新时间：{{ sectionDetail.section.updateTime }}&nbsp;&nbsp;&nbsp;&nbsp;学习时长：{{ sectionDetail.section.learnHour }}小时
        </view>
        <view class="at-article__content">
          <view class="section-content">
            <view class="section-video">
              <video class="section-video-content" v-if="videoUrl !== undefined" :src="videoUrl"></video>
            </view>
            <view class="at-article__section">
              <wxparse :html="sectionDetail.section.content" key={Math.random()} />
            </view>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script lang="ts">
import Taro from "@tarojs/taro";
import {onMounted, ref, unref} from 'vue';
import examApi from '../../api/exam.api';
import {filterLogin} from "../../utils/filter";

export default {
  setup() {
    const currentInstance = Taro.getCurrentInstance();
    const params = currentInstance.router.params;
    const courseId = ref<any>(params.courseId);
    const sectionId = ref<any>(params.sectionId);
    const pointId = ref<any>(params.pointId);

    const courseDetail = ref<object>();
    const sectionDetail = ref<object>();
    const pointDetail = ref<object>();
    const videoUrl = ref<string>();

    async function getCourseDetail() {
      const res = await examApi.courseDetail(courseId.value);
      const {code, result} = res;
      if (code === 0) {
        courseDetail.value = result;
      }
    }

    async function getSection() {
      if (sectionId.value === null || sectionId.value === undefined) {
        return;
      }
      const res = await examApi.getSectionDetail(sectionId.value);
      const {code, result} = res;
      if (code === 0) {
        sectionDetail.value = result;
        if (result.videoUrl !== undefined && result.videoUrl !== null && result.videoUrl !== '') {
          videoUrl.value = result.videoUrl;
        }
      }
    }

    async function getPoint() {
      if (pointId.value === null || pointId.value === undefined) {
        return;
      }
      const res = await examApi.getPointDetail(pointId.value);
      const {code, result} = res;
      if (code === 0) {
        pointDetail.value = result;
        if (result.videoUrl !== undefined && result.videoUrl !== null && result.videoUrl !== '') {
          videoUrl.value = result.videoUrl;
        }
      }
    }

    onMounted(() => {
      getCourseDetail();
      if (pointId.value !== undefined) {
        getPoint();
      } else {
        getSection();
      }
    });
    return {
      courseDetail,
      sectionDetail,
      pointDetail,
      videoUrl
    }
  }
}
</script>

<style>
.at-article__info {
  margin-top: 8px;
}

.section-content {
  margin: 10px;
  font-size: 14px;
  color: #666;
  overflow: hidden;
}

.section-video {

}

.section-video-content {
  width: 100%;
}
</style>