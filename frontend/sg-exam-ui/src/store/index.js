import Vue from 'vue'
import Vuex from 'vuex'
import app from './modules/app'
import permission from './modules/permission'
import tagsView from './modules/tagsView'
import user from './modules/user'
import getters from './getters'
import common from './modules/common'
import tags from './modules/tags'
import sysConfig from './modules/sysConfig'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    app,
    tags,
    permission,
    tagsView,
    user,
    common,
    sysConfig
  },
  getters
})

export default store
