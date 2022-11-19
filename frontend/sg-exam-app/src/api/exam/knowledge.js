import request from '@/router/axios'

const baseKnowledgeUrl = '/user-service/v1/knowledge/'

export function fetchKnowledgeList (query) {
  return request({
    url: baseKnowledgeUrl + 'knowledgeList',
    method: 'get',
    params: query
  })
}

export function getObj (id) {
  return request({
    url: baseKnowledgeUrl + id,
    method: 'get'
  })
}
