<template>
  <view class="bg-gray" v-if="userInfo !== undefined">
    <view class="avatar-container flex-row" @click="handleUserInfo">
      <nut-avatar class="avatar" :circle="true" size="large" :image="avatar"/>
      <view class="flex-col user-info">
        <text class="userName">{{ userInfo.name }}</text>
        <text class="userDesc">
          {{ userInfo.signature === null || userInfo.signature === '' ? '好好学习天天向上~' : userInfo.signature }}
        </text>
      </view>
      <view class="more-btn">
        <IconFont name="rect-right" size="15" color="#CCC"></IconFont>
      </view>
    </view>
    <view class="top-tab-container flex-row">
      <view class="top-tab-item" @click="handleClickTopTab('/pages/exam_pages/favorite/index')">我的收藏</view>
      <view class="top-tab-item" @click="handleClickTopTab('/pages/exam_pages/record/index')">我的考试</view>
      <view class="top-tab-item" @click="handleClickTopTab('/pages/exam_pages/record/index')">我的成绩</view>
    </view>
    <view class="container">
      <nut-cell-group>
        <nut-cell title="我的消息" arrow="right" @click="handleMessages">
          <template v-slot:icon>
            <IconFont name="message" color="#ffa200"></IconFont>
          </template>
          <template v-slot:link>
            <IconFont name="rect-right" color="#CCC"></IconFont>
          </template>
        </nut-cell>
        <nut-cell title="帮助" @click="handleClick">
          <template v-slot:icon>
            <IconFont name="ask" color="#F97D81"></IconFont>
          </template>
          <template v-slot:link>
            <IconFont name="rect-right" color="#CCC"></IconFont>
          </template>
        </nut-cell>
        <nut-cell title="联系客服" @click="handleClick">
          <template v-slot:icon>
            <IconFont name="link" color="#00a03e"></IconFont>
          </template>
          <template v-slot:link>
            <IconFont name="rect-right" color="#CCC"></IconFont>
          </template>
        </nut-cell>
        <nut-cell title="分享给朋友" @click="handleClickShare">
          <template v-slot:icon>
            <IconFont name="share" color="#35A7FF"></IconFont>
          </template>
          <template v-slot:link>
            <IconFont name="rect-right" color="#CCC"></IconFont>
          </template>
        </nut-cell>
        <nut-cell title="关于" @click="handleClickAbout">
          <template v-slot:icon>
            <IconFont name="my" color="#9881F5"></IconFont>
          </template>
          <template v-slot:link>
            <IconFont name="rect-right" color="#CCC"></IconFont>
          </template>
        </nut-cell>
        <nut-cell title="退出登录" @click="handleLogout">
          <template v-slot:icon>
            <IconFont name="tips" color="#2288A5"></IconFont>
          </template>
          <template v-slot:link>
            <IconFont name="rect-right" color="#CCC"></IconFont>
          </template>
        </nut-cell>
      </nut-cell-group>
    </view>
  </view>
</template>

<script lang="ts">
import {IconFont} from '@nutui/icons-vue-taro';
import {onMounted, ref} from 'vue';
import api from '../../api/api';
import Taro from "@tarojs/taro";
import {hideLoading, showLoading} from "../../utils/util";

export default {
  components: {IconFont},
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
      Taro.navigateTo({url: "/pages/user_pages/about/index"});
    }

    function handleLogout() {
      api.logout();
      Taro.reLaunch({url: "/pages/index/index"})
    }

    function handleUserInfo() {
      Taro.navigateTo({url: "/pages/user_pages/user_info/index"});
    }

    function handleMessages() {
      Taro.navigateTo({url: "/pages/user_pages/messages/index"});
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
      handleUserInfo,
      handleMessages
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
  height: 200px;
  color: rgba(80, 80, 80, 1);
  background-color: rgba(255, 255, 255, 1);
  font-size: 20px;
}

.top-tab-container {
  height: 60px;
  padding-bottom: 10px;
  color: rgba(80, 80, 80, 1);
  background-color: rgba(255, 255, 255, 1);
  font-size: 16px;
  text-align: center;
}

.avatar {
  margin-left: 20px;
}

.userName {
  margin-left: 20px;
  color: rgba(80, 80, 80, 1);
  font-size: 30px;
  text-align: left;
}

.userDesc {
  margin-left: 20px;
  margin-top: 8px;
  color: rgba(166, 166, 166, 1);
  font-size: 26px;
}

.top-tab-item {
  color: rgba(80, 80, 80, 1);
  font-size: 30px;
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

.user-info {
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  margin-right: 20px;
}

.more-btn {
  padding-right: 30px;
}
</style>