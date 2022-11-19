FROM nginx:1.13.6-alpine
MAINTAINER tangyi(1633736729@qq.com)

ARG TZ="Asia/Guangzhou"

ENV TZ ${TZ}

COPY sg-exam-app/dist /usr/share/nginx/html

COPY sg-exam-next-admin/dist /usr/share/nginx/html/admin

CMD ["nginx", "-g", "daemon off;"]
