<template>
  <view v-if="message !== undefined">
    <view class="no-border-message mt-22 ml-22 mr-22">
      <view class="message-title-container">
        <h3 class="message-title">{{message.title}}</h3>
      </view>
      <view class="message-detail-title-time mt-22">
        {{message.updateTime}}
      </view>
      <view class="message-content mt-22">
        <text>{{message.content}}</text>
      </view>
    </view>
  </view>
</template>

<script lang="ts">
import {onMounted, ref} from 'vue';
import userApi from "../../../api/user.api";
import {showLoading, hideLoading} from "../../../utils/util";
import Taro from "@tarojs/taro";

export default {
  setup() {
    const params = Taro.getCurrentInstance().router?.params;
    const messageId = params?.id;
    const message = ref<any>(undefined);

    async function fetch() {
      if (!messageId) {
        return;
      }
      const res = await userApi.getMessageDetail(messageId);
      const {code, result} = res;
      if (code === 0 && result !== null) {
        message.value = result;
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

    onMounted(() => {
      init();
    })

    return {
      init,
      message
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
