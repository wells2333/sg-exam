import request from '@/router/axios'

const baseAnswerUrl = '/exam/v1/answer/'

export function fetchAnswerList (query) {
  return request({
    url: baseAnswerUrl + 'answerList',
    method: 'get',
    params: query
  })
}

export function getAnswer (id) {
  return request({
    url: baseAnswerUrl + id,
    method: 'get'
  })
}

export function getAnswerInfo (id, query) {
  return request({
    url: baseAnswerUrl + id + '/info',
    method: 'get',
    params: query
  })
}

export function getAnswerListInfo (id, query) {
  return request({
    url: baseAnswerUrl + 'record/' + id + '/answerListInfo',
    method: 'get',
    params: query
  })
}

export function getAnswerByRecordId (recordId, currentSubjectId, nextSubjectType, nextType) {
  let url = baseAnswerUrl + 'record/' + recordId + '/answerInfo?'
  if (currentSubjectId !== undefined) {
    url += '&currentSubjectId=' + currentSubjectId
  }
  if (nextSubjectType !== undefined) {
    url += '&nextSubjectType=' + nextSubjectType
  }
  if (nextType !== undefined) {
    url += '&nextType=' + nextType
  }
  return request({
    url: url,
    method: 'get'
  })
}

export function addAnswer (obj) {
  return request({
    url: baseAnswerUrl,
    method: 'post',
    data: obj
  })
}

export function putAnswer (obj) {
  return request({
    url: baseAnswerUrl,
    method: 'put',
    data: obj
  })
}

export function markAnswer (obj) {
  return request({
    url: baseAnswerUrl + 'mark',
    method: 'put',
    data: obj
  })
}

export function delAnswer (id) {
  return request({
    url: baseAnswerUrl + id,
    method: 'delete'
  })
}

export function save (obj) {
  return request({
    url: baseAnswerUrl + 'save',
    method: 'post',
    data: obj
  })
}

export function saveAndNext (obj) {
  return request({
    url: baseAnswerUrl + 'saveAndNext',
    method: 'post',
    data: obj
  })
}

export function submit (obj) {
  return request({
    url: baseAnswerUrl + 'submit',
    method: 'post',
    data: obj
  })
}
