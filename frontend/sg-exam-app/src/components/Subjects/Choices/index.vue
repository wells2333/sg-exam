<template>
  <div>
    <div class="subject-content">
      <div class="subject-title">
        {{ subjectInfo.sort }}.
        <span class="subject-title-content" v-html="subjectInfo.subjectName"/>
        <span class="subject-title-content" v-if="subjectInfo.score !== undefined && subjectInfo.score !== 0">&nbsp;({{subjectInfo.score}})分</span>
      </div>
      <ul class="subject-options" v-for="option in options" :key="option.id">
        <li class="subject-option">
          <input class="toggle" type="checkbox" :checked="userAnswer === option.optionName" :id="'option' + option.id" @change="toggleOption(option)">
          <label :for="'option' + option.id">
            <span class="subject-option-prefix">{{ option.optionName }}&nbsp;</span>
            <span v-html="option.optionContent" class="subject-option-prefix" />
          </label>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Choices',
  data () {
    return {
      subjectInfo: {
        subjectName: '',
        score: 0
      },
      options: [],
      userAnswer: '',
    }
  },
  props: {
    onChoice: {
      function (){}
    }
  },
  methods: {
    getAnswer () {
      return this.userAnswer
    },
    setAnswer (answer) {
      this.userAnswer = answer
    },
    setSubjectInfo (subject) {
      this.subjectInfo = subject
      if (subject.hasOwnProperty('options')) {
        this.options = subject.options
      }
      if (subject.hasOwnProperty('answer')) {
        this.setAnswer(subject.answer.answer)
      }
    },
    getSubjectInfo () {
      this.subjectInfo.options = this.options
      return this.subjectInfo
    },
    // 选中选项
    toggleOption (option) {
      this.userAnswer = option.optionName
      this.onChoice(this.subjectInfo.sort)
    }
  }
}
</script>

<style lang="scss" scoped>
  @import "../../../assets/css/subject.scss";
</style>
