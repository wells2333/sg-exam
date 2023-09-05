import Taro from "@tarojs/taro"

const ENV_MAP = {
    'WEB': 'WEB',         // 微信小程序环境
    'WEAPP': 'WEAPP',     // web环境(H5)
                          // 其它环境自行补充
}

const checkEnv = () => {
    const env = Taro.getEnv();
    return function(envVal) {
        return env === envVal
    }
}

const checkEnvIns = checkEnv()

// web环境(H5)
export const isWeb = () => {
    return checkEnvIns(ENV_MAP.WEB)
}

// 微信小程序环境
export const isWeapp = () => {
    return checkEnvIns(ENV_MAP.WEAPP)
}