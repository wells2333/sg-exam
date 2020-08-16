import request from '@/router/axios'

const baseLogUrl = '/user/v1/log/'

export function fetchList (query) {
  return request({
    url: baseLogUrl + 'logList',
    method: 'get',
    params: query
  })
}

export function delObj (id) {
  return request({
    url: baseLogUrl + id,
    method: 'delete'
  })
}

export function delAllObj (obj) {
  return request({
    url: baseLogUrl + 'deleteAll',
    method: 'post',
    data: obj
  })
}
