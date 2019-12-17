<template>
  <div class="dashboard-editor-container">

    <panel-group @handleSetLineChartData="handleSetLineChartData"/>

    <el-row class="chart-wrapper" style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
      <div class="chart-wrapper-header">
        <div>趋势</div>
      </div>
      <div class="chart-wrapper-body">
        <line-chart :chart-data="lineChartData"/>
      </div>
    </el-row>

    <el-row :gutter="32">
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <div class="chart-wrapper-header">
            <div>趋势</div>
          </div>
          <div class="chart-wrapper-body">
            <raddar-chart/>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <div class="chart-wrapper-header">
            <div>趋势</div>
          </div>
          <div class="chart-wrapper-body">
            <pie-chart/>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <div class="chart-wrapper-header">
            <div>趋势</div>
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
import BarChart from './components/BarChart'

const lineChartData = {
  newVisitis: {
    expectedData: [100, 120, 161, 134, 105, 160, 165],
    actualData: [120, 82, 91, 154, 162, 140, 145]
  },
  messages: {
    expectedData: [200, 192, 120, 144, 160, 130, 140],
    actualData: [180, 160, 151, 106, 145, 150, 130]
  },
  purchases: {
    expectedData: [80, 100, 121, 104, 105, 90, 100],
    actualData: [120, 90, 100, 138, 142, 130, 130]
  },
  shoppings: {
    expectedData: [130, 140, 141, 142, 145, 150, 160],
    actualData: [120, 82, 91, 154, 162, 140, 130]
  }
}

export default {
  name: 'DashboardAdmin',
  components: {
    PanelGroup,
    LineChart,
    RaddarChart,
    PieChart,
    BarChart
  },
  data () {
    return {
      lineChartData: lineChartData.newVisitis
    }
  },
  methods: {
    handleSetLineChartData (type) {
      this.lineChartData = lineChartData[type]
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .dashboard-editor-container {
    padding: 20px;
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
    }
    .chart-wrapper-body {
      padding-top: 20px;
    }
  }
</style>
