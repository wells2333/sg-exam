import { setStore, getStore } from '@/utils/store'
import { SYS_CONFIG } from '@/utils/storeMap'
import { getSysConfig } from '@/api/admin/sys'

const sysConfig = {
  state: {
    sysConfig: getStore({
      name: SYS_CONFIG
    }) || {}
  },
  actions: {
    // 获取系统配置
    GetSysConfig ({ commit }) {
      return new Promise((resolve, reject) => {
        getSysConfig().then(response => {
          const data = response.data.result
          commit('SET_SYS_CONFIG', data)
          resolve(data)
        }).catch(error => {
          reject(error)
        })
      })
    }
  },
  mutations: {
    SET_SYS_CONFIG: (state, sysConfig) => {
      state.sysConfig = sysConfig
      setStore({
        name: SYS_CONFIG,
        content: state.sysConfig
      })
    }
  }
}
export default sysConfig
