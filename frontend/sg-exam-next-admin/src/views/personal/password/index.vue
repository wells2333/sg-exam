<template>
  <PageWrapper title="修改当前用户密码" content="修改成功后会自动退出当前登录！">
    <div class="py-8 bg-white flex flex-col justify-center items-center">
      <BasicForm @register="register" />
      <div class="flex justify-center">
        <a-button @click="resetFields"> 重置 </a-button>
        <a-button class="!ml-4" type="primary" @click="handleSubmit"> 确认 </a-button>
      </div>
    </div>
  </PageWrapper>
</template>
<script lang="ts">
import { defineComponent } from 'vue';
import { PageWrapper } from '/@/components/Page';
import { BasicForm, useForm } from '/@/components/Form';
import { updatePassword } from '/@/api/sys/user';
import { useRouter } from 'vue-router';
import { formSchema } from './pwd.data';
import { useMessage } from "/@/hooks/web/useMessage";
import { useUserStore } from "/@/store/modules/user";

export default defineComponent({
  name: 'ChangePassword',
  components: { BasicForm, PageWrapper },
  setup() {
    const { createMessage } = useMessage();
    const userStore = useUserStore();
    const [register, { validate, resetFields }] = useForm({
      size: 'large',
      labelWidth: 100,
      showActionButtonGroup: false,
      schemas: formSchema,
    });

    async function handleSubmit() {
      try {
        const values = await validate();
        const { passwordOld, passwordNew } = values;
        const { identifier } = userStore.getUserInfo;
        const params = {
          identifier,
          oldPassword: passwordOld,
          newPassword: passwordNew
        };
        const result = await updatePassword(params);
        if (result) {
          createMessage.success('保存成功');
        } else {
          createMessage.warn('保存失败');
        }
        const { router } = useRouter();
        router.push(pageEnum.BASE_LOGIN);
      } catch (error) {}
    }

    return { register, resetFields, handleSubmit };
  },
});
</script>
