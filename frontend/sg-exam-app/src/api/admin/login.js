import request from '@/router/axios'

const DEFAULT_TENANT_CODE = 'gitee'

export function loginByUsername(tenantCode = DEFAULT_TENANT_CODE, identifier, credential, code, randomStr) {
  const grantType = 'password'
  const scope = 'read'
  if (tenantCode === '') {
    tenantCode = DEFAULT_TENANT_CODE
  }
  return request({
    url: '/sg-user-service/login',
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
export function loginBySocial(tenantCode = DEFAULT_TENANT_CODE, social, code) {
  const grantType = 'mobile'
  const scope = 'read'
  if (tenantCode === '') {
    tenantCode = DEFAULT_TENANT_CODE
  }
  return request({
    url: '/sg-user-service/mobile/login',
    headers: {
      'Tenant-Code': tenantCode
    },
    method: 'post',
    params: {mobile: social, code, grant_type: grantType, scope}
  })
}

export function registerByUsername(tenantCode = DEFAULT_TENANT_CODE, identifier, email, credential, code, randomStr) {
  if (tenantCode === '') {
    tenantCode = DEFAULT_TENANT_CODE
  }
  return request({
    url: '/sg-user-service/v1/user/anonymousUser/register',
    method: 'post',
    params: {tenantCode, identifier, email, credential, randomStr, code},
    data: {identifier, email, credential}
  })
}

export function logout() {
  return request({
    url: '/sg-user-service/v1/token/logout',
    method: 'get'
  })
}

export function getUserInfo(token) {
  return request({
    url: '/sg-user-service/v1/user/info',
    method: 'get'
  })
}

/**
 * 刷新token
 */
export function refreshToken(token) {
  return request({
    url: '/sg-user-service/v1/token/refreshToken',
    headers: {
      'Authorization': token
    },
    method: 'get',
    params: {}
  })
}
