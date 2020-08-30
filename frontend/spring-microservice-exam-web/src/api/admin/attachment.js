import request from '@/router/axios'

const baseAttachmentUrl = '/user/v1/attachment/'

export function attachmentList () {
  return request({
    url: baseAttachmentUrl + 'attachmentList',
    method: 'get'
  })
}

export function fetchList (query) {
  return request({
    url: baseAttachmentUrl + 'attachmentList',
    method: 'get',
    params: query
  })
}

export function getObj (id) {
  return request({
    url: baseAttachmentUrl + id,
    method: 'get'
  })
}

export function preview (id) {
  return request({
    url: baseAttachmentUrl + id + '/preview',
    method: 'get'
  })
}

export function addObj (obj) {
  return request({
    url: baseAttachmentUrl,
    method: 'post',
    data: obj
  })
}

export function putObj (obj) {
  return request({
    url: baseAttachmentUrl,
    method: 'put',
    data: obj
  })
}

export function delAttachment (id) {
  return request({
    url: baseAttachmentUrl + id,
    method: 'delete'
  })
}

export function delAllObj (obj) {
  return request({
    url: baseAttachmentUrl + 'deleteAll',
    method: 'post',
    data: obj
  })
}
