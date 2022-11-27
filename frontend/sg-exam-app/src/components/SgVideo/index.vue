<template>
  <div class="sg-video-component">
    <video-player
      class="video-player-box"
      ref="sgVideoPlayer"
      :options="playerOptions"
      :playsinline="true"
      customEventName="customstatechangedeventname"
      @play="onPlayerPlay($event)"
      @pause="onPlayerPause($event)"
      @ended="onPlayerEnded($event)"
      @waiting="onPlayerWaiting($event)"
      @playing="onPlayerPlaying($event)"
      @loadeddata="onPlayerLoadeddata($event)"
      @timeupdate="onPlayerTimeupdate($event)"
      @canplay="onPlayerCanplay($event)"
      @canplaythrough="onPlayerCanplaythrough($event)"
      @statechanged="playerStateChanged($event)"
      @ready="playerReadied"
    >
    </video-player>
  </div>
</template>
<script>
import 'video.js/dist/video-js.css'
import {videoPlayer} from 'vue-video-player'

export default {
  name: 'SgVideo',
  props: {
    src: {
      type: String
    },
    cover_url: {
      type: String
    }
  },
  components: {
    videoPlayer
  },
  data() {
    return {
      playerOptions: {
        autoplay: false,
        muted: false,
        preload: 'auto',
        language: 'zh-CN',
        fluid: true,
        playbackRates: [0.7, 1.0, 1.5, 2.0],
        sources: [],
        poster: this.cover_url,
        notSupportedMessage: '此视频暂无法播放，请稍后再试',
        controlBar: {
          timeDivider: true,
          durationDisplay: true,
          remainingTimeDisplay: false,
          fullscreenToggle: true,
          currentTimeDisplay: true,
          volumeControl: false,
          playToggle: true,
          progressControl: true
        }
      }
    }
  },
  mounted() {

  },
  computed: {
    player() {
      return this.$refs.sgVideoPlayer.player
    }
  },
  methods: {
    setSrc(src) {
      this.playerOptions.sources = [{src}]
    },
    play() {
      this.$refs.sgVideoPlayer.player.play()
    },
    pause() {
      this.$refs.sgVideoPlayer.player.pause()
    },
    onPlayerPlay(player) {

    },
    onPlayerPause(player) {

    },
    onPlayerEnded(player) {

    },
    onPlayerWaiting(player) {

    },
    onPlayerPlaying(player) {

    },
    onPlayerLoadeddata(player) {

    },
    onPlayerTimeupdate(player) {

    },
    onPlayerCanplay(player) {

    },
    onPlayerCanplaythrough(player) {

    },
    playerStateChanged(playerCurrentState) {

    },
    playerReadied(player) {

    }
  }
}
</script>
<style lang="scss">
.sg-video-component {
  height: 400px;
  width: 400px;
  margin: 0 auto;
}

::v-deep .video-player {
  .vjs-big-play-button {
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);
  }
}

.video-player-box .video-js {
  padding-top: 0 !important;
  position: relative;
  height: 400px !important;
}
</style>
