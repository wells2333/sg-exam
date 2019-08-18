# API网关

HTTPS配置，两种方式：

1. NGINX配置SSL证书

2. API网关支持HTTPS

目前API网关已支持HTTPS，并且已经做了适配，转发请求时的协议会转为HTTP

配置见`gateway-service.yml`的ssl部分内容

# NGINX配置HTTPS

编辑 Nginx 根目录下的 conf/nginx.conf 文件。修改内容如下：

```
server {
     listen 443; #SSL 访问端口号为 443
     server_name www.domain.com; #填写绑定证书的域名
     ssl on; #启用 SSL 功能
     ssl_certificate 1_www.domain.com_bundle.crt; #证书文件名称
     ssl_certificate_key 2_www.domain.com.key; #私钥文件名称
     ssl_session_timeout 5m;
     ssl_protocols TLSv1 TLSv1.1 TLSv1.2; #请按照这个协议配置
     ssl_ciphers ECDHE-RSA-AES128-GCM-SHA256:HIGH:!aNULL:!MD5:!RC4:!DHE; #请按照这个套件配置，配置加密套件，写法遵循 openssl 标准。
     ssl_prefer_server_ciphers on;
     location / {
         root /var/www/www.domain.com; #网站主页路径。此路径仅供参考，具体请您按照实际目录操作。
         index  index.html index.htm;
     }
 }
 ```
 
 # 后续计划
 
 1. filters和predicates入库
 2. 服务多实例部署，支持流量切换、灰度发布、ABTest，思路：基于服务标签（主、备）、权重方式实现