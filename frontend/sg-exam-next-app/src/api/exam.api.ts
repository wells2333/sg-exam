import api from './api'

class examApi {

    async examinationList(data) {
        return api.fetchExam<Sg.ExamInfo>("/v1/examination/examinationList", data, "GET")
    }

    async userFavorites(data) {
        return api.fetchExam<Sg.ExamInfo>("/v1/examination/userFavorites", data, "GET")
    }

    // 开始考试
    async startExam(examinationId: string, userId: string) {
        return api.fetchExam<Sg.ExamInfo>("/v1/examRecord/start", {
            examinationId,
            userId
        }, "POST")
    }

    // 获取题目
    async allSubjects(recordId: string) {
        return api.fetchExam<Sg.ExamInfo>("/v1/examRecord/allSubjects/" + recordId, {}, "GET")
    }

    async saveAndNext(data: object, type: string, nextSubjectSortNo: string = '') {
        return api.fetchExam<Sg.ExamInfo>("/v1/answer/saveAndNext?type=" + type + '&nextSubjectSortNo=' + nextSubjectSortNo, data, "POST")
    }

    // 提交考试
    async submit(data: object) {
        return api.fetchExam<Sg.ExamInfo>("/v1/answer/submit", data, "POST")
    }

    // 提交考试
    async submitAllSubjects(data: object) {
        return api.fetchExam<Sg.ExamInfo>("/v1/answer/submitAll", data, "POST")
    }

    async duration(startTime: string) {
        return api.fetchExam<Sg.ExamInfo>("/v1/examRecord/duration?startTime=" + startTime, {}, "GET")
    }

    async favoriteExam(userId: string, examinationId: string, type: string) {
        return api.fetchExam<Sg.ExamInfo>("/v1/favoriteExam/favoriteExam/" + examinationId + "?userId=" + userId + "&type=" + type, {}, "POST");
    }
}

export default new examApi()