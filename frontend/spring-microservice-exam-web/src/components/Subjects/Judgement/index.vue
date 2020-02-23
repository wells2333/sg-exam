<template>
  <div>
    <div class="subject-exam-title">{{exam.examinationName}}（共{{subjectCount}}题，合计{{exam.totalScore}}分）</div>
    <div class="subject-content">
      <div class="subject-title">
        {{ index }}
        <span class="subject-title-content" v-html="subjectInfo.subjectName"/>
        <span class="subject-title-content">&nbsp;({{subjectInfo.score}})分</span>
      </div>
      <ul class="subject-options" v-for="option in options" :key="option.id">
        <li class="subject-option">
          <input class="toggle" type="checkbox" :checked="userAnswer === option.optionName" :id="'option' + option.id" @change="toggleOption(option)">
          <label :for="'option' + option.id">
            <span class="subject-option-prefix">{{ option.optionName }}&nbsp;</span>
          </label>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Judgement',
  data () {
    return {
      exam: {
        examinationName: '',
        totalScore: ''
      },
      subjectCount: 0,
      subjectInfo: {
        subjectName: '',
        score: 0
      },
      options: [
        { id: 1, optionName: '正确' },
        { id: 2, optionName: '错误' }
      ],
      userAnswer: '',
      index: ''
    }
  },
  methods: {
    getAnswer () {
      return this.userAnswer
    },
    setAnswer (answer) {
      this.userAnswer = answer
    },
    setSubjectInfo (exam, subject, subjectCount, index) {
      this.exam = exam
      this.subjectCount = subjectCount
      this.subjectInfo = subject
      if (subject.hasOwnProperty('answer')) {
        this.setAnswer(subject.answer.answer)
      }
      this.index = index + '.'
    },
    getSubjectInfo () {
      this.subjectInfo.options = this.options
      return this.subjectInfo
    },
    // 选中选项
    toggleOption (option) {
      this.userAnswer = option.optionName
    }
  }
}
</script>

<style lang="scss" scoped>
  @import "../../../styles/subject.scss";
</style>
