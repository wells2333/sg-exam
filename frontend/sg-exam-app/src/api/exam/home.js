import request from '@/router/axios'

const baseHomeUrl = '/sg-user-service/v1/home/'

export function summary (query) {
  return request({
    url: baseHomeUrl + 'summary',
    method: 'get',
    params: query
  })
}
