import request from '@/router/axios'
import {VUE_APP_TENANT_CODE, VUE_APP_API_CONTEXT_PATH} from '@/utils/env'

const baseUrl = VUE_APP_API_CONTEXT_PATH

export function loginByUsername(tenantCode = VUE_APP_TENANT_CODE, identifier, credential, code, randomStr) {
  const grantType = 'password'
  const scope = 'read'
  if (tenantCode === '') {
    tenantCode = VUE_APP_TENANT_CODE
  }
  return request({
    url: baseUrl + '/login',
    headers: {
      'Tenant-Code': tenantCode
    },
    method: 'post',
    params: {username: identifier, credential, randomStr, code, grant_type: grantType, scope}
  })
}

/**
 * 根据手机号登录
 */
export function loginBySocial(tenantCode = VUE_APP_TENANT_CODE, social, code) {
  const grantType = 'mobile'
  const scope = 'read'
  if (tenantCode === '') {
    tenantCode = VUE_APP_TENANT_CODE
  }
  return request({
    url: baseUrl + '/mobile/login',
    headers: {
      'Tenant-Code': tenantCode
    },
    method: 'post',
    params: {mobile: social, code, grant_type: grantType, scope}
  })
}

export function registerByUsername(tenantCode = VUE_APP_TENANT_CODE, identifier, email, credential, code, randomStr) {
  if (tenantCode === '') {
    tenantCode = VUE_APP_TENANT_CODE
  }
  return request({
    url: baseUrl + '/v1/user/anonymousUser/register',
    method: 'post',
    params: {tenantCode, identifier, email, credential, randomStr, code},
    data: {identifier, email, credential}
  })
}

export function logout() {
  return request({
    url: baseUrl + '/v1/token/logout',
    method: 'get'
  })
}

export function getUserInfo(token) {
  return request({
    url: baseUrl + '/v1/user/info',
    method: 'get'
  })
}

export function refreshToken(token) {
  return request({
    url: baseUrl + '/v1/token/refreshToken',
    headers: {
      'Authorization': token
    },
    method: 'get',
    params: {}
  })
}
