import request from '@/router/axios'

export function getSysConfig () {
  return request({
    url: '/user-service/v1/sysConfig',
    method: 'get'
  })
}

/**
 * 首页数据
 */
export function getDashboard () {
  return request({
    url: '/user-service/v1/dashboard',
    method: 'get'
  })
}

/**
 * 过去一周考试记录数据
 */
export function getExamRecordTendency (query) {
  return request({
    url: '/user-service/v1/dashboard/examRecordTendency',
    method: 'get',
    params: query
  })
}
