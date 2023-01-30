<template>
  <CollapseContainer title="基本设置" :canExpan="false">
    <a-row :gutter="24">
      <a-col :span="14">
        <BasicForm @register="register" @submit="handleSubmit"/>
      </a-col>
      <a-col :span="10">
        <div class="change-avatar">
          <div class="mb-2">头像</div>
          <CropperAvatar
            :uploadApi="uploadApi"
            :groupCode="groupCode"
            :value="avatar"
            btnText="更换头像"
            :btnProps="{ preIcon: 'ant-design:cloud-upload-outlined' }"
            @change="updateAvatar"
            width="150"
          />
        </div>
      </a-col>
    </a-row>
  </CollapseContainer>
</template>
<script lang="ts">
  import { Button, Row, Col } from 'ant-design-vue';
  import { computed, defineComponent, onMounted } from 'vue';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { CollapseContainer } from '/@/components/Container';
  import { CropperAvatar } from '/@/components/Cropper';

  import { useMessage } from '/@/hooks/web/useMessage';

  import headerImg from '/@/assets/images/header.jpg';
  import {getUserInfo, updateUserAvatar, uploadUserAvatar, updateUserInfo} from '/@/api/sys/user';
  import { baseSchemas } from './data';
  import { useUserStore } from '/@/store/modules/user';

  export default defineComponent({
    components: {
      BasicForm,
      CollapseContainer,
      Button,
      ARow: Row,
      ACol: Col,
      CropperAvatar,
    },
    setup() {
      const { createMessage } = useMessage();
      const userStore = useUserStore();

      const [register, { setFieldsValue, validate }] = useForm({
        labelWidth: 120,
        schemas: baseSchemas,
        showActionButtonGroup: true,
        showResetButton: false,
        submitButtonOptions: {
          text: '保存'
        }
      });

      onMounted(async () => {
        const data = await getUserInfo();
        setFieldsValue(data);
      });

      const avatar = computed(() => {
        const { avatar } = userStore.getUserInfo;
        return avatar || headerImg;
      });

      async function updateAvatar({ data}) {
        const userinfo = userStore.getUserInfo;
        const result = await updateUserAvatar({id: userinfo.id, identifier: userinfo.identifier, avatarId: data.result.id});
        if (result) {
          userinfo.avatar = result;
        }
        userStore.setUserInfo(userinfo);
      }

      async function handleSubmit() {
        const values = await validate();
        const result = await updateUserInfo(values);
        if (result) {
          createMessage.success('更新成功');
        } else {
          createMessage.warn('更新失败');
        }
      }

      return {
        avatar,
        register,
        uploadApi: uploadUserAvatar as any,
        groupCode: 'user_avatar',
        updateAvatar,
        handleSubmit
      };
    },
  });
</script>

<style lang="less" scoped>
  .change-avatar {
    img {
      display: block;
      margin-bottom: 15px;
      border-radius: 50%;
    }
  }
</style>
