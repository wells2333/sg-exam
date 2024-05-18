import request from '@/router/axios'
import {VUE_APP_API_CONTEXT_PATH} from '@/utils/env'

const baseUrl = VUE_APP_API_CONTEXT_PATH + '/v1/examination/'

export function canStart (id) {
  return request({
    url: baseUrl + 'canStart?id=' + id,
    method: 'get'
  })
}

export function anonymousUserCanStart (id) {
  return request({
    url: baseUrl + 'anonymousUser/canStart?id=' + id,
    method: 'get'
  })
}

export function fetchList (query) {
  return request({
    url: baseUrl + 'userExaminationList',
    method: 'get',
    params: query
  })
}

export function fetchAllSubjectList (query) {
  return request({
    url: baseUrl + 'anonymousUser/allSubjectList',
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

export function getObjDetail (id, query) {
  return request({
    url: baseUrl + id + '/detail',
    method: 'get',
    params: query
  })
}

export function anonymousUserGetObj (id, query) {
  return request({
    url: baseUrl + 'anonymousUser/' + id,
    method: 'get',
    params: query
  })
}

export function getSubjectIds (id, query) {
  return request({
    url: baseUrl + id + '/subjectIds',
    method: 'get',
    params: query
  })
}

export function anonymousUserGetSubjectIds (id, query) {
  return request({
    url: baseUrl + 'anonymousUser/' + id + '/subjectIds',
    method: 'get',
    params: query
  })
}

export function favoriteExamination (id, userId, type) {
  return request({
    url: VUE_APP_API_CONTEXT_PATH + '/v1/favorites/favExam/' + id + '?userId=' + userId + '&type=' + type,
    method: 'post'
  })
}
