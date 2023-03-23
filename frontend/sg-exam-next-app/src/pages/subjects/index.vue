<template>
  <view class="home-view">
    <view class="home-content" v-if="gridData.length > 0">
      <view class="home-item mt-22" v-for="(item, idx) in gridData" :key="idx" @click="handleClickCate(item)">
        <view class="home-item-left">
          <text>{{item.value}}</text>
          <nut-tag class="label" size="small" plain round>共 {{item.subjectCnt}} 题</nut-tag>
        </view>
        <IconFont class="home-item-right" name="rect-right" color="#346FC2"></IconFont>
      </view>
    </view>
  </view>
</template>

<script lang="ts">
import {IconFont} from '@nutui/icons-vue-taro';
import Taro from "@tarojs/taro";
import {onMounted, ref} from 'vue';
import examApi from '../../api/exam.api';
import operationApi from '../../api/operation.api';
import {showLoading, hideLoading} from '../../utils/util';

export default {
  components: {
    IconFont
  },
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
      if (code === 0 && result !== null) {
        tree.value = result;
        gridData.value = [];
        if (result.length > 0) {
          result.forEach(e => {
            gridData.value.push({
              id: e.id,
              subjectCnt: e.subjectCnt,
              value: e.categoryName
            });
          });
        }
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
      Taro.navigateTo({url: "/pages/exam_pages/subjects_list/index?categoryId=" + item.id + "&subjectCnt=" + item.subjectCnt})
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

</style>