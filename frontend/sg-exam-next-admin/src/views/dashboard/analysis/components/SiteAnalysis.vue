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
    <p v-if="activeKey === 'tab2'">
      <VisitAnalysisBar />
    </p>
  </Card>
</template>
<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import { Card } from 'ant-design-vue';
import VisitAnalysis from './VisitAnalysis.vue';
import VisitAnalysisBar from './VisitAnalysisBar.vue';
import { getDashboardTendency } from "/@/api/sys/dashboard";

export default defineComponent({
  name: 'DashboardTendency',
  components: { Card, VisitAnalysis, VisitAnalysisBar },
  setup() {
    const activeKey = ref('tab1');
    const tabListTitle = [
      {
        key: 'tab1',
        tab: '流量趋势',
      },
      {
        key: 'tab2',
        tab: '访问量',
      },
    ];

    function onTabChange(key) {
      activeKey.value = key;
    }

    async function fetch() {
      const result = await getDashboardTendency({pastDays: 7});
    }
    onMounted(() => {
      fetch();
    });
    return {activeKey, tabListTitle,onTabChange }
  }
});

</script>
