import api from './api'

class recordApi {

    // 获取考试记录
    async userRecords(userId: string, data: object) {
        return api.fetchExam<Sg.ExamInfo>("/v1/examRecord/userRecords/" + userId, data, "GET")
    }

    async recordDetails(recordId: string) {
        return api.fetchExam<Sg.ExamInfo>("/v1/examRecord/" + recordId + "/details", {}, "GET");
    }
}

export default new recordApi()