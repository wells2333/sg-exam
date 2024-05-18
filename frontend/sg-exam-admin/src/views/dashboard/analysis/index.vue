<template>
  <div class="p-4">
    <GrowCard :loading="loading" :userNumber="userNumber" :examNumber="examNumber" :recordNumber="recordNumber" :oUserNumber="oUserNumber" :tenantNumber="tenantNumber" class="enter-y" />
    <SiteAnalysis class="!my-4 enter-y" :loading="loading" />
  </div>
</template>
<script lang="ts">
import {defineComponent, ref} from 'vue';
import GrowCard from './components/GrowCard.vue';
import SiteAnalysis from './components/SiteAnalysis.vue';
import VisitSource from './components/VisitSource.vue';
import VisitRadar from './components/VisitRadar.vue';
import SalesProductPie from './components/SalesProductPie.vue';

export default defineComponent({
  name: 'DashboardAnalysis',
  components: { GrowCard, SiteAnalysis, VisitSource, VisitRadar, SalesProductPie },
  setup() {
    const loading = ref(true);
    setTimeout(() => {
      loading.value = false;
    }, 800);
    return { loading };
  },
});
</script>
