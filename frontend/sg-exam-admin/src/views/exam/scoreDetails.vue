<template>
  <div class="details-container">
    <el-row :gutter="40">
      <el-col :span="24">
        <el-card class="score-info">
          <div slot="header">
            <span>成绩详情</span>
          </div>
          <el-row>
            <el-col :span="6">
              <div class="user-info">
                <span class="user-info-item" :title="examRecord.examinationName">考试名称：{{ examRecord.examinationName | simpleStrFilter }}</span>
                <br>
                <span class="user-info-item" v-show="examRecord.userName !== undefined && examRecord.userName !== ''">考生姓名：{{ examRecord.userName }}</span>
                <br>
                <span class="user-info-item" v-show="examRecord.deptName !== undefined && examRecord.deptName !== ''">所属部门：{{ examRecord.deptName }}</span>
                <br>
                <span class="user-info-item" v-show="examRecord.startTime !== undefined">开始时间：
                  {{ examRecord.startTime | fmtDate('yyyy.MM.dd hh:mm') }}
                </span>
                <span class="user-info-item" v-show="examRecord.endTime !== undefined">结束时间：
                  {{ examRecord.endTime | fmtDate('yyyy.MM.dd hh:mm') }}
                </span>
                <br>
              </div>
            </el-col>
            <el-col :span="3">
              <div class="description">
                <div>成绩</div>
                <div class="description-score">{{ examRecord.score }}</div>
              </div>
            </el-col>
            <el-col :span="3">
              <div class="description">
                <div>总耗时</div>
                <div class="description-score">{{ examRecord.duration }}</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="score-chart">
                <div class="chart" ref="chart" style="height: 150px; width: 100%;"></div>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="24">
        <el-card class="subject-list">
          <el-table
            ref="multipleTable"
            :key="tableKey"
            :data="list"
            :default-sort="{ prop: 'id', order: 'descending' }"
            highlight-current-row
            style="width: 100%;">
            <el-table-column :label="$t('table.subjectName')" min-width="120">
              <template slot-scope="scope">
                <span v-html="scope.row.subject.subjectName"></span>
              </template>
            </el-table-column>
            <el-table-column :label="$t('table.subject.type')" min-width="90">
              <template slot-scope="scope">
                <el-tag :type="scope.row.subject.type | subjectTypeTagFilter" effect="dark" size="small">{{ scope.row.subject.type | subjectTypeFilter }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column :label="$t('table.userAnswer')" min-width="120">
              <template slot-scope="scope">
                <span>{{ scope.row.answer | simpleStrFilter(15) }}</span>
              </template>
            </el-table-column>
            <el-table-column :label="$t('table.subject.answer')" min-width="120">
              <template slot-scope="scope">
                <span>{{ scope.row.subject.answer.answer | simpleStrFilter }}</span>
              </template>
            </el-table-column>
            <el-table-column :label="$t('table.examRecord.markStatus')" min-width="90">
              <template slot-scope="scope">
                <el-tag :type="scope.row.markStatus | simpleTagStatusFilter(1)" effect="dark" size="small">{{ scope.row.markStatus | submitStatusFilter }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column :label="$t('table.answerCorrectType')" min-width="90">
              <template slot-scope="scope">
                <el-tag :type="scope.row.answerType | simpleTagStatusFilter(0)" effect="dark" size="small">{{ scope.row.answerType | correctTypeFilter }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column :label="$t('table.time')" min-width="90">
              <template slot-scope="scope">
                <span>{{ scope.row.duration }}</span>
              </template>
            </el-table-column>
            <el-table-column :label="$t('table.score')" min-width="90">
              <template slot-scope="scope">
                <span>{{ scope.row.score }}</span>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import CountTo from 'vue-count-to'
import waves from '@/directive/waves'
import { mapGetters } from 'vuex'
import SpinnerLoading from '@/components/SpinnerLoading'
import echarts from 'echarts'
import { examRecordDetails } from '@/api/exam/examRecord'
import { messageFail } from '@/utils/util'
require('echarts/theme/macarons')

export default {
  name: 'ScoreDetails',
  components: { SpinnerLoading, CountTo },
  directives: {
    waves
  },
  filters: {
    correctTypeFilter (type) {
      const correctTypeMap = {
        0: '正确',
        1: '错误'
      }
      return correctTypeMap[type]
    }
  },
  data () {
    return {
      score: 90,
      time: '50分钟',
      chart: null,
      chartData: [],
      tableKey: 0,
      list: [],
      examRecordId: undefined,
      examRecord: {
        examinationName: '',
        userName: '',
        deptName: '',
        score: 0,
        startTime: '',
        endTime: '',
        duration: ''
      }
    }
  },
  created () {
    this.examRecordId = this.$route.params.id
    this.getExamRecord()
  },
  computed: {
    ...mapGetters([
      'elements',
      'permissions'
    ])
  },
  methods: {
    getExamRecord () {
      examRecordDetails(this.examRecordId).then(response => {
        if (response.data.data === null) {
          messageFail(this, '加载成绩失败')
          return
        }
        this.examRecord = response.data.data
        const { inCorrectNumber, correctNumber, answers } = this.examRecord
        this.chartData.push({ name: '错误数', value: inCorrectNumber })
        this.chartData.push({ name: '正确数', value: correctNumber })
        this.list = answers
        this.initChart()
      }).catch(() => {
        messageFail(this, '加载成绩失败')
      })
    },
    initChart () {
      this.chart = echarts.init(this.$refs.chart, 'macarons')
      this.chart.setOption({
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        series: [
          {
            name: '答题统计',
            type: 'pie',
            radius: ['50%', '70%'],
            avoidLabelOverlap: false,
            animationEasing: 'cubicInOut',
            animationDuration: 2600,
            label: {
              normal: {
                formatter: function (param) {
                  return param.name + ': ' + Math.round(param.value)
                }
              }
            },
            data: this.chartData
          }
        ]
      })
    }
  }
}
</script>

<style lang="scss" scoped>
  .dialog-footer {
    margin-top: 20px;
  }
  .details-container {
    padding: 12px;
    .score-info {
      height: 250px;
    }
  }
  .user-info {
    font-size: 13px;
    .user-info-item {
      display:inline-block;
      padding:5px 10px;
      cursor:pointer;
    }
  }
  .description {
    display: inline-block;
    padding: 20px 0 0 20px;
    font-size: 18px;
    opacity: .9;
    .description-score {
      padding-top: 8px;
      font-size: 24px;
    }
  }
  .subject-list {
    padding: 12px;
    margin-top: 12px;
  }
</style>
