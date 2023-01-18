<template>
  <AtMessage/>
  <view v-show="!loading">
    <view class="mg-6 home-view-tips">题库信息</view>
    <view v-if="categoryInfo !== undefined">
      <view class='at-article__section'>
        <text class="at-article__h3">题库名称：{{categoryInfo.categoryName}}</text>
      </view>
      <view class='at-article__section'>
        <text class="at-article__h3">题目数量：</text>
        <at-tag class="label" type="primary" size="small">共{{categoryInfo.subjectCnt}}题</at-tag>
      </view>
      <view class='at-article__section'>
        <text class="at-article__h3">更新时间：{{categoryInfo.updateTime}}</text>
      </view>
      <view class='at-article__section'>
        <text class="at-article__p" v-if="categoryInfo.categoryDesc !== null">题库描述：{{categoryInfo.categoryDesc}}</text>
      </view>
    </view>
    <view v-if="categories.length > 0">
      <view class="mg-6 mg-top-10 mg-bottom-10 home-view-tips">子题库</view>
      <view class="mg-6 home-content">
        <view class="home-item" v-for="(item, idx) in categories" :key="idx" @click="handleClickCate(item)">
          <view class="home-item-left">
            <text>{{item.categoryName}}</text>
            <at-tag class="label" type="primary" size="small">共{{item.subjectCnt}}题</at-tag>
          </view>
          <AtIcon class="home-item-right" value='chevron-right' size='8' color='#346FC2'></AtIcon>
        </view>
      </view>
    </view>
    <view v-if="list.length > 0">
      <view class="mg-6 mg-top-10 mg-bottom-10 home-view-tips">题目列表</view>
      <view class="mg-6">
        <view class="subject-list-item" v-for="item in list">
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
              <view class="subject-list-item-name">
                <h4>{{item.subjectName}}</h4>
              </view>
            </view>
            <view class="subject-list-item-bottom">
              <view class="subject-list-item-level">
                <text>难度：</text>
                <at-rate :size="16" :value="item.level"/>
              </view>
            </view>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script lang="ts">
import Taro from "@tarojs/taro";
import {onMounted, ref} from 'vue';
import examApi from '../../api/exam.api';
import api from "../../api/api";
import {successMessage} from "../../utils/util";

export default {
  setup() {
    const params = Taro.getCurrentInstance().router?.params;
    const categoryId = params?.categoryId;
    const loading = ref<boolean>(true);
    const list = ref<any>([]);
    const page = ref<number>(1);
    const pageSize = ref<number>(10);
    const hasNextPage = ref<boolean>(true);
    const categoryInfo = ref<any>(undefined);
    const categories = ref<object[]>([]);

    async function fetch(append = false) {
      if (!hasNextPage.value) {
        successMessage('无更多数据');
        return;
      }
      await Taro.showLoading();
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
       Taro.hideLoading();
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
      Taro.navigateTo({url: "/pages/subjects_detail/index?id=" + item.id})
    }

    async function handleFavSubject(item) {
      item.favorite = !item.favorite;
      const type = item.favorite ? '1' : '0';
      const text = item.favorite ? '收藏' : '取消收藏';
      const {id} = await api.getUserInfo();
      const res = await examApi.favoriteSubject(id, item.id, type);
      const {code} = res;
      if (code === 0) {
        successMessage(text + '成功');
      }
    }

    onMounted(() => {
      init();
    });

    return {
      loading,
      list,
      categoryInfo,
      categories,
      init,
      nextPage,
      handleClickSubject,
      handleFavSubject,
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
      this.nextPage();
    } finally {
      Taro.stopPullDownRefresh();
    }
  },
}
</script>

<style>
.subject-list-item {
  margin-bottom: 16px;
  padding: 6px;
}

.subject-list-item:nth-child(odd) {
  background-color: #FAFBFC;
}

.subject-list-item-top, .subject-list-item-bottom {
  display: flex;
  justify-content: space-between;
}

.subject-list-item-label {
  display: flex;
  justify-content: space-between;
  margin-bottom: 6px;
}

.subject-list-item-level {
  margin-top: 10px;
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
  border: 1px solid #e2e2e2;
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
.subject-views {
  margin-right: 10px;
}

.subject-views-text {
  margin-right: 10px;
  font-size: 12px;
  color: #AAAAAA;
}
</style>