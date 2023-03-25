const local = 'http://192.168.100.16:9183';

class domain {
    baseUrl: String
    constructor() {
        this.baseUrl = ""
    }

    getBaseUrl() {
        // wx 为空，非小程序模式
        if (wx === undefined) {
            this.baseUrl = local;
            return this.baseUrl;
        }
        const { miniProgram: { envVersion } } = wx.getAccountInfoSync();
        console.log("envVersion: " + envVersion);
        switch (envVersion) {
            case "develop":
                // 开发版
                this.baseUrl = local;
                break;
            case "trial":
                // 体验版
                this.baseUrl = 'https://www.yunmianshi.com';
                break;
            default:
                // 正式版
                this.baseUrl = 'https://www.yunmianshi.com';
                break;
        }
        return this.baseUrl;
    }
}

export default new domain();
