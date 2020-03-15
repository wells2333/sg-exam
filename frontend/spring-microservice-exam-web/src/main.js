import Vue from 'vue'
import Cookies from 'js-cookie'
import 'normalize.css/normalize.css' // A modern alternative to CSS resets
import Element from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import '@/styles/index.scss' // global css
import App from './App'
import router from './router'
import store from './store'

import i18n from './lang' // Internationalization
import './icons' // icon
import './permission' // permission control
import './mock' // simulation data

import * as filters from './filters' // global filters
import { loadStyle } from './utils/util'
import * as urls from '@/config/env'
import Spinner from 'vue-spinkit'
import moment from 'moment'

const iconfontVersion = ['567566_r22zi6t8noas8aor', '599693_0b5sleso3f1j1yvi', '667895_xte3dcfrvbo6r']
const iconfontUrl = `//at.alicdn.com/t/font_$key.css`

Vue.use(Element, {
  size: Cookies.get('size') || 'medium', // set element-ui default size
  i18n: (key, value) => i18n.t(key, value)
})

Vue.prototype.moment = moment

// loading效果
Vue.component('Spinner', Spinner)

Vue.config.productionTip = false

Object.keys(urls).forEach(key => {
  Vue.prototype[key] = urls[key]
})

Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
})

iconfontVersion.forEach(ele => {
  loadStyle(iconfontUrl.replace('$key', ele))
})

const vue = new Vue({
  el: '#app',
  router,
  store,
  i18n,
  render: h => h(App)
})
console.log(vue)
