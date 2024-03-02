<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit"
              width="40%">
    <div v-if="value !== ''" style="text-align: center;">
      <QrCode :value="value"/>
    </div>
  </BasicModal>
</template>
<script lang="ts">
import {useI18n} from '/@/hooks/web/useI18n';
import {defineComponent, ref, computed} from 'vue';
import {BasicModal, useModalInner} from '/@/components/Modal';
import { QrCode } from '/@/components/Qrcode/index';

export default defineComponent({
  name: 'ExaminationQrCodeModal',
  components: {BasicModal, QrCode},
  emits: ['success', 'register'],
  setup(_) {
    const {t} = useI18n();
    const value = ref<string>('');

    const [registerModal, {setModalProps, closeModal}] = useModalInner(async (data) => {
      setModalProps({confirmLoading: false});
      value.value = data;
    });

    const getTitle = computed(() => t('common.generateQrCodeText'));

    async function handleSubmit() {
      closeModal();
    }

    return {
      t,
      registerModal,
      getTitle,
      value,
      handleSubmit
    };
  },
});
</script>
