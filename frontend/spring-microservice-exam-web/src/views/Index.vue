<template>
  <div>
    <o-header></o-header>
    <o-main></o-main>
    <o-footer></o-footer>
  </div>
</template>

<script>
import OHeader from './common/header'
import OMain from './common/main'
import OFooter from './common/footer'
export default {
  data () {
    return {}
  },
  components: {
    OHeader,
    OMain,
    OFooter
  }
}
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
  @import "../assets/css/mixin.scss";
  @import "../assets/css/index.scss";
</style>
