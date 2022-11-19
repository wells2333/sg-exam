<template>
  <BasicModal v-bind="$attrs" @register="registerModal" @ok="handleSubmit">
    <Upload
      accept=".jpg,.jpeg,.png"
      list-type="picture-card"
      class="avatar-uploader"
      v-model:file-list="fileList"
      :multiple="false"
      :show-upload-list="false"
      :action="action"
      :customRequest="customRequest"
    >
      <img v-if="imageUrl !== ''" :src="imageUrl" alt="avatar" />
      <div>
        <plus-outlined></plus-outlined>
        <div class="ant-upload-text">选择图片</div>
      </div>
    </Upload>
  </BasicModal>
</template>
<script lang="ts">
import { message, Upload } from 'ant-design-vue';
import { PlusOutlined, LoadingOutlined } from '@ant-design/icons-vue';
import { BasicModal, useModalInner } from '/@/components/Modal';
import { UploadOutlined } from '@ant-design/icons-vue';
import {defineComponent, ref, unref} from 'vue';
import {UserService} from '/@/api/services';
import { uploadApi } from '/@/api/attachment/attach';
import { updateCourse} from "/@/api/exam/course";

export default defineComponent({
  components: { BasicModal, UploadOutlined, Upload,  LoadingOutlined, PlusOutlined,},
  emits: ['success', 'register'],
  setup(_, { emit }) {
    const loading = ref<boolean>(false);
    const imageUrl = ref<string>('');
    let course = ref<Object>({});
    const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
      setModalProps({ confirmLoading: false });
      course = data;
      const logoUrl = unref(data).logoUrl;
      if (logoUrl && logoUrl !== '') {
        imageUrl.value = logoUrl;
      }
    });

    async function customRequest(formData) {
      formData.filename = formData.file.name;
      formData.data.groupCode = 'course';
      const result = await uploadApi(formData);
      const { data } = result;
      if (data && data.code === 0) {
        const {id, url} = data.result;
        unref(course).imageId = id;
        imageUrl.value = url;
        const updateResult = await updateCourse(unref(course).id, course);
        if (updateResult) {
          message.success('上传成功');
          loading.value = false;
        } else {
          message.error('上传失败');
        }
      } else {
        message.error('上传失败');
      }
    }

    function handleSubmit() {
      closeModal();
      emit('success');
    }

    const fileList = ref([]);
    return {
      loading,
      imageUrl,
      customRequest,
      action: UserService + 'v1/attachment/upload',
      fileList,
      headers: {
        authorization: 'authorization-text',
      },
      registerModal,
      handleSubmit
    };
  },
});
</script>

<style>
.avatar-uploader > .ant-upload {
  width: 128px;
  height: 128px;
}
.ant-upload-select-picture-card i {
  font-size: 32px;
  color: #999;
}

.ant-upload-select-picture-card .ant-upload-text {
  margin-top: 8px;
  color: #666;
}
</style>
