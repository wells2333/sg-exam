import CryptoJS from 'crypto-js'
import { v4 as uuidv4 } from 'uuid'

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
        {
          iv: iv,
          mode: CryptoJS.mode.CBC,
          padding: CryptoJS.pad.ZeroPadding
        })
      result[ele] = encrypted.toString()
    })
  }
  return result
}

/**
 * esc 监听全屏
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
 * 生成随机 len 位数字
 */
export const randomLenNum = (len, date) => {
  let random = ''
  random = Math.ceil(Math.random() * 100000000000000).toString().substr(0, typeof len === 'number' ? len : 4)
  if (date) random = random + Date.now()
  return random
}

/**
 * 返回附件的预览地址
 * @param sysConfig
 * @param fastFileId
 * @returns {string}
 */
export const getAttachmentPreviewUrl = function (sysConfig, fastFileId) {
  let url = ''
  if (isNotEmpty(sysConfig.fdfsHttpHost)) {
    url = sysConfig.fdfsHttpHost + '/' + fastFileId
  }
  return url
}

/**
 * 判断对象是否为空
 */
export const isNotEmpty = (obj) => {
  let flag = true
  if (obj === '' || obj == null || obj === undefined || obj === 'undefined') {
    flag = false
  }
  return flag
}

/**
 * 提示
 */
export const notify = (obj, title, msg, type, duration) => {
  obj.$notify({ title: title, message: msg, type: type, duration: duration, offset: 70 })
}

/**
 * 成功提示
 */
export const notifySuccess = (obj, msg) => {
  notify(obj, '成功', msg, 'success', 2000)
}

/**
 * 警告提示
 */
export const notifyWarn = (obj, msg) => {
  notify(obj, '警告', msg, 'warn', 2000)
}

/**
 * 失败提示
 */
export const notifyFail = (obj, msg) => {
  notify(obj, '失败', msg, 'error', 2000)
}

/**
 * 消息提示
 */
export const message = (obj, message, type) => {
  obj.$message({ message: message, type: type, offset: 70 })
}

/**
 * 成功消息提示
 */
export const messageSuccess = (obj, message) => {
  obj.$message({ message: message, type: 'success', offset: 70 })
}

/**
 * 警告消息提示
 */
export const messageWarn = (obj, message) => {
  obj.$message({ message: message, type: 'warning', offset: 70 })
}

/**
 * 失败消息提示
 */
export const messageFail = (obj, message) => {
  obj.$message({ message: message, type: 'error', offset: 70 })
}

/**
 * 手机号验证
 */
export const isValidPhone = (str) => {
  const reg = /^1[3|4|5|7|8][0-9]\d{8}$/
  return reg.test(str)
}

/**
 * 格式化时间戳
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
 */
export const isSuccess = (response) => {
  let success = true
  if (!isNotEmpty(response) || (response.code !== undefined && response.code !== 0)) {
    success = false
  }
  return success
}

/**
 * 按指定长度截取字符串，超出部分显示...
 */
export const cropStr = (str, len) => {
  let result = ''
  if (isNotEmpty(str)) {
    if (str.length > len) {
      result = str.substring(0, len) + '...'
    }
  }
  return result
}

/**
 * 截取指定长度
 */
export const commonFilter = (str, length) => {
  if (str === null || str === undefined) {
    return ''
  }
  if (str.length > length) {
    return str.substring(0, length) + '...'
  }
  return str
}

export const isCreate = (status) => {
  return status === 'create'
}

export const trimComma = (str) => {
  return str.replace(new RegExp('^,*|,*$', 'gm'), '')
}

export const uuid = () => {
  return uuidv4()
}

export const calculateDuration = (startDate) => {
  const durationMs = new Date().getTime() - startDate.getTime()
  const level1 = durationMs % (24 * 3600 * 1000)
  const hours = Math.floor(level1 / (3600 * 1000))

  const level2 = level1 % (3600 * 1000)
  const minutes = Math.floor(level2 / (60 * 1000))

  const level3 = level2 % (60 * 1000)
  const seconds = Math.round(level3 / 1000)

  let result = hours > 0 ? (hours < 10 ? '0' + hours : hours) : '00' + ':'
  result = result + (minutes > 0 ? (minutes < 10 ? '0' + minutes : minutes) : '00') + ':'
  result = result + (seconds > 0 ? (seconds < 10 ? '0' + seconds : seconds) : '00')
  return result
}
