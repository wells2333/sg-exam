<template>
  <div :class="className" :style="{height:height,width:width}"/>
</template>

<script>
import echarts from 'echarts' // echarts theme
import { debounce } from '@/utils'
require('echarts/theme/macarons')

export default {
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '350px'
    }
  },
  data () {
    return {
      chart: null
    }
  },
  mounted () {
    this.initChart()
    this.__resizeHandler = debounce(() => {
      if (this.chart) {
        this.chart.resize()
      }
    }, 100)
    window.addEventListener('resize', this.__resizeHandler)
  },
  beforeDestroy () {
    if (!this.chart) {
      return
    }
    window.removeEventListener('resize', this.__resizeHandler)
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    initChart () {
      this.chart = echarts.init(this.$el, 'macarons')
      this.chart.setOption({
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        legend: {
          left: 'center',
          bottom: '10',
          data: ['越南', '泰国', '苏丹', '蒙古', '老挝', '韩国', '哈萨克斯坦', '俄罗斯', '阿塞拜疆', '阿富汗']
        },
        calculable: true,
        series: [
          {
            name: '考生分布图',
            type: 'pie',
            roseType: 'radius',
            radius: [15, 95],
            center: ['50%', '38%'],
            data: [
              { value: 1, name: '越南' },
              { value: 3, name: '泰国' },
              { value: 1, name: '苏丹' },
              { value: 1, name: '蒙古' },
              { value: 3, name: '老挝' },
              { value: 1, name: '韩国' },
              { value: 3, name: '哈萨克斯坦' },
              { value: 5, name: '俄罗斯' },
              { value: 1, name: '阿塞拜疆' },
              { value: 1, name: '阿富汗' }
            ],
            animationEasing: 'cubicInOut',
            animationDuration: 2600,
            label: {
              normal: {
                formatter: function (param) {
                  return param.name + '\n' + Math.round(param.percent) + '%'
                }
              }
            }
          }
        ]
      })
    }
  }
}
</script>
