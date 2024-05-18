import {USER_SERVICE} from './api';
import Taro from "@tarojs/taro";
import {
    encryption
} from '../utils/util';
class authApi {

    async usernameLogin(params: object) {
        const user = encryption({
            data: params,
            key: '1234567887654321',
            param: ['password']
        });
        const password = user.password.replace(/\+/g, '%2B');
        let url = "/login?grant_type=password&scope=read&remember=true&username=" + params.username + "&credential=" + password;
        url = url + "&ignoreCode=1";
        return await new Promise<any>((resolve, reject) => {
            Taro.request<any>({
                timeout: 1000 * 5,
                url: USER_SERVICE + url,
                method: "POST",
                header: {'Tenant-Code': params.tenantCode},
                data: params,
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

    async mobileLogin(tenantCode: string, mobile: string, code: string, isTestPhone: boolean, data: object) {
        let url = "/mobile/login?mobile=" + mobile + "&code=" + code;
        if (isTestPhone) {
            url = url + "&ignoreCode=1";
        }
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