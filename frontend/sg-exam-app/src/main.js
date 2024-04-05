import Vue from 'vue'
import ElementUI from 'element-ui'
import App from './App'
import router from './router'
import store from './store'
import './permission'
import 'element-ui/lib/theme-chalk/base.css'
import 'element-ui/lib/theme-chalk/index.css'
import './icons'
import * as filters from './filters'
import CollapseTransition from 'element-ui/lib/transitions/collapse-transition'
import i18n from './locales/vueIN'

Vue.config.productionTip = false

Vue.use(ElementUI)
Vue.component(CollapseTransition.name, CollapseTransition)

Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
})

let vue = new Vue({
  el: '#app',
  router,
  store,
  i18n,
  components: { App },
  template: '<App/>'
})

Vue.use(vue)
