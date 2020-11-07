import request from '@/router/axios'
import { getRefreshToken } from '@/utils/auth'
const baseAuthenticationUrl = '/auth-service/v1/authentication/'

const basicAuthorization = 'Basic ' + btoa('web_app:secret')

/**
 * 登录
 * @param tenantCode 租户标识
 * @param identifier 账号
 * @param credential 密码
 * @param code 验证码
 * @param randomStr 随机数
 */
export function loginByUsername (tenantCode, identifier, credential, code, randomStr) {
  const grantType = 'password'
  const scope = 'read'
  return request({
    url: '/auth-service/oauth/token',
    headers: {
      'Authorization': basicAuthorization,
      'Tenant-Code': tenantCode
    },
    method: 'post',
    params: { username: identifier, credential, randomStr, code, grant_type: grantType, scope }
  })
}

export function logout (accesstoken, refreshToken) {
  return request({
    url: baseAuthenticationUrl + 'removeToken',
    method: 'post'
  })
}

export function getUserInfo (token) {
  return request({
    url: '/user-service/v1/user/info',
    method: 'get'
  })
}

/**
 * 刷新token
 */
export function refreshToken () {
  //  grant_type为refresh_token
  const grantType = 'refresh_token'
  const scope = 'read'
  const refreshToken = getRefreshToken()
  return request({
    url: '/auth-service/oauth/token',
    headers: {
      'Authorization': basicAuthorization
    },
    method: 'post',
    params: { grant_type: grantType, scope, refresh_token: refreshToken }
  })
}
