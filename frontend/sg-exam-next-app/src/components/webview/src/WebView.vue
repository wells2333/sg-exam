<template>
  <web-view :src="url"></web-view>
</template>
<script lang="ts">
import {defineComponent} from "vue";

export default defineComponent({
  name: 'WebView',
  components: {},
  props: {
    url: {
      type: String
    }
  },
});
</script>