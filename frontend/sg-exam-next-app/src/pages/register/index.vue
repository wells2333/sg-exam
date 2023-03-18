<template>
  <view class="register-main bg-blue">
    <view class="register-info ml-22 mr-22">
      <nut-input v-model="tenantCode" placeholder="请输入企业 ID"></nut-input>
      <nut-input v-model="username" placeholder="请输入用户名"></nut-input>
      <nut-input v-model="name" placeholder="请输入姓名"></nut-input>
      <nut-input type="password" v-model="password" placeholder="请输入密码"></nut-input>
      <nut-input type="password" v-model="passwordAgain" placeholder="请再次输入密码"></nut-input>
      <view class="register-btn mt-22">
        <nut-button block type="primary" @click="handleUsernameRegister" :loading="usernameRegisterLoading">
          注册
        </nut-button>
      </view>
    </view>
  </view>
</template>

<script lang="ts">
import {ref} from "vue";
import Taro from "@tarojs/taro";
import userApi from "../../api/user.api";
import {
  encryption,
  successMessage,
  warnMessage
} from '../../utils/util';
import {TENANT_CODE} from "../../constant/constant";

export default {
  setup() {
    const tenantCode = ref<string>('');
    const username = ref<string>('');
    const name = ref<string>('');
    const password = ref<string>('');
    const passwordAgain = ref<string>('');
    const usernameRegisterLoading = ref<boolean>(false);

    async function handleUsernameRegister() {
      let tenantCodeValue = tenantCode.value;
      if (tenantCodeValue === '') {
        tenantCodeValue = TENANT_CODE;
      }
      if (username.value === '') {
        await warnMessage('请输入用户名');
        return;
      }
      if (password.value === '') {
        await warnMessage('请输入密码');
        return;
      }
      if (password.value !== passwordAgain.value) {
        await warnMessage('两次输入的密码不一致');
        return;
      }
      const res = await userApi.checkExist(tenantCodeValue, username.value);
      const {code, result} = res;
      if (code === 0) {
        if (result) {
          await warnMessage('用户名已存在', 1000);
          return;
        }
        usernameRegisterLoading.value = true;
        try {
          if (await doRegister(tenantCodeValue)) {
            setTimeout(() => {
              Taro.reLaunch({url: "/pages/index/index"});
            }, 1000);
          }
        } finally {
          usernameRegisterLoading.value = false;
        }
      } else {
        await warnMessage('注册失败');
      }
    }

    async function doRegister(tenantCodeValue: string) {
      const params = {
        tenantCode: tenantCodeValue,
        username: username.value,
        password: password.value
      };
      const data = encryption({
        data: params,
        key: '1234567887654321',
        param: ['password']
      });
      data.identifier = data.username;
      data.credential = data.password;
      data.name = name.value;
      const res = await userApi.register(tenantCodeValue, data);
      const {code} = res;
      if (code === 0) {
        await successMessage('注册成功', 1000);
        return true;
      } else {
        await warnMessage('注册失败');
        return false;
      }
    }

    return {
      tenantCode,
      username,
      name,
      password,
      passwordAgain,
      usernameRegisterLoading,
      handleUsernameRegister
    }
  }
}
</script>

<style>
.register-main {
  padding-top: 60px;
}

.register-info {
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  -moz-box-shadow: 2px 2px 10px #909090;
  -webkit-box-shadow: 2px 2px 10px #909090;
  box-shadow: 2px 2px 10px #909090;
}

.register-btn {
  display: flex;
  align-items: center;
  margin-top: 80px;
}
</style>