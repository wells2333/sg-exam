import api from './api'

class userApi {

    userInfo() {
        return api.fetchUser<Sg.UserInfo>("/v1/user/info?identityType=4", {}, "GET");
    }

    sendSms(phone: string) {
        const tenantCode = api.getTenantCode();
        return api.fetchUserTenant<any>("/v1/mobile/sendSms/" + phone, {}, "GET", tenantCode);
    }

    sendSmsTenant(phone: string, tenantCode: string | undefined) {
        return api.fetchUserTenant<any>("/v1/mobile/sendSms/" + phone, {}, "GET", tenantCode);
    }

    bindPhoneNumber(userInfo: object) {
        return api.fetchUser<Sg.UserInfo>("/v1/user/bindPhoneNumber", userInfo, "PUT");
    }

    updateInfo(userInfo: object) {
        return api.fetchUser<Sg.UserInfo>("/v1/user/updateInfo", userInfo, "PUT");
    }

    uploadAudio(filePath: string, data: Object = {}) {
        return api.uploadUser<any>("/v1/speechSynthesis/uploadSpeech", filePath, data)
    }

    getNotice() {
        return api.fetchUser<any>("/v1/notice/getNotice", {}, "GET");
    }
}

export default new userApi()
