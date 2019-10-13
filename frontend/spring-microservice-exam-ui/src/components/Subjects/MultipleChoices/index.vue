<template>
  <el-form ref="dataSubjectForm" :rules="subjectRules" :model="subjectInfo" :label-position="labelPosition" label-width="100px">

    <el-row>
      <el-col :span="10">
        <div class="subject-info" v-if="subjectInfo.type === 3">
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
                <el-input v-model="subjectInfo.subjectName"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item :label="$t('table.subject.optionA')">
                <el-input v-model="subjectInfo.options[0].optionContent" @focus="updateTinymceContent(subjectInfo.options[0].optionContent, tinymceEdit.optionA)"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item :label="$t('table.subject.optionB')">
                <el-input v-model="subjectInfo.options[1].optionContent" @focus="updateTinymceContent(subjectInfo.options[1].optionContent, tinymceEdit.optionB)"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item :label="$t('table.subject.optionC')">
                <el-input v-model="subjectInfo.options[2].optionContent" @focus="updateTinymceContent(subjectInfo.options[2].optionContent, tinymceEdit.optionC)"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item :label="$t('table.subject.optionD')">
                <el-input v-model="subjectInfo.options[3].optionContent" @focus="updateTinymceContent(subjectInfo.options[3].optionContent, tinymceEdit.optionD)"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item :label="$t('table.subject.answer')" prop="answer">
                <!-- 选择题 -->
                <el-radio-group v-model="subjectInfo.answer.answer">
                  <el-radio :label="'A'">A</el-radio>
                  <el-radio :label="'B'">B</el-radio>
                  <el-radio :label="'C'">C</el-radio>
                  <el-radio :label="'D'">D</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item :label="$t('table.subject.analysis')" prop="analysis">
                <el-input v-model="subjectInfo.analysis" @focus="updateTinymceContent(subjectInfo.analysis, tinymceEdit.analysis)"/>
              </el-form-item>
            </el-col>
          </el-row>
        </div>
      </el-col>
      <el-col :span="14">
        <div class="subject-tinymce">
          <tinymce ref="choicesEditor" :height="350" v-model="choicesContent"/>
        </div>
      </el-col>
    </el-row>
  </el-form>
</template>

<script>
import Tinymce from '@/components/Tinymce'
import { isNotEmpty } from '@/utils/util'

export default {
  name: 'MultipleChoices',
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
          examinationId: '',
          categoryId: 0,
          subjectName: '',
          type: 3,
          choicesType: 1,
          options: [
            {subjectChoicesId: '', optionName: 'A', optionContent: ''},
            {subjectChoicesId: '', optionName: 'B', optionContent: ''},
            {subjectChoicesId: '', optionName: 'C', optionContent: ''},
            {subjectChoicesId: '', optionName: 'D', optionContent: ''}
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
        type: 1, // 类型 0：题目名称，1：选项A，2：选择B，3：选项C，4：选项D
        dialogTinymceVisible: false,
        tempValue: '',
        currentEdit: -1
      },
      // 编辑对象
      tinymceEdit: {
        subjectName: -1,
        optionA: 0,
        optionB: 1,
        optionC: 2,
        optionD: 3,
        answer: 4,
        analysis: 5
      }
    }
  },
  watch: {
    // 监听富文本编辑器的输入
    choicesContent: {
      handler: function (choicesContent) {
        this.saveTinymceContent(choicesContent)
      },
      immediate: true
    }
  },
  methods: {
    setSubjectInfo (subject) {
      this.subjectInfo = subject
    },
    getSubjectInfo () {
      return this.subjectInfo
    },
    setChoicesContent (choicesContent) {
      this.choicesContent = choicesContent
    },
    getChoicesContent () {
      return this.choicesContent
    },
    // 绑定富文本的内容
    updateTinymceContent (content, currentEdit) {
      // 重置富文本
      this.choicesContent = ''
      // 绑定当前编辑的对象
      this.tinymce.currentEdit = currentEdit
      // 选择题
      this.$refs.choicesEditor.setContent(content || '')
    },
    // 保存题目时绑定富文本的内容到subjectInfo
    saveTinymceContent (content) {
      switch (this.tinymce.currentEdit) {
        case this.tinymceEdit.subjectName:
          this.subjectInfo.subjectName = content
          break
        case this.tinymceEdit.optionA:
          this.subjectInfo.options[0].optionContent = content
          break
        case this.tinymceEdit.optionB:
          this.subjectInfo.options[1].optionContent = content
          break
        case this.tinymceEdit.optionC:
          this.subjectInfo.options[2].optionContent = content
          break
        case this.tinymceEdit.optionD:
          this.subjectInfo.options[3].optionContent = content
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
    resetTempSubject (serialNumber, score) {
      this.subjectInfo = {
        id: '',
        serialNumber: 1,
        examinationId: '',
        categoryId: 0,
        subjectName: '',
        type: 3,
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
      // 默认序号
      if (isNotEmpty(serialNumber)) {
        this.subjectInfo.serialNumber = serialNumber
      }

      // 默认分数
      if (isNotEmpty(score)) {
        this.subjectInfo.score = score
      }
    }
  }
}
</script>

<style lang="scss" scoped>
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }
  .editor-content{
    margin-top: 20px;
  }
  .subject-info {
    padding-right: 12px;
  }
  .subject-tinymce {
    padding-left: 12px;
  }
</style>
