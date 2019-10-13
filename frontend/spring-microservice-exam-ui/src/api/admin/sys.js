import request from '@/router/axios'

export function getSysConfig () {
  return request({
    url: '/api/user/v1/sysConfig',
    method: 'get'
  })
}

/**
 * 首页数据
 */
export function getDashboard () {
  return request({
    url: '/api/user/v1/dashboard',
    method: 'get'
  })
}
