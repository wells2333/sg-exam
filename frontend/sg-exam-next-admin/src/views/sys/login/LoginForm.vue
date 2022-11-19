<template>
  <LoginFormTitle v-show="getShow" class="enter-x" />
  <Form
    class="p-4 enter-x"
    :model="formData"
    :rules="getFormRules"
    ref="formRef"
    v-show="getShow"
    @keypress.enter="handleLogin"
  >
    <FormItem name="tenantCode" class="enter-x">
      <Input
        size="large"
        v-model:value="formData.tenantCode"
        :placeholder="t('sys.login.tenantCode')"
        class="fix-auto-fill"
      />
    </FormItem>
    <FormItem name="account" class="enter-x">
      <Input
        size="large"
        v-model:value="formData.account"
        :placeholder="t('sys.login.userName')"
        class="fix-auto-fill"
      />
    </FormItem>
    <FormItem name="password" class="enter-x">
      <InputPassword
        size="large"
        visibilityToggle
        v-model:value="formData.password"
        :placeholder="t('sys.login.password')"
      />
    </FormItem>

    <ARow class="enter-x">
      <ACol :span="12">
        <FormItem>
          <!-- No logic, you need to deal with it yourself -->
          <Checkbox v-model:checked="remember" size="small">
            {{ t('sys.login.remember') }}
          </Checkbox>
        </FormItem>
      </ACol>
      <ACol :span="12">
        <FormItem :style="{ 'text-align': 'right' }">
          <!-- No logic, you need to deal with it yourself -->
          <Button type="link" size="small" @click="setLoginState(LoginStateEnum.RESET_PASSWORD)">
            {{ t('sys.login.forgetPassword') }}
          </Button>
        </FormItem>
      </ACol>
    </ARow>

    <FormItem class="enter-x">
      <Button type="primary" size="large" block @click="handleLogin" :loading="loading">
        {{ t('sys.login.loginButton') }}
      </Button>
      <!-- <Button size="large" class="mt-4 enter-x" block @click="handleRegister">
        {{ t('sys.login.registerButton') }}
      </Button> -->
    </FormItem>
    <ARow class="enter-x">
      <ACol :md="8" :xs="24">
        <Button block @click="setLoginState(LoginStateEnum.MOBILE)">
          {{ t('sys.login.mobileSignInFormTitle') }}
        </Button>
      </ACol>
      <ACol :md="8" :xs="24" class="!my-2 !md:my-0 xs:mx-0 md:mx-2">
        <Button block @click="handleQrCodeLogin">
          {{ t('sys.login.qrSignInFormTitle') }}
        </Button>
      </ACol>
      <ACol :md="7" :xs="24">
        <Button block @click="setLoginState(LoginStateEnum.REGISTER)">
          {{ t('sys.login.registerButton') }}
        </Button>
      </ACol>
    </ARow>
  </Form>
</template>
<script lang="ts" setup>
import {computed, reactive, ref, unref} from 'vue';

import {Button, Checkbox, Col, Form, Input, Row} from 'ant-design-vue';
import LoginFormTitle from './LoginFormTitle.vue';

import {useI18n} from '/@/hooks/web/useI18n';
import {useMessage} from '/@/hooks/web/useMessage';

import {useUserStore} from '/@/store/modules/user';
import {
  LoginStateEnum,
  useFormRules,
  useFormValid,
  useLoginState,
  useQrCodeLoginUrl
} from './useLogin';
import {useDesign} from '/@/hooks/web/useDesign';
import {getTicket} from "/@/api/sys/user";
//import { onKeyStroke } from '@vueuse/core';

  const ACol = Col;
  const ARow = Row;
  const FormItem = Form.Item;
  const InputPassword = Input.Password;
  const { t } = useI18n();
  const { notification, createErrorModal } = useMessage();
  const { prefixCls } = useDesign('login');
  const userStore = useUserStore();

  const { setLoginState, getLoginState } = useLoginState();
  const { setQrCodeLoginUrl } = useQrCodeLoginUrl();
  const { getFormRules } = useFormRules();

  const formRef = ref();
  const loading = ref(false);
  const remember = ref(false);

  const formData = reactive({
    tenantCode: 'gitee',
    account: '',
    password: '',
  });

  const { validForm } = useFormValid(formRef);

  let timer;

  //onKeyStroke('Enter', handleLogin);

  const getShow = computed(() => unref(getLoginState) === LoginStateEnum.LOGIN);
  async function handleLogin() {
    const data = await validForm();
    if (!data) return;
    try {
      loading.value = true;
      const userInfo = await userStore.login({
        password: data.password,
        username: data.account,
        tenantCode: data.tenantCode,
        remember: unref(remember),
        mode: 'none',
        sceneStr: ''
      });
      if (userInfo) {
        notification.success({
          message: t('sys.login.loginSuccessTitle'),
          description: `${t('sys.login.loginSuccessDesc')}: ${userInfo.name}`,
          duration: 3,
        });
      }
    } catch (error) {
      let content = (error as unknown as Error).message || t('sys.api.networkExceptionMsg');
      const { response } = error;
      if (response) {
        const { data } = response;
        if (data && data.msg) {
          content = data.msg;
        }
      }
      createErrorModal({
        title: t('sys.api.errorTip'),
        content: content,
        getContainer: () => document.body.querySelector(`.${prefixCls}`) || document.body,
      });
    } finally {
      loading.value = false;
    }
  }

  async function handleQrCodeLogin() {
    const res = await getTicket();
    if (res) {
      setQrCodeLoginUrl('https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=' + res.ticket);
      setLoginState(LoginStateEnum.QR_CODE);
      // 设置定时器，轮询是否登录成功
      timer = window.setInterval(function() {
        handleGetOpenId(res.scene_str);
      }, 3000);
    }
  }

  async function handleGetOpenId(sceneStr: string) {
    const userInfo = await userStore.qrCodeLogin({
      password: '',
      username: '',
      tenantCode: '',
      remember: false,
      mode: 'none',
      sceneStr
    });
    if (userInfo) {
      if (timer) {
        // 清除定时器
        window.clearInterval(timer);
      }
      notification.success({
        message: t('sys.login.loginSuccessTitle'),
        description: `${t('sys.login.loginSuccessDesc')}: ${userInfo.name}`,
        duration: 3,
      });
    }
  }
</script>
