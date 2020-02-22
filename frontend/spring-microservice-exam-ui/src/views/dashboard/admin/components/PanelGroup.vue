<template>
  <el-row :gutter="40" class="panel-group">
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel" @click="handleSetLineChartData('purchases')">
        <div class="card-panel-icon-wrapper icon-tab">
          <svg-icon icon-class="tab" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">单位数</div>
          <count-to :start-val="0" :end-val="tenantNumber" :duration="2400" class="card-panel-num"/>
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel" @click="handleSetLineChartData('newVisitis')">
        <div class="card-panel-icon-wrapper icon-people">
          <svg-icon icon-class="peoples" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">用户数</div>
          <count-to :start-val="0" :end-val="onlineUserNumber" :duration="2000" class="card-panel-num"/>
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel" @click="handleSetLineChartData('messages')">
        <div class="card-panel-icon-wrapper icon-form">
          <svg-icon icon-class="form" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">考试数</div>
          <count-to :start-val="0" :end-val="examinationNumber" :duration="2200" class="card-panel-num"/>
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel" @click="handleSetLineChartData('shoppings')">
        <div class="card-panel-icon-wrapper icon-chart">
          <svg-icon icon-class="chart" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">考试次数</div>
          <count-to :start-val="0" :end-val="examinationRecordNumber" :duration="2600" class="card-panel-num"/>
        </div>
      </div>
    </el-col>
  </el-row>
</template>

<script>
import CountTo from 'vue-count-to'
import { getDashboard } from '@/api/admin/sys'
import { isNotEmpty, isSuccess } from '@/utils/util'

export default {
  components: {
    CountTo
  },
  data () {
    return {
      onlineUserNumber: 0,
      examinationNumber: 0,
      examUserNumber: 0,
      tenantNumber: 0,
      examinationRecordNumber: 0
    }
  },
  created () {
    // 获取首页数据
    this.getDashboardData()
  },
  methods: {
    handleSetLineChartData (type) {
      this.$emit('handleSetLineChartData', type)
    },
    getDashboardData () {
      getDashboard().then(response => {
        if (isSuccess(response)) {
          const data = response.data.data
          if (isNotEmpty(data.onlineUserNumber)) {
            this.onlineUserNumber = parseInt(data.onlineUserNumber)
          }
          if (isNotEmpty(data.examinationNumber)) {
            this.examinationNumber = parseInt(data.examinationNumber)
          }
          if (isNotEmpty(data.examUserNumber)) {
            this.examUserNumber = parseInt(data.examUserNumber)
          }
          if (isNotEmpty(data.tenantCount)) {
            this.tenantNumber = parseInt(data.tenantCount)
          }
          if (isNotEmpty(data.examinationRecordNumber)) {
            this.examinationRecordNumber = parseInt(data.examinationRecordNumber)
          }
        }
      }).catch(error => {
        console.error(error)
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.panel-group {
  .card-panel-col{
    margin-bottom: 32px;
  }
  .card-panel {
    height: 108px;
    cursor: pointer;
    font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #666;
    background: #fff;
    box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
    border-color: rgba(0, 0, 0, .05);
    border-radius: 6px;
    &:hover {
      .card-panel-icon-wrapper {
        color: #fff;
      }
      .icon-people {
         background: #40c9c6;
      }
      .icon-form {
        background: #36a3f7;
      }
      .icon-chart {
        background: #f4516c;
      }
      .icon-tab {
        background: #34bfa3
      }
    }
    .icon-people {
      color: #40c9c6;
    }
    .icon-form {
      color: #36a3f7;
    }
    .icon-chart {
      color: #f4516c;
    }
    .icon-tab {
      color: #34bfa3
    }
    .card-panel-icon-wrapper {
      float: left;
      margin: 14px 0 0 14px;
      padding: 16px;
      transition: all 0.38s ease-out;
      border-radius: 6px;
    }
    .card-panel-icon {
      float: left;
      font-size: 48px;
    }
    .card-panel-description {
      float: right;
      font-weight: bold;
      margin: 26px;
      margin-left: 0px;
      .card-panel-text {
        line-height: 18px;
        color: rgba(0, 0, 0, 0.45);
        font-size: 16px;
        margin-bottom: 12px;
      }
      .card-panel-num {
        font-size: 20px;
      }
    }
  }
}
</style>
