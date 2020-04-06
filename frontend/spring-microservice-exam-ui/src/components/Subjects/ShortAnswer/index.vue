<template>
  <el-form ref="dataSubjectForm" :rules="subjectRules" :model="subjectInfo" :label-position="labelPosition" label-width="100px">
    <el-row>
      <el-col :span="20" :offset="2">
          <el-row>
            <el-col :span="12">
              <el-form-item :label="$t('table.subject.score')" prop="score">
                <el-input v-model="subjectInfo.score"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="$t('table.subject.level')" prop="level">
                <el-rate v-model="subjectInfo.level" :max="4" :texts="['简单', '一般', '略难', '非常难']" show-text/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item :label="$t('table.subjectName')" prop="subjectName">
                <tinymce ref="subjectNameEditor" :height="60" v-model="subjectInfo.subjectName"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item :label="$t('table.subject.answer')" prop="answer">
                <tinymce ref="answerEditor" :height="60" v-model="subjectInfo.answer.answer"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item :label="$t('table.subject.analysis')" prop="analysis">
                <tinymce ref="analysisEditor" :height="60" v-model="subjectInfo.analysis"/>
              </el-form-item>
            </el-col>
          </el-row>
      </el-col>
    </el-row>
  </el-form>
</template>

<script>
import Tinymce from '@/components/Tinymce'
import { isNotEmpty } from '@/utils/util'

export default {
  name: 'ShortAnswer',
  components: {
    Tinymce
  },
  props: {
    subject: {
      type: Object,
      default: function () {
        return {
          id: '',
          serialNumber: 1,
          examinationId: -1,
          categoryId: -1,
          subjectName: '',
          type: 1,
          answer: {
            subjectId: '',
            answer: '',
            answerType: '',
            score: ''
          },
          score: 5,
          analysis: '',
          level: 2
        }
      }
    },
    content: {
      type: String,
      default: ''
    }
  },
  data () {
    return {
      subjectInfo: this.subject,
      labelPosition: 'right',
      // 表单校验规则
      subjectRules: {
        subjectName: [{ required: true, message: '请输入题目名称', trigger: 'change' }],
        score: [{ required: true, message: '请输入题目分值', trigger: 'change' }],
        answer: [{ required: true, message: '请输入答案', trigger: 'change' }]
      }
    }
  },
  methods: {
    setSubjectInfo (subject) {
      this.subjectInfo = subject
    },
    getSubjectInfo () {
      return this.subjectInfo
    },
    // 表单校验
    validate () {
      let valid = false
      this.$refs['dataSubjectForm'].validate((validate) => {
        valid = validate
      })
      return valid
    },
    clearValidate () {
      this.$refs['dataSubjectForm'].clearValidate()
    },
    resetTempSubject (serialNumber, score) {
      this.subjectInfo = {
        id: '',
        examinationId: undefined,
        categoryId: undefined,
        subjectName: '',
        type: 1,
        choicesType: 0,
        answer: {
          subjectId: '',
          answer: '',
          answerType: '',
          score: ''
        },
        score: 5,
        analysis: '',
        level: 2
      }
      // 默认分数
      if (isNotEmpty(score)) {
        this.subjectInfo.score = score
      }
      this.$refs['subjectNameEditor'].setContent('')
      this.$refs['answerEditor'].setContent('')
      this.$refs['analysisEditor'].setContent('')
    },
    initDefaultOptions () {

    }
  }
}
</script>

<style lang="scss" scoped>
  @import "../../../styles/subject.scss";
</style>
