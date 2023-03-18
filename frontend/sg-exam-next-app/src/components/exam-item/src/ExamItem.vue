<template>
  <view>
    <view @click="handleStart">
      <image class="card-item-img" :src="item.imageUrl"></image>
    </view>
    <view class="card-item-bottom" @click="handleStart">
      <h4 class="card-title">{{ item.examinationName }}</h4>
      <nut-tag :class="index === 0 ? 'card-item-bottom-first-tag card-item-bottom-tag' : 'card-item-bottom-tag'"
              type="success" size="small" v-for="(tag, index) in tags">
        {{ tag }}
      </nut-tag>
    </view>
    <view class="card-item-bottom-fixed">
      <view class="card-item-favorites">
        <view class="card-item-favorites-item">
          <IconFont name='edit' color='#AAAAAA'></IconFont>
          <text class="card-item-favorites-text">{{ startCount }}</text>
        </view>
        <view class="card-item-favorites-item" @click="handleFavoriteExam">
          <IconFont v-if="favorite" name='star-fill-n' color="#FFC82C"></IconFont>
          <IconFont v-else name='star-n' color="#AAAAAA"></IconFont>
          <text class="card-item-favorites-text">{{ favoriteCount }}</text>
        </view>
      </view>
      <view>
        <a href="#" @click="handleStart">查看</a>
      </view>
    </view>
  </view>
</template>

<script lang="ts">
import {defineComponent, ref} from 'vue';
import {IconFont} from '@nutui/icons-vue-taro';
import {transformToArray} from "../../../utils/util";
import examApi from "../../../api/exam.api";
import api from "../../../api/api";
import {successMessage} from "../../../utils/util";

export default defineComponent({
  name: 'ExamItem',
  components: {IconFont},
  props: {
    item: {
      type: Object
    }
  },
  emits: [
    'start',
    'fav'
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
      successMessage(message);
      emit('fav', props.item);
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

</style>