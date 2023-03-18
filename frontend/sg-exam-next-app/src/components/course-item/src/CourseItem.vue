<template>
  <view class="card-item-container">
    <view @click="handleStart">
      <image class="card-item-img" :src="item.imageUrl">
        <view class="card-item-img-top">
          <text class="card-item-img-top-text">{{ item.typeLabel }}</text>
        </view>
      </image>
    </view>
    <view class="card-item-bottom">
      <h4 class="card-title">{{ item.courseName }}</h4>
      <view class="card-simple-desc">
        <p v-if="item.simpleDesc !== null" >{{item.simpleDesc}}</p>
      </view>
    </view>
    <view class="card-item-bottom-fixed">
      <view class="card-item-favorites">
        <view class="card-item-favorites-item">
          <IconFont font-class-name="iconfont" class-prefix="icon" name='user' color='#AAAAAA' size="18"></IconFont>
          <text class="card-item-favorites-text">{{ memberCount }}</text>
        </view>
        <view class="card-item-favorites-item" @click="handleFavorite">
          <IconFont v-if="favorite" name='star-fill-n' color="#FFC82C"></IconFont>
          <IconFont v-else name='star-n' color="#AAAAAA"></IconFont>
          <text class="card-item-favorites-text">{{ favCount }}</text>
        </view>
      </view>
      <view>
        <a href="#" @click="handleStart" :class="item.chargeType === 0 ? 'charge-free' : 'charge-free charge-no-free'">{{item.chargeType === 0 || item.chargePrice === null ? '免费' : '$' + item.chargePrice}}</a>
      </view>
    </view>
  </view>
</template>

<script lang="ts">
import {IconFont} from '@nutui/icons-vue-taro';
import {defineComponent, ref} from 'vue';
import {transformToArray} from "../../../utils/util";
import api from "../../../api/api";
import examApi from "../../../api/exam.api";
import {successMessage} from "../../../utils/util";

export default defineComponent({
  name: 'CourseItem',
  components: {IconFont},
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
      successMessage(message);
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
</style>