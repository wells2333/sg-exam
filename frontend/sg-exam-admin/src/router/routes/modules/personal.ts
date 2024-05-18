import type { AppRouteModule } from '/@/router/types';

import { LAYOUT } from '/@/router/constant';
import { t } from '/@/hooks/web/useI18n';

const dashboard: AppRouteModule = {
  path: '/personal',
  name: '个人管理',
  component: LAYOUT,
  redirect: '/personal/details',
  meta: {
    orderNo: 12,
    icon: 'whh:paintroll',
    title: t('routes.personal.personal'),
  },
  children: [
    {
      path: 'details',
      name: '个人资料',
      component: () => import('/@/views/personal/details/index.vue'),
      meta: {
        // affix: true,
        title: t('routes.personal.details'),
      },
    },
    {
      path: 'password',
      name: '修改面',
      component: () => import('/@/views/personal/password/index.vue'),
      meta: {
        // affix: true,
        title: t('routes.personal.password'),
      },
    }
  ],
};

export default dashboard;
