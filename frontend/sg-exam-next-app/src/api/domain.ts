class domain {
    baseUrl: String
    constructor() {
        this.baseUrl = ""
    }

    getBaseUrl() {
        const { miniProgram: { envVersion } } = wx.getAccountInfoSync();
        console.log("envVersion: " + envVersion);
        switch (envVersion) {
            case "develop":
                // 开发版
                this.baseUrl = "http://192.168.100.16:9183";
                break;
            case "trial":
                // 体验版
                this.baseUrl = "https://www.yunmianshi.com";
                break;
            default:
                // 正式版
                this.baseUrl = "https://www.yunmianshi.com";
                break;
        }
        return this.baseUrl;
    }
}

export default new domain();
