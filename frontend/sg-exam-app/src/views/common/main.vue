<template>
  <section class="main">
    <div class="main-wrapper">
      <div class="main-content">
        <transition name="el-fade-in">
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
    handleSubmitExam () {
      console.log('handleSubmitExam')
    }
  }
}
</script>
