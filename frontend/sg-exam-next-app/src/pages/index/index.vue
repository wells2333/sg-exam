<template>
  <view class="index-main">
    <view class="index-main-title">
      <h2 v-if="sysConfig !== undefined">{{sysConfig.title}}</h2>
    </view>
    <view class="index-login">
      <nut-tabs v-model="current" :background="'#fff'">
        <nut-tab-pane title="账号登录">
          <view class='tab-content'>
            <nut-input v-model="tenantCode" placeholder="请输入企业 ID"></nut-input>
            <nut-input v-model="username" placeholder="请输入用户名"></nut-input>
            <nut-input type="password" v-model="password" placeholder="请输入密码"></nut-input>
            <view class="login-btn">
              <nut-button block type="primary" @click="handleUsernameLogin" :loading="usernameLoginLoading">
                登录
              </nut-button>
            </view>
          </view>
        </nut-tab-pane>
        <nut-tab-pane title="手机号登录">
          <view class='tab-content'>
            <nut-input v-model="tenantCode" placeholder="请输入企业 ID"></nut-input>
            <nut-input v-model="phone" placeholder="请输入手机号" clearable>
              <template #right>
                <nut-button type="primary" size="small" v-if="countDownNum === 60" @click="handleSendSms">
                  发送验证码
                </nut-button>
                <nut-button type="primary" size="small" v-else :disabled="true">
                  {{ countDownNum }}s后重新获取
                </nut-button>
              </template>
            </nut-input>
            <view>
              <nut-input v-model="sms" placeholder="请输入短信验证码"></nut-input>
            </view>
            <view class="login-btn">
              <nut-button block type="primary" @click="handlePhoneLogin" :loading="phoneLoginLoading">
                登录
              </nut-button>
            </view>
          </view>
        </nut-tab-pane>
      </nut-tabs>
    </view>
    <view class="index-register">
      <view @click="handleRegister">注册账号</view>
      <view class="index-register-divider ml-22 mr-22">|</view>
      <view @click="handleForgotPassword">忘记密码?</view>
    </view>
    <view class="index-login-divider">
      <nut-divider :style="{ color: 'white', borderColor: 'white', fontWeight: 'bold', padding: '0 16px' }">第三方快捷登录
      </nut-divider>
    </view>
    <view class="index-login-third">
      <IconFont font-class-name="iconfont" class-prefix="icon" name="weixin" size="30px" color="white"
                @click="handleWxLogin"></IconFont>
    </view>
  </view>
</template>
<script lang="ts">
import {onMounted, ref, unref} from 'vue';
import Taro from "@tarojs/taro"
import api from "../../api/api";
import authApi from "../../api/auth.api";
import userApi from "../../api/user.api";
import sendSms from "../../api/user.api";
import {shardMessage, TENANT_CODE} from "../../constant/constant";
import {
  hideLoading,
  showLoading,
  successMessage,
  validatePhoneValue,
  validateSmsValue,
  warnMessage
} from '../../utils/util';
import {IconFont} from '@nutui/icons-vue-taro';

export default {
  components: {IconFont},
  setup() {
    const tenantCode = ref<string>('');
    const username = ref<string>('');
    const password = ref<string>('');
    const phone = ref<string>('');
    const sms = ref<string>('');
    const countDownNum = ref<number>(60);
    const sysConfig = ref<any>(undefined);
    const showAuthorizeBtn = ref<boolean>(false);
    const usernameLoginLoading = ref<boolean>(false);
    const phoneLoginLoading = ref<boolean>(false);
    const current = ref<number>(0);
    const tabList = ref<any>([
      {title: '账号登录'}, {title: '手机登录'}
    ]);

    async function fetch() {
      const res = await userApi.getSysDefaultConfig();
      const {code, result} = res;
      if (code === 0) {
        sysConfig.value = {
          title: result.sys_wxapp_main_title
        };
      }
    }

    async function handleUsernameLogin() {
      try {
        if (username.value === '') {
          await warnMessage('请输入用户名');
          return;
        }
        if (password.value === '') {
          await warnMessage('请输入密码');
          return;
        }
        usernameLoginLoading.value = true;
        let tenantCodeValue = tenantCode.value;
        if (tenantCodeValue === '') {
          tenantCodeValue = TENANT_CODE;
        }
        const params = {
          tenantCode: tenantCodeValue,
          username: username.value,
          password: password.value
        };
        const loginResult = await authApi.usernameLogin(params);
        if (loginResult.code === 0 && loginResult.result) {
          api.setTenantCode(loginResult.result.tenantCode);
          api.setToken(loginResult.result.token);
          const {result} = await userApi.userInfo();
          api.setUserInfo(result);
          Taro.reLaunch({url: "/pages/home/index"})
        } else {
          await warnMessage(loginResult.message);
        }
      } finally {
        usernameLoginLoading.value = false;
      }
    }

    async function handleSendSms() {
      const phoneVal = unref(phone);
      if (await validatePhoneValue(phone)) {
        let tenantCodeValue = unref(tenantCode);
        if (tenantCodeValue === '') {
          tenantCodeValue = 'gitee';
        }
        await sendSms.sendSmsTenant(phoneVal, tenantCodeValue);
        countDown();
        await successMessage('发送成功');
      }
    }

    async function handlePhoneLogin() {
      const phoneVal = unref(phone);
      const isTestPhone = phoneVal === '666';
      if (isTestPhone || (await validatePhoneValue(phoneVal) && await validateSmsValue(unref(sms)))) {
        phoneLoginLoading.value = true;
        try {
          let tenantCodeValue = tenantCode.value;
          if (tenantCodeValue === '') {
            tenantCodeValue = TENANT_CODE;
          }
          const loginResult = await authApi.mobileLogin(tenantCodeValue, phoneVal, unref(sms), isTestPhone,
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
            await successMessage('登录成功');
            Taro.reLaunch({url: "/pages/home/index"});
          } else {
            await warnMessage(loginResult.message);
          }
        } finally {
          phoneLoginLoading.value = false;
        }
      }
    }

    function handleClick(value) {
      current.value = value;
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

    async function handleWxLogin() {
      if (wx === undefined) {
        return;
      }
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
                if (!hasPhone) {
                  await Taro.navigateTo({url: "/pages/user_pages/phone_info/index"});
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

    function handleRegister() {
      Taro.navigateTo({url: "/pages/register/index"});
    }

    function handleForgotPassword() {
      Taro.navigateTo({url: "/pages/forgotpassowrd/index"});
    }

    onMounted(async () => {
      await fetch();
    });

    return {
      current,
      tabList,
      handleClick,
      sysConfig,
      showAuthorizeBtn,
      tenantCode,
      username,
      password,
      phone,
      sms,
      countDownNum,
      usernameLoginLoading,
      phoneLoginLoading,
      handleRegister,
      handleForgotPassword,
      handleUsernameLogin,
      handleSendSms,
      handlePhoneLogin,
      handleWxLogin
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
.index-main {
  background-color: #6190E8;
  height: 100%;
  padding-top: 60px;
}

.index-main-title {
  text-align: center;
  font-size: 50px;
  font-weight: bold;
  color: #fff;
  margin-bottom: 30px;
}

.index-login {
  margin-left: 22px;
  margin-right: 22px;
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  -moz-box-shadow: 2px 2px 10px #909090;
  -webkit-box-shadow: 2px 2px 10px #909090;
  box-shadow: 2px 2px 10px #909090;
}

.login-btn {
  margin-top: 80px;
}

.index-login-divider {

}

.index-login-third {
  text-align: center;
}

.index-register {
  display: flex;
  justify-content: center;
  color: white;
  margin-top: 30px;
}

</style>
