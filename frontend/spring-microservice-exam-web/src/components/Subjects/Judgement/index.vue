<template>
  <el-form ref="dataSubjectForm" :rules="subjectRules" :model="subjectInfo" :label-position="labelPosition" label-width="100px">
    <el-row>
      <el-col :span="10">
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
              <el-form-item :label="$t('table.subjectName')" prop="subjectName">
                <el-input v-model="subjectInfo.subjectName" @focus="updateTinymceContent(subjectInfo.subjectName, tinymceEdit.subjectName)"/>
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
              <el-form-item :label="$t('table.subject.analysis')" prop="analysis" class="analysis-form-item">
                <el-input v-model="subjectInfo.analysis" @input="updateTinymceContent(subjectInfo.analysis, tinymceEdit.analysis)"/>
              </el-form-item>
            </el-col>
          </el-row>
        </div>
      </el-col>
      <el-col :span="14">
        <div class="subject-tinymce">
          <tinymce ref="choicesEditor" :height="350" v-model="choicesContent" @hasClick="hasClick"/>
        </div>
      </el-col>
    </el-row>
  </el-form>
</template>

<script>
import Tinymce from '@/components/Tinymce'
import { isNotEmpty } from '@/utils/util'

export default {
  name: 'Judgement',
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
            { subjectChoicesId: '', optionName: '正确', optionContent: '正确' },
            { subjectChoicesId: '', optionName: '错误', optionContent: '错误' }
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
      tinymce: {
        type: 1, // 类型 0：题目名称，1：选项
        dialogTinymceVisible: false,
        tempValue: '',
        currentEdit: -1
      },
      // 编辑对象
      tinymceEdit: {
        subjectName: -1,
        answer: 4,
        analysis: 5
      },
      options: []
    }
  },
  watch: {
    // 监听富文本编辑器的输入
    choicesContent: {
      handler: function (choicesContent) {
        if (isNotEmpty(this.$refs.choicesEditor)) {
          if (this.editType === 1 && this.$refs.choicesEditor.getHasClick()) {
            this.saveTinymceContent(choicesContent)
          }
        }
      },
      immediate: true
    }
  },
  methods: {
    initDefaultOptions () {
      this.options = [
        { subjectChoicesId: '', optionName: '正确', optionContent: '正确' },
        { subjectChoicesId: '', optionName: '错误', optionContent: '错误' }
      ]
    },
    setSubjectInfo (subject) {
      this.subjectInfo = subject
      this.initDefaultOptions()
    },
    getSubjectInfo () {
      return this.subjectInfo
    },
    // 绑定富文本的内容
    updateTinymceContent (content, currentEdit, type) {
      // 重置富文本
      this.choicesContent = ''
      // 绑定当前编辑的对象
      this.tinymce.currentEdit = currentEdit
      this.tinymce.type = type
      this.$refs.choicesEditor.setContent(content || '')
      this.editType = 0
      this.$refs.choicesEditor.setHashClick(false)
    },
    saveTinymceContent (content) {
      switch (this.tinymce.currentEdit) {
        case this.tinymceEdit.subjectName:
          this.subjectInfo.subjectName = content
          break
        case this.tinymceEdit.answer:
          this.subjectInfo.answer.answer = content
          break
        case this.tinymceEdit.analysis:
          this.subjectInfo.analysis = content
          break
      }
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
    },
    // 点击事件回调
    hasClick (hasClick) {
      this.editType = 1
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
