<template>
  <view v-show="!loading" class="subject-list-page">
    <view class="bg-white ml-22 mr-22">
      <view v-if="categories.length > 0">
        <view class="home-view-tips">子题库</view>
        <view class="home-content">
          <view class="home-item mt-22" v-for="(item, idx) in categories" :key="idx"
                @click="handleClickCate(item)">
            <view class="home-item-left">
              <text>{{ item.categoryName }}</text>
              <nut-tag class="label" size="small" plain round>
                共{{ item.subjectCnt }}题
              </nut-tag>
            </view>
            <IconFont class="home-item-right" name="rect-right" color="#346FC2"></IconFont>
          </view>
        </view>
      </view>
      <view v-if="list.length > 0">
        <view class="bg-white subject-list-item box-show" v-for="item in list">
          <view class="subject-list-item-label">
            <nut-tag type="success" size="small">{{ item.typeLabel }}</nut-tag>
            <view class="subject-list-item-right">
              <IconFont font-class-name="iconfont" class-prefix="icon" name='eye' color='#AAAAAA' size="18"></IconFont>
              <text class="subject-views-text">{{ item.views }}</text>
              <IconFont v-if="item.favorite" name='star-fill-n' color="#FFC82C" @click="handleFavSubject(item)"></IconFont>
              <IconFont v-else name='star-n' color="#AAAAAA" @click="handleFavSubject(item)"></IconFont>
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
                <nut-rate size="small" v-model="item.level" spacing="3" readonly/>
              </view>
            </view>
          </view>
        </view>
      </view>
    </view>
    <view>
      <nut-action-sheet v-model:visible="isOpenedAction" :menu-items="menuItems" @close="handleCloseAction"
                        @choose="chooseMenu"
                        title="请选择查看模式" cancel-text="取消">
      </nut-action-sheet>
    </view>
  </view>
</template>

<script lang="ts">
import {IconFont} from '@nutui/icons-vue-taro';
import Taro from "@tarojs/taro";
import {onMounted, ref} from 'vue';
import examApi from '../../../api/exam.api';
import api from '../../../api/api';
import {warnMessage, successMessage, showNoMoreData, showLoading, hideLoading} from '../../../utils/util';

export default {
  components: {
    IconFont
  },
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
    const menuItems = [{name: '预览模式'}, {name: '顺序刷题'}, {name: '随机刷题'}];

    async function fetch(append = false) {
      if (!hasNextPage.value) {
        await showNoMoreData();
        return;
      }
      await showLoading();
      try {
        const res = await examApi.getSubjects({
          categoryId,
          page: page.value,
          pageSize: pageSize.value,
          findFav: true,
          findView: true
        });
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

    async function handleClickCate(item) {
      if (item.subjectCnt === 0) {
        await warnMessage('暂无数据');
        return;
      }
      Taro.navigateTo({url: "/pages/exam_pages/subjects_list/index?categoryId=" + item.id})
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

    function chooseMenu(itemParams) {
      const name = itemParams.name;
      if (name === '预览模式') {
        Taro.navigateTo({url: "/pages/exam_pages/subjects_detail/index?mode=1&id=" + clickItem.value.id});
        handleCloseAction();
      } else if (name === '顺序刷题') {
        Taro.navigateTo({url: "/pages/exam_pages/subjects_detail/index?mode=2&id=" + clickItem.value.id});
        handleCloseAction();
      } else if (name === '随机刷题') {
        Taro.navigateTo({url: "/pages/exam_pages/subjects_detail/index?mode=3&id=" + clickItem.value.id});
        handleCloseAction();
      }
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
      menuItems,
      init,
      nextPage,
      handleClickSubject,
      handleFavSubject,
      handleClickCate,
      chooseMenu,
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
  margin-top: 22px;
  padding: 12px;
  border-radius: 10px;
}

.subject-list-item-top, .subject-list-item-bottom {
  display: flex;
  justify-content: space-between;
}

.subject-list-item-label {
  display: flex;
  justify-content: space-between;
  margin-top: 6px;
}

.subject-list-item-level {
  margin-bottom: 6px;
  display: inline-flex;
  font-size: 30px;
}

.subject-list-page .nut-action-sheet {
  padding-bottom: 100px;
}
</style>