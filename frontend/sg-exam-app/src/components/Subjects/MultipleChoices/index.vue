<template>
  <div>
    <div class="subject-content">
      <div class="subject-title">
        {{ subjectInfo.sort }}
        <span class="subject-title-content" v-html="subjectInfo.subjectName"/>
        <span class="subject-title-content"
              v-if="subjectInfo.score !== undefined && subjectInfo.score !== 0">&nbsp;({{ subjectInfo.score }})分</span>
      </div>
      <ul class="subject-options" v-for="option in options" :key="option.id">
        <li class="subject-option">
          <input class="toggle" type="checkbox" :checked="isChecked(option.optionName)"
                 :id="'option' + option.id" @change="toggleOption($event, option)">
          <label :for="'option' + option.id">
            <span class="subject-option-prefix">{{ option.optionName }}&nbsp;</span>
            <span v-html="option.optionContent" class="subject-option-prefix"/>
          </label>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import {isNotEmpty} from '@/utils/util'

export default {
  name: 'MultipleChoices',
  data() {
    return {
      subjectCount: 0,
      subjectInfo: {
        subjectName: '',
        score: 0
      },
      options: [],
      userAnswer: []
    }
  },
  watch: {},
  props: {
    onChoice: {
      function() {
      }
    }
  },
  methods: {
    getAnswer() {
      return this.userAnswer.join(',')
    },
    setAnswer(answer) {
      if (isNotEmpty(answer)) {
        this.userAnswer = answer.split(',')
      }
    },
    setSubjectInfo(subject, subjectCount) {
      this.subjectCount = subjectCount
      this.subjectInfo = subject
      if (subject.hasOwnProperty('options')) {
        this.options = subject.options
      }
      if (subject.hasOwnProperty('answer')) {
        this.setAnswer(subject.answer.answer)
      }
    },
    getSubjectInfo() {
      this.subjectInfo.options = this.options
      return this.subjectInfo
    },
    // 选中选项
    toggleOption($event, option) {
      if ($event.target.checked) {
        if (!this.userAnswer.includes(option.optionName)) {
          this.userAnswer.push(option.optionName)
        }
      } else {
        this.userAnswer.splice(this.userAnswer.findIndex(item => item === option.optionName), 1)
      }
      this.onChoice(this.subjectInfo.sort)
    },
    isChecked(optionName) {
      return this.userAnswer.includes(optionName)
    }
  }
}
</script>

<style lang="scss" scoped>
@import "../../../assets/css/subject.scss";
</style>
