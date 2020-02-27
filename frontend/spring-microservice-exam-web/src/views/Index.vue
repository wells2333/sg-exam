<template>
  <div>
    <o-header @handleSubmitExam="handleSubmitExam"></o-header>
    <o-main ref="mainRef"></o-main>
  </div>
</template>

<script>
import OHeader from './common/header'
import OMain from './common/main'
export default {
  data () {
    return {}
  },
  components: {
    OHeader,
    OMain
  },
  methods: {
    // 提交考试
    handleSubmitExam () {
      this.$refs.mainRef.handleSubmitExam()
    }
  }
}
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
  @import "../assets/css/mixin.scss";
  @import "../assets/css/index.scss";
</style>
