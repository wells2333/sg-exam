import { getObj } from '@/api/exam/course'
import {setStore, getStore} from '@/utils/store'

const course = {
  state: {
    course: getStore({
      name: 'course'
    }) || {}
  },
  actions: {
    GetCourseInfo ({ commit, state }, course) {
      return new Promise((resolve, reject) => {
        getObj(course.id).then(response => {
          commit('SET_COURSE', response.data.data)
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    }
  },
  mutations: {
    SET_COURSE: (state, course) => {
      state.course = course
      setStore({
        name: 'course',
        content: state.course
      })
    }
  }
}

export default course
