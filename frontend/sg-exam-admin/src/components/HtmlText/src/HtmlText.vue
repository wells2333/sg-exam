<template>
  <popover>
    <template #content>
      <div v-html="text"></div>
    </template>
    <div v-html="text" class="html-text"></div>
  </popover>
</template>
<script lang="ts">
import {defineComponent} from "vue";
import { Popover } from 'ant-design-vue';

export default defineComponent({
  name: 'HtmlText',
  components: {Popover},
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
.html-text {
  height: 22px;
  text-overflow: ellipsis;
  overflow: hidden;
}

.html-text p {
  margin: 0;
}
</style>
