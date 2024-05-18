import type { AppRouteModule } from '/@/router/types';

import { LAYOUT } from '/@/router/constant';
import { t } from '/@/hooks/web/useI18n';

const dashboard: AppRouteModule = {
  path: '/exam',
  name: '考务管理',
  component: LAYOUT,
  redirect: '/exam/course',
  meta: {
    orderNo: 11,
    icon: 'ion:aperture-outline',
    title: t('routes.exam.exam'),
  },
  children: [
    {
      path: 'course',
      name: '课程管理',
      component: () => import('/@/views/exam/course/index.vue'),
      meta: {
        // affix: true,
        title: t('routes.exam.course'),
      },
    },
    {
      path: 'examination',
      name: '考试管理',
      component: () => import('/@/views/exam/examination/index.vue'),
      meta: {
        // affix: true,
        title: t('routes.exam.examination'),
      },
    },
    {
      path: 'subject',
      name: '题库管理',
      component: () => import('/@/views/exam/subject/index.vue'),
      meta: {
        // affix: true,
        title: t('routes.exam.subject'),
      },
    },
    {
      path: 'score',
      name: '成绩管理',
      component: () => import('/@/views/exam/score/index.vue'),
      meta: {
        // affix: true,
        title: t('routes.exam.score'),
      },
    }
  ],
};

export default dashboard;
