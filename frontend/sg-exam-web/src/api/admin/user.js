import request from '@/router/axios'
import {VUE_APP_API_CONTEXT_PATH} from '@/utils/env'

const baseUrl = VUE_APP_API_CONTEXT_PATH + '/v1/user/'

export function fetchList (query) {
  return request({
    url: baseUrl + 'userList',
    method: 'get',
    params: query
  })
}

export function addObj (obj) {
  return request({
    url: baseUrl,
    method: 'post',
    data: obj
  })
}

export function getObj (id) {
  return request({
    url: baseUrl + id,
    method: 'get'
  })
}

export function delObj (id) {
  return request({
    url: baseUrl + id,
    method: 'delete'
  })
}

export function putObj (obj) {
  return request({
    url: baseUrl,
    method: 'put',
    data: obj
  })
}

export function updateObjInfo (obj) {
  return request({
    url: baseUrl + 'updateInfo',
    method: 'put',
    data: obj
  })
}

export function updatePassword (obj) {
  return request({
    url: baseUrl + 'anonymousUser/updatePassword',
    method: 'put',
    data: obj
  })
}

export function updateAvatar (obj) {
  return request({
    url: baseUrl + 'updateAvatar',
    method: 'post',
    data: obj
  })
}

export function delAllObj (obj) {
  return request({
    url: baseUrl + 'deleteAll',
    method: 'post',
    data: obj
  })
}

export function checkExist (username, tenantCode) {
  return request({
    url: baseUrl + 'anonymousUser/checkExist/' + username,
    method: 'get',
    params: { tenantCode, identityType: 1 }
  })
}
