<template>
  <div v-html="text" class="html-text"></div>
</template>
<script lang="ts">
import {defineComponent} from "vue";

export default defineComponent({
  name: 'HtmlText',
  props: {
    text: {
      type: String,
      default: ''
    }
  },
  setup(props) {
    return {}
  }
});
</script>

<style lang="less">
.html-text p {
  margin: 0;
}
</style>
