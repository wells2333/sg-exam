import request from '@/router/axios'

export function getSysConfig () {
  return request({
    url: '/user/v1/sysConfig',
    method: 'get'
  })
}
