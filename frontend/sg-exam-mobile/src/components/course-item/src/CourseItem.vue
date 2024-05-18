<template>
   <div class="course-item-container">
     <div @click="handleStart">
      <nut-row type="flex" wrap="nowrap">
        <nut-col :span="8">
          <image class="course-item-img" :src="item.imageUrl"></image>
        </nut-col>
        <nut-col :span="16" class="course-item-info">
          <h4 class="card-title">{{ item.courseName }}</h4>
           <div class="card-simple-desc">
            <p v-if="item.simpleDesc !== null" >{{item.simpleDesc}}</p>
          </div>
           <div class="course-item-info-member">
            <text class="card-simple-desc">{{ memberCount }} 人已报名</text>
          </div>
           <div class="course-detail-btn">
            <nut-button type="primary" size="small" @click="handleStart">了解详情</nut-button>
          </div>
        </nut-col>
      </nut-row>
    </div>
  </div>
</template>

<script lang="ts">
import {IconFont} from '@nutui/icons-vue-taro';
import {Row, Col} from '@nutui/nutui-taro';
import {defineComponent, ref} from 'vue';
import {transformToArray} from "../../../utils/util";
import api from "../../../api/api";
import examApi from "../../../api/exam.api";
import {successMessage} from "../../../utils/util";

export default defineComponent({
  name: 'CourseItem',
  components: {IconFont, Row, Col},
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

.course-item-container {
  border-radius: 6px;
  overflow: hidden;
  position: relative;
  margin-top: 20px;
  margin-bottom: 20px;
}

.course-item-img {
  width: 100%;
  height: 160px;
  border-radius: 8px;
  margin: 20px;
}

.course-item-info {
  margin-left: 30px;
  margin-top: 10px;
}

.course-item-info-member {
  position: absolute;
  bottom: 20px;
}

.course-detail-btn {
  position: absolute;
  bottom: 20px;
  right: 20px;
  text-align: center;
  text-transform: uppercase;
  border-radius: 10px;
  padding: 0 10px;
  font-size: 30px;
  color: white;
}
.card-simple-desc {

}
</style>