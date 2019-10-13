let baseUrl
if (process.env.NODE_ENV === 'development') {
  baseUrl = `http://127.0.0.1:9999/`
} else if (process.env.NODE_ENV === 'production') {
  baseUrl = ''
}
export {
  baseUrl
}
