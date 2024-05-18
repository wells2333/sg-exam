<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle">
    <p>{{downloadUrl}}</p>
    <a-button v-clipboard:copy="downloadUrl" v-clipboard:success="onSuccess">复制</a-button>
  </BasicModal>
</template>
<script lang="ts">
import { defineComponent, computed , ref} from 'vue';
import { BasicModal, useModalInner } from '/@/components/Modal';
import { BasicForm } from '/@/components/Form/index';
import {useMessage} from "/@/hooks/web/useMessage";

export default defineComponent({
  name: 'AttachmentInfoModal',
  components: { BasicModal, BasicForm },
  emits: ['success', 'register'],
  setup(_) {
    const { createMessage } = useMessage();
    const downloadUrl = ref<string>('');
    const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
      downloadUrl.value = data.result;
      setModalProps({ confirmLoading: false });
    });
    const getTitle = computed(() => ('查看'));
    const onSuccess = () => {
      createMessage.success('复制成功');
      closeModal();
    }
    return { downloadUrl, onSuccess, registerModal, getTitle };
  },
});
</script>
