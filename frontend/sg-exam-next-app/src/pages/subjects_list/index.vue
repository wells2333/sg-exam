<template>
  <view class="subject-list">
    <view class="subject-list-item" v-for="item in list">
      <view class="subject-list-item-top">
        <view class="subject-list-item-name">
          <h4>{{item.subjectName}}</h4>
        </view>
        <view>
          <at-tag type="primary" :circle="true" size="small">{{item.typeLabel}}</at-tag>
        </view>
      </view>
      <view class="subject-list-item-bottom">
        <view class="subject-list-item-level">
          <text>难度：</text>
          <at-rate :size="16" :value="item.level"/>
        </view>
        <view>
          <AtIcon value='star-2' size='8' color="#FFC82C"></AtIcon>
        </view>
      </view>
    </view>
  </view>
</template>

<script lang="ts">
import Taro from "@tarojs/taro";
import {onMounted, ref} from 'vue';
import examApi from '../../api/exam.api';

export default {
  onPullDownRefresh() {
    try {
      this.init();
    } finally {
      Taro.stopPullDownRefresh();
    }
  },
  setup() {
    const currentInstance = Taro.getCurrentInstance();
    const params = currentInstance.router.params;
    const categoryId = params.categoryId;
    const list = ref<any>([]);

    async function fetch() {
      const res = await examApi.getSubjects({categoryId});
      const {code, result} = res;
      if (code === 0 && result.list.length > 0) {
        list.value = result.list;
      }
    }

    async function init() {
      await fetch();
    }

    onMounted(() => {
      init();
    });
    return {
      list,
      init
    }
  }
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

.subject-list-item-name {
  width: 80%;
}

.subject-list-item:nth-child(odd) {
  background-color: #FAFBFC;
}

.subject-list-item-top, .subject-list-item-bottom {
  display: flex;
  justify-content: space-between;
}

.subject-list-item-level {
  margin-top: 10px;
  margin-bottom: 6px;
  display: inline-flex;
  font-size: 12px;
}
</style>