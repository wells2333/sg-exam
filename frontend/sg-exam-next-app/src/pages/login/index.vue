<template>
  <view>
    <view class="phone-login">
      <text class="phone-login-title">手机号登录</text>
    </view>
    <view>
      <at-input :value="tenantCode" @change="handleTenantCode" title="企业ID" placeholder="非必填"></at-input>
      <at-input type="phone" :value="phone" title="手机号" placeholder="请输入手机号" @change="handlePhoneChange"/>
    </view>
    <view>
      <at-input
          :value="sms"
          center
          clearable
          title="短信验证码"
          placeholder="请输入短信验证码"
          use-button-slot
          @change="handleSmsChange"
      >
        <at-button v-if="countDownNum === 60" slot="button" size="small" @click="handlePhoneLogin" style="margin-right: 10px;">
          发送验证码
        </at-button>
        <at-button v-else :disabled="true" slot="button" size="small">
          {{ countDownNum }}s后重新获取
        </at-button>
      </at-input>
    </view>
    <view class="phone-login-btn-view">
      <at-button type="primary" @click="handleLogin" :block="true">
        登录
      </at-button>
    </view>
  </view>
</template>

<script lang="ts">
import {ref, unref} from 'vue';
import Taro from "@tarojs/taro"
import api from "../../api/api";
import authApi from "../../api/auth.api";
import sendSms from '../../api/user.api';
import userApi from '../../api/user.api';
import {shardMessage, TENANT_CODE} from "../../constant/constant";

export default {
  setup() {
    const tenantCode = ref<string>('');
    const phone = ref<string>('');
    const sms = ref<string>('');
    const countDownNum = ref<number>(60);

    function handleTenantCode(value) {
      tenantCode.value = value;
    }

    function handlePhoneChange(value) {
      phone.value = value;
    }

    function handleSmsChange(value) {
      sms.value = value;
    }

    function countDown() {
      const timer = setInterval(function () {
        countDownNum.value = countDownNum.value - 1;
        if (countDownNum.value <= -1) {
          clearInterval(timer);
          countDownNum.value = 60;
        }
      }, 1000)
    }

    async function handlePhoneLogin() {
      const phoneVal = unref(phone);
      if (validatePhoneValue()) {
        await sendSms.sendSms(phoneVal);
        countDown();
        Taro.showToast({title: '短信发送成功'});
      }
    }

    function validatePhoneValue() {
      const phoneVal = unref(phone);
      if (phoneVal === '') {
        Taro.showToast({title: '请输入手机号', icon: 'error'});
        return false;
      }
      if (!(/^1[34578]\d{9}$/.test(phoneVal))) {
        Taro.showToast({title: '手机号格式错误', icon: 'error'});
        return false;
      }
      return true;
    }

    function validateSmsValue() {
      const smsVal = unref(sms);
      if (smsVal === '') {
        Taro.showToast({title: '请输入验证码', icon: 'error'});
        return false;
      }
      if (smsVal.length !== 4) {
        Taro.showToast({title: '请输入长度为4的验证码', icon: 'error'});
        return false;
      }
      return true;
    }

    async function handleLogin() {
      if (validatePhoneValue() && validateSmsValue()) {
        const phoneVal = unref(phone);
        Taro.showLoading({title: '登录中'})
        try {
          if (tenantCode.value === '') {
            handleTenantCode(TENANT_CODE);
          }
          const loginResult = await authApi.mobileLogin(unref(tenantCode), phoneVal, unref(sms),
              {
                name: phoneVal,
                gender: '0'
              }
          );
          if (loginResult.code === 0 && loginResult.result) {
            api.setTenantCode(loginResult.result.tenantCode);
            api.setToken(loginResult.result.token);
            const {result} = await userApi.userInfo();
            api.setUserInfo(result);
            Taro.showToast({title: '登录成功'});
            Taro.reLaunch({url: "/pages/home/index"})
          } else {
            Taro.showToast({title: loginResult.message, icon: 'error'});
          }
        } finally {
          Taro.hideLoading();
        }
      }
    }

    return {
      tenantCode,
      phone,
      sms,
      countDownNum,
      handleTenantCode,
      handlePhoneChange,
      handleSmsChange,
      handlePhoneLogin,
      handleLogin,
    }
  },
  onShareAppMessage() {
    return shardMessage;
  },
  async onShareTimeline() {
    return shardMessage;
  }
}
</script>

<style>
.phone-login {
  text-align: center;
}

.phone-login-title {
  width: 135px;
  height: 46px;
  left: 47px;
  top: 99px;
  letter-spacing: 4px;
  color: rgba(80, 80, 80, 1);
  font-size: 23px;
  line-height: 200%;
  text-align: center;
  font-weight: bold;
}

.phone-login-btn-view {
  width: 90%;
  text-align: center;
  margin-top: 20px;
  padding-left: 5%;
}
</style>