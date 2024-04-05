import request from '@/router/axios'
import {VUE_APP_API_CONTEXT_PATH} from '@/utils/env'

const baseUrl = VUE_APP_API_CONTEXT_PATH + '/v1/exam/evaluate/'

export function getExamEvaluateList (query) {
  return request({
    url: baseUrl + 'list',
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
