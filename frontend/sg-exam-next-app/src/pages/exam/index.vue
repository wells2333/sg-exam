<template>
  <AtMessage/>
  <view>
    <at-search-bar :value="searchValue" @action-click="handleSearch" @change="handleSearchChange" placeholder="搜索"/>
  </view>
  <view class="exam-view">
    <at-tabs :tab-list="tagsList" :current="current" @click="handleTabClick" color="rgb(7, 193, 96)">
      <at-tabs-pane title="考试">
        <view class="exam-item box-show-item flex-col" v-for="item in exams">
          <exam-item :item="item" @start="handleStart"></exam-item>
        </view>
      </at-tabs-pane>
      <at-tabs-pane title="练习">
        <view class="exam-item box-show-item flex-col" v-for="item in exams">
          <exam-item :item="item" @start="handleStart"></exam-item>
        </view>
      </at-tabs-pane>
      <at-tabs-pane title="问卷">
        <view class="exam-item box-show-item flex-col" v-for="item in exams">
          <exam-item :item="item" @start="handleStart"></exam-item>
        </view>
      </at-tabs-pane>
      <at-tabs-pane title="面试">
        <view class="exam-item box-show-item flex-col" v-for="item in exams">
          <exam-item :item="item" @start="handleStart"></exam-item>
        </view>
      </at-tabs-pane>
    </at-tabs>
    <at-load-more v-if="pageNumRef !== 1 || (pageNumRef === 1 && exams.length === 0)" :status="loadMoreStatus"
                  moreBtnStyle="size=5px;"></at-load-more>
  </view>
</template>
<script lang="ts">
import {onMounted, ref, unref} from 'vue';
import examApi from '../../api/exam.api';
import Taro from "@tarojs/taro";
import {ExamItem} from '../../components/exam-item';
import {examTypeTagList, shardMessage} from '../../constant/constant';

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
    // 当前切换的tab
    const current = ref<number>(active);
    let exams = ref<any>([]);
    let searchValue = ref<string>("");
    const hasNextPageRef = ref<boolean>(true);
    const nextPageRef = ref<number>(1);
    const loadMoreStatus = ref<string>('more');
    const pageNumRef = ref<number>(1);

    async function fetch(type, examinationName = "", append = true) {
      if (!unref(hasNextPageRef)) {
        loadMoreStatus.value = 'noMore';
        return;
      }
      if (loadMoreStatus.value === 'loading') {
        return;
      }
      loadMoreStatus.value = 'loading';
      try {
        // 查询启用的考试
        const examinationList = await examApi.examinationList({
          type,
          examinationName,
          status: 0,
          page: unref(nextPageRef),
          favorite: '1'
        });
        const {code, result} = examinationList
        if (code === 0) {
          const {list, hasNextPage, nextPage, pageNum} = result;
          if (append) {
            exams.value = [...exams.value, ...list];
          } else {
            exams.value = list;
          }
          hasNextPageRef.value = hasNextPage;
          nextPageRef.value = nextPage;
          pageNumRef.value = pageNum;
        }
      } finally {
        loadMoreStatus.value = hasNextPageRef.value ? 'more' : 'noMore';
      }
    }

    function handleStart(item) {
      Taro.navigateTo({url: "/pages/exam_detail/index?id=" + item.id})
    }

    function handleTabClick(value) {
      current.value = value;
      searchValue.value = "";
      Taro.setNavigationBarTitle({title: getTitleName(value)})
      resetList();
      resetPage();
      resetLoadMoreStatus();
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

    function resetLoadMoreStatus() {
      loadMoreStatus.value = 'more';
    }

    onMounted(() => {
      fetch(unref(current));
    });

    return {
      loadMoreStatus,
      tagsList: examTypeTagList,
      current,
      searchValue,
      handleStart,
      handleTabClick,
      handleSearchChange,
      handleSearch,
      fetch,
      resetList,
      resetPage,
      resetLoadMoreStatus,
      exams,
      hasNextPageRef,
      nextPageRef,
      pageNumRef
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