<template>
  <div class="container" v-if="items !== undefined">
    <swiper
        style="height: calc(100vh - 56px);"
        :indicator-dots="true"
        indicator-color="#fae37c"
        indicator-active-color="rgba(250, 227, 124, 0.2)"
    >
      <swiper-item v-for="(item,index) in items" :key="index">
        <div class="item">
          <img :src="item.icon"/>
          <div class="title">{{ item.title }}</div>
          <div class="des">{{ item.des }}</div>
          <view class="tenant-code-input">
            <at-input :value="tenantCode" @change="handleTenantCode" title="企业 ID" placeholder="企业 ID" :border="false" style="text-align: left"></at-input>
          </view>
          <view class="login-btn">
            <at-button circle type="primary" size="normal" @click="getUserProfile">
              <at-icon value='message' size="12"></at-icon>
              微信登录
            </at-button>
            <at-button circle type="primary" class="login-btn-item" size="normal" @click="handlePhoneLogin">
              <at-icon value='phone' size="12"></at-icon>
              手机号登录
            </at-button>
          </view>
        </div>
      </swiper-item>
    </swiper>
  </div>
  <section class="input"></section>
</template>
<script lang="ts">
import {onMounted, ref} from 'vue';
import Taro from "@tarojs/taro"
import api from "../../api/api";
import authApi from "../../api/auth.api";
import userApi from "../../api/user.api";
import {shardMessage, TENANT_CODE} from "../../constant/constant";
import {showLoading, hideLoading, successMessage, warnMessage} from '../../utils/util';

export default {
  setup() {
    const tenantCode = ref<string>("");
    const items = ref<any>(undefined);
    const showAuthorizeBtn = ref<boolean>(false);

    async function fetch() {
      const res = await userApi.getSysDefaultConfig();
      const {code, result} = res;
      if (code === 0) {
        items.value = [];
        items.value.push({
          title: result.sys_wxapp_main_title,
          des: result.sys_wxapp_sub_title,
          icon: result.sys_wxapp_avatar,
        });
      }
    }

    async function handleTenantCode(value) {
      if (new RegExp('^[A-z0-9]*$').test(value)) {
        tenantCode.value = value;
      } else {
        await warnMessage('只能输入英文或数字');
      }
    }

    async function getUserProfile() {
      wx.getUserProfile({
        desc: '用于完善用户资料',
        success: (res) => {
          const {userInfo} = res;
          const {nickName, gender, avatarUrl} = userInfo;
          showLoading('登录中');
          Taro.login({
            success: async (data) => {
              let tenantCodeValue = tenantCode.value;
              if (tenantCodeValue === '') {
                tenantCodeValue = TENANT_CODE;
              }
              const {code} = data;
              const loginResult = await authApi.wxlogin(tenantCodeValue, code, {name: nickName, gender, avatarUrl});
              if (loginResult.code === 0 && loginResult.result) {
                const {tenantCode, token, hasPhone} = loginResult.result;
                api.setTenantCode(tenantCode);
                api.setToken(token);
                const {result} = await userApi.userInfo();
                api.setUserInfo(result);
                await successMessage('登录成功');
                if (!hasPhone) {
                  await Taro.navigateTo({url: "/pages/phone_info/index"});
                } else {
                  await Taro.reLaunch({url: "/pages/home/index"});
                }
              } else {
                await warnMessage('登录失败');
              }
              hideLoading();
            },
            fail: async () => {
              await warnMessage('登录失败');
            }
          })
        },
        fail: async () => {
          await warnMessage('授权失败');
        }
      });
    }

    function handlePhoneLogin() {
      Taro.navigateTo({url: "/pages/login/index"})
    }

    onMounted(async () => {
      await fetch();
    });

    return {items, showAuthorizeBtn, tenantCode, handleTenantCode, getUserProfile, handlePhoneLogin}
  },
  onShareAppMessage() {
    return shardMessage;
  },
  async onShareTimeline() {
    return shardMessage;
  }
}
</script>
<style lang="less">
.cut-btn {
  border-radius: 50px;
}

swiper {
  swiper-item {
    display: flex;
    align-items: center;
    justify-content: center;

    .item {
      text-align: center;
      padding-bottom: 40 rpx;

      image {
        width: 500 rpx;
        height: 650 rpx;
      }

      .title {
        font-size: 36 rpx;
        font-weight: bold;
        margin: 10 rpx 0 rpx;
      }

      .des {
        font-size: 28 rpx;
        color: #969696;
        margin: 6px;
      }
    }
  }
}

.login-btn {
  display: inline-flex;
}

.login-btn-item {
  margin-left: 12px;
}

.tenant-code-input {
  margin-bottom: 8px;
}
</style>
