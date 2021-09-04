import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import { getToken } from '@/utils/auth'

NProgress.configure({ showSpinner: false })// NProgress Configuration

const whiteList = ['/', '/home', '/register', '/login', '/auth-redirect', '/404', '/401', '/lock', '/reset-password']// no redirect whitelist

router.beforeEach((to, from, next) => {
  NProgress.start() // 进度条
  if (to.path.indexOf('/mobile') !== -1) {
    next()
  } else if (getToken()) { // 判断是否已登录
    if (to.path === '/login' || to.path === '/register') {
      next({ path: '/' })
    } else {
      const userInfo = store.getters.userInfo
      if (Object.keys(userInfo).length === 0) { // 判断当前用户是否已拉取完user_info信息
        store.dispatch('GetUserInfo').then(res => { // 拉取user_info
          next({ ...to, replace: true }) // hack方法 确保addRoutes已完成
        }).catch((err) => {
          store.dispatch('FedLogOut').then(() => {
            Message.error(err || 'Verification failed, please login again')
            next({ path: '/' })
          })
        })
        // 获取系统配置信息
        const sysConfig = store.getters.sysConfig
        if (Object.keys(sysConfig).length === 0) {
          store.dispatch('GetSysConfig').then(res => {}).catch(() => {
            console.log('获取系统配置失败！')
          })
        }
      } else {
        next()
      }
    }
    NProgress.done()
  } else {
    if (whiteList.indexOf(to.path) !== -1) { // 在免登录白名单，直接进入
      next()
    } else {
      next('/login') // 重定向到登录页
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  NProgress.done()
})
