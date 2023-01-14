<template>
  <AtMessage/>
  <view class="subject-list">
    <view class="subject-list-item" v-for="item in list">
      <view class="subject-list-item-label">
        <at-tag type="primary" size="small">{{item.typeLabel}}</at-tag>
        <AtIcon value='star-2' size='8' :color="item.favorite === true ? '#FFC82C': '#AAAAAA'" @click="handleFavSubject(item)"></AtIcon>
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
</template>

<script lang="ts">
import Taro from "@tarojs/taro";
import {onMounted, ref} from 'vue';
import examApi from '../../api/exam.api';
import api from "../../api/api";
import {successMessage} from "../../utils/util";

export default {
  setup() {
    const params = Taro.getCurrentInstance().router.params;
    const categoryId = params.categoryId;
    const list = ref<any>([]);
    const page = ref<number>(1);
    const pageSize = ref<number>(10);
    const hasNextPage = ref<boolean>(true);

    async function fetch(append = false) {
      if (!hasNextPage.value) {
        successMessage('无更多数据');
        return;
      }
      await Taro.showLoading();
      try {
        const res = await examApi.getSubjects({categoryId, page: page.value, pageSize: pageSize.value, findFav: true});
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

    async function nextPage() {
      page.value = page.value + 1;
      await fetch(true);
    }

    async function init() {
      resetPage();
      await fetch();
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
      list,
      init,
      nextPage,
      handleClickSubject,
      handleFavSubject
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
.subject-list {
  margin: 6px;
}

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
</style>