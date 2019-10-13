import { constantRouterMap } from '@/router'
import { setStore, getStore } from '@/utils/store'
import { initMenu } from '@/utils/util'
import { GetMenu } from '@/api/admin/menu'
import router from '../../router'

const permission = {
  state: {
    routers: getStore({
      name: 'routers'
    }) || {},
    addRouters: []
  },
  mutations: {
    SET_ROUTERS: (state, routers) => {
      state.addRouters = routers
      state.routers = constantRouterMap.concat(routers)
      setStore({
        name: 'routers',
        content: state.routers
      })
      router.addRoutes(state.addRouters) // 动态添加可访问路由表
    }
  },
  actions: {
    GenerateRoutes ({ commit }, data) {
      let accessedRouters
      GetMenu().then(data => {
        accessedRouters = initMenu(data.data)
        commit('SET_ROUTERS', accessedRouters)
      })
    }
  }
}

export default permission
