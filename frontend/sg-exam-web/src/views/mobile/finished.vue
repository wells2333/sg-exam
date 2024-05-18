<template>
  <div class="m-finished">
    <el-row>
      <h1>{{this.$t('submit') + this.$t('success')}}</h1>
    </el-row>
    <el-row class="m-finished-btn">
      <el-button type="primary" size="medium" @click="handleReturn">
        {{ $t('return') }}
      </el-button>
    </el-row>
  </div>
</template>
<script>
export default {
  data() {
    return {}
  },
  methods: {
    handleReturn: function () {
      window.opener = null;
      window.open("about:blank", "_top").close()
    }
  }
}
</script>

<style scoped>
.m-finished {
  text-align: center;
  margin: 50px;
}

.m-finished-btn {
  margin-top: 30px;
}
</style>
