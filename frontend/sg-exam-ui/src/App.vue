<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
export default{
  name: 'App',
  mounted () {
    document.getElementById('app').style.display = 'block'
    document.getElementById('preloader-init').style.display = 'none'
  }
}
</script>
