<template>
    <div class="outer">
        <Spinner name="three-bounce" color="#409EFF" fadeIn="quarter"/>
    </div>

</template>

<style>
    .outer{
        height: 100%;
        width: 100%;
        position: absolute;
        background-color: rgba(255,255,255,0.7);
        z-index: 1100;
        text-align: center;
    }
    .sk-spinner[data-v-39432f99]{
        margin-top: 250px;
    }
</style>

<script>
import Spinner from 'vue-spinkit'
export default {
  components: {
    Spinner
  }

}
</script>
