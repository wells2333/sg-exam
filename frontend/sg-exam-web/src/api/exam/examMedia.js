import request from '@/router/axios'
import {VUE_APP_API_CONTEXT_PATH} from '@/utils/env'

const baseUrl = VUE_APP_API_CONTEXT_PATH + '/v1/examMedia/'

export function playSpeech (id) {
  return request({
    url: baseUrl + 'playSpeech?subjectId=' + id,
    method: 'get',
    params: {subjectId: id}
  })
}
