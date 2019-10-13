FROM nginx:1.13.6-alpine
MAINTAINER tangyi(1633736729@qq.com)

ARG TZ="Asia/Guangzhou"

ENV TZ ${TZ}

RUN apk upgrade --update \
    && apk add bash tzdata \
    && ln -sf /usr/share/zoneinfo/${TZ} /etc/localtime \
    && echo ${TZ} > /etc/timezone \
    && rm -rf /var/cache/apk/*

COPY dist /usr/share/nginx/html

CMD ["nginx", "-g", "daemon off;"]
