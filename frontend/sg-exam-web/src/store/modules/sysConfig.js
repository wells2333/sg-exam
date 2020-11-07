import { setStore, getStore } from '@/utils/store'
import { getSysConfig } from '@/api/admin/sys'

const sysConfig = {
  state: {
    sysConfig: getStore({
      name: 'sys_config'
    }) || {}
  },
  actions: {
    // 获取系统配置
    GetSysConfig ({ commit }) {
      return new Promise((resolve, reject) => {
        getSysConfig().then(response => {
          const data = response.data.data
          commit('SET_SYS_CONFIG', data)
          resolve()
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
        name: 'sys_config',
        content: state.sysConfig
      })
    }
  }
}
export default sysConfig
