<template>
  <AtMessage/>
  <view>
    <view class="exam-item box-show-item flex-col" v-for="item in exams">
      <exam-item :item="item"></exam-item>
    </view>
  </view>
</template>

<script lang="ts">
import {onMounted, ref, unref} from 'vue';
import examApi from '../../api/exam.api';
import {ExamItem} from '../../components/exam-item';
import api from "../../api/api";
import {filterLogin} from "../../utils/filter";
import Taro from "@tarojs/taro";

export default {
  components: {
    'exam-item': ExamItem
  },
  onPullDownRefresh() {

  },
  onReachBottom() {
  },
  setup() {
    let exams = ref<any>([]);
    const hasNextPageRef = ref<boolean>(true);
    const nextPageRef = ref<number>(1);
    const loadMoreStatus = ref<string>('more');
    const pageNumRef = ref<number>(1);
    async function fetch() {
      if (!unref(hasNextPageRef)) {
        loadMoreStatus.value = 'noMore';
        return;
      }
      if (loadMoreStatus.value === 'loading') {
        return;
      }
      loadMoreStatus.value = 'loading';
      try {
        const {id} = api.getUserInfo();
        const res = await examApi.userFavorites({
          userId: id,
          page: unref(nextPageRef)
        });
        const {code, result} = res
        if (code === 0) {
          const {list, hasNextPage, nextPage, pageNum} = result;
          exams.value = [...exams.value, ...list];
          hasNextPageRef.value = hasNextPage;
          nextPageRef.value = nextPage;
          pageNumRef.value = pageNum;
        }
      } finally {
        loadMoreStatus.value = hasNextPageRef.value ? 'more' : 'noMore';
      }
    }

    onMounted(() => {
      fetch();
    });
    return {
      exams,
      loadMoreStatus,
      pageNumRef
    }
  }
}
</script>

<style>
.flex-col {
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.exam-item {
  margin: 8px 10px 8px 10px;
  background: white;
  border-radius: 6px;
  overflow: hidden;
  position: relative;
}
</style>