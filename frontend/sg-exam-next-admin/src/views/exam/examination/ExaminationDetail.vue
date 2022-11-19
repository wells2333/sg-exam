<template>
  <BasicDrawer v-bind="$attrs" @register="registerDrawer" title="查看详情" width="50%">
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
    const [registerDrawer] = useDrawerInner(async (data) => {
      record.value = data.record;
    });

    return {
      registerDrawer,
      record,
      examinationDetailFormSchema,
    };
  },
});
</script>
