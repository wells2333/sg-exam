<template>
  <div>
    <div class="subject-content">
      <div class="subject-title">
        {{ subjectInfo.sort }}.
        <span class="subject-title-content"
              v-if="subjectInfo.score !== undefined && subjectInfo.score !== 0">&nbsp;({{ subjectInfo.score }}{{$t('exam.startExam.score')}})&nbsp;</span>
        <span class="subject-title-content" v-html="subjectInfo.subjectName"/>
      </div>
      <div class="subject-speech-info" v-if="subjectInfo.speechId && subjectInfo.speechUrl">
        <sg-audio ref="sgAudio" :src="subjectInfo.speechUrl"></sg-audio>
      </div>
      <div class="subject-video-info" v-if="subjectInfo.subjectVideoId">
        <sg-video ref="sgVideo"></sg-video>
      </div>
      <div class="subject-tinymce">
        <tinymce ref="shortAnswerEditor" :height="height" v-model="userAnswer"/>
      </div>
    </div>
  </div>
</template>

<script>
import Tinymce from '@/components/Tinymce'
import SgAudio from '@/components/SgAudio'
import SgVideo from '@/components/SgVideo'
import {setVideoSrc, pauseVideo, pauseAudio, setAudioSrc} from '@/utils/busi'

export default {
  name: 'ShortAnswer',
  components: {
    Tinymce,
    SgAudio,
    SgVideo
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
      if (this.$refs.shortAnswerEditor && this.userAnswer !== null) {
        this.$refs.shortAnswerEditor.setContent(this.userAnswer)
      } else {
        this.$refs.shortAnswerEditor.setContent('')
      }
      this.onChoice(this.subjectInfo.sort)
    },
    setSubjectInfo(subject) {
      this.subjectInfo = subject
      if (subject.hasOwnProperty('answer')) {
        this.setAnswer(subject.answer.answer)
      }
      setVideoSrc(subject, this.$refs)
      setAudioSrc(subject, this.$refs, subject.autoPlaySpeech)
    },
    getSubjectInfo() {
      return this.subjectInfo
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

.subject-tinymce {
  margin: 12px;
}
</style>
