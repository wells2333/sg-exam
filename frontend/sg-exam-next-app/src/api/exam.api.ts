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

    async courseList(params: object) {
        return api.fetchExam<Sg.ExamInfo>("/v1/course/courseList", params, "GET");
    }

    async popularCourses() {
        return api.fetchExam<Sg.ExamInfo>("/v1/course/popularCourses", {}, "GET");
    }

    async courseDetail(id: string) {
        return api.fetchExam<Sg.ExamInfo>("/v1/course/detail/" + id, {}, "GET");
    }

    async joinCourse(id: string|undefined, type: string) {
        return api.fetchExam<Sg.ExamInfo>("/v1/course/" + id + "/join?type=" + type, {}, "POST");
    }

    async courseEvaluate(id: string|undefined, params: object) {
        return api.fetchExam<Sg.ExamInfo>("/v1/evaluate/list?courseId=" + id, params, "GET");
    }

    async addEvaluate(data: object) {
        return api.fetchExam<Sg.ExamInfo>("/v1/evaluate", data, "POST");
    }

    async getSectionDetail(id: string) {
        return api.fetchExam<Sg.ExamInfo>("/v1/section/watchSection/" + id, {}, "GET");
    }

    async getPointDetail(id: string) {
        return api.fetchExam<Sg.ExamInfo>("/v1/knowledgePoint/detail/" + id, {}, "GET");
    }
}

export default new examApi()