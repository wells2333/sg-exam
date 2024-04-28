import axios from 'axios'
import store from '../store'
import { getToken, setToken, getTenantCode } from '@/utils/auth'
import { isSuccess } from '@/utils/util'
import { refreshToken } from '@/api/admin/login'
import { Message } from 'element-ui'
import errorCode from '@/const/errorCode'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css'// progress bar style
const whiteList = ['/auth/authentication/removeToken']// 白名单

// 超时时间
const instance = axios.create({
  // baseURL: baseURL,
  timeout: 60000,
  withCredentials: true
})

// 跨域请求，允许保存cookie
NProgress.configure({ showSpinner: false })// NProgress Configuration

// 是否正在刷新token标记
let isRefreshing = false
// refresh 次数阈值
let refreshCnt = 0
// 缓存所有请求(重试队列、每一项都是待执行的函数)
let requests = []

// 封装刷新token的方法
function doRefreshToken(config) {
  async function refresh() {
    const token = getToken()
    if (!isRefreshing) {
      isRefreshing = true
      refreshCnt++
      if (refreshCnt > 3) {
        Message({ message: 'token 刷新次数过多，请重新登录！', type: 'error' })
        return
      }
      try {
        refreshToken(token).then(response => {
          setToken(response.data.result.token)
          store.commit('SET_TOKEN', response.data.result.token)
          requests.forEach((cb) => cb())
          requests = []
        }).catch(err => {
          console.log(err, 'error')
        })
      } catch (error) {
        Message({ message: 'token 刷新失败，请重新登录！', type: 'error' })
        // 这里执行需重新登录的相关操作
      } finally {
        isRefreshing = false
      }
    }
  }

  refresh()

  return new Promise((resolve) => {
    requests.push((_) => {
      config.headers['Authorization'] = getToken()
      resolve(instance(config))
    })
  })
}

// HTTP request拦截
instance.interceptors.request.use(config => {
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
instance.interceptors.response.use(data => {
  NProgress.done()
  // 请求失败，弹出提示信息
  if (!isSuccess(data.data)) {
    const { code, message, result } = data.data
    // token失效
    if (result === 401) {
      return doRefreshToken(data.config)
    }
    const errMsg = errorCode[String(code)] || message || errorCode['default']
    Message({ message: errMsg, type: 'error' })
  }
  return data
}, error => {
  NProgress.done()
  if (error.response) {
    // const originalRequest = error.config
    // // 接口返回401并且已经重试过，自动刷新token
    // if ((error.response.status === 401 || error.response.status === 403) && !originalRequest._retry) {
    //   // 退出请求
    //   if (originalRequest.url.indexOf('removeToken') !== -1) {
    //     return
    //   }
    //   return refreshToken().then(response => {
    //     // 保存新的token
    //     setToken(response.data.token)
    //     store.commit('SET_TOKEN', response.data.token)
    //     // 带上新的token
    //     originalRequest.headers['Authorization'] = response.data.token
    //     // 重新请求
    //     return axios(originalRequest)
    //   }).catch(() => {
    //     // 刷新失败，执行退出
    //     store.dispatch('LogOut').then(() => location.reload())
    //   })
    // } else if (error.response.status === 423) {
    //   Message({ message: '演示环境不能操作', type: 'warning' })
    // } else if (error.response.status === 404) {
    //   // 跳转到404页面
    //   router.replace({
    //     path: '404',
    //     query: { redirect: router.currentRoute.fullPath }
    //   })
    // } else {
    // 其它错误则弹出提示
    const { code, msg } = error.response.data
    const errMsg = errorCode[String(code)] || msg || errorCode['default']
    Message({ message: errMsg, type: 'error' })
    // }
  }
  return Promise.reject(new Error(error))
})

export default instance
