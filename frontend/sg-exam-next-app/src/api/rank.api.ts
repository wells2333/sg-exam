import api from './api'

class rankApi {
    async getRankList(data: object) {
        return api.fetchExam<Sg.ExamInfo>("/v1/answer/rankInfo", data, "GET")
    }
}

export default new rankApi()