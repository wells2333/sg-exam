'use strict'
module.exports = {
  NODE_ENV: '"production"',
  ENV_CONFIG: '"prod"',
  // 通过环境变量传入租户code
  TENANT_CODE: process.env.TENANT_CODE || '"gitee"'
}
