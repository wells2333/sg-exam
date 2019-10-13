import request from '@/router/axios'

const baseExamRecordUrl = '/api/exam/v1/examRecord/'

export function fetchList (query) {
  return request({
    url: baseExamRecordUrl + 'examRecordList',
    method: 'get',
    params: query
  })
}

export function getObj (id, query) {
  return request({
    url: baseExamRecordUrl + id,
    method: 'get',
    params: query
  })
}

export function addObj (obj) {
  return request({
    url: baseExamRecordUrl,
    method: 'post',
    data: obj
  })
}

export function start (obj) {
  return request({
    url: baseExamRecordUrl + 'start',
    method: 'post',
    data: obj
  })
}

export function getCurrentTime () {
  return request({
    url: baseExamRecordUrl + 'currentTime',
    method: 'get'
  })
}
