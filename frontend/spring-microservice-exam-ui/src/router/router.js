import Vue from 'vue'
import VueRouter from 'vue-router'
import store from '@/store'
import { formatRoutes } from '@/utils/util'
import baseRouter from './_router'

Vue.use(VueRouter)

export default new VueRouter({
  // mode: 'history',
  strict: process.env.NODE_ENV !== 'production',
  scrollBehavior (to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    } else {
      if (from.meta.keepAlive) {
        from.meta.savedPosition = document.body.scrollTop
      }
      return {
        x: 0,
        y: to.meta.savedPosition || 0
      }
    }
  },
  routes: [].concat(...formatRoutes(store.state.routers), baseRouter)
})
