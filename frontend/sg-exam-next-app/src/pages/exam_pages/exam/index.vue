<template>
  <view>
    <nut-searchbar v-model="searchValue" @action-click="handleSearch" placeholder="搜索"/>
  </view>
  <view class="exam-view">
    <nut-tabs v-model="current" @click="handleTabClick">
      <nut-tab-pane title="考试">
        <view class="exam-item box-show-item flex-col" v-for="item in exams">
          <exam-item :item="item" @start="handleStart"></exam-item>
        </view>
      </nut-tab-pane>
      <nut-tab-pane title="练习">
        <view class="exam-item box-show-item flex-col" v-for="item in exams">
          <exam-item :item="item" @start="handleStart"></exam-item>
        </view>
      </nut-tab-pane>
      <nut-tab-pane title="问卷">
        <view class="exam-item box-show-item flex-col" v-for="item in exams">
          <exam-item :item="item" @start="handleStart"></exam-item>
        </view>
      </nut-tab-pane>
      <nut-tab-pane title="面试">
        <view class="exam-item box-show-item flex-col" v-for="item in exams">
          <exam-item :item="item" @start="handleStart"></exam-item>
        </view>
      </nut-tab-pane>
    </nut-tabs>
  </view>
</template>
<script lang="ts">
import {onMounted, ref, unref} from 'vue';
import examApi from '../../../api/exam.api';
import Taro from "@tarojs/taro";
import {ExamItem} from '../../../components/exam-item';
import {examTypeTagList, shardMessage} from '../../../constant/constant';

export default {
  components: {
    'exam-item': ExamItem
  },
  setup() {
    const currentInstance = Taro.getCurrentInstance();
    const params = currentInstance.router.params;
    let active = 0;
    if (params.active !== undefined) {
      active = Number(params.active);
      Taro.setNavigationBarTitle({title: getTitleName(active)})
    }
    const current = ref<number>(active);
    let exams = ref<any>([]);
    let searchValue = ref<string>("");
    const hasNextPageRef = ref<boolean>(true);
    const nextPageRef = ref<number>(1);
    const loading = ref<boolean>(false);
    const pageNumRef = ref<number>(1);

    async function fetch(type, examinationName = "", append = true) {
      if (!unref(hasNextPageRef)) {
        return;
      }
      if (loading.value) {
        return;
      }
      loading.value = true;
      try {
        // 查询启用的考试
        const examinationList = await examApi.examinationList({
          type,
          examinationName,
          status: 1,
          page: unref(nextPageRef),
          favorite: '1'
        });
        const {code, result} = examinationList
        if (code === 0) {
          const {list, hasNextPage, nextPage, pageNum} = result;
          if (list != null && list.length > 0) {
            if (append) {
              exams.value = [...exams.value, ...list];
            } else {
              exams.value = list;
            }
          }
          hasNextPageRef.value = hasNextPage;
          nextPageRef.value = nextPage;
          pageNumRef.value = pageNum;
        }
      } finally {
        loading.value = false;
      }
    }

    function handleStart(item) {
      Taro.navigateTo({url: "/pages/exam_pages/exam_detail/index?id=" + item.id})
    }

    function handleTabClick({paneKey}) {
      const value = Number(paneKey);
      searchValue.value = "";
      Taro.setNavigationBarTitle({title: getTitleName(value)})
      resetList();
      resetPage();
      fetch(value);
    }

    function getTitleName(value) {
      let title = '考试';
      if (value === 1) {
        title = '练习';
      } else if (value === 2) {
        title = '问卷';
      } else if (value === 3) {
        title = '面试';
      }
      return title + '列表';
    }

    function handleSearchChange(value) {
      searchValue.value = value;
      if (value === '') {
        fetch(unref(current));
      }
    }

    function handleSearch() {
      resetList();
      resetPage();
      fetch(unref(current), unref(searchValue), false);
    }

    function resetPage() {
      hasNextPageRef.value = true;
      nextPageRef.value = 1;
    }

    function resetList() {
      exams.value = [];
    }

    function showEmptyList() {
      return !loading && (exams === undefined || exams === null || exams.length === 0);
    }

    onMounted(() => {
      fetch(unref(current));
    });

    return {
      loading,
      tagsList: examTypeTagList,
      current,
      searchValue,
      exams,
      hasNextPageRef,
      nextPageRef,
      pageNumRef,
      handleStart,
      handleTabClick,
      handleSearchChange,
      handleSearch,
      fetch,
      resetList,
      resetPage,
      showEmptyList
    };
  },
  onPullDownRefresh() {
    try {
      this.resetPage();
      this.resetList();
      this.fetch(unref(this.current), '', false);
    } finally {
      Taro.stopPullDownRefresh();
    }
  },
  onReachBottom() {
    try {
      this.fetch(unref(this.current));
    } finally {
      Taro.stopPullDownRefresh();
    }
  },
  onShareAppMessage() {
    return shardMessage;
  },
  async onShareTimeline() {
    return shardMessage;
  }
}
</script>

<style>
.exam-view .nut-tab-pane {
  padding: 0;
}

.flex-col {
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.exam-item {
  margin: 26px 22px 26px 22px;
  background: white;
  border-radius: 6px;
  overflow: hidden;
  position: relative;
}
</style>