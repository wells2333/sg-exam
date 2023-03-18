<template>
  <view>
    <view v-for="item in messages" class="messages mt-22 mb-22 ml-22 mr-22" @click="handleClickMessage(item)">
      <view class="message-title-container mt-22">
        <h3 class="message-title overflow-hidden-text mt-22">{{item.title}}</h3>
        <text class="message-title-time mt-22">{{item.updateTime}}</text>
      </view>
      <view class="message-content overflow-hidden-text mt-22 mb-22">
        {{item.content}}
      </view>
    </view>
  </view>
</template>

<script lang="ts">
import {onMounted, ref} from 'vue';
import api from '../../../api/api';
import userApi from "../../../api/user.api";
import {showLoading, hideLoading} from "../../../utils/util";
import Taro from "@tarojs/taro";

export default {
  setup() {
    const messages = ref<any>(undefined);

    async function fetch() {
      const userInfo = api.getUserInfo();
      const {id} = userInfo;
      // 站内信
      const type = 0;
      const res = await userApi.getMessages(id, type);
      const {code, result} = res;
      if (code === 0 && result !== null) {
        messages.value = result.list;
      }
    }

    async function init() {
      try {
        await showLoading();
        await fetch();
      } finally {
        hideLoading();
      }
    }

    function handleClickMessage(item) {
      Taro.navigateTo({url: "/pages/user_pages/message_detail/index?id=" + item.id});
    }

    onMounted(() => {
      init();
    })

    return {
      init,
      messages,
      handleClickMessage
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

</style>
