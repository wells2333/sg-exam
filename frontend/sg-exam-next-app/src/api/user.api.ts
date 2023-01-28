import api from './api'

class userApi {

    userInfo() {
        return api.fetchUser<Sg.UserInfo>("/v1/user/info?identityType=4", {}, "GET");
    }

    sendSms(phone: string) {
        return api.fetchUser<any>("/v1/mobile/sendSms/" + phone, {}, "GET");
    }

    uploadAudio(filePath: string, data: Object = {}) {
        return api.uploadUser<any>("/v1/speechSynthesis/uploadSpeech", filePath, data)
    }

    getNotice() {
        return api.fetchUser<any>("/v1/notice/getNotice", {}, "GET");
    }
}

export default new userApi()