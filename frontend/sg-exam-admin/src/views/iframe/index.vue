<template>
  <iframe v-if="$route.query.src" ref="iframe" :src="$route.query.src" class="iframe"/>
  <iframe v-else ref="iframe" :src="urlPath" class="iframe"/>
</template>

<script>
import NProgress from 'nprogress' // progress bar
import { mapGetters } from 'vuex'

export default {
  name: 'Iframe',
  components: {
    ...mapGetters(['tagList']),
    tagListNum: function () {
      return this.tagList.length !== 0
    }
  },
  props: ['routerPath'],
  data () {
    return {
      urlPath: this.getUrlPath()
    }
  },
  watch: {
    $route: function () {
      this.load()
    },
    routerPath: function (val) {
      this.urlPath = this.getUrlPath()
    }
  },
  created () {
    NProgress.configure({ showSpinner: false })
  },
  mounted () {
    this.load()
  },
  methods: {
    // 显示等待框
    show () {
      NProgress.start()
    },
    // 隐藏等待狂
    hide () {
      NProgress.done()
    },
    // 加载浏览器窗口变化自适应
    resize () {
      window.onresize = () => {
        this.iframeInit()
      }
    },
    // 加载组件
    load () {
      this.resize()
      this.show()
      this.$route.query.src = this.$route.query.src
        ? this.$route.query.src.replace('$', '#') : ''
      // 超时3s自动隐藏等待狂，加强用户体验
      let time = 3
      const timeFunc = setInterval(() => {
        time--
        if (time === 0) {
          this.hide()
          clearInterval(timeFunc)
        }
      }, 1000)
      this.iframeInit()
    },
    iframeInit () {
      const iframe = this.$refs.iframe
      if (!iframe) {
        return
      }
      const clientHeight = document.documentElement.clientHeight - 120
      iframe.style.height = `${clientHeight}px`
      if (iframe.attachEvent) {
        iframe.attachEvent('onload', () => {
          this.hide()
        })
      } else {
        iframe.onload = () => {
          this.hide()
        }
      }
    },
    getUrlPath: function () {
      let url = window.location.href
      url = url.replace('/iframe', '')
      return url
    }
  }
}
</script>

<style>
  .iframe {
    width: 100%;
    height: 100%;
    border: 0;
    overflow: hidden;
    box-sizing: border-box;
  }
</style>
