<template>
  <div class="countdown-timer" :title="$t('timeLeft')"><i class="el-icon-timer"></i>&nbsp; {{ timeLeft }}</div>
</template>

<script>
export default {
  props: {
    minute: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      // 将分钟转换为秒，用于内部计时
      secondsLeft: this.minute * 60
    };
  },
  computed: {
    // 计算并格式化显示剩余时间
    timeLeft() {
      const minutes = Math.floor(this.secondsLeft / 60);
      const seconds = this.secondsLeft % 60;
      return `${minutes}:${seconds < 10 ? "0" : ""}${seconds}`;
    }
  },
  created() {
    // 组件创建时启动倒计时
    this.startCountdown();
  },
  methods: {
    startCountdown() {
      const timer = setInterval(() => {
        if (this.secondsLeft > 0) {
          this.secondsLeft -= 1;
        } else {
          clearInterval(timer); // 倒计时结束，清除计时器
          this.$emit("finished"); // 可以通过事件告知父组件倒计时结束
        }
      }, 1000); // 每秒更新一次
    }
  },
  beforeDestroy() {
    // 组件销毁前清除计时器，防止内存泄露
    if (this.timer) {
      clearInterval(this.timer);
    }
  }
};
</script>

<style scoped>
.countdown-timer {
  font-size: 22px;
  color: #1a1f2f;
  font-weight: 700;
}
</style>
