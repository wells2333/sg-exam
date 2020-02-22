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
        path: '/functions',
        name: 'functions',
        component: () => import('@/views/function/function')
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
        path: '/practice',
        name: 'practice',
        component: () => import('@/views/exam/startPractice')
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
    path: '*',
    redirect: '/home'
  }
]
export default new Router({
  routes: constantRouterMap
})
