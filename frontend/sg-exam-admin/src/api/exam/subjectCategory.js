import request from '@/router/axios'

const baseSubjectCategoryUrl = '/exam-service/v1/subjectCategory/'

export function fetchCategoryTree (query) {
  return request({
    url: baseSubjectCategoryUrl + 'categories',
    method: 'get',
    params: query
  })
}

export function addCategory (obj) {
  return request({
    url: baseSubjectCategoryUrl,
    method: 'post',
    data: obj
  })
}

export function getCategory (id) {
  return request({
    url: baseSubjectCategoryUrl + id,
    method: 'get'
  })
}

export function delCategory (id) {
  return request({
    url: baseSubjectCategoryUrl + id,
    method: 'delete'
  })
}

export function putCategory (obj) {
  return request({
    url: baseSubjectCategoryUrl,
    method: 'put',
    data: obj
  })
}
