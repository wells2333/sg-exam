import Vue from 'vue'
import store from './store'

if (process.env.NODE_ENV === 'production') {
  Vue.config.errorHandler = function (err, vm, info, a) {
  // Don't ask me why I use Vue.nextTick, it just a hack.
  // detail see https://forum.vuejs.org/t/dispatch-in-vue-config-errorhandler-has-some-problem/23500
    Vue.nextTick(() => {
      store.commit('ADD_LOG', {
        message: err.message,
        stack: err.stack,
        info: info,
        url: window.location.href
      })
    })
  }
}
