import { getStore, setStore } from '@/utils/store'

const tenant = {
  state: {
    tenantCode: getStore({ name: 'tenantCode' }) || ''
  },
  mutations: {
    SET_TENANT_CODE: (state, tenantCode) => {
      state.tenantCode = tenantCode
      setStore({ name: 'tenantCode', content: state.tenantCode })
    }
  }
}
export default tenant
