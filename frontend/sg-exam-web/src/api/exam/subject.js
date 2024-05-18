import request from '@/router/axios'
import {VUE_APP_API_CONTEXT_PATH} from '@/utils/env'

const baseUrl = VUE_APP_API_CONTEXT_PATH + '/v1/subject/'

export function subjectList () {
  return request({
    url: baseUrl + 'subjectList',
    method: 'get'
  })
}

export function fetchSubjectList (query) {
  return request({
    url: baseUrl + 'subjectList',
    method: 'get',
    params: query
  })
}

export function getObj (id, query) {
  return request({
    url: baseUrl + id,
    method: 'get',
    params: query
  })
}

export function addSubject (obj) {
  return request({
    url: baseUrl,
    method: 'post',
    data: obj
  })
}

export function putSubject (obj) {
  return request({
    url: baseUrl,
    method: 'put',
    data: obj
  })
}

export function delSubject (id) {
  return request({
    url: baseUrl + id,
    method: 'delete'
  })
}

export function delAllSubject (obj) {
  return request({
    url: baseUrl + 'deleteAll',
    method: 'post',
    data: obj
  })
}

export function getSubjectAnswer (obj) {
  return request({
    url: baseUrl + 'subjectAnswer',
    method: 'get',
    params: obj
  })
}

export function anonymousUserGetSubjectAnswer (obj) {
  return request({
    url: baseUrl + 'anonymousUser/subjectAnswer',
    method: 'get',
    params: obj
  })
}
