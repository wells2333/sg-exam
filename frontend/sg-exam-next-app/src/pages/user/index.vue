<template>
  <view class="bg-gray" v-if="userInfo !== undefined">
    <view class="avatar-container flex-row" @click="handleUserInfo">
      <at-avatar class="avatar" :circle="true" size="large" :image="avatar"/>
      <view class="flex-col user-info">
        <text class="userName">{{userInfo.name}}</text>
        <text class="userDesc">{{userInfo.signature === null || userInfo.signature === '' ? '好好学习天天向上~' : userInfo.signature}}</text>
      </view>
      <view class="more-btn">
        <at-icon value='chevron-right' size='15' color='#CCC'></at-icon>
      </view>
    </view>
    <view class="top-tab-container flex-row">
      <view class="top-tab-item" @click="handleClickTopTab('/pages/favorite/index')">我的收藏</view>
      <view class="top-tab-item" @click="handleClickTopTab('/pages/record/index')">我的考试</view>
      <view class="top-tab-item" @click="handleClickTopTab('/pages/record/index')">我的成绩</view>
    </view>
    <view class="container">
      <at-list>
        <at-list-item title="消息中心" arrow="right" :iconInfo="{ value: 'message', color: '#ffa200'}"
                      :hasBorder="true"
                      @click="handleClick"></at-list-item>
        <at-list-item title="帮助" arrow="right" :iconInfo="{ value: 'help', color: '#F97D81'}"
                      :hasBorder="true"
                      @click="handleClick"></at-list-item>
        <at-list-item title="联系客服" arrow="right" :iconInfo="{ value: 'phone', color: '#00a03e'}"
                      :hasBorder="true"
                      @click="handleClick"></at-list-item>
        <at-list-item title="分享给朋友" arrow="right" class="share-box" :iconInfo="{ value: 'share', color: '#35A7FF'}"
                      :hasBorder="true"
                      @click="handleClickShare"></at-list-item>
        <at-list-item title="关于" arrow="right" :iconInfo="{ value: 'link', color: '#9881F5'}"
                      @click="handleClickAbout"></at-list-item>
        <at-list-item title="退出登录" arrow="right" :iconInfo="{ value: 'alert-circle', color: '#2288A5'}"
                      @click="handleLogout"></at-list-item>
      </at-list>
    </view>
  </view>
</template>

<script lang="ts">
import {onMounted, ref} from 'vue';
import api from '../../api/api';
import Taro from "@tarojs/taro";
import {hideLoading, showLoading} from "../../utils/util";

export default {
  setup() {
    const userInfo = ref<any>(undefined);
    const avatar = ref<string>(undefined);

    async function fetch() {
      userInfo.value = api.getUserInfo();
      avatar.value = userInfo.value.avatar;
      if (userInfo.value.avatar === '') {
        avatar.value = 'https://img.yzcdn.cn/vant/cat.jpeg';
      }
    }

    function handleClickTopTab(url) {
      Taro.navigateTo({url});
    }

    function handleClick() {
      Taro.showToast({title: '功能正在开发中', icon: 'none'});
    }

    function handleClickShare() {
      Taro.showShareMenu({
        withShareTicket: true
      });
    }

    function handleClickAbout() {
      Taro.navigateTo({url: "/pages/about/index"});
    }

    function handleLogout() {
      api.logout();
      Taro.reLaunch({ url: "/pages/index/index" })
    }

    function handleUserInfo() {
      Taro.navigateTo({url: "/pages/user_info/index"});
    }

    async function init() {
      try {
        await showLoading();
        await fetch();
      } finally {
        hideLoading();
      }
    }
    onMounted(() => {
      init();
    });

    return {
      init,
      avatar,
      userInfo,
      handleClickTopTab,
      handleClick,
      handleClickShare,
      handleClickAbout,
      handleLogout,
      handleUserInfo
    }
  },
  onPullDownRefresh() {
    try {
      this.init();
    } finally {
      Taro.stopPullDownRefresh();
    }
  },
}
</script>

<style>
.avatar-container {
  height: 100px;
  color: rgba(80, 80, 80, 1);
  background-color: rgba(255, 255, 255, 1);
  font-size: 16px;
}

.top-tab-container {
  height: 60px;
  color: rgba(80, 80, 80, 1);
  background-color: rgba(255, 255, 255, 1);
  font-size: 16px;
  text-align: center;
}

.avatar {
  margin-left: 20px;
}

.userName {
  margin-left: 10px;
  color: rgba(80, 80, 80, 1);
  font-size: 18px;
  text-align: left;
}

.userDesc {
  margin-left: 10px;
  margin-top: 8px;
  color: rgba(166, 166, 166, 1);
  font-size: 13px;
}

.top-tab-item {
  color: rgba(80, 80, 80, 1);
  font-size: 14px;
  flex: 1;
  border-right: 1px solid rgba(236, 238, 241, 1);
}

.top-tab-item:last-child {
  border-right: none;
}

.share-box {
  margin-top: 10px;
}

.container {
  margin-top: 10px;
}

.user-info{
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  margin-right: 20px;
}
.more-btn {
  padding-right: 5px;
}
</style>