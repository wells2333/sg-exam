import request from '@/router/axios'

export function getSysConfig () {
  return request({
    url: '/user-service/v1/sysConfig',
    method: 'get'
  })
}
