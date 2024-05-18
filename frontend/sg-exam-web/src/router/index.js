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
        path: '/exams',
        name: 'exams',
        component: () => import('@/views/exam/exams')
      },
      {
        path: '/exam-details',
        name: 'exam-details',
        component: () => import('@/views/exam/examDetails')
      },
      {
        path: '/start-exam-a/:id',
        name: 'start-exam-a',
        component: () => import('@/views/exam/startExamA')
      },
      {
        path: '/start-exam-b/:id',
        name: 'start-exam-b',
        component: () => import('@/views/exam/startExamB')
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
      },
      {
        path: '/search',
        name: 'search',
        component: () => import('@/views/exam/search')
      }
    ]
  },
  {
    path: '/mobile',
    name: 'mobile',
    component: () => import('@/views/mobile/index')
  },
  {
    path: '/mobile-finished',
    name: 'mobile-finished',
    component: () => import('@/views/mobile/finished')
  },
  {
    path: '*',
    redirect: '/home'
  }
]
export default new Router({
  routes: constantRouterMap
})
