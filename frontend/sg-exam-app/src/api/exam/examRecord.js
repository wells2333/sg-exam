import request from '@/router/axios'
import {VUE_APP_API_CONTEXT_PATH} from '@/utils/env'

const baseUrl = VUE_APP_API_CONTEXT_PATH + '/v1/examRecord/'

export function fetchList (query) {
  return request({
    url: baseUrl + 'examRecordList',
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

export function addObj (obj) {
  return request({
    url: baseUrl,
    method: 'post',
    data: obj
  })
}

export function start (obj) {
  return request({
    url: baseUrl + 'start',
    method: 'post',
    data: obj
  })
}

export function anonymousUserStart (obj) {
  return request({
    url: baseUrl + 'anonymousUser/start',
    method: 'post',
    params: obj
  })
}

export function getAllSubjects (examinationId) {
  return request({
    url: baseUrl + 'allSubjects/' + examinationId,
    method: 'get',
    params: {}
  })
}

export function anonymousUserGetAllSubjects (examinationId) {
  return request({
    url: baseUrl + 'anonymousUser/allSubjects/' + examinationId,
    method: 'get',
    params: {}
  })
}

export function getCurrentTime () {
  return request({
    url: baseUrl + 'currentTime',
    method: 'get'
  })
}

// 查询成绩详情
export function examRecordDetails (id) {
  return request({
    url: baseUrl + id + '/details',
    method: 'get'
  })
}

export function calculateDuration (startTime) {
  return request({
    url: baseUrl + 'calculateDuration',
    method: 'get',
    params: {
      startTime
    }
  })
}
