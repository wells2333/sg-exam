<template>
  <view class="exam-record">
    <nut-searchbar v-model="searchValue" @action-click="handleSearch"
                   placeholder="请输入名称"></nut-searchbar>
    <nut-tabs v-model="current" @change="handleTabClick">
      <nut-tab-pane title="考试">
        <view class="mt-22">
          <view class="item flex-row" v-for="item in records" @click="goToDetails($event, item)">
            <view class="item-cont">
              <view class="flex-row">
                <text class="item-name">{{ item.examinationName }}</text>
              </view>
              <text class="item-time">{{ item.startTime }}</text>
            </view>
            <view class="item-status">{{ item.submitStatusName }}</view>
          </view>
        </view>
      </nut-tab-pane>
      <nut-tab-pane title="练习">
        <view class="mt-22">
          <view class="item flex-row" v-for="item in records" @click="goToDetails($event, item)">
            <view class="item-cont">
              <view class="flex-row">
                <text class="item-name">{{ item.examinationName }}</text>
              </view>
              <text class="item-time">{{ item.startTime }}</text>
            </view>
            <view class="item-status">{{ item.submitStatusName }}</view>
          </view>
        </view>
      </nut-tab-pane>
      <nut-tab-pane title="问卷">
        <view class="mt-22">
          <view class="item flex-row" v-for="item in records" @click="goToDetails($event, item)">
            <view class="item-cont">
              <view class="flex-row">
                <text class="item-name">{{ item.examinationName }}</text>
              </view>
              <text class="item-time">{{ item.startTime }}</text>
            </view>
            <view class="item-status">{{ item.submitStatusName }}</view>
          </view>
        </view>
      </nut-tab-pane>
      <nut-tab-pane title="面试">
        <view class="mt-22">
          <view class="item flex-row" v-for="item in records" @click="goToDetails($event, item)">
            <view class="item-cont">
              <view class="flex-row">
                <text class="item-name">{{ item.examinationName }}</text>
              </view>
              <text class="item-time">{{ item.startTime }}</text>
            </view>
            <view class="item-status">{{ item.submitStatusName }}</view>
          </view>
        </view>
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
    let records = ref<any>([]);
    const loading = ref<boolean>(false);
    const hasNextPageRef = ref<boolean>(true);
    const nextPageRef = ref<number>(1);
    let searchValue = ref<string>("");

    async function fetch(type, examinationName = "", append = true) {
      if (!unref(hasNextPageRef)) {
        await showNoMoreData();
        return;
      }
      if (loading.value) {
        return;
      }
      const {id} = api.getUserInfo();
      loading.value = true;
      await showLoading();
      try {
        const recordsRes = await recordApi.userRecords(id, {type, examinationName});
        const {code, result} = recordsRes
        if (code === 0) {
          const {list, hasNextPage, nextPage} = result;
          if (append) {
            records.value = [...records.value, ...list];
          } else {
            records.value = list;
          }
          hasNextPageRef.value = hasNextPage;
          nextPageRef.value = nextPage;
        }
      } finally {
        hideLoading();
        loading.value = false;
      }
    }

    function goToDetails($event, item) {
      const recordId = unref(item).id;
      const examinationId = unref(item).examinationId;
      Taro.navigateTo({url: `/pages/exam_pages/answer/index?recordId=${recordId}&examinationId=${examinationId}`});
    }

    function getTypeTag(item) {
      if (item.type === 0) {
        return 'success';
      } else if (item.type === 1) {
        return 'primary';
      } else if (item.type === 2) {
        return 'warning';
      } else if (item.type === 3) {
        return 'danger';
      }
      return 'success';
    }

    function handleTabClick({paneKey}) {
      current.value = paneKey;
      searchValue.value = "";
      resetList();
      resetPage();
      fetch(paneKey);
    }

    function handleSearchChange(value) {
      searchValue.value = value;
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
      records.value = [];
    }

    async function init() {
      try {
        await showLoading();
        resetPage();
        await fetch(unref(current), "", false);
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
      records,
      init,
      goToDetails,
      getTypeTag,
      handleTabClick,
      handleSearchChange,
      handleSearch,
      fetch,
      resetPage,
      resetList
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
  padding: 10px 0;
  position: relative;
}

.item-name {
  color: #323233;
  font-size: 30px;
}

.item-time {
  margin-top: 3px;
  color: #969799;
  font-size: 26px;
}

.item-status {
  color: #969799;
  font-size: 24px;
}

.item-status::after {
  content: " >";
  position: relative;
  padding-left: 4px;
}
</style>