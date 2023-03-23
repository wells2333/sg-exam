<template>
  <view>
    <view>
      <nut-searchbar v-model="searchValue" @search="handleSearch" placeholder="搜索"/>
    </view>
    <view class="exam-view">
      <nut-tabs v-model="current" @click="handleTabClick">
        <nut-tab-pane title="考试">
          <view class="exam-item box-show-item flex-col" v-for="item in itemList">
            <exam-item :item="item" @start="handleStart"></exam-item>
          </view>
          <nut-empty v-if="!loading && itemList.length === 0"></nut-empty>
        </nut-tab-pane>
        <nut-tab-pane title="练习">
          <view class="exam-item box-show-item flex-col" v-for="item in itemList">
            <exam-item :item="item" @start="handleStart"></exam-item>
          </view>
          <nut-empty v-if="!loading && itemList.length === 0"></nut-empty>
        </nut-tab-pane>
        <nut-tab-pane title="问卷">
          <view class="exam-item box-show-item flex-col" v-for="item in itemList">
            <exam-item :item="item" @start="handleStart"></exam-item>
          </view>
          <nut-empty v-if="!loading && itemList.length === 0"></nut-empty>
        </nut-tab-pane>
        <nut-tab-pane title="面试">
          <view class="exam-item box-show-item flex-col" v-for="item in itemList">
            <exam-item :item="item" @start="handleStart"></exam-item>
          </view>
          <nut-empty v-if="!loading && itemList.length === 0"></nut-empty>
        </nut-tab-pane>
      </nut-tabs>
    </view>
  </view>
</template>
<script lang="ts">
import {onMounted, ref, unref} from 'vue';
import examApi from '../../../api/exam.api';
import Taro from "@tarojs/taro";
import {ExamItem} from '../../../components/exam-item';
import {examTypeTagList, shardMessage} from '../../../constant/constant';
import {hideLoading, showLoading, warnMessage} from "../../../utils/util";

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
    let itemList = ref<any>([]);
    let searchValue = ref<string>("");
    const hasNextPageRef = ref<boolean>(true);
    const nextPageRef = ref<number>(1);
    const loading = ref<boolean>(false);
    const pageNumRef = ref<number>(1);

    async function fetch(type, examinationName = '', append = true) {
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
              itemList.value = [...itemList.value, ...list];
            } else {
              itemList.value = list;
            }
          } else {
            if (append) {
              await warnMessage('无更多数据');
            } else {
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

    function handleStart(item) {
      Taro.navigateTo({url: "/pages/exam_pages/exam_detail/index?id=" + item.id})
    }

    function handleTabClick({paneKey}) {
      const value = Number(paneKey);
      searchValue.value = "";
      Taro.setNavigationBarTitle({title: getTitleName(value)})
      resetPage();
      fetch(value, '', false);
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

    function handleSearch() {
      resetPage();
      fetch(unref(current), unref(searchValue), false);
    }

    function resetPage() {
      hasNextPageRef.value = true;
      nextPageRef.value = 1;
    }

    onMounted(() => {
      fetch(unref(current));
    });

    return {
      loading,
      tagsList: examTypeTagList,
      current,
      searchValue,
      itemList,
      hasNextPageRef,
      nextPageRef,
      pageNumRef,
      handleStart,
      handleTabClick,
      handleSearch,
      fetch,
      resetPage,
    };
  },
  onPullDownRefresh() {
    try {
      this.resetPage();
      this.fetch(unref(this.current), '', false);
    } finally {
      Taro.stopPullDownRefresh();
    }
  },
  onReachBottom() {
    try {
      this.fetch(unref(this.current), '', true);
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