import request from '@/router/axios'
import {VUE_APP_API_CONTEXT_PATH} from '@/utils/env'

const baseUrl = VUE_APP_API_CONTEXT_PATH + '/v1/home/'

export function summary (query) {
  return request({
    url: baseUrl + 'summary',
    method: 'get',
    params: query
  })
}
