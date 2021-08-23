import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css'// progress bar style
import { setTitle } from '@/utils/util'
NProgress.configure({ showSpinner: false })// NProgress Configuration

// permission judge function
function hasPermission (roles, permissionRoles) {
  if (roles.indexOf('admin') >= 0) return true // admin permission passed directly
  if (!permissionRoles) return true
  return roles.some(role => permissionRoles.indexOf(role) >= 0)
}

const whiteList = ['/login', '/auth-redirect', '/404', '/401', '/500', '/lock']// no redirect whitelist

router.beforeEach((to, from, next) => {
  NProgress.start() // start progress bar
  const value = to.query.src ? to.query.src : to.path
  const label = to.query.name ? to.query.name : to.name
  if (whiteList.indexOf(value) === -1) {
    store.commit('ADD_TAG', {
      label: label,
      value: value,
      query: to.query
    })
  }
  if (store.getters.access_token) { // determine if there has token
    if (to.path === '/login') {
      next({ path: '/' })
      NProgress.done()
    } else {
      const userInfo = store.getters.userInfo
      if (Object.keys(userInfo).length === 0) { // 判断当前用户是否已拉取完user_info信息
        store.dispatch('GetUserInfo').then(res => { // 拉取user_info
          next({ ...to, replace: true }) // hack方法 确保addRoutes已完成 ,set the replace: true so the navigation will not leave a history record
        }).catch((err) => {
          store.dispatch('FedLogOut').then(() => {
            Message.error(err || 'Verification failed, please login again')
            next({ path: '/' })
          })
        })
      } else {
        // 没有动态改变权限的需求可直接next() 删除下方权限判断 ↓
        if (hasPermission(store.getters.roles, to.meta.roles)) {
          if (to.path !== '/dashboard' && to.path.endsWith('/dashboard')) {
            next({ path: '/dashboard' })
          } else {
            next()
          }
        } else {
          next({ path: '/401', replace: true, query: { noGoBack: true } })
        }
      }
    }
  } else {
    if (whiteList.indexOf(to.path) !== -1) { // 在免登录白名单，直接进入
      next()
    } else {
      next(`/login?redirect=dashboard`) // 否则全部重定向到登录页
      NProgress.done() // if current page is login will not trigger afterEach hook, so manually handle it
    }
  }
})

// 寻找子菜单的父类
function findMenuParent (tag) {
  const tagCurrent = []
  tagCurrent.push(tag)
  return tagCurrent
}

router.afterEach(() => {
  NProgress.done() // 结束进度条
  setTimeout(() => {
    const tag = store.getters.tag
    setTitle(tag.label)
    store.commit('SET_TAG_CURRENT', findMenuParent(tag))
  }, 0)
})
