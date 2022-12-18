import Taro from "@tarojs/taro";

/**
 * 生成随机len位数字
 */
export const randomLenNum = (len, date) => {
    let random = ''
    random = Math.ceil(Math.random() * 100000000000000).toString().substr(0, typeof len === 'number' ? len : 4)
    if (date) random = random + Date.now()
    return random
}

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

export const successMessage = (msg: string = '操作成功', duration: number = 800) => {
    Taro.atMessage({
        message: msg,
        type: 'success',
        duration: duration
    });
}

export const warnMessage = (msg: string = '操作失败', duration: number = 800) => {
    Taro.atMessage({
        message: msg,
        type: 'warning',
        duration: duration
    });
}