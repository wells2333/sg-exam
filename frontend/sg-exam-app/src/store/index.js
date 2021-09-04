import Vue from 'vue'
import Vuex from 'vuex'

// 用户模块
import user from './modules/user'

// 课程模块
import course from './modules/course'

// 考试模块
import exam from './modules/exam'

// 练习模块
import practice from './modules/practice'

// getters
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
