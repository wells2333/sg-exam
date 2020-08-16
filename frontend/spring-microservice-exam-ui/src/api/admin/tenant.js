import request from '@/router/axios'

const baseTenantUrl = '/user/v1/tenant/'

export function fetchList (query) {
  return request({
    url: baseTenantUrl + 'tenantList',
    method: 'get',
    params: query
  })
}

export function addObj (obj) {
  return request({
    url: baseTenantUrl,
    method: 'post',
    data: obj
  })
}

export function getObj (id) {
  return request({
    url: baseTenantUrl + id,
    method: 'get'
  })
}

export function delObj (id) {
  return request({
    url: baseTenantUrl + id,
    method: 'delete'
  })
}

export function putObj (obj) {
  return request({
    url: baseTenantUrl,
    method: 'put',
    data: obj
  })
}

export function delAllObj (obj) {
  return request({
    url: baseTenantUrl + 'deleteAll',
    method: 'post',
    data: obj
  })
}
