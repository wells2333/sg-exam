<template>
  <div :class="[prefixCls, { fullscreen }]">
    <div class="sg-upload">
      <div class="sg-upload-files">
        <Image v-if="type === 'img' && file" style="width: 40px;height: 40px;cursor: pointer;overflow: hidden;" :src="file.url" :alt="file.name"/>
        <a target="_blank" v-else-if="file">{{file.name}}</a>
        <Icon v-if="file" icon="ant-design:delete-outlined" class="sg-upload-del-btn"
              @click="handleDelete" title="删除"/>
      </div>
      <div class="sg-upload-content">
        <Upload
          name="file"
          :multiple="multiple"
          @change="handleChange"
          :action="uploadUrl"
          :customRequest="customRequest"
          :showUploadList="showUploadList"
          :accept="accept"
        >
          <a-button type="primary" v-bind="{ ...getButtonProps }" preIcon="carbon:cloud-upload">上传</a-button>
        </Upload>
      </div>
    </div>
  </div>
</template>
<script lang="ts">
import {computed, defineComponent, ref, watch} from 'vue';

import {Image, message, Upload} from 'ant-design-vue';
import {useDesign} from '/@/hooks/web/useDesign';
import {deleteAttachment, getAttachment, uploadApi} from "/@/api/attachment/attach";
import {UserService} from "/@/api/services";
import Icon from '/@/components/Icon';
import {isFunction} from "/@/utils/is";

export default defineComponent({
  name: 'SgUpload',
  components: {Upload, Image, Icon},
  props: {
    value: {
      type: String,
      default: undefined,
    },
    type: {
      type: String,
      default: 'img'
    },
    api: {
      type: Function,
      default: undefined
    },
    handleDone: {
      type: Function,
      default: undefined
    },
    uploadUrl: {
      type: String,
      default: UserService + 'v1/attachment/upload',
    },
    accept: {
      type: String,
      default: '.jpg,.jpeg,.gif,.png,.webp,.avi,.rmvb,.mov,.wmv,.asf,.dat,.mp4'
    },
    showUploadList: {
      type: Boolean,
      default: false
    },
    multiple: {
      type: Boolean,
      default: false
    },
    fullscreen: {
      type: Boolean,
    },
    disabled: {
      type: Boolean,
      default: false,
    },
    uploadParams: {
      type: Object,
      default: undefined
    }
  },
  emits: ['uploading', 'done', 'error'],
  setup(props, {emit}) {
    let uploading = false;
    const {prefixCls} = useDesign('sg-upload');
    const type = ref<string>(props.type);
    const file = ref(undefined);
    const getButtonProps = computed(() => {
      const {disabled} = props;
      return {
        disabled,
      };
    });

    function handleChange(info: Recordable) {
      const file = info.file;
      const status = file?.status;
      const url = file?.response?.url;
      const name = file?.name;
      if (status === 'uploading') {
        if (!uploading) {
          emit('uploading', name);
          uploading = true;
        }
      } else if (status === 'done') {
        emit('done', name, url);
        uploading = false;
      } else if (status === 'error') {
        emit('error');
        uploading = false;
      }
    }

    async function customRequest(formData) {
      formData.filename = formData.file.name;
      const {api} = props;
      let result;
      if (api !== undefined && isFunction(api)) {
        result = await props.api?.(formData);
      } else {
        result = await uploadApi(formData);
      }
      const {data} = result;
      if (data && data.code === 0) {
        const {id, url} = data.result;
        file.value = {id, name: formData.file.name, url};
        emit('done', file);
        // 回调handleDone
        if (props.handleDone !== undefined) {
          props.handleDone?.(file.value);
        }
        message.success('上传成功');
      } else {
        message.error('上传失败');
      }
    }

    function handleClickImg() {
      if (file && file.value.url) {
        window.open(file.value.url);
      }
    }

    async function handleDelete() {
      if (file && file.value.url) {
        await deleteAttachment(file.value.id);
        file.value = undefined;
        message.success('删除成功');
      }
    }

    function resetFile() {
      file.value = undefined;
    }

    watch(
      () => props.value,
      async (val: string, prevVal: string) => {
        resetFile();
        if (val) {
          const res = await getAttachment(val);
          if (res) {
            const {id, attachName, url} = res;
            if (id) {
              file.value = {id, name: attachName, url};
            }
          }
        }
      },
    );
    return {
      prefixCls,
      type,
      file,
      handleChange,
      customRequest,
      getButtonProps,
      handleClickImg,
      handleDelete,
      resetFile,
    };
  },
});
</script>
<style lang="less" scoped>
@prefix-cls: ~'@{namespace}-sg-upload';

.@{prefix-cls} {
  position: absolute;
  top: 0px;
  z-index: 20;

  &.fullscreen {
    position: fixed;
    z-index: 10000;
  }
}

.sg-upload {
  display: inline-flex;
}

.sg-upload-files {
  display: flex;
  align-items: center;
}

.sg-upload-del-btn {
  margin-left: 20px;
  margin-right: 20px;
  color: #ff4d4f;
  cursor: pointer;
}
</style>
