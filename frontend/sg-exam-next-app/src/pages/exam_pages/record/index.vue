<template>
  <view class="exam-record">
    <nut-searchbar v-model="searchValue" @search="handleSearch"
                   placeholder="请输入名称"></nut-searchbar>
    <nut-tabs v-model="current" @change="handleTabClick">
      <nut-tab-pane title="考试">
        <view class="item flex-row" v-for="item in itemList" @click="goToDetails(item)">
          <view class="item-cont">
            <view class="flex-row">
              <text class="item-name">{{ item.examinationName }}</text>
            </view>
            <text class="item-time">{{ item.startTime }}</text>
          </view>
          <view class="item-status">{{ item.submitStatusName }}</view>
        </view>
        <nut-empty v-if="!loading && itemList.length === 0"></nut-empty>
      </nut-tab-pane>
      <nut-tab-pane title="练习">
        <view class="item flex-row" v-for="item in itemList" @click="goToDetails(item)">
          <view class="item-cont">
            <view class="flex-row">
              <text class="item-name">{{ item.examinationName }}</text>
            </view>
            <text class="item-time">{{ item.startTime }}</text>
          </view>
          <view class="item-status">{{ item.submitStatusName }}</view>
        </view>
        <nut-empty v-if="!loading && itemList.length === 0"></nut-empty>
      </nut-tab-pane>
      <nut-tab-pane title="问卷">
        <view class="item flex-row" v-for="item in itemList" @click="goToDetails(item)">
          <view class="item-cont">
            <view class="flex-row">
              <text class="item-name">{{ item.examinationName }}</text>
            </view>
            <text class="item-time">{{ item.startTime }}</text>
          </view>
          <view class="item-status">{{ item.submitStatusName }}</view>
        </view>
        <nut-empty v-if="!loading && itemList.length === 0"></nut-empty>
      </nut-tab-pane>
      <nut-tab-pane title="面试">
        <view class="item flex-row" v-for="item in itemList" @click="goToDetails(item)">
          <view class="item-cont">
            <view class="flex-row">
              <text class="item-name">{{ item.examinationName }}</text>
            </view>
            <text class="item-time">{{ item.startTime }}</text>
          </view>
          <view class="item-status">{{ item.submitStatusName }}</view>
        </view>
        <nut-empty v-if="!loading && itemList.length === 0"></nut-empty>
      </nut-tab-pane>
    </nut-tabs>
  </view>
</template>
<script lang="ts">
import {onMounted, ref, unref} from 'vue';
import api from '../../../api/api';
import recordApi from '../../../api/record.api';
import Taro from '@tarojs/taro';
import {examTypeTagList} from '../../../constant/constant';
import {showNoMoreData, showLoading, hideLoading} from '../../../utils/util';

export default {
  setup() {
    const currentInstance = Taro.getCurrentInstance();
    const params = currentInstance.router?.params;
    let type = 0;
    if (params !== undefined && params.type !== undefined) {
      type = Number(params.type);
    }
    const current = ref<number>(type);
    let itemList = ref<any>([]);
    const loading = ref<boolean>(false);
    const hasNextPageRef = ref<boolean>(true);
    const nextPageRef = ref<number>(1);
    const pageNumRef = ref<number>(1);
    let searchValue = ref<string>('');

    async function fetch(type, examinationName = '', append = true) {
      if (!unref(hasNextPageRef)) {
        await showNoMoreData();
        return;
      }
      if (loading.value) {
        return;
      }
      loading.value = true;
      await showLoading();
      if (!append) {
        itemList.value = [];
      }
      try {
        const {id} = api.getUserInfo();
        const recordsRes = await recordApi.userRecords(id, {type, examinationName});
        const {code, result} = recordsRes
        if (code === 0) {
          const {list, hasNextPage, nextPage, pageNum} = result;
          if (list !== null && list.length > 0) {
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
        hideLoading();
      }
    }

    function goToDetails(item) {
      const recordId = unref(item).id;
      const examinationId = unref(item).examinationId;
      Taro.navigateTo({url: `/pages/exam_pages/answer/index?recordId=${recordId}&examinationId=${examinationId}`});
    }

    function handleTabClick({paneKey}) {
      current.value = paneKey;
      searchValue.value = '';
      resetPage();
      fetch(paneKey);
    }

    function handleSearchChange(value) {
      searchValue.value = value;
    }

    function handleSearch() {
      resetPage();
      fetch(unref(current), unref(searchValue), false);
    }

    function resetPage() {
      hasNextPageRef.value = true;
      nextPageRef.value = 1;
    }

    async function init() {
      try {
        await showLoading();
        resetPage();
        await fetch(unref(current), '', false);
      } finally {
        hideLoading();
      }
    }

    onMounted(() => {
      init();
    });

    return {
      loading,
      searchValue,
      tagsList: examTypeTagList,
      current,
      itemList,
      init,
      goToDetails,
      handleTabClick,
      handleSearchChange,
      handleSearch,
      fetch,
      resetPage
    };
  },
  onPullDownRefresh() {
    try {
      this.init();
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
}
</script>

<style>
.exam-record .nut-tab-pane {
  padding: 0;
}

.flex-row {
  display: flex;
  flex-direction: row;
  align-items: center;
}

.item {
  justify-content: space-between;
  margin: 0 24px;
  border-bottom: 1px solid rgb(235, 237, 240);
  padding: 20px 0;
  position: relative;
}

.item-name {
  color: #323233;
  font-size: 30px;
}

.item-time {
  margin-top: 10px;
  color: #969799;
  font-size: 26px;
}

.item-status {
  color: #969799;
  font-size: 26px;
}

.item-status::after {
  content: " >";
  position: relative;
  padding-left: 4px;
}
</style>