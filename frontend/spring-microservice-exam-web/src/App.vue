<template>
  <div id="app">
    <router-view clas="main"> </router-view>
  </div>
</template>

<script>
export default {
  name: 'app'
}
</script>

<style lang="scss" rel="stylesheet/scss">
  @import "assets/css/style.scss";
  #app {
    height: 100%;
  }
</style>
