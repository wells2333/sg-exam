<template>
  <view>
    <my-webview v-if="url !== undefined" :url="url"></my-webview>
  </view>
</template>

<script lang="ts">
import {onMounted, ref} from 'vue';
import {WebView} from '../../components/webview';
import Taro from "@tarojs/taro";

export default {
  components: {
    'my-webview': WebView
  },
  setup() {
    const currentInstance = Taro.getCurrentInstance();
    const params = currentInstance.router.params;
    const url = ref<string>(params?.url);
    return {
      url
    }
  }
}
</script>