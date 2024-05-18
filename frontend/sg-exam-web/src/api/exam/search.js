import request from '@/router/axios'
import {VUE_APP_API_CONTEXT_PATH} from '@/utils/env'

const baseUrl = VUE_APP_API_CONTEXT_PATH + '/v1/search/'

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
