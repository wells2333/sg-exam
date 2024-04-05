import request from '@/router/axios'
import {VUE_APP_API_CONTEXT_PATH} from '@/utils/env'

const baseUrl = VUE_APP_API_CONTEXT_PATH + '/v1/answer/'

export function fetchAnswerList (query) {
  return request({
    url: baseUrl + 'answerList',
    method: 'get',
    params: query
  })
}

export function getAnswer (id) {
  return request({
    url: baseUrl + id,
    method: 'get'
  })
}

export function getAnswerInfo (id, query) {
  return request({
    url: baseUrl + id + '/info',
    method: 'get',
    params: query
  })
}

export function getAnswerListInfo (id, query) {
  return request({
    url: baseUrl + 'record/' + id + '/answerListInfo',
    method: 'get',
    params: query
  })
}

export function addAnswer (obj) {
  return request({
    url: baseUrl,
    method: 'post',
    data: obj
  })
}

export function putAnswer (obj) {
  return request({
    url: baseUrl,
    method: 'put',
    data: obj
  })
}

export function delAnswer (id) {
  return request({
    url: baseUrl + id,
    method: 'delete'
  })
}

export function save (obj) {
  return request({
    url: baseUrl + 'save',
    method: 'post',
    data: obj
  })
}

export function saveAndNext (obj, type, nextSubjectSortNo, subjectId) {
  let url = baseUrl + 'saveAndNext?type=' + type
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
  let url = baseUrl + 'anonymousUser/saveAndNext?nextType=' + nextType
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
    url: baseUrl + 'submit',
    method: 'post',
    data: obj
  })
}

export function anonymousUserSubmit (obj) {
  return request({
    url: baseUrl + 'anonymousUser/submit',
    method: 'post',
    data: obj
  })
}

export function submitAll (obj) {
  return request({
    url: baseUrl + 'submitAll',
    method: 'post',
    data: obj
  })
}

export function anonymousUserSubmitAll (examinationId, obj) {
  return request({
    url: baseUrl + 'anonymousUser/submitAll?examinationId=' + examinationId,
    method: 'post',
    data: obj
  })
}

// 查询考试排名
export function examRankInfo (query) {
  return request({
    url: baseUrl + 'rankInfo',
    method: 'get',
    params: query
  })
}
