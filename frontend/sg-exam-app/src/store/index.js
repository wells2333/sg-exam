import Vue from 'vue'
import Vuex from 'vuex'
import user from './modules/user'
import course from './modules/course'
import exam from './modules/exam'
import practice from './modules/practice'
import getters from './getters'
import sysConfig from './modules/sysConfig'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    user,
    exam,
    practice,
    course,
    sysConfig
  },
  getters
})

export default store
