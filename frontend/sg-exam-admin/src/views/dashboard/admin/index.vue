<template>
  <div class="dashboard-editor-container">

    <panel-group @handleSetLineChartData="handleSetLineChartData"/>

    <el-row class="chart-wrapper" style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
      <div class="chart-wrapper-header">
        <div>考试次数</div>
        <div class="chart-wrapper-header-select">
          <el-select v-model="query.pastDays" placeholder="请选择" size="mini" @change="getExamRecordData">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </div>
      </div>
      <div class="chart-wrapper-body">
        <line-chart :chart-data="lineChartData"/>
      </div>
    </el-row>
    <el-row :gutter="32">
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <div class="chart-wrapper-header">
            <div>难度等级分布</div>
          </div>
          <div class="chart-wrapper-body">
            <another-pie-chart/>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <div class="chart-wrapper-header">
            <div>考生分布图</div>
          </div>
          <div class="chart-wrapper-body">
            <pie-chart/>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <div class="chart-wrapper-header">
            <div>考生水平分布</div>
          </div>
          <div class="chart-wrapper-body">
            <bar-chart/>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import PanelGroup from './components/PanelGroup'
import LineChart from './components/LineChart'
import RaddarChart from './components/RaddarChart'
import PieChart from './components/PieChart'
import AnotherPieChart from './components/AnotherPieChart'
import BarChart from './components/BarChart'
import { getExamRecordTendency } from '@/api/admin/sys'

const dashboardData = {
  examRecordData: {
    data: [120, 82, 91, 154, 162, 140, 145],
    date: ['星期一', '星期二', '星期三', '星期四', '星期五', '星期六', '星期日']
  },
  messages: {
    examRecordData: [180, 160, 151, 106, 145, 150, 130]
  },
  purchases: {
    examRecordData: [120, 90, 100, 138, 142, 130, 130]
  },
  shoppings: {
    examRecordData: [120, 82, 91, 154, 162, 140, 130]
  }
}

export default {
  name: 'DashboardAdmin',
  components: {
    PanelGroup,
    LineChart,
    RaddarChart,
    PieChart,
    AnotherPieChart,
    BarChart
  },
  created () {
    this.getExamRecordData()
  },
  data () {
    return {
      query: {
        pastDays: 7
      },
      lineChartData: {
        examRecordData: [],
        examRecordDate: []
      },
      options: [{
        value: 7,
        label: '过去7天'
      }, {
        value: 15,
        label: '过去15天'
      }, {
        value: 30,
        label: '过去30天'
      }]
    }
  },
  methods: {
    handleSetLineChartData (type) {
      this.lineChartData = dashboardData[type]
    },
    getExamRecordData () {
      getExamRecordTendency(this.query).then(response => {
        this.lineChartData = response.data.data
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .dashboard-editor-container {
    padding: 12px;
    .chart-wrapper {
      background: #fff;
      padding: 16px 16px 0;
      margin-bottom: 32px;
      box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
    }
    .chart-wrapper-header {
      padding: 8px 12px;
      border-bottom: 1px solid #EBEEF5;
      -webkit-box-sizing: border-box;
      box-sizing: border-box;
      .chart-wrapper-header-select {
        position: absolute;
        right: 0;
        top: 6px;
        padding: 8px 12px;
      }
    }
    .chart-wrapper-body {
      padding-top: 12px;
    }
  }
</style>
