import request from '@/router/axios'

const baseExamMediaUrl = '/sg-user-service/v1/examMedia/'

export function playSpeech (id) {
  return request({
    url: baseExamMediaUrl + id,
    method: 'get',
    params: {subjectId: id}
  })
}
