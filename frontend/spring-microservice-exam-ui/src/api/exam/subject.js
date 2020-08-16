import request from '@/router/axios'

const baseSubjectUrl = '/exam/v1/subject/'

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

export function getSubject (id, query) {
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

export function delSubject (id, query) {
  return request({
    url: baseSubjectUrl + id,
    method: 'delete',
    params: query
  })
}

export function delAllSubject (obj) {
  return request({
    url: baseSubjectUrl + 'deleteAll',
    method: 'post',
    data: obj
  })
}

// 导出
export function exportSubject (ids, examinationId, categoryId) {
  let url = baseSubjectUrl + 'export?'
  if (examinationId !== undefined && examinationId !== null && examinationId !== '') {
    url = url + 'examinationId=' + examinationId
  }
  if (categoryId !== undefined && categoryId !== null && categoryId !== '') {
    url = url + '&categoryId=' + categoryId
  }
  return request({
    url: url,
    method: 'post',
    responseType: 'arraybuffer',
    headers: { 'filename': 'utf-8' },
    data: ids
  })
}
