<template>
  <view class="bg-gray">
    <view class="avatar-container flex-row">
      <at-avatar class="avatar" :circle="true" size="large" :image="avatar"/>
      <view class="flex-col">
        <text class="userName">{{userInfo.name}}</text>
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
      </at-list>
    </view>
  </view>
</template>

<script lang="ts">
import {onMounted, ref} from 'vue';
import api from '../../api/api';
import Taro from "@tarojs/taro";

export default {
  setup() {
    const userInfo = ref<any>(api.getUserInfo());
    const avatar = ref<string>(userInfo.value.avatar);
    if (userInfo.value.avatar === '') {
      avatar.value = 'https://img.yzcdn.cn/vant/cat.jpeg';
    }

    async function fetch() {

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

    onMounted(() => {
      fetch();
    });

    return {
      avatar,
      userInfo,
      handleClickTopTab,
      handleClick,
      handleClickShare,
      handleClickAbout
    }
  }
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
</style>