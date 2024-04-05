import request from '@/router/axios'
import {VUE_APP_API_CONTEXT_PATH} from '@/utils/env'

const baseUrl = VUE_APP_API_CONTEXT_PATH + '/v1/knowledge/'

export function fetchKnowledgeList (query) {
  return request({
    url: baseUrl + 'knowledgeList',
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
