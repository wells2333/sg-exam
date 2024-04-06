<template>
  <div>
    <div class="subject-content">
      <div class="subject-title">
        <div class="subject-title-content" v-html="subjectInfo.subjectName"/>
      </div>
      <div class="subject-speech-info" v-if="subjectInfo.speechUrl">
        <sg-audio ref="sgAudio" :src="subjectInfo.speechUrl"></sg-audio>
      </div>
      <div class="subject-video-info" v-if="subjectInfo.subjectVideoUrl">
        <sg-video ref="sgVideo"></sg-video>
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
import SgAudio from '@/components/SgAudio'
import SgVideo from '@/components/SgVideo'
import {isNotEmpty, uuid} from '@/utils/util'
import {setVideoSrc, pauseVideo, pauseAudio, setAudioSrc, replaceFirstP} from '@/utils/busi'

export default {
  name: 'MultipleChoices',
  components: {
    SgAudio,
    SgVideo
  },
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
      } else {
        this.userAnswer = []
      }
    },
    setSubjectInfo(subject, subjectCount) {
      this.subjectCount = subjectCount
      this.subjectInfo = subject
      if (subject.hasOwnProperty('options')) {
        subject.options.forEach(o => {
          o.id = uuid()
        })
        this.options = subject.options
      }
      this.processSubjectInfo(this.subjectInfo)
      setVideoSrc(subject, this.$refs)
      setAudioSrc(subject, this.$refs, subject.autoPlaySpeech)
    },
    processSubjectInfo(subject) {
      subject.subjectName = replaceFirstP(subject.subjectName, this.$t('exam.subject.subjectTypeMultiChoices'), subject.sort)
      if (subject.hasOwnProperty('answer')) {
        this.setAnswer(subject.answer.answer)
      }
    },
    getSubjectInfo() {
      this.subjectInfo.options = this.options
      return this.subjectInfo
    },
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
