<template>
  <div>
    <div class="subject-exam-title">{{exam.examinationName}}（共{{subjectCount}}题，合计{{exam.totalScore}}分）</div>
    <div class="subject-content">
      <div class="subject-title">
        {{ index }}
        <span class="subject-title-content" v-html="subjectInfo.subjectName"/>
        <span class="subject-title-content">&nbsp;({{subjectInfo.score}})分</span>
        <div class="subject-tinymce">
          <tinymce ref="editor" :height="300" v-model="userAnswer"/>
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
      return this.subjectInfo
    }
  }
}
</script>

<style lang="scss" scoped>
  @import "../../../styles/subject.scss";
  .subject-tinymce {
    margin: 12px;
  }
</style>
