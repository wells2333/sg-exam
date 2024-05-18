import Taro from "@tarojs/taro";
import CryptoJS from "crypto-js";
import {unref} from "vue";

export const parseRes = (res) => {
    let result = undefined;
    if (res && res.code === 0) {
        result = res.result;
    }
    return result;
}

export const transformToArray = (val) => {
    let res = [];
    if (val !== undefined && val !== null && val !== '') {
        if (val.indexOf(',') != -1) {
            res = val.split(',');
        } else {
            res.push(val);
        }
    }
    return res;
}

export const joinArray = (arr) => {
    return arr.join(',');
}

export const isNotEmpty = (val) => {
    return val !== undefined && val !== null && val !== '';
}

export const getTime = (time) => {
    let hh = time.getHours();
    let mf = time.getMinutes() < 10 ? '0' + time.getMinutes() : time.getMinutes();
    let ss = time.getSeconds() < 10 ? '0' + time.getSeconds() : time.getSeconds();
    let yy = time.getFullYear();
    let mm = time.getMonth() + 1;
    let dd = time.getDate();
    return yy + '-' + mm + '-' + dd + ' ' + hh + '-' + mf + '-' + ss;
}

export const getDuration = (start, end) => {
    //计算时间差,并把毫秒转换成秒
    let difftime = (start - end) / 1000;
    // 天 24*60*60*1000
    let days = parseInt(String(difftime / 86400));
    // 小时 60*60 总小时数-过去的小时数=现在的小时数
    let hours = parseInt(String(difftime / 3600)) - 24 * days;
    // 分钟 -(day*24) 以60秒为一整份 取余 剩下秒数 秒数/60 就是分钟数
    let minutes = parseInt(String(difftime % 3600 / 60));
    // 以60秒为一整份 取余 剩下秒数
    let seconds = parseInt(String(difftime % 60));
    if (days > 0) {
        return days + "天" + hours + "小时" + minutes + "分钟";
    } else if (hours > 0) {
        return hours + "小时" + minutes + "分钟";
    } else if (minutes > 0) {
        return minutes + "分钟";
    } else {
        return seconds + "秒";
    }
}

export const successMessage = async (msg: string = '操作成功', duration: number = 500) => {
    await Taro.showToast({
        title: msg,
        icon: 'success',
        duration
    });
}

export const warnMessage = async (msg: string = '操作失败', duration: number = 500) => {
    await Taro.showToast({
        title: msg,
        icon: 'error',
        duration
    });
}

export const showNoMoreData = async (title: string = '无更多数据') => {
    await Taro.showToast({title, icon: 'none', duration: 800});
}

export const showLoading = async (title: string = '加载中') => {
    await Taro.showLoading({title});
}

export const hideLoading = () => {
    Taro.hideLoading();
}

export const validatePhoneValue = async (phone) => {
    const phoneVal = unref(phone);
    if (phoneVal === '') {
        await warnMessage('请输入手机号');
        return false;
    }
    if (!(/^1[34578]\d{9}$/.test(phoneVal))) {
        await warnMessage('手机号格式错误');
        return false;
    }
    return true;
}

export const validateEmail = async (email) => {
    const emailVal = unref(email);
    if (emailVal === '') {
        await warnMessage('请输入邮箱');
        return false;
    }
    if (!(/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/.test(emailVal))) {
        await warnMessage('邮箱格式错误');
        return false;
    }
    return true;
}

export const validateSmsValue = async (sms) => {
    const smsVal = unref(sms);
    if (smsVal === '') {
        await warnMessage('请输入验证码');
        return false;
    }
    if (smsVal.length !== 4) {
        await warnMessage('请输入长度为4的验证码');
        return false;
    }
    return true;
}

export const encryption = (params) => {
    let {
        data,
        type,
        param,
        key
    } = params
    const result = JSON.parse(JSON.stringify(data))
    if (type === 'Base64') {
        param.forEach(ele => {
            result[ele] = btoa(result[ele])
        })
    } else {
        param.forEach(ele => {
            let data = result[ele]
            key = CryptoJS.enc.Latin1.parse(key)
            let iv = key
            let encrypted = CryptoJS.AES.encrypt(
                data,
                key,
                { iv: iv,
                    mode: CryptoJS.mode.CBC,
                    padding: CryptoJS.pad.ZeroPadding
                })
            result[ele] = encrypted.toString()
        })
    }
    return result
}