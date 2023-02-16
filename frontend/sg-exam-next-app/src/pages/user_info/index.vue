<template>
  <view class="bg-gray">
    <view class="container">
      <view class="at-input">
        <view class="at-input__container">
          <text class="at-input__title">头像</text>
          <at-avatar class="user-info-avatar" :circle="true" size="small" :image="avatar" @click="handleSelectAvatar"/>
        </view>
      </view>
      <at-input title="昵称" :value="userInfo.name" @change="handleChangeName"></at-input>
      <at-input title="账号" :value="userInfo.identifier" disabled></at-input>
      <view class="at-input">
        <view class="at-input__container">
          <text class="at-input__title">性别</text>
          <radio-group>
            <label>
              <radio class="gender_checkbox" :value="userInfo.gender" color="#6190E8" :checked="true">男</radio>
            </label>
            <label>
              <radio class="gender_checkbox" :value="userInfo.gender" color="#6190E8">女</radio>
            </label>
          </radio-group>
        </view>
      </view>
      <at-input title="手机" :value="userInfo.phone" @change="handleChangePhone"></at-input>
      <at-input title="邮箱" :value="userInfo.email" @change="handleChangeEmail"></at-input>
    </view>
    <view class="user-info-save-btn">
      <at-button type="primary" @click="handleSave">保存</at-button>
    </view>
  </view>
</template>

<script lang="ts">
import {ref} from 'vue';
import api from '../../api/api';
import userApi from "../../api/user.api";
import Taro from "@tarojs/taro";
import {successMessage, warnMessage} from "../../utils/util";

export default {
  setup() {
    const userInfo = ref<any>(api.getUserInfo());
    const avatar = ref<string>(userInfo.value.avatar);
    if (userInfo.value.avatar === '') {
      avatar.value = 'https://img.yzcdn.cn/vant/cat.jpeg';
    }

    async function handleSave() {
      const res = await userApi.updateInfo({
        id: userInfo.value.id,
        tenantCode: userInfo.value.tenantCode,
        identifier: userInfo.value.identifier,
        identityType: userInfo.value.identityType,
        name: userInfo.value.name,
        gender: userInfo.value.gender,
        phone: userInfo.value.phone,
        email: userInfo.value.email
      });
      if (res && res.code === 0) {
        await successMessage('保存成功');
        const {result} = await userApi.userInfo();
        api.setUserInfo(result);
      } else {
        await warnMessage('保存失败');
      }
    }

    function handleSelectAvatar() {

    }

    function handleChangeName(value) {
      userInfo.value.name = value;
    }

    function handleChangePhone(value) {
      userInfo.value.phone = value;
    }

    function handleChangeEmail(value) {
      userInfo.value.email = value;
    }

    return {
      avatar,
      userInfo,
      handleSave,
      handleSelectAvatar,
      handleChangeName,
      handleChangePhone,
      handleChangeEmail
    }
  }
}
</script>

<style>
.container {
  margin-top: 10px;
}

.user-info-avatar {
  margin-right: 10px;
}

.user-info-save-btn {
  margin-top: 50px;
  margin-left: 10px;
  margin-right: 10px;
}

.gender_checkbox {
  margin-right: 18px;
}
</style>