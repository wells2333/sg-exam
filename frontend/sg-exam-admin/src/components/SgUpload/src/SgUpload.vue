<template>
  <div :class="[prefixCls, { fullscreen }]">
    <div class="sg-upload">
      <div class="sg-upload-files">
        <Image v-if="type === 'img' && file"
               style="width: 40px;height: 40px;cursor: pointer;overflow: hidden;" :src="file.url"
               :alt="file.name"/>
        <a target="_blank" v-else-if="showFileList && file" :href="file.url">{{ file.name }}</a>
        <Icon v-if="showFileList && file" icon="ant-design:delete-outlined"
              class="sg-upload-del-btn"
              @click="handleDelete" title="删除"/>
      </div>
      <div class="sg-upload-content">
        <Upload
          name="file"
          :multiple="multiple"
          @change="handleChange"
          :action="uploadUrl"
          :customRequest="uploadFile"
          :before-upload="beforeUpload"
          :showUploadList="showUploadList"
          :accept="accept"
        >
          <a-button type="primary" v-bind="{ ...getButtonProps }" preIcon="carbon:cloud-upload">
            上传
          </a-button>
        </Upload>
      </div>
      <div class="sg-upload-progress">
        <Progress v-show="showProgress" :percent="uploadPercent" size="small"/>
      </div>
    </div>
  </div>
</template>
<script lang="ts">
import {computed, defineComponent, ref, watch} from 'vue';
import {Image, message, Progress, Upload} from 'ant-design-vue';
import type {UploadRequestOption} from 'ant-design-vue/es/vc-upload/interface';
import {useDesign} from '/@/hooks/web/useDesign';
import {
  deleteAttachment,
  mergeChunks,
  prepareUploadChunks,
  uploadChunk
} from "/@/api/attachment/attach";
import {UserService} from "/@/api/services";
import Icon from '/@/components/Icon';
import sparkMD5 from 'spark-md5'
import {isFunction} from "/@/utils/is";

export default defineComponent({
  name: 'SgUpload',
  components: {Upload, Image, Icon, Progress},
  props: {
    value: {
      type: String,
      default: undefined,
    },
    imageId: {
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
      default: '.jpg,.jpeg,.gif,.png,.webp,.avi,.rmvb,.mov,.wmv,.asf,.dat,.mp4,.pdf,.xml,.doc,.dot'
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
    groupCode: {
      type: String,
      default: ''
    }
  },
  emits: ['uploading', 'done', 'error'],
  setup(props, {emit}) {
    let uploading = false;
    const {prefixCls} = useDesign('sg-upload');
    const type = ref<string>(props.type);
    const file = ref(undefined);
    const uploadPercent = ref<number>(0);
    const showProgress = ref<boolean>(false);
    const getButtonProps = computed(() => {
      const {disabled} = props;
      return {
        disabled,
      };
    });
    const groupCode = ref<string>(props.groupCode)
    const ChunkSize = 1024 * 1024 * 10
    interface Chunk {
      hash: string,
      filename: string,
      index: number,
      chunk: Blob
    }

    function handleChange(info: Recordable) {
      const file = info.file;
      const status = file?.status;
      const url = file?.response?.url;
      const name = file?.name;
      uploadPercent.value = 0;
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

    async function handleDelete() {
      if (file && file.value.id) {
        try {
          await deleteAttachment(file.value.id);
        }catch {}
        file.value = undefined;
        if (props.handleDone !== undefined) {
          props.handleDone?.("null");
        }
        message.success('删除成功');
        emit('done', file);
      }
    }

    function resetFile() {
      file.value = undefined;
    }

    function beforeUpload(file: File) {

    }

    async function uploadFile(params: UploadRequestOption<File>) {
      const {api, handleFormData} = props;
      if (handleFormData !== undefined && isFunction(handleFormData)) {
        await props.handleFormData?.(params);
      }
      let data;
      if (api !== undefined && isFunction(api)) {
        params.filename = params.file.name;
        const result = await props.api?.(params,
          function onUploadProgress(progressEvent: ProgressEvent) {
            let v = ((progressEvent.loaded / progressEvent.total) * 100) | 0;
            if (v === 100) {
              v = 99;
            }
            uploadPercent.value = v;
          });
        data = result.data.result;
      } else if (params.file) {
        const file = params.file as File
        // md5 值
        const hash = await calculateHash(file)
        const prepareData = {
          attachName: file.name,
          hash,
          attachSize: file.size
        }
        const prepareRes = await prepareUploadChunks(groupCode.value, prepareData)
        if (prepareRes) {
          const chunks = createFileChunks(file, ChunkSize, hash)
          data = await uploadChunks(chunks, hash, prepareRes)
        } else {
          message.error("准备上传文件失败")
        }
      }
      if (data) {
        const {id, url} = data;
        file.value = {id, name: file.name, url};
        if (props.handleDone !== undefined) {
          props.handleDone?.(file.value);
        }
        uploadPercent.value = 100;
        message.success('上传成功');
        emit('done', file);
      } else {
        message.warning('上传失败');
      }
    }

    async function calculateHash(file: File) {
      return new Promise(resolve => {
        const spark = new sparkMD5.ArrayBuffer()
        const reader = new FileReader()
        const size = file.size
        const offset = 2 * 1024 * 1024
        // 第一个 2M，最后一个区块数据全要
        const chunks = [file.slice(0, offset)]
        let cur = offset
        while (cur < size) {
          if (cur + offset >= size) {
            // 最后一个区快
            chunks.push(file.slice(cur, cur + offset))
          } else {
            // 中间的区块
            const mid = cur + offset / 2
            const end = cur + offset
            chunks.push(file.slice(cur, cur + 2))
            chunks.push(file.slice(mid, mid + 2))
            chunks.push(file.slice(end - 2, end))
          }
          cur += offset
        }
        // 中间的，取前中后各 2 各字节
        reader.readAsArrayBuffer(new Blob(chunks))
        reader.onload = e => {
          spark.append(e?.target?.result as ArrayBuffer)
          resolve(spark.end())
        }
      });
    }

    function createFileChunks(file: File, size = ChunkSize, hash: any) {
      const chunks = ref<any>([])
      let cur = 0
      let index = 0
      const ext = "." + file.name.substring(file.name.lastIndexOf(".") + 1)
      while (cur < file.size) {
        chunks.value.push({
          filename: hash + "-" + index + ext,
          index,
          hash,
          chunk: file.slice(cur, cur + size)
        })
        cur += size
        index++
      }
      return chunks.value
    }

    async function uploadChunks(chunks: Array<Chunk>, hash: any, prepareRes: any) {
      let index = 0
      const taskPool: Array<Promise<any>> = []
      // 设置浏览器运行最大并发数  目前 6 个为当前的主流
      const max = 6
      let allProgress = index
      let uploadId = ''
      if (prepareRes) {
        uploadId = prepareRes.uploadId
      }
      while (index < chunks.length) {
        const chunk = chunks[index]
        const params = {
          file: chunk.chunk,
          data: {
            filename: chunk.filename,
            uploadId
          }
        };
        // 分片 ID 从 1 开始
        const task = uploadChunk(params, chunk.hash, chunk.index + 1, (progress) => {
            if (progress.loaded === progress.total) {
              allProgress++;
              let percent = Math.floor(((allProgress / chunks.length) * 100) * 10) / 10
              if (percent === 100) {
                percent = 99;
              }
              uploadPercent.value = percent
            }
          }
        )
        task.then(() => {
          taskPool.splice(taskPool.findIndex(item => item === task), 1)
        })
        taskPool.push(task)
        if (taskPool.length === max) {
          // 竞赛等出一个执行完毕的请求
          await Promise.race(taskPool)
        }
        index++
      }
      await Promise.all(taskPool)
      return await mergeChunks(hash)
    }

    watch(
      () => props.value,
      async (val: string, prevVal: string) => {
        resetFile();
        if (val) {
          file.value = {url:val,id:props.imageId};
        }
      },
    );
    return {
      prefixCls,
      type,
      file,
      uploadPercent,
      showProgress,
      handleChange,
      uploadFile,
      getButtonProps,
      handleDelete,
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
