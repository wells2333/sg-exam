import { validatenull } from '@/utils/validate'

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
  window.sessionStorage.setItem(name, JSON.stringify(obj))
}

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

export const removeStore = params => {
  const {
    name
  } = params
  window.sessionStorage.removeItem(name)
}
