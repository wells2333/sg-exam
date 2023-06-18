<template>
  <div class="sg-video-component">
    <video-player
      v-show="dataSrc !== undefined"
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
      dataSrc: undefined,
      playerOptions: {
        autoplay: false,
        muted: false,
        preload: 'auto',
        language: 'zh-CN',
        fluid: true,
        playbackRates: [0.7, 1.0, 1.5, 2.0],
        sources: [],
        poster: this.cover_url,
        notSupportedMessage: this.$t('media.video.playError'),
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
  watch: {
    src: {
      immediate: true,
      handler (val) {
        if (val) {
          this.setSrc(val)
        }
      }
    }
  },
  methods: {
    setSrc(src) {
      this.dataSrc = src
      this.playerOptions.sources = [{src}]
    },
    play() {
      if (this.$refs.sgVideoPlayer && this.$refs.sgVideoPlayer.player) {
        this.$refs.sgVideoPlayer.player.play()
      }
    },
    pause() {
      if (this.$refs.sgVideoPlayer && this.$refs.sgVideoPlayer.player) {
        this.$refs.sgVideoPlayer.player.pause()
      }
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
  min-height: 400px;
  width: auto;
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
  .vjs-big-play-button {
    top: 50%;
    left: 50%;
    margin-left: -1.5em;
    height: -0.75em;
  }
}
</style>
