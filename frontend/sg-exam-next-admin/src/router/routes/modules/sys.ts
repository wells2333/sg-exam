import type { AppRouteModule } from '/@/router/types';

import { LAYOUT } from '/@/router/constant';
import { t } from '/@/hooks/web/useI18n';

const dashboard: AppRouteModule = {
  path: '/sys',
  name: '系统管理',
  component: LAYOUT,
  redirect: '/sys/tenant',
  meta: {
    orderNo: 10,
    icon: 'ion:settings-outline',
    title: t('routes.sys.sys'),
  },
  children: [
    {
      path: 'tenant',
      name: '单位管理',
      component: () => import('/@/views/sys/tenant/index.vue'),
      meta: {
        // affix: true,
        title: t('routes.sys.tenant'),
      },
    },
    {
      path: 'user',
      name: '用户管理',
      component: () => import('/@/views/sys/user/index.vue'),
      meta: {
        // affix: true,
        title: t('routes.sys.user'),
      },
    }, {
      path: 'role',
      name: '角色管理',
      component: () => import('/@/views/sys/role/index.vue'),
      meta: {
        // affix: true,
        title: t('routes.sys.role'),
      },
    },{
      path: 'menu',
      name: '菜单管理',
      component: () => import('/@/views/sys/menu/index.vue'),
      meta: {
        // affix: true,
        title: t('routes.sys.menu'),
      },
    },{
      path: 'dept',
      name: '部门管理',
      component: () => import('/@/views/sys/dept/index.vue'),
      meta: {
        // affix: true,
        title: t('routes.sys.dept'),
      },
    },
  ],
};

export default dashboard;
