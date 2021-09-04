<template>
  <el-breadcrumb class="app-breadcrumb" separator="/">
    <transition-group name="breadcrumb">
      <el-breadcrumb-item v-for="(item,index) in levelList" v-if="item.name" :key="item.path">
        <span v-if="index === levelList.length - 1" class="no-redirect">{{ item.name }}</span>
        <span v-else>{{ item.name }}</span>
      </el-breadcrumb-item>
    </transition-group>
  </el-breadcrumb>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  data () {
    return {
      levelList: null
    }
  },
  watch: {
    $route () {
      this.getBreadcrumb()
    }
  },
  created () {
    this.getBreadcrumb()
  },
  computed: {
    ...mapGetters(['tag', 'menu'])
  },
  methods: {
    getBreadcrumb () {
      let matched = this.$route.matched.filter(item => item.name)
      const first = matched[0]
      if (first) {
        let lowerFirst = first.name.trim().toLocaleLowerCase()
        // iframe类型菜单
        if (lowerFirst === 'iframe'.toLocaleLowerCase()) {
          let tags = []
          // 查找父菜单
          if (this.menu && this.menu.length > 0) {
            for (let i = 0; i < this.menu.length; i++) {
              let currentMenu = this.menu[i]
              // 查找子菜单
              if (currentMenu.children) {
                for (let j = 0; j < currentMenu.children.length; j++) {
                  if (currentMenu.children[j].name === this.tag.label) {
                    tags.push({ path: currentMenu.path, name: currentMenu.name, meta: { title: currentMenu.name } })
                  }
                }
              }
            }
          }
          matched = tags.concat([{ path: this.tag.value, name: this.tag.label, meta: { title: this.tag.label } }])
        } else if (lowerFirst !== 'Dashboard'.toLocaleLowerCase()) {
          matched = [{ path: '/dashboard', meta: { title: '首页' } }].concat(matched)
        }
      }
      this.levelList = matched
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .app-breadcrumb.el-breadcrumb {
    display: inline-block;
    font-size: 14px;
    line-height: 50px;
    margin-left: 10px;
    .no-redirect {
      color: #97a8be;
      cursor: text;
    }
  }
</style>
