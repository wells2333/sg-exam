<template>
  <view class="bg-gray">
    <view class="container" v-if="userInfo !== undefined">
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
          <radio-group @change="handleChangeGender">
            <label>
              <radio class="gender_checkbox" color="#6190E8" value="0" :checked="gender === 0">男</radio>
            </label>
            <label>
              <radio class="gender_checkbox" color="#6190E8" value="1" :checked="gender === 1">女</radio>
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
import {onMounted, ref} from 'vue';
import api from '../../api/api';
import userApi from "../../api/user.api";
import Taro from "@tarojs/taro";
import {successMessage, warnMessage} from "../../utils/util";

export default {
  setup() {
    const userInfo = ref<any>(undefined);
    const gender = ref<any>(0);
    const avatar = ref<string>();
    async function handleSave() {
      const res = await userApi.updateInfo({
        id: userInfo.value.id,
        tenantCode: userInfo.value.tenantCode,
        identifier: userInfo.value.identifier,
        identityType: userInfo.value.identityType,
        name: userInfo.value.name,
        gender: gender.value,
        phone: userInfo.value.phone,
        email: userInfo.value.email
      });
      if (res && res.code === 0) {
        await successMessage('保存成功');
        const {result} = await userApi.userInfo();
        userInfo.value = result;
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

    function handleChangeGender({detail}) {
      gender.value = detail.value;
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

    onMounted(() => {
      fetch();
    })

    return {
      avatar,
      userInfo,
      gender,
      handleSave,
      handleSelectAvatar,
      handleChangeName,
      handleChangePhone,
      handleChangeEmail,
      handleChangeGender
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
