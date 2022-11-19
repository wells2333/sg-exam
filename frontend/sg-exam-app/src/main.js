// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import ElementUI from 'element-ui'
import App from './App'
import router from './router'
import store from './store'
import './permission' // 权限控制
import 'element-ui/lib/theme-chalk/base.css'
import 'element-ui/lib/theme-chalk/index.css'
import './icons' // icon
import * as filters from './filters' // global filters
import CollapseTransition from 'element-ui/lib/transitions/collapse-transition'

Vue.config.productionTip = false

Vue.use(ElementUI)
Vue.component(CollapseTransition.name, CollapseTransition)

Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
})

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
})
