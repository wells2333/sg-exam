<template>
  <el-form ref="dataSubjectForm" :rules="subjectRules" :model="subjectInfo" :label-position="labelPosition" label-width="100px">
    <el-row>
      <el-col :span="20" :offset="2">
        <div class="subject-info">
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
              <el-form-item :label="$t('table.subject.answer')" prop="answer">
                <el-radio-group v-model="subjectInfo.answer.answer">
                  <el-radio v-for="(option) in options" :label="option.optionName" :key="option.optionName">{{ option.optionName }}</el-radio>
                </el-radio-group>
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
              <el-divider>选项列表</el-divider>
              <el-form-item v-for="(option, index) in options" :label="option.optionName" :key="option.optionName"
                            :prop="'options.' + index + '.optionContent'" label-width="15px">
                <el-row :gutter="5">
                  <el-col :span="2">
                    <el-input v-model="option.optionName"/>
                  </el-col>
                  <el-col :span="21">
                    <el-row :gutter="5">
                      <el-col :span="23">
                        <tinymce :height="60" v-model="option.optionContent"/>
                      </el-col>
                      <el-col :span="1">
                        <el-button @click.prevent="removeOption(option)">删除</el-button>
                      </el-col>
                    </el-row>
                  </el-col>
                </el-row>
              </el-form-item>
              <el-button type="success" @click.prevent="addOption()" style="display:block;margin:0 auto">新增选项</el-button>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item :label="$t('table.subject.analysis')" prop="analysis" class="analysis-form-item">
                <tinymce ref="analysisEditor" :height="60" v-model="subjectInfo.analysis"/>
              </el-form-item>
            </el-col>
          </el-row>
        </div>
      </el-col>
    </el-row>
  </el-form>
</template>

<script>

import Tinymce from '@/components/Tinymce'
import { isNotEmpty, message } from '@/utils/util'

export default {
  name: 'Choices',
  components: {
    Tinymce
  },
  props: {
    subject: {
      type: Object,
      default: function () {
        return {
          id: '',
          examinationId: undefined,
          categoryId: undefined,
          subjectName: '',
          type: 0,
          choicesType: 0,
          options: [
            { subjectChoicesId: '', optionName: 'A', optionContent: '' },
            { subjectChoicesId: '', optionName: 'B', optionContent: '' },
            { subjectChoicesId: '', optionName: 'C', optionContent: '' },
            { subjectChoicesId: '', optionName: 'D', optionContent: '' }
          ],
          answer: {
            subjectId: '',
            answer: '',
            answerType: '',
            score: ''
          },
          score: 5,
          analysis: '',
          level: 2,
          editType: 0 // 0: 输入框，1：富文本
        }
      }
    },
    choices: {
      type: String,
      default: ''
    }
  },
  data () {
    return {
      subjectInfo: this.subject,
      choicesContent: this.choices,
      labelPosition: 'right',
      // 表单校验规则
      subjectRules: {
        subjectName: [{ required: true, message: '请输入题目名称', trigger: 'change' }],
        score: [{ required: true, message: '请输入题目分值', trigger: 'change' }],
        answer: [{ required: true, message: '请输入答案', trigger: 'change' }]
      },
      options: [],
      optionCollapseActives: ['1'],
      analysisCollapseActives: ['2']
    }
  },
  methods: {
    initDefaultOptions () {
      this.options = [
        { subjectChoicesId: '', optionName: 'A', optionContent: '' },
        { subjectChoicesId: '', optionName: 'B', optionContent: '' },
        { subjectChoicesId: '', optionName: 'C', optionContent: '' },
        { subjectChoicesId: '', optionName: 'D', optionContent: '' }
      ]
      this.subjectInfo.answer.answer = 'A'
    },
    setSubjectInfo (subject) {
      this.subjectInfo = subject
      if (this.subjectInfo.options.length > 0) {
        this.options = this.subjectInfo.options
      } else {
        this.initDefaultOptions()
      }
    },
    getSubjectInfo () {
      this.subjectInfo.options = this.options
      return this.subjectInfo
    },
    setChoicesContent (choicesContent) {
      this.choicesContent = choicesContent
    },
    getChoicesContent () {
      return this.choicesContent
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
    resetTempSubject (score) {
      this.subjectInfo = {
        id: '',
        examinationId: undefined,
        categoryId: undefined,
        subjectName: '',
        type: 0,
        choicesType: 0,
        options: [
          { subjectChoicesId: '', optionName: 'A', optionContent: '' },
          { subjectChoicesId: '', optionName: 'B', optionContent: '' },
          { subjectChoicesId: '', optionName: 'C', optionContent: '' },
          { subjectChoicesId: '', optionName: 'D', optionContent: '' }
        ],
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
      this.initDefaultOptions()
      this.$refs['subjectNameEditor'].setContent('')
      this.$refs['analysisEditor'].setContent('')
    },
    addOption () {
      // 校验
      if (this.options.length > 0) {
        let option = this.options[this.options.length - 1]
        if (!isNotEmpty(option.optionName)) {
          message(this, '请先输入选项再添加', 'warning')
          return
        }
        this.options.push({ subjectChoicesId: '', optionName: '', optionContent: '' })
      } else {
        this.options.push({ subjectChoicesId: '', optionName: '', optionContent: '' })
      }
    },
    removeOption (item) {
      let index = this.options.indexOf(item)
      if (index !== -1) {
        this.options.splice(index, 1)
      }
    }
  }
}

</script>

<style lang="scss" scoped>
  @import "../../../styles/subject.scss";
  .el-rate {
    margin-top: 8px;
  }
  .analysis-form-item {
    margin-top: 20px;
  }
</style>
