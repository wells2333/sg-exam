import axios from 'axios'
import store from '../store'
import router from './index'
import { getToken, setToken, getRefreshToken, getTenantCode } from '@/utils/auth'
import { isNotEmpty, isSuccess } from '@/utils/util'
import { refreshToken } from '@/api/admin/login'
import { Message } from 'element-ui'
import errorCode from '@/const/errorCode'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css'// progress bar style
const whiteList = ['/auth/authentication/removeToken']// 白名单

// 超时时间
axios.defaults.timeout = 30000
// 跨域请求，允许保存cookie
axios.defaults.withCredentials = true
NProgress.configure({ showSpinner: false })// NProgress Configuration

// HTTP request拦截
axios.interceptors.request.use(config => {
  NProgress.start() // start progress bar
  if (store.getters.token && whiteList.indexOf(config.url) === -1) {
    const authorization = config.headers['Authorization']
    if (authorization === undefined || authorization.indexOf('Basic') === -1) {
      config.headers['Authorization'] = getToken() // 让每个请求携带token
    }
  }
  // 增加租户编号请求头
  const tenantCode = config.headers['Tenant-Code']
  if (tenantCode === undefined) {
    config.headers['Tenant-Code'] = getTenantCode()
  }
  return config
}, error => {
  return Promise.reject(error)
})

// HTTP response拦截
axios.interceptors.response.use(data => {
  NProgress.done()
  // 请求失败，弹出提示信息
  if (!isSuccess(data.data)) {
    const { code, message } = data.data
    const errMsg = errorCode[String(code)] || message || errorCode['default']
    Message({ message: errMsg, type: 'error' })
  }
  return data
}, error => {
  NProgress.done()
  if (error.response) {
    const originalRequest = error.config
    const currentRefreshToken = getRefreshToken()
    // 接口返回401并且已经重试过，自动刷新token
    if ((error.response.status === 401 || error.response.status === 403) && !originalRequest._retry && isNotEmpty(currentRefreshToken)) {
      // 退出请求
      if (originalRequest.url.indexOf('removeToken') !== -1) {
        return
      }
      return refreshToken().then(response => {
        // 保存新的token
        setToken(response.data.token)
        store.commit('SET_TOKEN', response.data.token)
        // 带上新的token
        originalRequest.headers['Authorization'] = response.data.token
        // 重新请求
        return axios(originalRequest)
      }).catch(() => {
        // 刷新失败，执行退出
        store.dispatch('LogOut').then(() => location.reload())
      })
    } else if (error.response.status === 423) {
      Message({ message: '演示环境不能操作', type: 'warning' })
    } else if (error.response.status === 404) {
      // 跳转到404页面
      router.replace({
        path: '404',
        query: { redirect: router.currentRoute.fullPath }
      })
    } else {
      // 其它错误则弹出提示
      const { code, msg } = error.response.data
      const errMsg = errorCode[String(code)] || msg || errorCode['default']
      Message({ message: errMsg, type: 'error' })
    }
  }
  return Promise.reject(new Error(error))
})

export default axios
