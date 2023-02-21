import request from '@/router/axios'

export function getSysConfig () {
  return request({
    url: '/sg-user-service/v1/config/getDefaultSysConfig',
    method: 'get'
  })
}
