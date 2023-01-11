import request from '@/router/axios'

const baseTenantUrl = '/sg-user-service/v1/mobile/'

export function sendSms (mobile) {
  return request({
    url: baseTenantUrl + 'sendSms/' + mobile,
    method: 'get'
  })
}
