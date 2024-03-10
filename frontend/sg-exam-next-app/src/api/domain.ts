import Taro from "@tarojs/taro";

const local = 'https://yunmianshi.com.cn';

class domain {
    baseUrl: String
    constructor() {
        this.baseUrl = ""
    }

    getBaseUrl() {
        // wx 为空，非小程序模式
        const { miniProgram } = Taro.getAccountInfoSync();
        if(!miniProgram) {
            this.baseUrl = local;
            return this.baseUrl;
        }
        const { envVersion } = miniProgram
        console.log("envVersion: " + envVersion);
        switch (envVersion) {
            case "develop":
                // 开发版
                this.baseUrl = local;
                break;
            case "trial":
                // 体验版
                this.baseUrl = 'https://yunmianshi.com.cn';
                break;
            default:
                // 正式版
                this.baseUrl = 'https://yunmianshi.com.cn';
                break;
        }
        return this.baseUrl;
    }
}

export default new domain();
