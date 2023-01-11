import request from '@/router/axios'

const baseAnswerUrl = '/sg-user-service/v1/answer/'

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

export function saveAndNext (obj, type, nextSubjectSortNo, subjectId) {
  let url = baseAnswerUrl + 'saveAndNext?type=' + type
  if (nextSubjectSortNo !== undefined) {
    url += '&nextSubjectSortNo=' + nextSubjectSortNo
  }
  if (subjectId !== undefined) {
    url += '&subjectId=' + subjectId
  }
  return request({
    url: url,
    method: 'post',
    data: obj
  })
}

export function anonymousUserSaveAndNext (obj, nextType, nextSubjectId, nextSubjectType) {
  let url = baseAnswerUrl + 'anonymousUser/saveAndNext?nextType=' + nextType
  if (nextSubjectId !== undefined) {
    url += '&nextSubjectId=' + nextSubjectId
  }
  if (nextSubjectType !== undefined) {
    url += '&nextSubjectType=' + nextSubjectType
  }
  return request({
    url: url,
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

export function anonymousUserSubmit (obj) {
  return request({
    url: baseAnswerUrl + 'anonymousUser/submit',
    method: 'post',
    data: obj
  })
}

export function anonymousUserSubmitAll (obj, examinationId, identifier) {
  return request({
    url: baseAnswerUrl + 'anonymousUser/submitAll/' + examinationId + '?identifier=' + identifier,
    method: 'post',
    data: obj
  })
}
