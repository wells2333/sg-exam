<template>
  <Card
    :tab-list="tabListTitle"
    v-bind="$attrs"
    :active-tab-key="activeKey"
    @tabChange="onTabChange"
  >
    <p v-if="activeKey === 'tab1'">
      <VisitAnalysis />
    </p>
  </Card>
</template>
<script lang="ts">
import {defineComponent, ref} from 'vue';
import { Card } from 'ant-design-vue';
import VisitAnalysis from './VisitAnalysis.vue';

export default defineComponent({
  name: 'DashboardTendency',
  components: { Card, VisitAnalysis },
  setup() {
    const activeKey = ref('tab1');
    const tabListTitle = [
      {
        key: 'tab1',
        tab: '考试数量',
      }
    ];

    function onTabChange(key) {
      activeKey.value = key;
    }
    return {activeKey, tabListTitle, onTabChange }
  }
});

</script>
