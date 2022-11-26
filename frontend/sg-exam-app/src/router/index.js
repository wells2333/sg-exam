import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export const constantRouterMap = [
  {
    path: '/',
    name: 'Index',
    component: () => import('@/views/Index'),
    redirect: '/home',
    children: [
      {
        path: 'home',
        name: 'home',
        component: () => import('@/views/home/home')
      },
      {
        path: '/us',
        name: 'us',
        component: () => import('@/views/us/us')
      },
      {
        path: '/login',
        name: 'login',
        component: () => import('@/views/login/login')
      },
      {
        path: '/register',
        name: 'register',
        component: () => import('@/views/login/login')
      },
      {
        path: '/practices',
        name: 'practices',
        component: () => import('@/views/exam/practices')
      },
      {
        path: '/exams',
        name: 'exams',
        component: () => import('@/views/exam/exams')
      },
      {
        path: '/start/:id',
        name: 'start',
        component: () => import('@/views/exam/startExam')
      },
      {
        path: '/exam-record',
        name: 'exam-record',
        component: () => import('@/views/exam/examRecords')
      },
      {
        path: '/knowledge',
        name: 'knowledge',
        component: () => import('@/views/exam/knowledge')
      },
      {
        path: '/practices',
        name: 'practices',
        component: () => import('@/views/exam/practices')
      },
      {
        path: '/courses',
        name: 'courses',
        component: () => import('@/views/exam/courses')
      },
      {
        path: '/course-details',
        name: 'course-details',
        component: () => import('@/views/exam/courseDetails')
      },
      {
        path: '/course-section',
        name: 'course-section',
        component: () => import('@/views/exam/section')
      },
      {
        path: '/account',
        name: 'account',
        component: () => import('@/views/personal/account')
      },
      {
        path: '/password',
        name: 'password',
        component: () => import('@/views/personal/password')
      },
      {
        path: '/incorrect',
        name: 'incorrect',
        component: () => import('@/views/exam/incorrect')
      },
      {
        path: '/reset-password',
        name: 'reset-password',
        component: () => import('@/views/personal/resetPassword')
      }
    ]
  },
  {
    path: '/mobile',
    name: 'Mobile',
    component: () => import('@/views/mobile/Index')
  },
  {
    path: '/mobile-v2',
    name: 'MobileV2',
    component: () => import('@/views/mobileV2/Index')
  },
  {
    path: '*',
    redirect: '/home'
  }
]
export default new Router({
  routes: constantRouterMap
})
