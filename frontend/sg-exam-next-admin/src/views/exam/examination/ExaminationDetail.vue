<template>
  <BasicDrawer v-bind="$attrs" @register="registerDrawer" :title="t('common.viewText')" width="50%">
    <Description
      title=""
      :bordered="true"
      :column="1"
      :data="record"
      :schema="examinationDetailFormSchema"
    />
  </BasicDrawer>
</template>
<script lang="ts">
import { useI18n } from '/@/hooks/web/useI18n';
import { defineComponent, ref } from 'vue';
import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
import { Description } from '/@/components/Description';
import { examinationDetailFormSchema } from './examination.data';

const record = ref({});

export default defineComponent({
  name: 'ExaminationDetailDrawer',
  components: { BasicDrawer, Description },
  emits: ['success', 'register'],
  setup(_) {
    const { t } = useI18n();
    const [registerDrawer] = useDrawerInner(async (data) => {
      record.value = data.record;
    });

    return {
      t,
      registerDrawer,
      record,
      examinationDetailFormSchema,
    };
  },
});
</script>
