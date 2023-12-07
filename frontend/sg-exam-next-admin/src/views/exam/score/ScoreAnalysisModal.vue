<template>
  <div>
    <BasicModal v-bind="$attrs" @register="registerModal" title="成绩分析" @ok="handleSubmit"
                width="90%">
    </BasicModal>
  </div>
</template>
<script lang="ts">
import {useI18n} from '/@/hooks/web/useI18n';
import {defineComponent, computed, ref, unref} from 'vue';
import {BasicTable, TableAction} from '/@/components/Table';
import {BasicModal, useModalInner} from '/@/components/Modal';
import {BasicForm} from '/@/components/Form/index';

export default defineComponent({
  name: 'ScoreAnalysisModal',
  components: {BasicTable, TableAction, BasicModal, BasicForm},
  emits: ['success', 'register'],
  setup(_) {
    const {t} = useI18n();
    const examinationId = ref<any>(undefined);
    const [registerModal, {setModalProps, closeModal}] = useModalInner(async (data) => {
      examinationId.value = data?.id || undefined;
      setModalProps({confirmLoading: false});
    });

    const getTitle = computed(() => t('common.viewText'));

    function handleSubmit() {

    }
    return {t, registerModal, handleSubmit, getTitle};
  },
});
</script>
