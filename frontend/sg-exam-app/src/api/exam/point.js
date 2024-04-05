import request from '@/router/axios'
import {VUE_APP_API_CONTEXT_PATH} from '@/utils/env'

const baseUrl = VUE_APP_API_CONTEXT_PATH + '/v1/knowledgePoint/'

export function getKnowledgePoint (id) {
  return request({
    url: baseUrl + id,
    method: 'get'
  })
}

export function getKnowledgePointDetail (id) {
  return request({
    url: baseUrl + 'detail/' + id,
    method: 'get'
  })
}
