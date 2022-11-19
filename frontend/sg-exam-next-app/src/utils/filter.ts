import api from "../api/api";
import Taro from "@tarojs/taro";
import validToken from "../api/auth.api";

export const filterLogin = () => {
    const token = api.getToken();
    const indexUrl = '/pages/index/index';
    if (token === null || token === undefined || token === '') {
        Taro.showToast({title: '请先登录', icon: 'error'});
        Taro.reLaunch({url: indexUrl});
        return
    }
    validToken.validToken(token).then(res => {
        const {code, result} = res;
        if (code != 0 || !result) {
            Taro.showToast({title: '请先登录', icon: 'error'});
            Taro.reLaunch({url: indexUrl})
        }
    }).catch(err => {
        console.error(err);
    })
}