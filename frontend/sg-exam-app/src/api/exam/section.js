import request from '@/router/axios'
import {VUE_APP_API_CONTEXT_PATH} from '@/utils/env'

const baseUrl = VUE_APP_API_CONTEXT_PATH + '/v1/section/'

export function getSection (id) {
  return request({
    url: baseUrl + id,
    method: 'get'
  })
}

export function watchSection (id) {
  return request({
    url: baseUrl + 'watchSection/' + id,
    method: 'get'
  })
}
