<template>
  <div>
    <div class="subject-content">
      <div class="subject-title">
        {{ subjectInfo.sort }}
        <span class="subject-title-content" v-html="subjectInfo.subjectName"/>
        <span class="subject-title-content"
              v-if="subjectInfo.score !== undefined && subjectInfo.score !== 0">&nbsp;({{ subjectInfo.score }})分</span>
      </div>
      <div class="subject-video-info" v-if="subjectInfo.subjectVideoId && subjectInfo.subjectVideoName">
        <sg-video ref="sgVideo"></sg-video>
      </div>
      <ul class="subject-options" v-for="option in options" :key="option.id">
        <li class="subject-option">
          <input class="toggle" type="checkbox" :checked="userAnswer === option.optionName"
                 :id="'option' + option.id" @change="toggleOption(option)">
          <label :for="'option' + option.id">
            <span class="subject-option-prefix">{{ option.optionName }}&nbsp;</span>
          </label>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import SgVideo from '@/components/SgVideo'
import {setVideoSrc, pauseVideo} from '@/utils/busi'

export default {
  name: 'Judgement',
  components: {
    SgVideo
  },
  data() {
    return {
      subjectCount: 0,
      subjectInfo: {
        subjectName: '',
        score: 0
      },
      options: [
        {id: 1, optionName: '正确'},
        {id: 2, optionName: '错误'}
      ],
      userAnswer: ''
    }
  },
  props: {
    onChoice: {
      function() {
      }
    }
  },
  methods: {
    getAnswer() {
      return this.userAnswer
    },
    setAnswer(answer) {
      this.userAnswer = answer
    },
    setSubjectInfo(subject) {
      this.subjectInfo = subject
      if (subject.hasOwnProperty('answer')) {
        this.setAnswer(subject.answer.answer)
      }
      setVideoSrc(subject, this.$refs)
    },
    getSubjectInfo() {
      this.subjectInfo.options = this.options
      return this.subjectInfo
    },
    toggleOption(option) {
      this.userAnswer = option.optionName
      this.onChoice(this.subjectInfo.sort)
    },
    beforeSave() {
      pauseVideo(this.$refs)
    }
  }
}
</script>

<style lang="scss" scoped>
@import "../../../assets/css/subject.scss";
</style>
