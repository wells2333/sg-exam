import request from '@/router/axios'

const baseExamRecordUrl = '/exam/v1/examRecord/'

export function examRecordList () {
  return request({
    url: baseExamRecordUrl + 'examRecordList',
    method: 'get'
  })
}

export function fetchExamRecordList (query) {
  return request({
    url: baseExamRecordUrl + 'examRecordList',
    method: 'get',
    params: query
  })
}

export function getObj (id) {
  return request({
    url: baseExamRecordUrl + id,
    method: 'get'
  })
}

export function addObj (obj) {
  return request({
    url: baseExamRecordUrl,
    method: 'post',
    data: obj
  })
}

export function putObj (obj) {
  return request({
    url: baseExamRecordUrl,
    method: 'put',
    data: obj
  })
}

export function completeMarking (obj) {
  return request({
    url: baseExamRecordUrl + 'completeMarking',
    method: 'put',
    data: obj
  })
}

export function delObj (id) {
  return request({
    url: baseExamRecordUrl + id,
    method: 'delete'
  })
}

export function delAllObj (obj) {
  return request({
    url: baseExamRecordUrl + 'deleteAll',
    method: 'post',
    data: obj
  })
}

// 导出
export function exportObj (obj) {
  return request({
    url: baseExamRecordUrl + 'export',
    method: 'post',
    responseType: 'arraybuffer',
    headers: { 'filename': 'utf-8' },
    data: obj
  })
}

// 查询成绩详情
export function examRecordDetails (id) {
  return request({
    url: baseExamRecordUrl + id + '/details',
    method: 'get'
  })
}
