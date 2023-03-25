<template>
  <view>
    <nut-searchbar v-model="searchValue" @search="handleSearch" placeholder="搜索"/>
    <view class="ml-22 mr-22">
      <view class="box-show-item mb-bottom-20" v-for="course in itemList">
        <course-item :item="course" @click="handleClickCourse(course)"></course-item>
      </view>
      <nut-empty v-if="!loading && itemList.length === 0"></nut-empty>
    </view>
  </view>
</template>

<script lang="ts">
import Taro from '@tarojs/taro';
import {onMounted, ref, unref} from 'vue';
import examApi from '../../../api/exam.api';
import {CourseItem} from '../../../components/course-item';
import {showLoading, hideLoading} from '../../../utils/util';

export default {
  components: {
    'course-item': CourseItem
  },
  setup() {
    let searchValue = ref<string>('');
    const itemList = ref<any>([]);
    const hasNextPageRef = ref<boolean>(true);
    const nextPageRef = ref<number>(1);
    const loading = ref<boolean>(false);
    const pageNumRef = ref<number>(1);

    async function fetch(courseName: string = '', append = true) {
      if (!unref(hasNextPageRef)) {
        return;
      }
      if (loading.value) {
        return;
      }
      loading.value = true;
      if (!append) {
        itemList.value = [];
      }
      await showLoading();
      try {
        const res = await examApi.courseList({
          courseName,
          status: 1,
          page: unref(nextPageRef),
        });
        const {code, result} = res;
        if (code === 0) {
          const {list, hasNextPage, nextPage, pageNum} = result;
          if (list != null && list.length > 0) {
            if (append) {
              itemList.value = [...itemList.value, ...list];
            } else {
              itemList.value = list;
            }
          } else {
            if (!append) {
              itemList.value = [];
            }
          }
          hasNextPageRef.value = hasNextPage;
          nextPageRef.value = nextPage;
          pageNumRef.value = pageNum;
        }
      } finally {
        loading.value = false;
        await hideLoading();
      }
    }

    function handleSearch() {
      resetPage();
      fetch(searchValue.value, false);
    }

    function resetPage() {
      hasNextPageRef.value = true;
      nextPageRef.value = 1;
    }

    function handleClickCourse(course) {
      Taro.navigateTo({url: "/pages/exam_pages/course_detail/index?courseId=" + course.id})
    }

    async function init() {
      try {
        await showLoading();
        await fetch();
      } finally {
        hideLoading();
      }
    }

    onMounted(() => {
      init();
    });

    return {
      searchValue,
      itemList,
      loading,
      init,
      fetch,
      handleSearch,
      resetPage,
      handleClickCourse
    }
  },
  onPullDownRefresh() {
    try {
      this.resetPage();
      this.fetch('', false);
    } finally {
      Taro.stopPullDownRefresh();
    }
  },
  onReachBottom() {
    try {
      this.fetch('', true);
    } finally {
      Taro.stopPullDownRefresh();
    }
  },
}
</script>

<style>

</style>