module.exports = {
  NODE_ENV: '"development"',
  ENV_CONFIG: '"dev"',
  BASE_API: '"https://api-dev"',
  ATTACHMENT_API:'"http://localhost:4000"',
  // 通过环境变量传入租户code
  TENANT_CODE: process.env.TENANT_CODE || '"gitee"'
}
