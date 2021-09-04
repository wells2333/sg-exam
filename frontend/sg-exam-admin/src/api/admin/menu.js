import request from '@/router/axios'

const baseMenuUrl = '/user-service/v1/menu/'

/* 获取菜单 */
export function GetMenu () {
  return request({
    url: baseMenuUrl + 'userMenu',
    method: 'get'
  })
}

export function fetchTree (query) {
  return request({
    url: baseMenuUrl + 'menus',
    method: 'get',
    params: query
  })
}

export function addObj (obj) {
  return request({
    url: baseMenuUrl,
    method: 'post',
    data: obj
  })
}

export function getObj (id) {
  return request({
    url: baseMenuUrl + id,
    method: 'get'
  })
}

export function delObj (id) {
  return request({
    url: baseMenuUrl + id,
    method: 'delete'
  })
}

export function putObj (obj) {
  return request({
    url: baseMenuUrl,
    method: 'put',
    data: obj
  })
}

// 导出
export function exportObj (obj) {
  return request({
    url: baseMenuUrl + 'export',
    method: 'post',
    responseType: 'arraybuffer',
    headers: { 'filename': 'utf-8' },
    data: obj
  })
}
