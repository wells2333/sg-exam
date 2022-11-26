<template>
  <div>
    <div class="subject-content">
      <div class="subject-title">
        {{ subjectInfo.sort }}
        <span class="subject-title-content" v-html="subjectInfo.subjectName"/>
        <span class="subject-title-content"
              v-if="subjectInfo.score !== undefined && subjectInfo.score !== 0">&nbsp;({{ subjectInfo.score }})分</span>
        <div class="subject-tinymce">
          <tinymce ref="editor" :height="height" v-model="userAnswer"/>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Tinymce from '@/components/Tinymce'

export default {
  name: 'ShortAnswer',
  components: {
    Tinymce
  },
  props: {
    height: {
      type: Number,
      required: false,
      default: 260
    },
    onChoice: {
      function() {
      }
    }
  },
  data() {
    return {
      subjectInfo: {
        subjectName: '',
        score: 0
      },
      userAnswer: ''
    }
  },
  methods: {
    getAnswer() {
      return this.userAnswer
    },
    setAnswer(answer) {
      this.userAnswer = answer
      this.onChoice(this.subjectInfo.sort)
    },
    setSubjectInfo(subject) {
      this.subjectInfo = subject
      if (subject.hasOwnProperty('answer')) {
        this.setAnswer(subject.answer.answer)
      }
    },
    getSubjectInfo() {
      return this.subjectInfo
    }
  }
}
</script>

<style lang="scss" scoped>
@import "../../../assets/css/subject.scss";

.subject-tinymce {
  margin: 12px;
}
</style>
