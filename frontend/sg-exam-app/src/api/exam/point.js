import request from '@/router/axios'

const baseCourseUrl = '/user-service/v1/knowledgePoint/'

export function getKnowledgePoint (id) {
  return request({
    url: baseCourseUrl + id,
    method: 'get'
  })
}

export function getKnowledgePointDetail (id) {
  return request({
    url: baseCourseUrl + 'detail/' + id,
    method: 'get'
  })
}
