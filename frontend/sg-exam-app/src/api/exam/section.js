import request from '@/router/axios'

const baseCourseUrl = '/user-service/v1/section/'

export function getSection (id) {
  return request({
    url: baseCourseUrl + id,
    method: 'get'
  })
}

export function watchSection (id) {
  return request({
    url: baseCourseUrl + 'watchSection/' + id,
    method: 'get'
  })
}
