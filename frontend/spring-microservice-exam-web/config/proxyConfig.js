// 网关地址
const GATEWAY_HOST = process.env.GATEWAY_HOST || 'localhost'
const GATEWAY_PORT = process.env.GATEWAY_PORT || '9180'

// 转发配置
module.exports = {
  proxyList: {
    '/api': {
      target: 'http://' + GATEWAY_HOST + ':' + GATEWAY_PORT,
      changeOrigin: true,
      pathRewrite: {
        '^/api': '/api'
      }
    }
  }
}
