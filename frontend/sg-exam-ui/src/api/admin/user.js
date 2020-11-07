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
    url: baseUserUrl + obj.id,
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

// 导出
export function exportObj (obj) {
  return request({
    url: baseUserUrl + 'export',
    method: 'post',
    responseType: 'arraybuffer',
    headers: { 'filename': 'utf-8' },
    data: obj
  })
}

// 重置密码
export function resetPassword (obj) {
  return request({
    url: baseUserUrl + 'anonymousUser/resetPassword',
    method: 'put',
    data: obj
  })
}
