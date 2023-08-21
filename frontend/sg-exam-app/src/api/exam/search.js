import request from '@/router/axios'

const baseUrl = '/sg-user-service/v1/search/'

export function searchByQuery (query) {
  return request({
    url: baseUrl + 'search',
    method: 'get',
    params: query
  })
}

export function searchDetailByQuery (query) {
  return request({
    url: baseUrl + 'searchDetail',
    method: 'get',
    params: query
  })
}

export function searchRank () {
  return request({
    url: baseUrl + 'searchRank',
    method: 'get'
  })
}
