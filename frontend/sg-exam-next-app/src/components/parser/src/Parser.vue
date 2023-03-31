<template>
  <view v-if="env === 'WEAPP'">
    <wx-parse :class="customClass" :html="html" key={Math.random()}></wx-parse>
  </view>
  <view v-else>
    <view v-html="html"></view>
  </view>
</template>

<script lang="ts">
import wxParse from '../../wxparse/index';
import Taro from '@tarojs/taro';
import {watch} from 'vue';

export default {
  components: {
    'wx-parse': wxParse
  },
  props: {
    customClass: {
      type: String,
      default: () => ''
    },
    html: {
      type: String,
      default: () => ''
    }
  },
  setup(props) {
    const env = Taro.getEnv();
    watch(
        () => props.html,
        async (html) => {
        },
        {flush: 'post'},
    );
    return {
      env
    }
  }
}
</script>