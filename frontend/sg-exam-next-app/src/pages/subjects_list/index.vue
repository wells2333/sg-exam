<template>
  <view v-show="!loading">
    <view class="bg-white">
      <view class="mg-8 home-view-tips">题库信息</view>
      <view v-if="categoryInfo !== undefined">
        <view class='subjects-item-desc'>
          <text>题库名称：{{categoryInfo.categoryName}}</text>
        </view>
        <view class='subjects-item-desc'>
          <text>题目数量：共 {{subjectCnt !== undefined ? subjectCnt : categoryInfo.subjectCnt}} 题</text>
        </view>
        <view class='subjects-item-desc'>
          <text>更新时间：{{categoryInfo.updateTime}}</text>
        </view>
        <view class='subjects-item-desc'>
          <text v-if="categoryInfo.categoryDesc !== null">题库描述：{{categoryInfo.categoryDesc}}</text>
        </view>
      </view>
      <view v-if="categories.length > 0">
        <view class="mg-8 mg-top-10 mg-bottom-10 home-view-tips">子题库</view>
        <view class="mg-8 home-content">
          <view class="home-item gray-border" v-for="(item, idx) in categories" :key="idx" @click="handleClickCate(item)">
            <view class="home-item-left">
              <text>{{item.categoryName}}</text>
              <at-tag class="label" type="primary" size="small">共{{item.subjectCnt}}题</at-tag>
            </view>
            <AtIcon class="home-item-right" value='chevron-right' size='8' color='#346FC2'></AtIcon>
          </view>
        </view>
      </view>
      <view v-if="list.length > 0">
        <view class="mg-8 mg-top-10 mg-bottom-10 home-view-tips">题目列表</view>
        <view class="bg-gray mg-8">
          <view class="bg-white gray-border subject-list-item" v-for="item in list">
            <view class="subject-list-item-label">
              <at-tag type="primary" size="small">{{item.typeLabel}}</at-tag>
              <view>
                <AtIcon class="subject-views" value='eye' size='8' color="#AAAAAA"></AtIcon>
                <text class="subject-views-text">{{item.views}}</text>
                <AtIcon value='star-2' size='8' :color="item.favorite === true ? '#FFC82C': '#AAAAAA'" @click="handleFavSubject(item)"></AtIcon>
              </view>
            </view>
            <view @click="handleClickSubject(item)">
              <view class="subject-list-item-top">
                <view class="subject-title">
                  <wxparse class="subject-title-content" :html="item.subjectName" key={Math.random()}></wxparse>
                </view>
              </view>
              <view class="subject-list-item-bottom">
                <view class="subject-list-item-level color-gray">
                  <text>难度：</text>
                  <at-rate :size="16" :value="item.level"/>
                </view>
              </view>
            </view>
          </view>
        </view>
      </view>
    </view>
    <view>
      <AtActionSheet :isOpened="isOpenedAction" @close="handleCloseAction" title="请选择查看模式" cancelText="取消">
        <AtActionSheetItem @click="handlePreviewAction">预览模式</AtActionSheetItem>
        <AtActionSheetItem @click="handleSortedAction">顺序刷题</AtActionSheetItem>
        <AtActionSheetItem @click="handleRandomAction">随机刷题</AtActionSheetItem>
      </AtActionSheet>
    </view>
  </view>
</template>

<script lang="ts">
import Taro from "@tarojs/taro";
import {onMounted, ref} from 'vue';
import examApi from '../../api/exam.api';
import api from "../../api/api";
import {successMessage, showNoMoreData, showLoading, hideLoading} from "../../utils/util";

export default {
  setup() {
    const params = Taro.getCurrentInstance().router?.params;
    const categoryId = params?.categoryId;
    const subjectCnt = params?.subjectCnt;
    const loading = ref<boolean>(true);
    const list = ref<any>([]);
    const page = ref<number>(1);
    const pageSize = ref<number>(10);
    const hasNextPage = ref<boolean>(true);
    const categoryInfo = ref<any>(undefined);
    const categories = ref<object[]>([]);
    const isOpenedAction = ref<boolean>(false);
    const clickItem = ref<any>(undefined);

    async function fetch(append = false) {
      if (!hasNextPage.value) {
        await showNoMoreData();
        return;
      }
      await showLoading();
      try {
        const res = await examApi.getSubjects({categoryId, page: page.value, pageSize: pageSize.value, findFav: true, findView: true});
        const {code, result} = res;
        hasNextPage.value = result.hasNextPage;
        if (code === 0 && result.list.length > 0) {
          if (append) {
            result.list.forEach(e => {
              list.value.push(e);
            });
          } else {
            list.value = result.list;
          }
        }
      } finally {
       hideLoading();
      }
    }

    async function fetchCategoryInfo() {
      if (categoryId === undefined) {
        return;
      }
      const res = await examApi.getCategoryInfo(categoryId);
      const {code, result} = res;
      if (code === 0 && result !== null) {
        categoryInfo.value = result;
      }
    }

    async function fetchChildCategory() {
      if (categoryId === undefined) {
        return;
      }
      const res = await examApi.getSubjectCntByParentId(categoryId);
      const {code, result} = res;
      if (code === 0 && result !== null && result.length > 0) {
        categories.value = result;
      }
    }

    async function nextPage() {
      page.value = page.value + 1;
      await fetch(true);
    }

    function handleClickCate(item) {
      Taro.navigateTo({url: "/pages/subjects_list/index?categoryId=" + item.id})
    }

    async function init() {
      try {
        loading.value = true;
        resetPage();
        await fetchCategoryInfo();
        await fetchChildCategory();
        await fetch();
      } finally {
        loading.value = false;
      }
    }

    function resetPage() {
      page.value = 1;
      pageSize.value = 10;
      hasNextPage.value = true;
    }

    function handleClickSubject(item) {
      clickItem.value = item;
      isOpenedAction.value = true;
    }

    async function handleFavSubject(item) {
      item.favorite = !item.favorite;
      const type = item.favorite ? '1' : '0';
      const text = item.favorite ? '收藏' : '取消收藏';
      const {id} = await api.getUserInfo();
      const res = await examApi.favoriteSubject(id, item.id, type);
      const {code} = res;
      if (code === 0) {
        await successMessage(text + '成功');
      }
    }

    function handlePreviewAction() {
      Taro.navigateTo({url: "/pages/subjects_detail/index?mode=1&id=" + clickItem.value.id});
      handleCloseAction();
    }

    function handleSortedAction() {
      Taro.navigateTo({url: "/pages/subjects_detail/index?mode=2&id=" + clickItem.value.id});
      handleCloseAction();
    }

    function handleRandomAction() {
      Taro.navigateTo({url: "/pages/subjects_detail/index?mode=3&id=" + clickItem.value.id});
      handleCloseAction();
    }

    function handleCloseAction() {
      isOpenedAction.value = false;
    }

    onMounted(() => {
      init();
    });

    return {
      loading,
      subjectCnt,
      list,
      categoryInfo,
      categories,
      isOpenedAction,
      init,
      nextPage,
      handleClickSubject,
      handleFavSubject,
      handleClickCate,
      handlePreviewAction,
      handleSortedAction,
      handleRandomAction,
      handleCloseAction
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
      this.nextPage();
    } finally {
      Taro.stopPullDownRefresh();
    }
  },
}
</script>

<style>
.subject-list-item {
  margin-bottom: 12px;
  padding: 6px;
  border-radius: 10px;
}

.subject-list-item-top, .subject-list-item-bottom {
  display: flex;
  justify-content: space-between;
}

.subject-list-item-label {
  display: flex;
  justify-content: space-between;
}

.subject-list-item-level {
  margin-bottom: 6px;
  display: inline-flex;
  font-size: 12px;
}

.home-content {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
}
.home-item {
  border-radius: 12px;
  margin-bottom: 10px;
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

.subjects-item-desc {
  font-size: 14px;
  color: gray;
  margin-left: 12px;
}
</style>