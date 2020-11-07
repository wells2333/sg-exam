import { validatenull } from './validate'
import { baseUrl } from '@/config/env'
import CryptoJS from 'crypto-js'
import store from "../store";

/**
 * 加密处理
 */
export const encryption = (params) => {
  var {
    data,
    type,
    param,
    key
  } = params
  const result = JSON.parse(JSON.stringify(data))
  if (type === 'Base64') {
    param.forEach(ele => {
      result[ele] = btoa(result[ele])
    })
  } else {
    param.forEach(ele => {
      var data = result[ele]
      key = CryptoJS.enc.Latin1.parse(key)
      var iv = key
      var encrypted = CryptoJS.AES.encrypt(
        data,
        key,
        { iv: iv,
          mode: CryptoJS.mode.CBC,
          padding: CryptoJS.pad.ZeroPadding
        })
      result[ele] = encrypted.toString()
    })
  }
  return result
}

export const initMenu = (router, menu) => {
  if (menu.length === 0) {
    return
  }
  return router.addRoutes(formatRoutes(menu))
}

export const formatRoutes = (aMenu) => {
  const aRouter = []
  aMenu.forEach(oMenu => {
    const {
      path,
      component,
      name,
      icon,
      children,
      redirect
    } = oMenu
    if (!validatenull(component)) {
      const oRouter = {
        path: path,
        component (resolve) {
          let componentPath = ''
          if (component === 'Layout') {
            require(['../views/layout/Layout.vue'], resolve)
            return
          } else {
            componentPath = component
          }
          require([`../${componentPath}.vue`], resolve)
        },
        name: name,
        icon: icon,
        redirect: redirect,
        children: validatenull(children) ? [] : formatRoutes(children),
        title: name
      }
      aRouter.push(oRouter)
    }
  })
  return aRouter
}

/**
 * 浏览器判断是否全屏
 */
export const fullscreenToggel = () => {
  if (fullscreenEnable()) {
    exitFullScreen()
  } else {
    reqFullScreen()
  }
}
/**
 * esc监听全屏
 */
export const listenfullscreen = (callback) => {
  function listen () {
    callback()
  }
  document.addEventListener('fullscreenchange', function (e) {
    listen()
  })
  document.addEventListener('mozfullscreenchange', function (e) {
    listen()
  })
  document.addEventListener('webkitfullscreenchange', function (e) {
    listen()
  })
  document.addEventListener('msfullscreenchange', function (e) {
    listen()
  })
}

/**
 * 浏览器判断是否全屏
 */
export const fullscreenEnable = () => {
  var isFullscreen = document.fullscreenEnabled ||
    window.fullScreen ||
    document.mozFullscreenEnabled ||
    document.webkitIsFullScreen
  return isFullscreen
}

/**
 * 浏览器全屏
 */
export const reqFullScreen = () => {
  if (document.documentElement.requestFullScreen) {
    document.documentElement.requestFullScreen()
  } else if (document.documentElement.webkitRequestFullScreen) {
    document.documentElement.webkitRequestFullScreen()
  } else if (document.documentElement.mozRequestFullScreen) {
    document.documentElement.mozRequestFullScreen()
  }
}

/**
 * 浏览器退出全屏
 */
export const exitFullScreen = () => {
  if (document.documentElement.requestFullScreen) {
    document.exitFullScreen()
  } else if (document.documentElement.webkitRequestFullScreen) {
    document.webkitCancelFullScreen()
  } else if (document.documentElement.mozRequestFullScreen) {
    document.mozCancelFullScreen()
  }
}

/**
 * 递归寻找子类的父类
 */
export const findParent = (menu, id) => {
  for (let i = 0; i < menu.length; i++) {
    if (menu[i].children.length !== 0) {
      for (let j = 0; j < menu[i].children.length; j++) {
        if (menu[i].children[j].id === id) {
          return menu[i]
        } else {
          if (menu[i].children[j].children.length !== 0) {
            return findParent(menu[i].children[j].children, id)
          }
        }
      }
    }
  }
}

/**
 * 总体路由处理器
 */
export const resolveUrlPath = (url, name) => {
  let reqUrl = url
  if (url.indexOf('#') !== -1 && url.indexOf('http') === -1) {
    const port = reqUrl.substr(reqUrl.indexOf(':'))
    reqUrl = `/iframe/urlPath?src=${baseUrl}${port}${reqUrl.replace('#', '').replace(port, '')}}&name=${name}`
  } else if (url.indexOf('http') !== -1) {
    reqUrl = `/iframe/urlPath?src=${reqUrl}&name=${name}`
  } else {
    reqUrl = `${reqUrl}`
  }
  return reqUrl
}

/**
 * 总体路由设置器
 */
export const setUrlPath = ($route) => {
  let value = ''
  if ($route.query.src) {
    value = $route.query.src
  } else {
    value = $route.path
  }
  return value
}

/**
 * 动态插入css
 */
export const loadStyle = url => {
  const link = document.createElement('link')
  link.type = 'text/css'
  link.rel = 'stylesheet'
  link.href = url
  const head = document.getElementsByTagName('head')[0]
  head.appendChild(link)
}

/**
 * 生成随机len位数字
 */
export const randomLenNum = (len, date) => {
  let random = ''
  random = Math.ceil(Math.random() * 100000000000000).toString().substr(0, typeof len === 'number' ? len : 4)
  if (date) random = random + Date.now()
  return random
}

/**
 * 检查选中
 * @param multipleSelection
 * @param obj
 * @returns {boolean}
 */
export const checkMultipleSelect = (multipleSelection, obj) => {
  if (multipleSelection.length === 0) {
    obj.$message({
      message: '请选择记录！',
      type: 'warning'
    })
    return false
  }
  return true
}

/**
 * 设置浏览器头部标题
 */
export const setTitle = function (title) {
  title = title ? `${title}——硕果云` : '硕果云'
  window.document.title = title
}

/**
 * 导出Excel
 */
export const exportExcel = function (response) {
  const blob = new Blob([response.data], { type: 'application/vnd.ms-excel;charset=utf-8' })
  const link = document.createElement('a')
  link.href = window.URL.createObjectURL(blob)
  // 获取文件名
  const disposition = response.headers['content-disposition']
  if (disposition !== undefined) {
    link.download = decodeURI(disposition.substring(disposition.indexOf('=') + 2, disposition.length - 1))
    link.click()
  }
}

/**
 * 判断对象是否为空
 * @param obj
 * @returns {boolean}
 */
export const isNotEmpty = (obj) => {
  let flag = true
  if (obj === '' || obj == null || obj === undefined || obj === 'undefined') {
    flag = false
  }
  return flag
}

/**
 * 通知
 * @param obj
 * @param title
 * @param msg
 * @param type
 * @param duration
 */
export const notify = (obj, title, msg, type, duration) => {
  obj.$notify({ title: title, message: msg, type: type, duration: duration })
}

/**
 * 成功通知
 * @param obj
 * @param msg
 */
export const notifySuccess = (obj, msg) => {
  notify(obj, '成功', msg, 'success', 2000)
}

/**
 * 失败通知
 * @param obj
 * @param msg
 */
export const notifyFail = (obj, msg) => {
  notify(obj, '失败', msg, 'error', 2000)
}

/**
 * 消息提示
 * @param obj
 * @param message
 * @param type
 */
export const message = (obj, message, type) => {
  obj.$message({ message: message, type: type })
}

/**
 * 成功消息提示
 * @param obj
 * @param message
 */
export const messageSuccess = (obj, message) => {
  obj.$message({ message: message, type: 'success' })
}

/**
 * 警告消息提示
 * @param obj
 * @param message
 */
export const messageWarn = (obj, message) => {
  obj.$message({ message: message, type: 'warning' })
}

/**
 * 失败消息提示
 * @param obj
 * @param message
 */
export const messageFail = (obj, message) => {
  obj.$message({ message: message, type: 'error' })
}

/**
 * 格式化时间戳
 * @param date
 * @param fmt
 * @returns {*}
 */
export const formatDate = (date, fmt) => {
  if (/(y+)/.test(fmt)) {
    fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length))
  }
  let o = {
    'M+': date.getMonth() + 1,
    'd+': date.getDate(),
    'h+': date.getHours(),
    'm+': date.getMinutes(),
    's+': date.getSeconds()
  }
  for (let k in o) {
    if (new RegExp(`(${k})`).test(fmt)) {
      let str = o[k] + ''
      fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? str : padLeftZero(str))
    }
  }
  return fmt
}

export const padLeftZero = (str) => {
  return ('00' + str).substr(str.length)
}

/**
 * 判断响应是否成功
 * @param obj
 * @returns {boolean}
 */
export const isSuccess = (response) => {
  let success = true
  if (!isNotEmpty(response) || (response.code !== undefined && response.code !== 200)) {
    success = false
  }
  return success
}

/**
 * 截取指定长度
 * @param str
 * @param length
 * @returns {string}
 */
export const commonFilter = (str, length) => {
  if (str.length > length) {
    return str.substring(0, length) + '...'
  }
  return str
}

/**
 * 新建状态
 */
export const isCreate = (status) => {
  return status === 'create'
}

export const trimComma = (str) => {
  return str.replace(new RegExp('^,*|,*$', 'gm'), '')
}

export const isExistVisitView = (visitedViews, code) => {
  let isExist = false
  visitedViews.forEach(view => {
    if (view.name === code) {
      isExist = true
    }
  })
  return isExist
}
