import request from '@/router/axios'
import {VUE_APP_API_CONTEXT_PATH} from '@/utils/env'

const baseUrl = VUE_APP_API_CONTEXT_PATH + '/v1/favorites/'

export function getUserFavorites (params) {
  return request({
    url: baseUrl + 'user/favorites',
    method: 'get',
    params
  })
}
