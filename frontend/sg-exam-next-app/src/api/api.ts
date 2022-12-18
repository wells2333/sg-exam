import Taro from "@tarojs/taro"
import domain from "./domain";

export const TENANT_CODE = `gitee`;

export const USER_SERVICE = domain.getBaseUrl() + "/user-service";

interface result<T = any> {
    code: number
    data: T
    msg: string
    [x: string]: any
}

class api {
    tenantCode: string
    token: string
    userInfo: object
    examination: Object
    examRecord: Object
    subject: Object
    subjectTotalCount: number
    answeredCount: number
    duration: string
    cards: Object
    constructor() {
        this.tenantCode = ""
        this.token = ""
        this.userInfo = {}
        this.examination = {}
        this.examRecord = {}
        this.subject = {}
        this.subjectTotalCount = 0
        this.answeredCount = 0
        this.duration = ""
        this.cards = []
    }

    setTenantCode(tenantCode: string) {
        this.tenantCode = tenantCode
        wx.setStorage({ key: 'tenantCode', data: tenantCode })
    }

    getTenantCode() {
        return wx.getStorageSync('tenantCode');
    }

    setToken(token: string) {
        this.token = token
        wx.setStorage({ key: 'token', data: token })
    }

    getToken() {
        return wx.getStorageSync('token');
    }

    setUserInfo(userInfo: object) {
        this.userInfo = userInfo
        wx.setStorage({ key: 'userInfo', data: userInfo })
    }

    getUserInfo() {
        return wx.getStorageSync('userInfo');
    }

    setExamination(examination: object) {
        this.examination = examination
        wx.setStorage({ key: 'examination', data: examination })
    }

    getExamination() {
        return wx.getStorageSync('examination');
    }

    setExamRecord(examRecord: object) {
        this.examRecord = examRecord
        wx.setStorage({ key: 'examRecord', data: examRecord })
    }

    getExamRecord() {
        return wx.getStorageSync('examRecord');
    }

    setSubject(subject: object) {
        this.subject = subject
        wx.setStorage({ key: 'subject', data: subject })
    }

    getSubject() {
        return wx.getStorageSync('subject');
    }

    setSubjectTotalCount(subjectTotalCount: number) {
        this.subjectTotalCount = subjectTotalCount
        wx.setStorage({ key: 'subjectTotalCount', data: subjectTotalCount })
    }

    getSubjectTotalCount() {
        return wx.getStorageSync('subjectTotalCount');
    }

    setAnsweredCount(answeredCount: number) {
        this.answeredCount = answeredCount
        wx.setStorage({ key: 'answeredCount', data: answeredCount })
    }

    getAnsweredCount() {
        return wx.getStorageSync('answeredCount');
    }

    setDuration(duration: string) {
        this.duration = duration
        wx.setStorage({ key: 'duration', data: duration })
    }

    getDuration() {
        return wx.getStorageSync('duration');
    }

    setCards(cards: Object) {
        this.cards = cards
        wx.setStorage({ key: 'cards', data: cards })
    }

    getCards() {
        return wx.getStorageSync('cards');
    }

    async fetchExam<T>(url: string, data: Object = {}, method: "GET" | "POST" = "POST") {
        return this.fetch<T>(USER_SERVICE, url, data, method);
    }

    async fetchUser<T>(url: string, data: Object = {}, method: "GET" | "POST" = "POST") {
        return this.fetch<T>(USER_SERVICE, url, data, method);
    }

    async uploadUser<T>(url: string, filePath: string, data: Object = {}) {
        return this.upload<T>(USER_SERVICE, url, filePath, data);
    }

    async fetchAuth<T>(url: string, data: Object = {}, method: "GET" | "POST" = "POST") {
        return this.fetch<T>(USER_SERVICE, url, data, method);
    }

    async fetch<T>(service: string, url: string, data: Object = {}, method: "GET" | "POST" = "POST") {
        let token: string = this.token || await Taro.getStorage({ key: 'token' }).then(el => el.data).catch(() => "")
        let tenantCode: string = this.tenantCode || await Taro.getStorage({ key: 'tenantCode' }).then(el => el.data).catch(() => "")
        return await new Promise<result<T>>((resolve, reject) => {
            Taro.request<result<T>>({
                timeout: 1000 * 60,
                url: service + url,
                data,
                method,
                header: { 'Authorization': token, 'Tenant-Code': tenantCode },
                success: res => {
                    const { code } = res.data;
                    // token 失效
                    if (code === 401) {
                        Taro.clearStorageSync();
                        Taro.reLaunch({ url: "/pages/index/index" })
                    } else {
                        resolve(res.data as any)
                    }
                },
                fail: e => {
                    Taro.showToast({ title: '服务器错误' })
                    Taro.hideLoading()
                    reject(e)
                }
            })
        })
    }

    async upload<T>(service: string, url: string, filePath: string, data: Object = {}) {
        let token: string = this.token || await Taro.getStorage({ key: 'token' }).then(el => el.data).catch(() => "")
        let tenantCode: string = this.tenantCode || await Taro.getStorage({ key: 'tenantCode' }).then(el => el.data).catch(() => "")
        return await new Promise<result<T>>((resolve, reject) => {
            Taro.uploadFile({
                url: service + url,
                filePath: filePath,
                name: 'file',
                header: { 'Authorization': token, 'Tenant-Code': tenantCode, 'content-type': 'multipart/form-data' },
                formData: data,
                success: (res) => {
                    console.log(res);
                    resolve(res.data as any)
                },
                fail: (e) => {
                    Taro.showToast({ title: '服务器错误' })
                    reject(e)
                },
            });
        })
    }
}

export default new api()