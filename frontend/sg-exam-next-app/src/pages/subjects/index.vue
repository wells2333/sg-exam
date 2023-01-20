<template>
  <view>
    <view class="banner-swiper-view">
      <swiper
          class='banner-swiper'
          indicatorColor='#999'
          indicatorActiveColor='#333'
          current='current'
          duration='500'
          interval='5000'
          circular='false'
          autoplay='false'
          indicatorDots='true'>
        <swiper-item v-for="(item, idx) in banners" :key="idx">
          <view @click="handleClickBanner(item)">
            <image :src="item.imageUrl" class="banner-swiper-image"/>
          </view>
        </swiper-item>
      </swiper>
    </view>
    <view class="home-view">
      <view class="home-view-tips">热门题库</view>
      <view class="home-content">
        <view class="home-item gray-border" v-for="(item, idx) in gridData" :key="idx" @click="handleClickCate(item)">
          <view class="home-item-left">
            <text>{{item.value}}</text>
            <at-tag class="label" type="primary" size="small">共 {{item.subjectCnt}} 题</at-tag>
          </view>
          <AtIcon class="home-item-right" value='chevron-right' size='8' color='#346FC2'></AtIcon>
        </view>
      </view>
    </view>
  </view>
</template>

<script lang="ts">
import Taro from "@tarojs/taro";
import {onMounted, ref} from 'vue';
import examApi from '../../api/exam.api';
import operationApi from "../../api/operation.api";
import {showLoading, hideLoading} from "../../utils/util";

export default {
  setup() {
    const banners = ref<any>([]);
    const tree = ref<any>([]);
    const gridData = ref<any>([]);

    async function fetchBanners() {
      banners.value = [];
      const res = await operationApi.bannerList({});
      const {code, result} = res;
      if (code == 0 && result && result.list && result.list.length > 0) {
        banners.value = [...result.list];
      }
    }

    async function fetchCategoryTree() {
      const res = await examApi.categoryTreeWithSubjectCnt();
      const {code, result} = res;
      if (code === 0 && result !== null && result.length > 0) {
        tree.value = result;
        gridData.value = [];
        result.forEach(e => {
          gridData.value.push({
            id: e.id,
            subjectCnt: e.subjectCnt,
            value: e.categoryName
          });
        });
      }
    }

    function handleClickBanner(item) {
      const {redirectUrl} = item;
      if (redirectUrl === null || redirectUrl === '') {
        return;
      }
      if (redirectUrl.startsWith('http')) {
        Taro.navigateTo({url: "/pages/webview/index?url=" + redirectUrl})
      } else {
        Taro.navigateTo({url: redirectUrl});
      }
    }

    async function init() {
      try {
        await showLoading();
        await fetchBanners();
        await fetchCategoryTree();
      } finally {
        hideLoading();
      }
    }

    function handleClickCate(item) {
      Taro.navigateTo({url: "/pages/subjects_list/index?categoryId=" + item.id})
    }

    onMounted(() => {
      init();
    });

    return {
      banners,
      gridData,
      init,
      handleClickBanner,
      handleClickCate
    }
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
    } finally {
      Taro.stopPullDownRefresh();
    }
  },
}
</script>

<style>
.home-content {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
}
.home-item {
  border-radius: 12px;
  margin-bottom: 20px;
  padding-top: 6px;
  padding-bottom: 6px;
  padding-right: 12px;
  width: 170px;
  flex-shrink: 0;
  box-sizing: border-box;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
}
.home-item:nth-child(odd) {
  margin-right: 15px;
}

.home-item-left {
  color: black;
  display: flex;
  flex-direction: column;
  padding-left: 22px;
}
.home-item-left .label {
  margin-top: 4px;
  margin-bottom: 4px;
}
.home-item-right {

}

</style>