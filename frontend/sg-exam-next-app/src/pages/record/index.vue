<template>
  <view>
    <at-search-bar :value="searchValue" @action-click="handleSearch" @change="handleSearchChange"
                   placeholder="请输入名称"></at-search-bar>
    <at-tabs :tab-list="tagsList" :current="current" @click="handleTabClick" color="rgb(7, 193, 96)">
      <at-tabs-pane title="考试">
        <view class="item flex-row" v-for="item in records" @click="goToDetails($event, item)">
          <view class="item-cont">
            <view class="flex-row">
              <text class="item-name">{{ item.examinationName }}</text>
              <at-tag :circle="true" size="small" type="primary" class="item-type">{{ item.typeLabel }}</at-tag>
            </view>
            <text class="item-time">{{ item.startTime }}</text>
          </view>
          <view class="item-status">{{ item.submitStatusName }}</view>
        </view>
        <view v-if="!loading && (records === null || records.length === 0)">
          <at-load-more status="noMore"></at-load-more>
        </view>
      </at-tabs-pane>
      <at-tabs-pane title="练习">
        <view class="item flex-row" v-for="item in records" @click="goToDetails($event, item)">
          <view class="item-cont">
            <view class="flex-row">
              <text class="item-name">{{ item.examinationName }}</text>
              <at-tag :circle="true" size="small" type="primary" class="item-type">{{ item.typeLabel }}</at-tag>
            </view>
            <text class="item-time">{{ item.startTime }}</text>
          </view>
          <view class="item-status">{{ item.submitStatusName }}</view>
        </view>
        <view v-if="!loading && (records === null || records.length === 0)">
          <at-load-more status="noMore"></at-load-more>
        </view>
      </at-tabs-pane>
      <at-tabs-pane title="问卷">
        <view class="item flex-row" v-for="item in records" @click="goToDetails($event, item)">
          <view class="item-cont">
            <view class="flex-row">
              <text class="item-name">{{ item.examinationName }}</text>
              <at-tag :circle="true" size="small" type="primary" class="item-type">{{ item.typeLabel }}</at-tag>
            </view>
            <text class="item-time">{{ item.startTime }}</text>
          </view>
          <view class="item-status">{{ item.submitStatusName }}</view>
        </view>
        <view v-if="!loading && (records === null || records.length === 0)">
          <at-load-more status="noMore"></at-load-more>
        </view>
      </at-tabs-pane>
      <at-tabs-pane title="面试">
        <view class="item flex-row" v-for="item in records" @click="goToDetails($event, item)">
          <view class="item-cont">
            <view class="flex-row">
              <text class="item-name">{{ item.examinationName }}</text>
              <at-tag :circle="true" size="small" type="primary" class="item-type">{{ item.typeLabel }}</at-tag>
            </view>
            <text class="item-time">{{ item.startTime }}</text>
          </view>
          <view class="item-status">{{ item.submitStatusName }}</view>
        </view>
        <view v-if="!loading && (records === null || records.length === 0)">
          <at-load-more status="noMore"></at-load-more>
        </view>
      </at-tabs-pane>
    </at-tabs>
  </view>
</template>
<script lang="ts">
import {onMounted, ref, unref} from 'vue';
import api from "../../api/api";
import recordApi from '../../api/record.api';
import Taro from '@tarojs/taro';
import {examTypeTagList} from '../../constant/constant';

export default {
  setup() {
    const current = ref<number>(0);
    let records = ref<any>([]);
    const loading = ref<boolean>(false);
    const hasNextPageRef = ref<boolean>(true);
    const nextPageRef = ref<number>(1);
    let searchValue = ref<string>("");

    async function fetch(type, examinationName = "", append = true) {
      if (!unref(hasNextPageRef)) {
        Taro.showToast({title: '无更多数据', icon: 'none', duration: 1500});
        return;
      }
      if (loading.value) {
        return;
      }
      const {id} = api.getUserInfo();
      loading.value = true;
      Taro.showLoading({title: '加载中'})
      try {
        const recordsRes = await recordApi.userRecords(id, {type});
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
        Taro.hideLoading();
        loading.value = false;
      }
    }

    onMounted(() => {
      fetch(unref(current));
    });

    function goToDetails($event, item) {
      const recordId = unref(item).id;
      const examinationId = unref(item).examinationId;
      Taro.navigateTo({url: `/pages/answer/index?recordId=${recordId}&examinationId=${examinationId}`});
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

    function handleTabClick(value) {
      current.value = value;
      searchValue.value = "";
      resetList();
      resetPage();
      fetch(value);
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

    return {
      loading,
      searchValue,
      tagsList: examTypeTagList,
      current,
      records,
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
      this.resetPage();
      this.fetch(unref(this.current), "", false);
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
.flex-col {
  display: flex;
  flex-direction: column;
  justify-content: center;
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
  font-size: 16px;
}

.item-type {
  font-size: 14px;
  margin-left: 5px;
}

.item-time {
  margin-top: 3px;
  color: #969799;
  font-size: 14px;
}

.item-status {
  color: #969799;
  font-size: 14px;
}

.item-status::after {
  content: ">";
  position: relative;
  padding-left: 4px;
}

.at-tag--primary{
  color: #78A4F4;
  border-color: #78A4F4;
  background-color: #FFF;
}

</style>