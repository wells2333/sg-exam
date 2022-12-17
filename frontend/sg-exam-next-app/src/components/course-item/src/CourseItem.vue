<template>
  <view class="course-item-container">
    <view @click="handleStart">
      <image class="course-img" :src="item.imageUrl">
        <view class="course-img-top">
          <text class="course-img-top-text">{{ item.typeLabel }}</text>
        </view>
      </image>
    </view>
    <view class="course-item-bottom">
      <view @click="handleStart">
        <view>
          <text class="course-title">{{ item.courseName }}</text>
        </view>
        <view class="course-simple-desc">
          <text v-if="item.simpleDesc !== null" >{{item.simpleDesc}}</text>
        </view>
      </view>
      <view class="course-item-favorites">
        <view class="course-item-favorites-item">
          <at-icon value='user' size='10' color='#AAAAAA'></at-icon>
          <text class="course-item-favorites-text">{{ memberCount }}</text>
        </view>
        <view class="course-item-favorites-item" @click="handleFavorite">
          <at-icon v-if='favorite' value='star-2' size='10' color='#FFC82C'></at-icon>
          <at-icon v-else value='star-2' size='10' color='#AAAAAA'></at-icon>
          <text class="course-item-favorites-text">{{ favCount }}</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script lang="ts">
import {defineComponent, ref} from 'vue';
import {transformToArray} from "../../../utils/util";
import api from "../../../api/api";
import examApi from "../../../api/exam.api";
import Taro from "@tarojs/taro";

export default defineComponent({
  name: 'CourseItem',
  components: {},
  props: {
    item: {
      type: Object
    }
  },
  emits: [
    'click'
  ],
  setup(props, {emit}) {
    const tags = ref<any>([]);
    const memberCount = ref<any>(0);
    const favorite = ref<boolean>(false);
    const favCount = ref<number>(0);
    if (props.item.tags !== null && props.item.tags !== '') {
      tags.value = transformToArray(props.item.tags);
    }
    if (props.item.memberCount !== undefined && props.item.memberCount > 0) {
      memberCount.value = props.item.memberCount;
    }
    if (props.item.favorite !== undefined && props.item.favorite) {
      favorite.value = props.item.favorite;
    }
    if (props.item.favCount !== undefined && props.item.favCount) {
      favCount.value = props.item.favCount;
    }

    function handleStart() {
      emit('click', props.item);
    }

    async function handleFavorite() {
      let type = 1;
      let message = '收藏成功';
      favorite.value = !favorite.value;
      props.item.favorite = favorite.value;
      if (favorite.value) {
        favCount.value = favCount.value + 1;
      } else {
        if (favCount.value > 0) {
          favCount.value = favCount.value - 1;
          type = 0;
          message = '取消收藏成功';
        }
      }
      const {id} = api.getUserInfo();
      await examApi.favoriteCourse(id, props.item.id, type);
      Taro.atMessage({
        message: message,
        type: 'info',
        duration: 500
      });
      emit('fav', props.item);
    }

    return {
      tags,
      memberCount,
      favorite,
      favCount,
      handleStart,
      handleFavorite
    }
  }
})
</script>

<style>
.at-tag--primary {
  color: #78A4F4;
  border-color: #78A4F4;
  background-color: #FFF;
}
.course-item-container {
  border-radius: 6px;
  overflow: hidden;
  position: relative;
}
</style>