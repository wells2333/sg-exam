<template>
  <section class="main">
    <transition name="fade-transform" mode="out-in">
      <router-view :key="key"/>
    </transition>
  </section>
</template>

<script>
export default {
  name: 'Main',
  computed: {
    key () {
      return this.$route.fullPath
    }
  }
}
</script>

<style scoped>
  .main {
    min-height: calc(100vh - 454px);
    width: 100%;
    position: relative;
    overflow: hidden;
  }
</style>
