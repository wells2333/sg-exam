import request from '@/router/axios'

const baseRoleUrl = '/user-service/v1/role/'

export function roleList () {
  return request({
    url: baseRoleUrl + 'roleList',
    method: 'get'
  })
}

export function fetchList (query) {
  return request({
    url: baseRoleUrl + 'roleList',
    method: 'get',
    params: query
  })
}

export function allRoles (query) {
  return request({
    url: baseRoleUrl + 'allRoles',
    method: 'get',
    params: query
  })
}

export function getObj (id) {
  return request({
    url: baseRoleUrl + id,
    method: 'get'
  })
}

export function addObj (obj) {
  return request({
    url: baseRoleUrl,
    method: 'post',
    data: obj
  })
}

export function putObj (obj) {
  return request({
    url: baseRoleUrl,
    method: 'put',
    data: obj
  })
}

export function delObj (id) {
  return request({
    url: baseRoleUrl + id,
    method: 'delete'
  })
}

export function permissionUpdate (id, menus) {
  return request({
    url: baseRoleUrl + 'roleMenuUpdate',
    method: 'put',
    data: {
      id: id,
      menuIds: menus
    }
  })
}

export function fetchRoleTree (roleName) {
  return request({
    url: '/user-service/v1/menu/roleTree/' + roleName,
    method: 'get'
  })
}

export function fetchDeptTree (query) {
  return request({
    url: '/user-service/v1/dept/depts',
    method: 'get',
    params: query
  })
}

export function delAllObj (obj) {
  return request({
    url: baseRoleUrl + 'deleteAll',
    method: 'post',
    data: obj
  })
}
