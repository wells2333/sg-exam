import request from '@/router/axios'

const baseSubjectCategoryUrl = '/exam/v1/subjectCategory/'

export function fetchTree (query) {
  return request({
    url: baseSubjectCategoryUrl + 'categories',
    method: 'get',
    params: query
  })
}

export function addObj (obj) {
  return request({
    url: baseSubjectCategoryUrl,
    method: 'post',
    data: obj
  })
}

export function getObj (id) {
  return request({
    url: baseSubjectCategoryUrl + id,
    method: 'get'
  })
}

export function delObj (id) {
  return request({
    url: baseSubjectCategoryUrl + id,
    method: 'delete'
  })
}

export function putObj (obj) {
  return request({
    url: baseSubjectCategoryUrl,
    method: 'put',
    data: obj
  })
}
