import { setStore, getStore, removeStore } from '@/utils/store'
import { TENANT_CODE } from '../../config/prod.env'
import { TOKEN, REFRESH_TOKEN, TENANTCODE } from '@/utils/storeMap'
export function getToken () {
  return getStore({ name: TOKEN })
}

export function setToken (token) {
  return setStore({ name: TOKEN }, token)
}

export function removeToken () {
  return removeStore({ name: TOKEN })
}

export function getRefreshToken () {
  return getStore({ name: REFRESH_TOKEN })
}

export function setRefreshToken (token) {
  return setStore({ name: REFRESH_TOKEN }, token)
}

export function removeRefreshToken () {
  return removeStore({ name: REFRESH_TOKEN })
}

export function getTenantCode () {
  if (TENANT_CODE !== undefined) {
    return TENANT_CODE
  }
  return getStore({ name: TENANTCODE })
}
