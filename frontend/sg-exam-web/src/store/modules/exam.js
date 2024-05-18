import { start } from '@/api/exam/examRecord'
import {setStore, getStore} from '@/utils/store'
import { EXAM, EXAM_RECORD, SUBJECT, INCORRECT_RECORD, CARDS } from '@/utils/storeMap'

import { submit } from '@/api/exam/answer'

const exam = {
  state: {
    exam: getStore({
      name: EXAM
    }) || {},
    examRecord: getStore({
      name: EXAM_RECORD
    }) || {},
    subject: getStore({
      name: SUBJECT
    }) || {},
    incorrectRecord: getStore({
      name: INCORRECT_RECORD
    }) || {},
    cards: getStore({
      name: CARDS
    }) || {}
  },
  actions: {
    // 设置题目信息
    SetSubjectInfo ({ commit, state }, subject) {
      commit('SET_SUBJECT', subject)
    },
    // 开始考试
    StartExam ({ commit, state }, examRecord) {
      return new Promise((resolve, reject) => {
        start(examRecord).then(response => {
          commit('SET_EXAM', response.data.result.examination)
          commit('SET_EXAM_RECORD', response.data.result.examRecord)
          commit('SET_SUBJECT', response.data.result.subjectDto)
          commit('SET_CARDS', response.data.result.cards)
          resolve(response.data.result)
        }).catch(error => {
          reject(error)
        })
      })
    },
    // 提交考试
    SubmitExam ({ commit, state }, exam) {
      return new Promise((resolve, reject) => {
        submit(exam).then(response => {
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },
    // 当前错题记录
    SetIncorrectRecord ({ commit, state }, incorrectRecord) {
      commit('SET_INCORRECT_RECORD', incorrectRecord)
    },
    ClearExam ({ commit }) {
      commit('SET_EXAM', undefined)
      commit('SET_SUBJECT', undefined)
    }
  },
  mutations: {
    SET_EXAM: (state, exam) => {
      state.exam = exam
      setStore({
        name: EXAM,
        content: state.exam
      })
    },
    SET_EXAM_RECORD: (state, examRecord) => {
      state.examRecord = examRecord
      setStore({
        name: EXAM_RECORD,
        content: state.examRecord
      })
    },
    SET_SUBJECT: (state, subject) => {
      state.subject = subject
      setStore({
        name: SUBJECT,
        content: state.subject
      })
    },
    SET_INCORRECT_RECORD: (state, incorrectRecord) => {
      state.incorrectRecord = incorrectRecord
      setStore({
        name: INCORRECT_RECORD,
        content: state.incorrectRecord
      })
    },
    SET_CARDS: (state, cards) => {
      state.cards = cards
      setStore({
        name: CARDS,
        content: state.cards
      })
    }
  }
}

export default exam
