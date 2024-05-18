<template>
  <div class="right di main-wrap" v-loading="audio.waiting">
    <audio ref="audio" class="dn"
           :src="src" :preload="audio.preload"
           @play="onPlay"
           @error="onError"
           @waiting="onWaiting"
           @pause="onPause"
           @timeupdate="onTimeupdate"
           @loadedmetadata="onLoadedmetadata"
           :autoplay="autoplay"
           muted="muted"
    ></audio>
    <div class="w-full">
      <div class="flex items-center w-10/12 mx-auto slide">
        <el-slider v-show="!controlList.noProcess" v-model="sliderTime"
                   :format-tooltip="formatProcessToolTip" @change="changeCurrentTime"
                   class="slider_time"></el-slider>
        <span type="info" class="second">{{ audio.maxTime | formatSecond }}</span>
        <div class="mt-3 flex items-center w-1/2 mx-auto justify-around">
          <el-tooltip class="item" effect="dark" :content="audio.playing | transPlayPause" placement="top">
            <el-button type="text"  class="audio-icon"  :icon="audio.playing ? 'el-icon-video-pause':
          'el-icon-video-play'" @click="startPlayOrPause">
            </el-button>
          </el-tooltip>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {playSpeech} from '@/api/exam/examMedia'
import {messageWarn} from '@/utils/util'

function realFormatSecond(second) {
  let secondType = typeof second
  if (secondType === 'number' || secondType === 'string') {
    second = parseInt(second)
    let hours = Math.floor(second / 3600)
    second = second - hours * 3600
    let mimute = Math.floor(second / 60)
    second = second - mimute * 60
    return hours + ':' + ('0' + mimute).slice(-2) + ':' + ('0' + second).slice(-2)
  } else {
    return '0:00:00'
  }
}

export default {
  name: 'voicetotext',
  props: {
    theSpeeds: {
      type: Array,
      default() {
        return [1, 1.5, 2]
      }
    },
    theControlList: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      src: '',
      subjectId: '',
      autoPlay: 0,
      audio: {
        currentTime: 0,
        maxTime: 0,
        playing: false,
        muted: false,
        speed: 1,
        waiting: true,
        preload: 'auto'
      },
      sliderTime: 0,
      volume: 100,
      speeds: this.theSpeeds,
      controlList: {
        noProcess: false
      }
    }
  },
  methods: {
    setControlList() {
      let controlList = this.theControlList.split(' ')
      controlList.forEach((item) => {
        if (this.controlList[item] !== undefined) {
          this.controlList[item] = true
        }
      })
    },
    formatProcessToolTip(index = 0) {
      index = parseInt(this.audio.maxTime / 100 * index)
      return this.$t('media.audio.progress') + realFormatSecond(index)
    },
    changeCurrentTime(index) {
      this.pausePlay()
      this.$refs.audio.currentTime = parseInt(index / 100 * this.audio.maxTime)
    },
    startPlayOrPause() {
      return this.audio.playing ? this.pausePlay() : this.startPlay()
    },
    startPlay() {
      if (this.subjectId !== '') {
        playSpeech(this.subjectId).then(e => {
          if (e && e.data && e.data.result) {
            const {limit} = e.data.result
            if (!limit) {
              this.$refs.audio.play()
            } else {
              messageWarn(this, this.$t('media.audio.limit'))
            }
          } else {
            messageWarn(this, this.$t('media.audio.playFailed'))
          }
        }).catch(err => {
          console.error(err)
        })
      } else {
        this.$refs.audio.play()
      }
    },
    pausePlay() {
      this.$refs.audio.pause()
    },
    onPause() {
      this.audio.playing = false
    },
    onError() {
      this.audio.waiting = true
    },
    onWaiting(res) {

    },
    onPlay(res) {
      this.audio.playing = true
      this.audio.loading = false
      if (!this.controlList.onlyOnePlaying) {
        return
      }
      let target = res.target
      let audios = document.getElementsByTagName('audio');
      [...audios].forEach((item) => {
        if (item !== target) {
          item.pause()
        }
      })
    },
    onTimeupdate(res) {
      this.audio.currentTime = res.target.currentTime
      this.sliderTime = parseInt(this.audio.currentTime / this.audio.maxTime * 100)
    },
    onLoadedmetadata(res) {
      this.audio.waiting = false
      this.audio.maxTime = parseInt(res.target.duration)
      if (this.autoPlay === 1) {
        // TODO 浏览器限制，不允许自动播放音频
        this.startPlay()
      }
    },
    setSrc(src, autoPlay, subjectId) {
      this.autoPlay = autoPlay
      this.src = src
      this.subjectId = subjectId
    },
    setSrcWithoutSubjectId(src, autoPlay) {
      this.autoPlay = autoPlay
      this.src = src
    }
  },
  filters: {
    formatSecond(second = 0) {
      return realFormatSecond(second)
    },
    transPlayPause(value) {
      if (this) {
        return value ? this.$t('media.audio.pause') : this.$t('media.audio.play')
      } else {
        return ''
      }
    },
    transMutedOrNot(value) {
      return value ? this.$t('media.audio.notMuted') : this.$t('media.audio.muted')
    },
    transSpeed(value) {
      return this.$t('media.audio.transSpeed') + value
    }
  },
  created() {
    this.setControlList()
  }
}
</script>

<style scoped lang="scss">
.main-wrap {
  margin-bottom: 10px;
}
.right {
  width: 100%;
  display: inline-block;

  .slider {
    display: inline-block;
    position: relative;
    top: 14px;
    margin-left: 15px;
  }

  .slider_time {
    width: 100%;
    margin: 0 10px;
  }

  .slider_voice {
    width: 80px;
  }

  .download {
    color: #409EFF;
    margin-left: 15px;
  }

  .slide {
    display: flex;
    flex-direction: row;
    align-items: center;
  }

  .second {
    color: #909399;
    font-size: 12px;
    margin-left: -5px;
    margin-right: 20px;
  }

  .dn {
    display: none;
  }
  .audio-icon {
    font-size: 30px;
    margin-right: 10px;
    padding: 0;
  }
}

</style>
