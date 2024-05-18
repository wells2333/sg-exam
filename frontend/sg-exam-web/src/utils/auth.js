import { setStore, getStore, removeStore } from '@/utils/store'
import { TOKEN, REFRESH_TOKEN, TENANTCODE } from '@/utils/storeMap'
import store from '@/store'
import {VUE_APP_TENANT_CODE} from '@/utils/env'

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
  const sysConfig = store.getters.sysConfig
  if (sysConfig.sys_tenant_code != null) {
    return sysConfig.sys_tenant_code
  }
  if (VUE_APP_TENANT_CODE !== undefined) {
    return VUE_APP_TENANT_CODE
  }
  return getStore({ name: TENANTCODE })
}
