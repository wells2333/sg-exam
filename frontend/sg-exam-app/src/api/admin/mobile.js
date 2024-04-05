import request from '@/router/axios'
import {VUE_APP_API_CONTEXT_PATH} from '@/utils/env'

const baseUrl = VUE_APP_API_CONTEXT_PATH

export function sendSms (mobile) {
  return request({
    url: baseUrl + '/v1/mobile/sendSms/' + mobile,
    method: 'get'
  })
}
