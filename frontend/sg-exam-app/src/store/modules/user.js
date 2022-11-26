import {loginByUsername, loginBySocial, registerByUsername, logout, getUserInfo} from '@/api/admin/login'
import { setToken, removeToken, setRefreshToken, removeRefreshToken } from '@/utils/auth'
import { setStore, getStore } from '@/utils/store'
import { encryption } from '@/utils/util'

const user = {
  state: {
    userInfo: getStore({
      name: 'userInfo'
    }) || {},
    permissions: getStore({
      name: 'permissions'
    }) || {},
    roles: getStore({
      name: 'roles'
    }) || [],
    menu: getStore({
      name: 'menu'
    }) || [],
    isInitMenu: getStore({
      name: 'isInitMenu'
    }) || false,
    token: getStore({
      name: 'token'
    }) || '',
    refresh_token: getStore({
      name: 'refresh_token'
    }) || '',
    tenantCode: getStore({
      name: 'tenantCode'
    }) || ''
  },
  actions: {
    // 根据用户名登录
    LoginByUsername ({ commit, state, dispatch }, userInfo) {
      return new Promise((resolve, reject) => {
        const user = encryption({
          data: userInfo,
          key: '1234567887654321',
          param: ['credential']
        })

        // 根据用户名、密码、租户code登录
        loginByUsername(user.identifier, user.credential, user.code, user.randomStr).then(response => {
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
    // 根据手机号登录
    LoginBySocial ({ commit, state, dispatch }, userInfo) {
      return new Promise((resolve, reject) => {
        const user = encryption({
          data: userInfo,
          key: '1234567887654321',
          param: ['credential']
        })
        // 根据用户手机号、短信验证码获取token
        loginBySocial(user.phone, user.code).then(response => {
          const data = response.data
          setToken(data.token)
          setRefreshToken(data.token)
          commit('SET_TOKEN', data.token)
          commit('SET_REFRESH_TOKEN', data.token)
          commit('SET_TENANT_CODE', data.tenantCode)
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
          key: '1234567887654321',
          param: ['credential']
        })
        registerByUsername(user.identifier, user.email, user.credential, user.code, user.randomStr).then(response => {
          resolve()
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
    // 登出
    LogOut ({ commit, state }) {
      return new Promise((resolve, reject) => {
        logout(state.token, state.refresh_token).then(() => {
          // 清除权限
          commit('SET_PERMISSIONS', [])
          // 清除用户信息
          commit('SET_USER_INFO', {})
          commit('SET_TOKEN', '')
          commit('SET_REFRESH_TOKEN', '')
          commit('SET_ROLES', [])
          // 清除系统配置信息
          commit('SET_SYS_CONFIG', {})
          // 清除租户信息
          commit('SET_TENANT_CODE', {})
          removeToken()
          resolve()
        }).catch(error => {
          console.error(error)
        })
      })
    },
    // 注销session
    FedLogOut ({ commit }) {
      return new Promise(resolve => {
        // 清除权限
        commit('SET_PERMISSIONS', [])
        // 清除用户信息
        commit('SET_USER_INFO', {})
        commit('SET_TOKEN', '')
        commit('SET_REFRESH_TOKEN', '')
        commit('SET_ROLES', [])
        // 清除系统配置信息
        commit('SET_SYS_CONFIG', {})
        // 清除租户信息
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
        name: 'token',
        content: state.token
      })
    },
    SET_USER_INFO: (state, userInfo) => {
      state.userInfo = userInfo
      setStore({
        name: 'userInfo',
        content: state.userInfo
      })
    },
    SET_REFRESH_TOKEN: (state, rfToken) => {
      state.refresh_token = rfToken
      setStore({
        name: 'refresh_token',
        content: state.refresh_token
      })
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
      setStore({
        name: 'roles',
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
        name: 'permissions',
        content: state.permissions
      })
    },
    SET_TENANT_CODE: (state, tenantCode) => {
      state.tenantCode = tenantCode
      setStore({
        name: 'tenantCode',
        content: state.tenantCode
      })
    }
  }
}
export default user
