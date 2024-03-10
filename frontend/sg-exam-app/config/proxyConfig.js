const USER_SERVICE_HOST = process.env.USER_SERVICE_HOST
const USER_SERVICE_PORT = process.env.USER_SERVICE_PORT


// 转发配置
module.exports = {
  proxyList: {
    '/sg-user-service': {
      target: USER_SERVICE_PORT === '80' ? `https://${USER_SERVICE_HOST}`: `http://${USER_SERVICE_HOST}:${USER_SERVICE_PORT}`,
      changeOrigin: true
    }
  }
}
