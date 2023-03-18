<template>
  <view class="phone-info">
    <nut-form>
      <nut-form-item label="电话">
        <nut-input type='text' v-model="phone" placeholder='请输入手机号'>
          <template #right>
            <nut-button type="primary" size="small" v-if="countDownNum === 60" @click="handleSendSms">
              发送验证码
            </nut-button>
            <nut-button type="primary" size="small" v-else :disabled="true">
              {{ countDownNum }}s后重新获取
            </nut-button>
          </template>
        </nut-input>
      </nut-form-item>
      <nut-form-item label="验证码">
        <nut-input type='text' v-model="sms" placeholder="请输入短信验证码"></nut-input>
      </nut-form-item>
      <nut-form-item label="邮箱">
        <nut-input type='text' placeholder='请输入邮箱' v-model="email"/>
      </nut-form-item>
      <nut-form-item label="性别">
        <nut-radio-group v-model="gender" text-position="left" direction="horizontal">
          <nut-radio class="gender_checkbox" color="#6190E8" shape="button" :label="0">男</nut-radio>
          <nut-radio class="gender_checkbox" color="#6190E8" shape="button" :label="1">女</nut-radio>
        </nut-radio-group>
      </nut-form-item>
    </nut-form>
    <view class="phone-info-btn-view">
      <nut-button type="primary" class="phone-info-btn" block :loading="isLoading" @click="handleSubmit">保存</nut-button>
      <nut-button type="default" class="phone-info-btn" block @click="handleSkip">跳过</nut-button>
    </view>
  </view>
</template>

<script lang="ts">
import {ref, unref} from 'vue';
import Taro from "@tarojs/taro";
import {successMessage, warnMessage, validatePhoneValue, validateEmail, validateSmsValue} from "../../../utils/util";
import api from "../../../api/api";
import userApi from "../../../api/user.api";
import sendSms from "../../../api/user.api";

export default {
  setup() {
    const phone = ref<string>('');
    const sms = ref<string>('');
    const countDownNum = ref<number>(60);
    const email = ref<string>('');
    const gender = ref<number>(0);
    const isLoading = ref<boolean>(false);

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
      try {
        isLoading.value = true;
        const {id, identifier, tenantCode} = await api.getUserInfo();
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
      } finally {
        isLoading.value = false;
      }
    }

    async function handleSkip() {
      await Taro.reLaunch({url: "/pages/home/index"});
    }

    return {
      isLoading,
      phone,
      sms,
      countDownNum,
      email,
      gender,
      handleSendSms,
      handleSubmit,
      handleSkip
    }
  }
}
</script>

<style>
.phone-info .nut-input {
  border-bottom: none;
}

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
  margin-right: 18px;
}
</style>
