import request from '@/router/axios'

const baseKnowledgeUrl = '/exam-service/v1/knowledge/'

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

export function addObj (obj) {
  return request({
    url: baseKnowledgeUrl,
    method: 'post',
    data: obj
  })
}

export function putObj (obj) {
  return request({
    url: baseKnowledgeUrl,
    method: 'put',
    data: obj
  })
}

export function delObj (id) {
  return request({
    url: baseKnowledgeUrl + id,
    method: 'delete'
  })
}

export function delAllObj (obj) {
  return request({
    url: baseKnowledgeUrl + 'deleteAll',
    method: 'post',
    data: obj
  })
}
