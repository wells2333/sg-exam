FROM mysql:5.7
MAINTAINER tangyi(1633736729@qq.com)

ENV TZ=Asia/Shanghai
RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
COPY ./sg-exam-auth.sql /docker-entrypoint-initdb.d
COPY ./sg-exam-user.sql /docker-entrypoint-initdb.d
COPY ./sg-exam-exam.sql /docker-entrypoint-initdb.d