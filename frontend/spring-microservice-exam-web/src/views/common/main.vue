<template>
  <section class="main">
    <div class="main-wrapper">
      <div class="main-content">
        <transition name="fade-transform" mode="out-in">
          <router-view :key="key"/>
        </transition>
      </div>
    </div>
  </section>
</template>

<script>
export default {
  name: 'Main',
  computed: {
    key () {
      return this.$route.fullPath
    }
  },
  methods: {
    // TODO 提交考试
    handleSubmitExam () {
      console.log('handleSubmitExam')
    }
  }
}
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
  .main {
    min-height: calc(100vh - 454px);
    width: 100%;
    position: relative;
    overflow: hidden;
    .main-wrapper {
      max-width: none;
      padding-left: 0;
      padding-right: 0;
      padding-bottom: 0;
      .main-content {
        margin-top: -2rem;
        width: 100%;
        overflow: hidden;
        min-width: 1220px;
      }
    }
  }
</style>
