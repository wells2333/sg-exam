import { USER_SERVICE } from './api';
import Taro from "@tarojs/taro";

class authApi {

    /**
     * 微信登录
     */
    async wxlogin(tenantCode: string, code: string, data: Object = {}) {
        const url = "/wx/login?code=" + code;
        return await new Promise<any>((resolve, reject) => {
            Taro.request<any>({
                timeout: 1000 * 5,
                url: USER_SERVICE + url,
                data,
                method: "POST",
                header: {'Tenant-Code': tenantCode},
                success: res => {
                    resolve(res.data as any)
                },
                fail: e => {
                    Taro.showToast({title: '登录失败', icon: 'error', duration: 1500})
                    reject(e)
                }
            });
        });
    }

    async mobileLogin(tenantCode: string, mobile: string, code: string, data: object) {
        const url = "/mobile/login?mobile=" + mobile + "&code=" + code;
        return await new Promise<any>((resolve, reject) => {
            Taro.request<any>({
                timeout: 1000 * 5,
                url: USER_SERVICE + url,
                method: "POST",
                header: {'Tenant-Code': tenantCode},
                data: data,
                success: res => {
                    resolve(res.data as any)
                },
                fail: e => {
                    Taro.showToast({title: '登录失败', icon: 'error', duration: 1500})
                    reject(e)
                }
            });
        });
    }

    validToken(token: string) {
        const url = "/v1/token/validToken";
        return new Promise<any>((resolve, reject) => {
            Taro.request<any>({
                timeout: 1000 * 5,
                url: USER_SERVICE + url,
                method: "GET",
                header: {'Authorization': token},
                success: res => {
                    resolve(res.data as any)
                },
                fail: e => {
                    Taro.showToast({title: 'token校验失败', icon: 'error', duration: 1500})
                    reject(e)
                }
            });
        });
    }
}

export default new authApi()