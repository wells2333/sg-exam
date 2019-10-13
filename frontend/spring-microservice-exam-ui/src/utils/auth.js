import { setStore, getStore, removeStore } from '@/utils/store'

export function getToken () {
  return getStore({ name: 'access_token' })
}

export function setToken (token) {
  return setStore({ name: 'access_token' }, token)
}

export function removeToken () {
  return removeStore({ name: 'access_token' })
}

export function getRefreshToken () {
  return getStore({ name: 'refresh_token' })
}

export function setRefreshToken (token) {
  return setStore({ name: 'refresh_token' }, token)
}

export function removeRefreshToken () {
  return removeStore({ name: 'refresh_token' })
}

export function getTenantCode () {
  return getStore({ name: 'tenantCode' })
}
