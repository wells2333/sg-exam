let merge = require('webpack-merge')
let prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  ENV_CONFIG: '"dev"',
  // 通过环境变量传入租户code
  TENANT_CODE: process.env.TENANT_CODE || '"gitee"'
})
