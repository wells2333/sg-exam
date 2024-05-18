import request from '@/router/axios'
import {VUE_APP_API_CONTEXT_PATH} from '@/utils/env'

const baseUrl = VUE_APP_API_CONTEXT_PATH + '/v1/course/'

export function courseList (query) {
  return request({
    url: baseUrl + 'userCourseList',
    method: 'get',
    params: query
  })
}

export function popularCourses (query) {
  return request({
    url: baseUrl + 'popularCourses',
    method: 'get',
    params: query
  })
}

export function getObj (id) {
  return request({
    url: baseUrl + id,
    method: 'get'
  })
}

export function addObj (obj) {
  return request({
    url: baseUrl,
    method: 'post',
    data: obj
  })
}

export function putObj (obj) {
  return request({
    url: baseUrl,
    method: 'put',
    data: obj
  })
}

export function delObj (id) {
  return request({
    url: baseUrl + id,
    method: 'delete'
  })
}

export function delAllObj (obj) {
  return request({
    url: baseUrl + 'deleteAll',
    method: 'post',
    data: obj
  })
}

export function getCourseDetail (id) {
  return request({
    url: baseUrl + 'detail/' + id,
    method: 'get'
  })
}

export function joinCourse (id) {
  return request({
    url: baseUrl + id + '/join',
    method: 'post'
  })
}

export function getCourseAttach (id) {
  return request({
    url: baseUrl + id + '/attach',
    method: 'get'
  })
}

export function favoriteCourse (id, userId, type) {
  return request({
    url: VUE_APP_API_CONTEXT_PATH + '/v1/favorites/favCourse/' + id + '?userId=' + userId + '&type=' + type,
    method: 'post'
  })
}
