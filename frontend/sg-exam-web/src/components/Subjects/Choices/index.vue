<template>
  <div>
    <div class="subject-content">
      <div class="subject-title">
        <span class="subject-title-content" v-html="subjectInfo.subjectName"></span>
      </div>
      <div class="subject-speech-info" v-if="subjectInfo.speechUrl">
        <sg-audio ref="sgAudio" :src="subjectInfo.speechUrl" :autoPlay="subjectInfo.autoPlaySpeech"></sg-audio>
      </div>
      <div class="subject-video-info" v-if="subjectInfo.subjectVideoUrl">
        <sg-video ref="sgVideo"></sg-video>
      </div>
      <ul class="subject-options" v-for="option in options" :key="option.id">
        <li class="subject-option">
          <input class="toggle" type="checkbox" :checked="userAnswer === option.optionName"
                 :id="'option' + option.id" @change="toggleOption(option)">
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
import SgAudio from '@/components/SgAudio'
import SgVideo from '@/components/SgVideo'
import {setVideoSrc, pauseVideo, pauseAudio, setAudioSrc, replaceFirstP} from '@/utils/busi'
import {uuid} from '@/utils/util'

export default {
  name: 'Choices',
  components: {
    SgAudio,
    SgVideo
  },
  data() {
    return {
      subjectInfo: {
        subjectName: '',
        score: 0
      },
      options: [],
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
      this.processSubjectInfo(this.subjectInfo)
      setVideoSrc(subject, this.$refs)
      setAudioSrc(subject, this.$refs, subject.autoPlaySpeech)
    },
    processSubjectInfo(subject) {
      subject.subjectName = replaceFirstP(subject.subjectName, this.$t('exam.subject.subjectTypeChoices'), subject.sort)
      if (subject.hasOwnProperty('options')) {
        subject.options.forEach(o => {
          o.id = uuid()
        })
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
    toggleOption(option) {
      this.userAnswer = option.optionName
      this.onChoice(this.subjectInfo.sort)
    },
    beforeSave() {
      pauseVideo(this.$refs)
      pauseAudio(this.$refs)
    }
  }
}
</script>

<style lang="scss" scoped>
@import "../../../assets/css/subject.scss";
</style>
