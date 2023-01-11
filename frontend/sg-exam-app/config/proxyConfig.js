// user服务地址
const USER_SERVICE_HOST = process.env.USER_SERVICE_HOST || 'localhost'
const USER_SERVICE_PORT = process.env.USER_SERVICE_PORT || '9183'

// auth服务地址
const AUTH_SERVICE_HOST = process.env.AUTH_SERVICE_HOST || 'localhost'
const AUTH_SERVICE_PORT = process.env.AUTH_SERVICE_PORT || '9182'

// exam服务地址
const EXAM_SERVICE_HOST = process.env.EXAM_SERVICE_HOST || 'localhost'
const EXAM_SERVICE_PORT = process.env.EXAM_SERVICE_PORT || '9184'

// 转发配置
module.exports = {
  proxyList: {
    '/sg-user-service': {
      target: 'http://' + USER_SERVICE_HOST + ':' + USER_SERVICE_PORT,
      changeOrigin: true
    },
    '/sg-exam-service': {
      target: 'http://' + EXAM_SERVICE_HOST + ':' + EXAM_SERVICE_PORT,
      changeOrigin: true
    },
    '/sg-auth-service': {
      target: 'http://' + AUTH_SERVICE_HOST + ':' + AUTH_SERVICE_PORT,
      changeOrigin: true
    }
  }
}
