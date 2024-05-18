import request from '@/router/axios'
import {VUE_APP_API_CONTEXT_PATH} from '@/utils/env'

const baseUrl = VUE_APP_API_CONTEXT_PATH + '/v1/attachment/'

export function attachmentList () {
  return request({
    url: baseUrl + 'attachmentList',
    method: 'get'
  })
}

export function fetchList (query) {
  return request({
    url: baseUrl + 'attachmentList',
    method: 'get',
    params: query
  })
}

export function getObj (id) {
  return request({
    url: baseUrl + id,
    method: 'get'
  })
}

export function preview (id) {
  return request({
    url: baseUrl + id + '/preview',
    method: 'get'
  })
}

export function addObj (obj) {
  return request({
    url: baseUrl,
    method: 'post',
    data: obj
  })
}

export function putObj (obj) {
  return request({
    url: baseUrl,
    method: 'put',
    data: obj
  })
}

export function delAttachment (id) {
  return request({
    url: baseUrl + id,
    method: 'delete'
  })
}

export function delAllObj (obj) {
  return request({
    url: baseUrl + 'deleteAll',
    method: 'post',
    data: obj
  })
}
