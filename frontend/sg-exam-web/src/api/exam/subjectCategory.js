import request from '@/router/axios'
import {VUE_APP_API_CONTEXT_PATH} from '@/utils/env'

const baseUrl = VUE_APP_API_CONTEXT_PATH + '/v1/subjectCategory/'

export function fetchTree (query) {
  return request({
    url: baseUrl + 'categories',
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
