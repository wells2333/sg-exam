import api from './api'

class operationApi {

    async bannerList(data) {
        return api.fetchExam<Sg.BannerInfo>("/v1/operation/banner/list", data, "GET")
    }
}

export default new operationApi()