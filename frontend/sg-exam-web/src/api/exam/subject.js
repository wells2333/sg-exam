import request from '@/router/axios'

const baseSubjectUrl = '/exam-service/v1/subject/'

export function subjectList () {
  return request({
    url: baseSubjectUrl + 'subjectList',
    method: 'get'
  })
}

export function fetchSubjectList (query) {
  return request({
    url: baseSubjectUrl + 'subjectList',
    method: 'get',
    params: query
  })
}

export function getObj (id, query) {
  return request({
    url: baseSubjectUrl + id,
    method: 'get',
    params: query
  })
}

export function addSubject (obj) {
  return request({
    url: baseSubjectUrl,
    method: 'post',
    data: obj
  })
}

export function putSubject (obj) {
  return request({
    url: baseSubjectUrl,
    method: 'put',
    data: obj
  })
}

export function delSubject (id) {
  return request({
    url: baseSubjectUrl + id,
    method: 'delete'
  })
}

export function delAllSubject (obj) {
  return request({
    url: baseSubjectUrl + 'deleteAll',
    method: 'post',
    data: obj
  })
}

export function getSubjectAnswer (obj) {
  return request({
    url: baseSubjectUrl + 'subjectAnswer',
    method: 'get',
    params: obj
  })
}

export function anonymousUserGetSubjectAnswer (obj) {
  return request({
    url: baseSubjectUrl + 'anonymousUser/subjectAnswer',
    method: 'get',
    params: obj
  })
}
