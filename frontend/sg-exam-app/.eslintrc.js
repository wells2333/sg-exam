module.exports = {
  root: true,
  parserOptions: {
    parser: 'babel-eslint'
  },
  env: {
    browser: true,
  },
  extends: [
    'plugin:vue/essential',
    'standard'
  ],
  plugins: [
    'vue'
  ],
  rules: {
    'generator-star-spacing': 'off',
    'space-before-function-paren': 0,
    'no-debugger': process.env.NODE_ENV === 'production' ? 'error' : 'off',
    "quotes": "off",
    'semi': 0,
    "no-unused-vars": "off",
    'no-trailing-spaces': 'off'
  }
}
