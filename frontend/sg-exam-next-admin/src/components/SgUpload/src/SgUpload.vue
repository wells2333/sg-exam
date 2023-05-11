<template>
  <div :class="[prefixCls, { fullscreen }]">
    <div class="sg-upload">
      <div class="sg-upload-files">
        <Image v-if="type === 'img' && file" style="width: 40px;height: 40px;cursor: pointer;overflow: hidden;" :src="file.url" :alt="file.name"/>
        <a target="_blank" v-else-if="showFileList && file" :href="file.url">{{file.name}}</a>
        <Icon v-if="showFileList && file" icon="ant-design:delete-outlined" class="sg-upload-del-btn"
              @click="handleDelete" title="删除"/>
      </div>
      <div class="sg-upload-content">
        <Upload
          name="file"
          :multiple="multiple"
          @change="handleChange"
          :action="uploadUrl"
          :customRequest="customRequest"
          :before-upload="beforeUpload"
          :showUploadList="showUploadList"
          :accept="accept"
        >
          <a-button type="primary" v-bind="{ ...getButtonProps }" preIcon="carbon:cloud-upload">上传</a-button>
        </Upload>
      </div>
      <div class="sg-upload-progress">
        <Progress v-show="showProgress" :percent="percent" size="small" />
      </div>
    </div>
  </div>
</template>
<script lang="ts">
import {computed, defineComponent, ref, watch} from 'vue';
import {Image, message, Progress, Upload} from 'ant-design-vue';
import {useDesign} from '/@/hooks/web/useDesign';
import {deleteAttachment, getAttachment, uploadApi} from "/@/api/attachment/attach";
import {UserService} from "/@/api/services";
import Icon from '/@/components/Icon';
import {isFunction} from "/@/utils/is";

export default defineComponent({
  name: 'SgUpload',
  components: {Upload, Image, Icon, Progress},
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
    handleFormData: {
      type: Function,
      default: undefined
    },
    handleDone: {
      type: Function,
      default: undefined
    },
    uploadUrl: {
      type: String,
      default: UserService + '/v1/attachment/upload',
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
    },
    showFileList: {
      type: Boolean,
      default: true,
    },
  },
  emits: ['uploading', 'done', 'error'],
  setup(props, {emit}) {
    let uploading = false;
    const {prefixCls} = useDesign('sg-upload');
    const type = ref<string>(props.type);
    const file = ref(undefined);
    const percent = ref<number>(0);
    const showProgress = ref<boolean>(false);
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
      percent.value = 0;
      showProgress.value = true;
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
      const {api, handleFormData} = props;
      if (handleFormData !== undefined && isFunction(handleFormData)) {
        await props.handleFormData?.(formData);
      }
      let result;
      if (api !== undefined && isFunction(api)) {
        result = await props.api?.(formData,
          function onUploadProgress(progressEvent: ProgressEvent) {
            let v = ((progressEvent.loaded / progressEvent.total) * 100) | 0;
            if (v === 100) {
              v = 99;
            }
            percent.value = v;
        });
      } else {
        result = await uploadApi(formData, function onUploadProgress(progressEvent: ProgressEvent) {
          let v = ((progressEvent.loaded / progressEvent.total) * 100) | 0;
          if (v === 100) {
            v = 99;
          }
          percent.value = v;
        });
      }
      const {data} = result;
      if (data && data.code === 0) {
        const {id, url} = data.result;
        file.value = {id, name: formData.file.name, url};
        emit('done', file);
        if (props.handleDone !== undefined) {
          props.handleDone?.(file.value);
        }
        percent.value = 100;
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

    function beforeUpload(file: File) {

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
            showProgress.value = false;
          }
        }
      },
    );
    return {
      prefixCls,
      type,
      file,
      percent,
      showProgress,
      handleChange,
      customRequest,
      getButtonProps,
      handleClickImg,
      handleDelete,
      resetFile,
      beforeUpload,
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
  display: flex;
  align-items: center;
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

.sg-upload-progress {
  width: 120px;
  margin-left: 10px;
}
</style>
