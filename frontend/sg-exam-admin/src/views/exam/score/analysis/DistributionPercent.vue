<template>
  <Card title="成绩分布占比" :loading="loading">
    <div ref="chartRef" :style="{ width, height }"></div>
  </Card>
</template>
<script lang="ts" setup>
import {Ref, ref, unref, watch} from 'vue';
import { Card } from 'ant-design-vue';
import { useECharts } from '/@/hooks/web/useECharts';
const props = defineProps({
  loading: Boolean,
  width: {
    type: String as PropType<string>,
    default: '100%',
  },
  height: {
    type: String as PropType<string>,
    default: '300px',
  },
  value: {type: Object},
});
const chartRef = ref<HTMLDivElement | null>(null);
const { setOptions } = useECharts(chartRef as Ref<HTMLDivElement>);
const option = {
  tooltip: {
    trigger: 'item',
  },
  legend: {
    bottom: '1%',
    left: 'top',
  },
  series: [
    {
      color: ['#5ab1ef', '#b6a2de', '#67e0e3', '#2ec7c9'],
      name: '成绩占比',
      type: 'pie',
      top: '-10%',
      radius: ['20%', '60%'],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 10,
        borderColor: '#fff',
        borderWidth: 2,
      },
      label: {
        show: false,
        position: 'bottom',
      },
      emphasis: {
        label: {
          show: true,
          fontSize: '12',
          fontWeight: 'bold',
        },
      },
      labelLine: {
        show: false,
      },
      data: [],
      animationType: 'scale',
      animationEasing: 'exponentialInOut',
      animationDelay: function () {
        return Math.random() * 100;
      },
    },
  ],
}

watch(
  () => props.value,
  () => {
    if (props.value) {
      const data = unref(props.value).scoreDistributionPercent;
      const seriesData = [];
      Object.keys(data).forEach(key => {
        seriesData.push({name: key, value: data[key]});
      })
      option.series[0].data = seriesData
      setOptions(option);
    }
  },
  { immediate: true },
);
</script>
