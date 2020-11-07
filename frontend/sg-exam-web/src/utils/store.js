import { validatenull } from '@/utils/validate'
/**
 * 存储sessionStorage
 */
export const setStore = (params) => {
  const {
    name,
    content,
    type
  } = params
  const obj = {
    dataType: typeof (content),
    content: content,
    type: type,
    datetime: new Date().getTime()
  }
  // 直接放到sessionStorage
  window.sessionStorage.setItem(name, JSON.stringify(obj))
}
/**
 * 获取sessionStorage
 */
export const getStore = (params) => {
  const {
    name
  } = params
  let obj = {}
  let content
  obj = window.sessionStorage.getItem(name)
  if (validatenull(obj)) return
  obj = JSON.parse(obj)
  if (obj.dataType === 'string') {
    content = obj.content
  } else if (obj.dataType === 'number') {
    content = Number(obj.content)
  } else if (obj.dataType === 'object') {
    content = obj.content
  }
  return content
}
/**
 * 删除sessionStorage
 */
export const removeStore = params => {
  const {
    name
  } = params
  window.sessionStorage.removeItem(name)
}
