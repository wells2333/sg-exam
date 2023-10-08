<template>
  <view class="rank">
    <view class="item flex-row" v-for="item in itemList" :key="item.rankNum">
      <view class="rank-num flex-row">
        <view class="icon">
          <image
            v-if="item.rankNum === 1"
            src="https://yunmianshi.com/attach-storage/yunmianshi/exam/image/10/champion.png"
          />
          <image
            v-else-if="item.rankNum === 2"
            src="https://yunmianshi.com/attach-storage/yunmianshi/exam/image/10/runnerUp.png"
          />
          <image
            v-else-if="item.rankNum === 3"
            src="https://yunmianshi.com/attach-storage/yunmianshi/exam/image/10/secondRunnerUp.png"
          />
        </view>
        <text>{{ item.rankNum }}</text>
      </view>
      <view class="userinfo flex-row">
        <nut-avatar class="avatar" size="normal" circle="true">
          <img :src="item.avatarUrl" />
        </nut-avatar>
        <view class="name">{{ item.name }}</view>
      </view>
      <view class="score">{{ item.score }}</view>
    </view>
  </view>
</template>
<script lang="ts">
import { onMounted, ref } from "vue";
import rankApi from "../../../api/rank.api";
import Taro from "@tarojs/taro";
import { showLoading, hideLoading } from "../../../utils/util";

export default {
  setup() {
    const itemList = ref<any>(undefined);
    async function fetch() {
      const res = await rankApi.getRankList({
        examinationId: 3
      });
      const { code, result } = res;
      console.log(result, 'res')
      if (code === 0 && result !== null) {
        itemList.value = result;
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
    });

    return {
      init,
      itemList,
    };
  },
  onPullDownRefresh() {
    try {
      this.init();
    } finally {
      Taro.stopPullDownRefresh();
    }
  },
};
</script>

<style>
.flex-row {
  display: flex;
  flex-direction: row;
  align-items: center;
}
.item {
  margin: 0 2rpx;
  border-bottom: 1px solid rgb(235, 237, 240);
  padding: 20px;
}
.rank-num {
  margin-right: 20px;
  display: flex;
  font-size: 36px;
}
.icon {
  width: 50px;
  height: 50px;
  margin-right: 20px;
}
.icon image {
  width: 100%;
  height: 100%;
}
.userinfo {
  flex: 1;
}
.name {
  margin-left: 20px;
  margin-right: 20px;
  font-size: 36px;
}
.score {
  font-size: 34px;
}
</style>
