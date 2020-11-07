import request from '@/router/axios'

const baseClientUrl = '/oauth/v1/client/'

export function clientList () {
  return request({
    url: baseClientUrl + 'clientList',
    method: 'get'
  })
}

export function fetchList (query) {
  return request({
    url: baseClientUrl + 'clientList',
    method: 'get',
    params: query
  })
}

export function getObj (id) {
  return request({
    url: baseClientUrl + id,
    method: 'get'
  })
}

export function addObj (obj) {
  return request({
    url: baseClientUrl,
    method: 'post',
    data: obj
  })
}

export function putObj (obj) {
  return request({
    url: baseClientUrl,
    method: 'put',
    data: obj
  })
}

export function delObj (id) {
  return request({
    url: baseClientUrl + id,
    method: 'delete'
  })
}

export function delAllObj (obj) {
  return request({
    url: baseClientUrl + 'deleteAll',
    method: 'post',
    data: obj
  })
}
