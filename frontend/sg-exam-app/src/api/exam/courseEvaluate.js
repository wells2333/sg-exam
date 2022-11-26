import request from '@/router/axios'

const baseCourseUrl = '/user-service/v1/evaluate/'

export function getEvaluateList (query) {
  return request({
    url: baseCourseUrl + 'list',
    method: 'get',
    params: query
  })
}

export function addObj (obj) {
  return request({
    url: baseCourseUrl,
    method: 'post',
    data: obj
  })
}
