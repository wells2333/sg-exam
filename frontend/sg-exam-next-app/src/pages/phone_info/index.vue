<template>
  <view class="phone-info">
    <at-input name='value' title='电话' type='phone' placeholder='请输入手机号' :value="phone" @change="handlePhoneChange"/>
    <at-input
        :value="sms"
        center
        clearable
        title="验证码"
        placeholder="请输入短信验证码"
        use-button-slot
        @change="handleSmsChange"
    >
      <at-button v-if="countDownNum === 60" slot="button" size="small" @click="handleSendSms" style="margin-right: 10px;">
        发送验证码
      </at-button>
      <at-button v-else :disabled="true" slot="button" size="small">
        {{ countDownNum }}s后重新获取
      </at-button>
    </at-input>
    <at-input name='value' title='邮箱' type='text' placeholder='请输入邮箱' :value="email" @change="handleEmailChange"/>
    <view class="at-input">
      <view class="at-input__container">
        <text class="at-input__title">性别</text>
        <radio-group @change="handleGenderChange">
          <label>
            <radio class="gender_checkbox" value="0" color="#6190E8" :checked="true">男</radio>
          </label>
          <label>
            <radio class="gender_checkbox" value="1" color="#6190E8">女</radio>
          </label>
        </radio-group>
      </view>
    </view>
    <view class="phone-info-btn-view">
      <at-button type="primary" class="phone-info-btn" @click="handleSubmit">保存</at-button>
      <at-button type="primary" class="phone-info-btn" @click="handleSkip">跳过</at-button>
    </view>
  </view>
</template>

<script lang="ts">
import {ref, unref} from 'vue';
import Taro from "@tarojs/taro";
import {successMessage, warnMessage, validatePhoneValue, validateEmail, validateSmsValue} from "../../utils/util";
import api from "../../api/api";
import userApi from "../../api/user.api";
import sendSms from "../../api/user.api";

export default {
  setup() {
    const phone = ref<string>('');
    const sms = ref<string>('');
    const countDownNum = ref<number>(60);
    const email = ref<string>('');
    const gender = ref<string>("0");

    function handlePhoneChange(value) {
      phone.value = value;
    }

    function handleSmsChange(value) {
      sms.value = value;
    }

    async function handleSendSms() {
      const phoneVal = unref(phone);
      if (await validatePhoneValue(phone)) {
        await sendSms.sendSms(phoneVal);
        countDown();
        await successMessage('发送成功');
      }
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

    function handleEmailChange(value) {
      email.value = value;
    }

    async function handleSubmit() {
      if (!await validatePhoneValue(phone)) {
        return;
      }
      if (!await validateSmsValue(sms)) {
        return;
      }
      const emailValue = email.value;
      if (emailValue !== '' && !await validateEmail(email)) {
        return;
      }
      const {id, identifier, tenantCode}  = await api.getUserInfo();
      const res = await userApi.bindPhoneNumber({
        id,
        identifier,
        tenantCode,
        phone: phone.value,
        email: emailValue,
        code: sms.value
      });
      if (res && res.code === 0) {
        await successMessage('保存成功');
        await Taro.reLaunch({url: "/pages/home/index"});
      } else {
        await warnMessage(res.message);
      }
    }

    async function handleSkip() {
      await Taro.reLaunch({url: "/pages/home/index"});
    }

    function handleGenderChange({detail}) {
      gender.value = detail.value;
    }
    return {
      phone,
      sms,
      countDownNum,
      email,
      handlePhoneChange,
      handleSmsChange,
      handleSendSms,
      handleSubmit,
      handleSkip,
      handleEmailChange,
      handleGenderChange
    }
  }
}
</script>

<style>
.phone-info {
  margin-top: 20px;
  margin-left: 8px;
  margin-right: 8px;
  text-align: left;
}

.phone-info-btn-view {
  padding-top: 20px;
}

.phone-info-btn {
  margin-top: 16px;
}

.gender_checkbox {
  padding-left: 16px;
}
</style>
