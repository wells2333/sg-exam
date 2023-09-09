import request from '@/router/axios'

const baseCourseUrl = '/sg-user-service/v1/exam/evaluate/'

export function getExamEvaluateList (query) {
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
