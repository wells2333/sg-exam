import { setStore, getStore } from '@/utils/store'
import { STATE_INFO } from '@/utils/storeMap'

const stateInfo = {
  state: {
    stateInfo: getStore({
      name: STATE_INFO
    }) || {}
  },
  actions: {
    SetStateInfo ({ commit, state }, stateInfo) {
      commit('SET_STATE_INFO', stateInfo)
    }
  },
  mutations: {
    SET_STATE_INFO: (state, stateInfo) => {
      state.stateInfo = stateInfo
      setStore({
        name: STATE_INFO,
        content: state.stateInfo
      })
    }
  }
}
export default stateInfo
