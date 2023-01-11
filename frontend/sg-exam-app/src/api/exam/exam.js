import request from '@/router/axios'

const baseExaminationUrl = '/sg-user-service/v1/examination/'

export function fetchList (query) {
  return request({
    url: baseExaminationUrl + 'examinationList',
    method: 'get',
    params: query
  })
}

export function fetchAllSubjectList (query) {
  return request({
    url: baseExaminationUrl + 'anonymousUser/allSubjectList',
    method: 'get',
    params: query
  })
}

export function getObj (id, query) {
  return request({
    url: baseExaminationUrl + id,
    method: 'get',
    params: query
  })
}

export function anonymousUserGetObj (id, query) {
  return request({
    url: baseExaminationUrl + 'anonymousUser/' + id,
    method: 'get',
    params: query
  })
}

export function getSubjectIds (id, query) {
  return request({
    url: baseExaminationUrl + id + '/subjectIds',
    method: 'get',
    params: query
  })
}

export function anonymousUserGetSubjectIds (id, query) {
  return request({
    url: baseExaminationUrl + 'anonymousUser/' + id + '/subjectIds',
    method: 'get',
    params: query
  })
}

export function addObj (obj) {
  return request({
    url: baseExaminationUrl,
    method: 'post',
    data: obj
  })
}

export function putObj (obj) {
  return request({
    url: baseExaminationUrl,
    method: 'put',
    data: obj
  })
}

export function delObj (id) {
  return request({
    url: baseExaminationUrl + id,
    method: 'delete'
  })
}

export function delAllObj (obj) {
  return request({
    url: baseExaminationUrl + 'deleteAll',
    method: 'post',
    data: obj
  })
}
