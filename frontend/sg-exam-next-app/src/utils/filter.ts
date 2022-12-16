import api from "../api/api";
import validToken from "../api/auth.api";

export const filterLogin = () => {
    return new Promise(async (resolve, reject) => {
        const token = api.getToken();
        if (token === null || token === undefined || token === '') {
            reject(false)
        }
        const res = await validToken.validToken(token);
        const {code, result} = res;
        if (code != 0 || result === false) {
            reject(false)
        }
        resolve(true)
    })
}