import CryptoJS from 'crypto-js'

/**
 * 加密处理
 */
export function encryption(params): string {
  let {
    data,
    type,
    param,
    key
  } = params
  const result = JSON.parse(JSON.stringify(data))
  if (type === 'Base64') {
    param.forEach(ele => {
      result[ele] = btoa(result[ele])
    })
  } else {
    param.forEach(ele => {
      let data = result[ele]
      key = CryptoJS.enc.Latin1.parse(key)
      let iv = key
      let encrypted = CryptoJS.AES.encrypt(
        data,
        key,
        { iv: iv,
          mode: CryptoJS.mode.CBC,
          padding: CryptoJS.pad.ZeroPadding
        })
      result[ele] = encrypted.toString()
    })
  }
  return result
}
