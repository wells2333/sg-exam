import request from '@/router/axios'

const baseUserUrl = '/user-service/v1/user/'

export function fetchList (query) {
  return request({
    url: baseUserUrl + 'userList',
    method: 'get',
    params: query
  })
}

export function addObj (obj) {
  return request({
    url: baseUserUrl,
    method: 'post',
    data: obj
  })
}

export function getObj (id) {
  return request({
    url: baseUserUrl + id,
    method: 'get'
  })
}

export function delObj (id) {
  return request({
    url: baseUserUrl + id,
    method: 'delete'
  })
}

export function putObj (obj) {
  return request({
    url: baseUserUrl,
    method: 'put',
    data: obj
  })
}

export function updateObjInfo (obj) {
  return request({
    url: baseUserUrl + 'updateInfo',
    method: 'put',
    data: obj
  })
}

export function updatePassword (obj) {
  return request({
    url: baseUserUrl + 'anonymousUser/updatePassword',
    method: 'put',
    data: obj
  })
}

export function updateAvatar (obj) {
  return request({
    url: baseUserUrl + 'updateAvatar',
    method: 'put',
    data: obj
  })
}

export function delAllObj (obj) {
  return request({
    url: baseUserUrl + 'deleteAll',
    method: 'post',
    data: obj
  })
}

export function checkExist (username, tenantCode) {
  return request({
    url: baseUserUrl + 'anonymousUser/checkExist/' + username,
    method: 'get',
    params: { tenantCode, identityType: 1 }
  })
}
