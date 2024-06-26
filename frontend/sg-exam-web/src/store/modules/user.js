import {loginByUsername, loginBySocial, registerByUsername, logout, getUserInfo} from '@/api/admin/login'
import { setToken, removeToken, setRefreshToken, removeRefreshToken } from '@/utils/auth'
import { setStore, getStore } from '@/utils/store'
import { TOKEN, USERINFO, REFRESH_TOKEN, ROLES, PERMISSIONS, TENANTCODE, SYS_CONFIG } from '@/utils/storeMap'
import {VUE_APP_ENCRYPTION_KEY} from '@/utils/env'
import { encryption } from '@/utils/util'

const encryptionKey = VUE_APP_ENCRYPTION_KEY

const user = {
  state: {
    userInfo: getStore({
      name: USERINFO
    }) || {},
    permissions: getStore({
      name: PERMISSIONS
    }) || {},
    roles: getStore({
      name: ROLES
    }) || [],
    menu: getStore({
      name: 'menu'
    }) || [],
    isInitMenu: getStore({
      name: 'isInitMenu'
    }) || false,
    token: getStore({
      name: TOKEN
    }) || '',
    refresh_token: getStore({
      name: REFRESH_TOKEN
    }) || '',
    tenantCode: getStore({
      name: TENANTCODE
    }) || '',
    sysConfig: getStore({
      name: SYS_CONFIG
    }) || ''
  },
  actions: {
    LoginByUsername ({ commit, state, dispatch }, userInfo) {
      return new Promise((resolve, reject) => {
        const user = encryption({
          data: userInfo,
          key: encryptionKey,
          param: ['credential']
        })
        loginByUsername(user.tenantCode, user.identifier, user.credential, user.code, user.randomStr).then(response => {
          const data = response.data
          const {code, result, message} = data
          if (code === 0) {
            setToken(result.token)
            setRefreshToken(result.token)
            commit('SET_TOKEN', result.token)
            commit('SET_REFRESH_TOKEN', result.token)
            commit('SET_TENANT_CODE', result.tenantCode)
            resolve()
          } else {
            reject(message)
          }
        }).catch(error => {
          reject(error)
        })
      })
    },
    LoginBySocial ({ commit, state, dispatch }, userInfo) {
      return new Promise((resolve, reject) => {
        const user = encryption({
          data: userInfo,
          key: encryptionKey,
          param: ['credential']
        })
        loginBySocial(user.tenantCode, user.phone, user.code).then(response => {
          const data = response.data
          const {code, result} = data
          if (code === 0) {
            setToken(result.token)
            setRefreshToken(result.token)
            commit('SET_TOKEN', result.token)
            commit('SET_REFRESH_TOKEN', result.token)
            commit('SET_TENANT_CODE', result.tenantCode)
          }
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },
    RegisterByUsername ({ commit, state, dispatch }, userInfo) {
      return new Promise((resolve, reject) => {
        const user = encryption({
          data: userInfo,
          key: encryptionKey,
          param: ['credential']
        })
        registerByUsername(user.tenantCode, user.identifier, user.email, user.credential, user.code, user.randomStr).then(response => {
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },
    GetUserInfo ({ commit, state, dispatch }) {
      return new Promise((resolve, reject) => {
        getUserInfo(state.token).then(response => {
          const data = response.data.result
          commit('SET_ROLES', data.roles)
          commit('SET_USER_INFO', data)
          commit('SET_PERMISSIONS', data.permissions)
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },
    LogOut ({ commit, state }) {
      return new Promise((resolve, reject) => {
        logout(state.token, state.refresh_token).then(() => {
          commit('SET_PERMISSIONS', [])
          commit('SET_USER_INFO', {})
          commit('SET_TOKEN', '')
          commit('SET_REFRESH_TOKEN', '')
          commit('SET_ROLES', [])
          commit('SET_SYS_CONFIG', {})
          commit('SET_TENANT_CODE', {})
          removeToken()
          resolve()
        }).catch(error => {
          console.error(error)
        })
      })
    },
    FedLogOut ({ commit }) {
      return new Promise(resolve => {
        commit('SET_PERMISSIONS', [])
        commit('SET_USER_INFO', {})
        commit('SET_TOKEN', '')
        commit('SET_REFRESH_TOKEN', '')
        commit('SET_ROLES', [])
        commit('SET_SYS_CONFIG', {})
        commit('SET_TENANT_CODE', {})
        removeToken()
        removeRefreshToken()
        resolve()
      })
    }
  },
  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
      setStore({
        name: TOKEN,
        content: state.token
      })
    },
    SET_USER_INFO: (state, userInfo) => {
      state.userInfo = userInfo
      setStore({
        name: USERINFO,
        content: state.userInfo
      })
    },
    SET_REFRESH_TOKEN: (state, rfToken) => {
      state.refresh_token = rfToken
      setStore({
        name: REFRESH_TOKEN,
        content: state.refresh_token
      })
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
      setStore({
        name: ROLES,
        content: state.roles
      })
    },
    SET_PERMISSIONS: (state, permissions) => {
      const list = {}
      for (let i = 0; i < permissions.length; i++) {
        list[permissions[i]] = true
      }
      state.permissions = list
      setStore({
        name: PERMISSIONS,
        content: state.permissions
      })
    },
    SET_TENANT_CODE: (state, tenantCode) => {
      state.tenantCode = tenantCode
      setStore({
        name: TENANTCODE,
        content: state.tenantCode
      })
    }
  }
}
export default user
