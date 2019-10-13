<template>
  <el-scrollbar wrap-class="scrollbar-wrapper">
    <el-menu
      :show-timeout="200"
      :default-active="$route.path"
      :collapse="isCollapse"
      mode="vertical"
      background-color="#304156"
      text-color="#bfcbd9"
      active-text-color="#409EFF"
    >
      <sidebar-item :menu="menu" :is-collapse="isCollapse"/>
    </el-menu>
  </el-scrollbar>
</template>

<script>
import { mapGetters } from 'vuex'
import SidebarItem from './SidebarItem'
import { validatenull } from '@/utils/validate'
import { initMenu, setUrlPath } from '@/utils/util'

export default {
  components: { SidebarItem },
  created () {
    if (validatenull(this.menu)) {
      this.$store.dispatch('GetMenu').then(data => {
        initMenu(this.$router, data)
      })
    }
  },
  computed: {
    ...mapGetters(['menu', 'tag', 'isCollapse']),
    nowTagValue: function () {
      return setUrlPath(this.$route)
    }
  }
}
</script>
