<template>
   <div class="exam-item-container">
     <div @click="handleStart">
      <nut-row type="flex" wrap="nowrap">
        <nut-col :span="8">
          <image class="exam-item-img" :src="item.imageUrl"></image>
        </nut-col>
        <nut-col :span="16" class="exam-item-info">
          <h4 class="card-title">{{ simpleStrFilter(item.examinationName) }}</h4>
           <div class="card-simple-desc">
            <p v-if="item.tags !== null" >{{item.tags}}</p>
          </div>
           <div class="course-item-info-member">
            <text class="card-simple-desc">{{ startCount }} 人已参加</text>
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
import {defineComponent, ref, computed} from 'vue';
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

    const simpleStrFilter = (str, length) => {
      if (length === undefined) {
        length = 15
      }
      if (str === null || str === undefined) {
        return ''
      }

      if (str.length > length) {
        return str.substring(0, length) + '...'
      }
      return str
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
      handleStart,
      simpleStrFilter
    }
  }
})
</script>

<style>
.exam-item-container {
  border-radius: 6px;
  overflow: hidden;
  position: relative;
  margin-top: 20px;
  margin-bottom: 20px;
}

.exam-item-img {
  width: 100%;
  height: 160px;
  border-radius: 8px;
  margin: 20px;
}

.exam-item-info {
  margin-left: 30px;
  margin-top: 10px;
}
</style>