Auth Service
=============

### 登录认证接口说明

支持三种方式登录：

1. 账号密码+图片验证码
2. 手机号+短信验证码
3. 微信小程序code

#### 账号密码+图片验证码登录

POST：`/api/auth/api/v1//token?grant_type=password&scope=read&username=admin&credential=lBTqrKS0kZixOFXeZ0HRng==&randomStr=86111562225179514&code=mf3f`

url参数：
```
{
    grant_type: password, // 固定为password
    scope: read,    // 固定为read
    username: '',   // 账号
    credential: '', // 加密后的密码，加密算法在下面的密码加密部分
    randomStr: '',  // 随机数，生成算法在下面的随机数生成部分
    code: ''    // 验证码
}
```

1. 先生成随机数：randomStr

通过: `/api/user/v1/code/${randomStr}`获取验证码

2. 获取token

登录示例代码：

```
/**
 * 登录
 * @param identifier 账号
 * @param credential 密码
 * @param code 验证码
 * @param randomStr 随机数
 */
export function loginByUsername (identifier, credential, code, randomStr) {
  const grantType = 'password'
  const scope = 'read'
  return request({
    url: '/api/auth/oauth/token',
    headers: {
      'Authorization': basicAuthorization
    },
    method: 'post',
    params: {username: identifier, credential, randomStr, code, grant_type: grantType, scope}
  })
}

```
其中：

1. `const basicAuthorization = 'Basic ' + btoa('web_app:spring-microservice-exam-secret')`
2. 密码需要加密，具体看**密码加密**部分
3. 请求头要带`Tenant-Code`，即租户标识

#### 二 手机号+验证码登录

发送短信接口：`/api/user/v1/mobile/sendSms/` + ${mobile}，其中mobile为手机号

登录接口URL：`/api/auth/mobile/token`

```
/**
 * 根据手机号登录
 * @param social 手机号
 * @param code 验证码
 */
export function loginBySocial (social, code) {
  const grantType = 'mobile'
  const scope = 'read'
  return request({
    url: '/api/auth/mobile/token',
    headers: {
      'Authorization': basicAuthorization
    },
    method: 'post',
    params: {mobile: social, code, grant_type: grantType, scope},
    data: {
        name: userInfo.nickName,
        sex: userInfo.gender,
        avatarUrl: userInfo.avatarUrl
    }
  })
}
```

响应体：
```
{
    "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9",
    "token_type": "bearer",
    "refresh_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9",
    "expires_in": 3599,
    "scope": "read write",
    "loginType": "SMS",
    "tenantCode": "gitee",
    "jti": "e446adad-df0a-490a-9cde-8ac69c846335"
}
```

#### 三 微信小程序+code登录

POST：`/api/auth/wx/token?grant_type=wx&scope=read&code=` + code

其中grant_type、scope固定，code为wx.login返回的code

1. 请求头参数：
```
{
    'Authorization': 'Basic d2ViX2FwcDpzcHJpbmctbWljcm9zZXJ2aWNlLWV4YW0tc2VjcmV0',
    'Tenant-Code': 'gitee'  // 租户标识，目前固定为gitee
}
```
Authorization为`clientId:clientSecret`的base64编码，即：`'Basic ' + btoa('web_app:spring-microservice-exam-secret')`

2. 请求体参数:
```
{
    name: userInfo.nickName,
    sex: userInfo.gender, // 男：0，女：1
    avatarUrl: userInfo.avatarUrl
}
```

3. 响应体：

```
{
    access_token: "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJsb2dpblR5cGUiOiJXWCIsInVzZXJfbmFtZSI6Im92TGw0NUluUm40SHpfanJwRWstZ0Yta0VGZjgiLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXSwidGVuYW50Q29kZSI6ImdpdGVlIiwiZXhwIjoxNTYyNDE1ODUxLCJhdXRob3JpdGllcyI6WyJyb2xlX3VzZXIiXSwianRpIjoiOWE4MzUxNzAtMDA4NS00MGU3LWEzZWQtNTkyODFmZThlYWVmIiwiY2xpZW50X2lkIjoid2ViX2FwcCJ9.WsL4fW4ZsiJqlb-Nvj9RMAgdimOFpR4925OtZDPw2AAKG8daIkRIH5HXb91mS6mdI7ldhtiwEV1Lo5Glka1T3DYYeWaZdLsuHt66JyzGTrunuIP7msXfHEfO5QmvoytX2RwbqaS4riBqcc5Oh-Fry8Negb1wc3nV0-zjVqepPFmdDyU0eGL3g6lZMSpi4QPZUlBV2VHqjHw4bf9M1Z1PbZZmJrEWiY48Zdsm-kaW-uwPo7FGi7jXHrnUI7dJgKXsuVyWi04LdwWy2vhohjl7tZd29q5Wr3kmj8nQf3EYiub_Tx9ielVEApgnNqMwLNnWgcX1YNd5nQcs0Ahf785p0w",
    expires_in: 2287,
    jti: "9a835170-0085-40e7-a3ed-59281fe8eaef",
    loginType: "WX",
    refresh_token: "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJsb2dpblR5cGUiOiJXWCIsInVzZXJfbmFtZSI6Im92TGw0NUluUm40SHpfanJwRWstZ0Yta0VGZjgiLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXSwiYXRpIjoiOWE4MzUxNzAtMDA4NS00MGU3LWEzZWQtNTkyODFmZThlYWVmIiwidGVuYW50Q29kZSI6ImdpdGVlIiwiZXhwIjoxNTYyNDMzODUxLCJhdXRob3JpdGllcyI6WyJyb2xlX3VzZXIiXSwianRpIjoiNzQ4ZjFhNTMtZGRhYi00OGNjLWFhYzYtZWU5MzMzYzI0NWUyIiwiY2xpZW50X2lkIjoid2ViX2FwcCJ9.WRdOKI0Z2PX9IwxthEf_LSOWAyaz1AoM7T_ytkzlDuislSgMI6cZ7rJozMJdozfXa7xWl1swJ_r6-t2EoDC0gqDFnrIjmhgfJH6yIZSgWUQMTzXP29iHtkgRBvvDwPbDmHHRZjwV3eQvL6tFNJguBoywcCv81Ycy8tPBIWgbkbg98Fs--vbs0HznDHldf_Kzu_0_1Us0UkYPhN6YNJdlNfCxzSbXOrwuem-VIcw-yulw1s5-s_5sjqSRJ6HjVbwRyQrJizAmKkDN5YiSxf61fJDO2quUhc0DXY0OmKphL5EqlrgTCoQYCVoeYOLvcnUX5wIB3YdbP2WJiiYRrtx57g",
    scope: "read write",
    tenantCode: "gitee",    // 租户标识
    token_type: "bearer"
}
```

4. 登录示例代码：

```
// 登录
wx.login({
  success: res => {
    // 发送 res.code 到后台换取 openId, sessionKey, unionId
    wx.request({
      url: 'http://localhost:9180/api/auth/wx/token?grant_type=wx&scope=read&code=' + res.code,
      header: {
        'Authorization': 'Basic d2ViX2FwcDpzcHJpbmctbWljcm9zZXJ2aWNlLWV4YW0tc2VjcmV0',
        'Tenant-Code': 'gitee'
      },
      method: 'POST',
      data: {
        name: userInfo.nickName,
        sex: userInfo.gender,
        avatarUrl: userInfo.avatarUrl
      },
      success (res) {
        console.log(res.data)
      }
    })
  }
})
```


#### 随机数生成

```
/**
 * 生成随机len位数字
 */
export const randomLenNum = (len, date) => {
  let random = ''
  random = Math.ceil(Math.random() * 100000000000000).toString().substr(0, typeof len === 'number' ? len : 4)
  if (date) random = random + Date.now()
  return random
}
```

如生成4位长度的随机数： let randomStr = randomLenNum(4)

#### 密码加密

```
const user = encryption({
  data: userInfo,
  key: '1234567887654321',
  param: ['credential']
})
```

```
/**
 * 加密处理
 */
export const encryption = (params) => {
  var {
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
      var data = result[ele]
      key = CryptoJS.enc.Latin1.parse(key)
      var iv = key
      var encrypted = CryptoJS.AES.encrypt(
        data,
        key,
        {
          iv: iv,
          mode: CryptoJS.mode.CBC,
          padding: CryptoJS.pad.ZeroPadding
        })
      result[ele] = encrypted.toString()
    })
  }
  return result
}
```

#### 刷新token

用于token失效后（token有效期默认为一个小时），根据refresh_token重新获取token

1. token失效：token失效后，请求的api接口会统一返回403，可以定义http请求的拦截器，判断后端返回的状态码是否为403，是则尝试刷新token
2. 根据不同的认证方式，`POST`到对应的认证URL，设置参数`grant_type=refresh_token、scope、refresh_token`，如账号密码登录方式下的刷新token：

```
/**
 * 刷新token
 */
export function refreshToken () {
  //  grant_type为refresh_token
  const grantType = 'refresh_token'
  const scope = 'read'
  // refresh_token的具体值在登录获取token的时候返回的
  const refreshToken = {refresh_token} 
  return request({
    url: '/api/auth/oauth/token',
    headers: {
      'Authorization': 'Basic ' + btoa('web_app:spring-microservice-exam-secret')
    },
    method: 'post',
    params: { grant_type: grantType, scope, refresh_token: refreshToken }
  })
}
```