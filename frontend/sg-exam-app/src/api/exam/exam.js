import request from '@/router/axios'

const baseExaminationUrl = '/sg-user-service/v1/examination/'

export function canStart (id) {
  return request({
    url: baseExaminationUrl + 'anonymousUser/canStart?id=' + id,
    method: 'get'
  })
}

export function fetchList (query) {
  return request({
    url: baseExaminationUrl + 'userExaminationList',
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

export function getObjDetail (id, query) {
  return request({
    url: baseExaminationUrl + id + '/detail',
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
