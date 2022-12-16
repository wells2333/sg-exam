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
          <at-icon value='star-2' size='10' color='#AAAAAA'></at-icon>
          <text class="course-item-favorites-text">{{ memberCount }}</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script lang="ts">
import {defineComponent, ref} from 'vue';
import {transformToArray} from "../../../utils/util";

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
    if (props.item.tags !== null && props.item.tags !== '') {
      tags.value = transformToArray(props.item.tags);
    }
    if (props.item.memberCount !== undefined && props.item.memberCount > 0) {
      memberCount.value = props.item.memberCount;
    }
    if (props.item.favorite !== undefined && props.item.favorite) {
      favorite.value = props.item.favorite;
    }

    function handleStart() {
      emit('click', props.item);
    }

    return {
      tags,
      memberCount,
      favorite,
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
.course-item-container {
  border-radius: 6px;
  overflow: hidden;
  position: relative;
}
</style>