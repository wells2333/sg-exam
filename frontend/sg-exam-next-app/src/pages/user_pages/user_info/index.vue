<template>
  <view class="user-info" v-if="userInfo !== undefined">
    <nut-form>
      <nut-form-item label="ID">
        <nut-input class="nut-input-text" type="text" v-model="userInfo.id" disabled></nut-input>
      </nut-form-item>
      <nut-form-item label="头像">
        <nut-avatar class="user-info-avatar" :circle="true" size="small" :image="avatar"/>
      </nut-form-item>
      <nut-form-item label="昵称">
        <nut-input class="nut-input-text" type="text" v-model="userInfo.name" placeholder="请输入昵称"></nut-input>
      </nut-form-item>
      <nut-form-item label="签名">
        <nut-input class="nut-input-text" type="text" title="签名" v-model="userInfo.signature" placeholder="请输入签名"></nut-input>
      </nut-form-item>
      <nut-form-item label="性别">
        <nut-radio-group v-model="gender" text-position="left" direction="horizontal">
          <nut-radio class="gender_checkbox" color="#6190E8" shape="button" :label="0">男</nut-radio>
          <nut-radio class="gender_checkbox" color="#6190E8" shape="button" :label="1">女</nut-radio>
        </nut-radio-group>
      </nut-form-item>
      <nut-form-item label="手机">
        <nut-input class="nut-input-text" type="text" title="手机" v-model="userInfo.phone" placeholder="请输入手机号"></nut-input>
      </nut-form-item>
      <nut-form-item label="邮箱">
        <nut-input class="nut-input-text" type="text" title="邮箱" v-model="userInfo.email" placeholder="请输入邮箱地址"></nut-input>
      </nut-form-item>
    </nut-form>
    <view class="user-info-save-btn">
      <nut-button type="primary" @click="handleSave">保存</nut-button>
    </view>
  </view>
</template>

<script lang="ts">
import {onMounted, ref} from 'vue';
import api from '../../../api/api';
import userApi from "../../../api/user.api";
import {
  showLoading,
  hideLoading,
  successMessage,
  validateEmail,
  validatePhoneValue,
  warnMessage
} from "../../../utils/util";
import Taro from "@tarojs/taro";

export default {
  setup() {
    const userInfo = ref<any>(undefined);
    const gender = ref<any>(0);
    const avatar = ref<string>();

    async function handleSave() {
      const {id, tenantCode, identifier, identityType, name, phone, email, signature} = userInfo.value;
      if (phone !== '' && !await validatePhoneValue(phone)) {
        await warnMessage('手机号格式不正确');
        return;
      }
      if (email !== '' && !await validateEmail(email)) {
        await warnMessage('邮箱格式不正确');
        return;
      }
      try {
        await showLoading();
        const res = await userApi.updateInfo({
          id,
          tenantCode,
          identifier,
          identityType,
          name,
          gender: gender.value,
          phone,
          email,
          signature
        });
        if (res && res.code === 0) {
          await successMessage('保存成功');
          const {result} = await userApi.userInfo();
          userInfo.value = result;
          api.setUserInfo(result);
        } else {
          await warnMessage('保存失败');
        }
      } finally {
        hideLoading();
      }
    }

    async function fetch() {
      const {result} = await userApi.userInfo();
      userInfo.value = result;
      gender.value = userInfo.value.gender;
      avatar.value = userInfo.value.avatar;
      if (userInfo.value.avatar === '') {
        avatar.value = 'https://img.yzcdn.cn/vant/cat.jpeg';
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
      avatar,
      userInfo,
      gender,
      handleSave
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
.user-info .nut-input {
  border-bottom: none;
}

.user-info-avatar {
  margin-right: 10px;
}

.user-info-save-btn {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 50px;
  margin-left: 10px;
  margin-right: 10px;
}

.gender_checkbox {
  margin-right: 18px;
}
</style>
