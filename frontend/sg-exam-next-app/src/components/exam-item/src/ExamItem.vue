<template>
  <view @click="handleStart">
    <image class="exam-img" :src="item.imageUrl">
      <view class="exam-img-top">
        <text class="exam-img-top-text">{{ item.typeLabel }}</text>
      </view>
    </image>
  </view>
  <view class="exam-item-bottom">
    <view @click="handleStart">
      <view>
        <text class="exam-title">{{ item.examinationName }}</text>
      </view>
      <view>
        <view>
          <view>
            <at-tag :class="index === 0 ? 'exam-item-bottom-first-tag exam-item-bottom-tag' : 'exam-item-bottom-tag'"
                    type="primary" :circle="true" size="small" v-for="(tag, index) in tags">
              {{ tag }}
            </at-tag>
          </view>
          <view class="exam-time">
            <text>{{ item.startTime === null ? "-" : item.startTime.substr(0, 16) }}</text>
          </view>
        </view>
      </view>
    </view>
    <view class="exam-item-favorites">
      <view class="exam-item-favorites-item" @click="handleStart">
        <at-icon value='edit' size='10' color='#AAAAAA'></at-icon>
        <text class="exam-item-favorites-text">{{ startCount }}</text>
      </view>
      <view class="exam-item-favorites-item" @click="handleFavoriteExam">
        <at-icon v-if='favorite' value='star-2' size='10' color='#FFC82C'></at-icon>
        <at-icon v-else value='star-2' size='10' color='#AAAAAA'></at-icon>
        <text class="exam-item-favorites-text">{{ favoriteCount }}</text>
      </view>
    </view>
  </view>
</template>

<script lang="ts">
import {defineComponent, ref} from 'vue';
import Taro from "@tarojs/taro";
import {transformToArray} from "../../../utils/util";
import examApi from "../../../api/exam.api";
import api from "../../../api/api";

export default defineComponent({
  name: 'ExamItem',
  components: {},
  props: {
    item: {
      type: Object
    }
  },
  emits: [
    'start'
  ],
  setup(props, {emit}) {
    const tags = ref<any>([]);
    const startCount = ref<any>(0);
    const favoriteCount = ref<number>(0);
    const favorite = ref<boolean>(false);
    if (props.item.tags !== null && props.item.tags !== '') {
      tags.value = transformToArray(props.item.tags);
    }
    if (props.item.startCount !== undefined && props.item.startCount > 0) {
      startCount.value = props.item.startCount;
    }
    if (props.item.favoritesCount !== undefined && props.item.favoritesCount > 0) {
      favoriteCount.value = props.item.favoritesCount;
    }
    if (props.item.favorite !== undefined && props.item.favorite) {
      favorite.value = props.item.favorite;
    }

    async function handleFavoriteExam() {
      let type = 1;
      let message = '收藏成功';
      favorite.value = !favorite.value;
      if (favorite.value) {
        favoriteCount.value = favoriteCount.value + 1;
      } else {
        if (favoriteCount.value > 0) {
          favoriteCount.value = favoriteCount.value - 1;
          type = 0;
          message = '取消收藏成功';
        }
      }
      const {id} = api.getUserInfo();
      await examApi.favoriteExam(id, props.item.id, type);
      Taro.atMessage({
        message: message,
        type: 'info',
        duration: 800
      });
    }

    function handleStart() {
      emit('start', props.item);
    }

    return {
      tags,
      startCount,
      favoriteCount,
      favorite,
      handleFavoriteExam,
      handleStart
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

.exam-item-favorites {
  margin-right: 8px;
  display: inline-flex;
  justify-content: space-between;
  align-items: center;
}

.exam-item-favorites-item {
  margin-left: 12px;
}

.exam-item-favorites-text {
  color: #AAAAAA;
  font-size: 14px;
}
</style>