# encoding: utf-8
import http.client

host = "localhost"
port = 9183

def register():
    conn = http.client.HTTPConnection(host, port)
    headers = {
        'Content-Type': 'application/json;charset=UTF-8'
    }
    # 注册 200 个账号，test_sg_1 ~ test_sg_200，密码 123456
    for i in range(1, 201):
        payload = "{\"identifier\":\"test_sg_" + str(i) + "\",\"email\":\"test_sg@qq.com\",\"credential\":\"lBTqrKS0kZixOFXeZ0HRng==\"}"
        conn.request("POST", "/sg-user-service/v1/user/anonymousUser/register?tenantCode=gitee&ignoreCode=1", payload, headers)
        res = conn.getresponse()
        data = res.read()
        print(data.decode("utf-8"))

def login():
    conn = http.client.HTTPConnection(host, port)
    headers = {
        'Content-Type': 'application/json;charset=UTF-8',
        'Tenant-Code': 'gitee'
    }
    for i in range(1, 200):
        payload = "{}"
        conn.request("POST", "/sg-user-service/login?grant_type=password&ignoreCode=1&username=test_sg_" + str(i) + "&credential=lBTqrKS0kZixOFXeZ0HRng==&remember=false", payload, headers)
        res = conn.getresponse()
        data = res.read()
        print(data.decode("utf-8"))

def main():
# register()
    login()

if __name__ == "__main__":
    main()