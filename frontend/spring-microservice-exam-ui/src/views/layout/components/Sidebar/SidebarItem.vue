<template>
  <div class="menu-wrapper">
    <template v-for="(item, index) in menu">
      <el-menu-item v-if="item.children.length===0 " :index="filterPath(item.path,index)" :key="item.label" @click="open(item)">
        <svg-icon :icon-class="item.icon" />
        <span slot="title">{{ item.label }}</span>
      </el-menu-item>

      <el-submenu v-else :index="filterPath(item.label,index)" :key="item.label">
        <template slot="title">
          <svg-icon :icon-class="item.icon" />
          <span slot="title" :class="{'el-menu--display':isCollapse}">{{ item.label }}</span>
        </template>
        <template v-for="(child,cindex) in item.children">
          <el-menu-item v-if="child.children.length==0" :index="filterPath(child.path,cindex)" :key="cindex" @click="open(child)">
            <span slot="title">{{ child.label }}</span>
          </el-menu-item>
          <sidebar-item v-else :menu="[child]" :key="cindex" :is-collapse="isCollapse"/>
        </template>
      </el-submenu>
    </template>
  </div>
</template>

<script>
import Item from './Item'
import { resolveUrlPath } from '@/utils/util'

export default {
  name: 'SidebarItem',
  components: { Item },
  props: {
    menu: {
      type: Array
    },
    isCollapse: {
      type: Boolean
    }
  },
  data () {
    return {}
  },
  methods: {
    filterPath (path, index) {
      return path == null ? index + '' : path
    },
    open (item) {
      this.$router.push({
        path: resolveUrlPath(item.path, item.label),
        query: item.query
      })
    }
  }
}
</script>
