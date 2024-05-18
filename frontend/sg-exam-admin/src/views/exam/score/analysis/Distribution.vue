<template>
  <div ref="chartRef" :style="{ height, width }"></div>
</template>
<script lang="ts" setup>
import {basicProps} from './props';
import {defineProps, onMounted, ref, Ref, unref, watch} from 'vue';
import {useECharts} from '/@/hooks/web/useECharts';

const props = defineProps({
  ...basicProps,
  value: {type: Object},
});

const chartRef = ref<HTMLDivElement | null>(null);
const {setOptions} = useECharts(chartRef as Ref<HTMLDivElement>);
const option = {
  title: {
    text: '成绩分布图',
    x: 'center',
    y: 'bottom',
    textStyle: {
      fontFamily: "Arial",
      fontSize: 14,
      fontStyle: "normal",
      fontWeight: "normal",
      color: "#333",
    }
  },
  tooltip: {
    trigger: 'axis',
    axisPointer: {
      lineStyle: {
        width: 1,
        color: '#019680',
      },
    },
  },
  grid: {left: '1%', right: '1%', top: '2  %', bottom: '10%', containLabel: true},
  xAxis: {
    type: 'category',
    data: [],
  },
  yAxis: {
    type: 'value',
    name: '人数',
    max: 100,
    splitNumber: 4,
    nameTextStyle: {
      color: '#019680'
    }
  },
  series: [
    {
      data: [],
      type: 'bar',
      barMaxWidth: 80,
    },
  ],
}

onMounted(() => {
  watch(
    () => unref(props).value,
    (value) => {
      if (value) {
        const data = unref(value).scoreDistribution;
        if (data) {
          option.xAxis.data = Object.keys(data);
          option.series[0].data = Object.values(data);
          setOptions(option);
        }
      }
    },
  );
});
</script>
