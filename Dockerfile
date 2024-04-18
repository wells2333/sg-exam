FROM centos:8
MAINTAINER tangyi(1633736729@qq.com)

# setup repo
RUN cd /etc/yum.repos.d/
RUN sed -i 's/mirrorlist/#mirrorlist/g' /etc/yum.repos.d/CentOS-*
RUN sed -i 's|#baseurl=http://mirror.centos.org|baseurl=http://vault.centos.org|g' /etc/yum.repos.d/CentOS-*

# clean yum cache
RUN yum clean all
RUN yum makecache

# install nginx
RUN rpm -Uvh http://nginx.org/packages/centos/8/x86_64/RPMS/nginx-1.20.0-1.el8.ngx.x86_64.rpm
RUN yum install -y nginx
RUN nginx -v

# install jdk17
RUN yum install -y java-17-openjdk

# copy files
COPY frontend/sg-exam-app/dist /usr/share/nginx/html
COPY frontend/sg-exam-next-admin/dist /usr/share/nginx/html/admin
COPY config-repo/nginx/nginx.conf /etc/nginx/nginx.conf

WORKDIR /apps/data/web/working
ADD sg-user-service/build/libs/*.jar app.jar
ADD setup.sh setup.sh
ADD config-repo/application.yml config-repo/application.yml
ADD config-repo/sg-user-service.yml config-repo/sg-user-service.yml

ENV TZ=Asia/Shanghai

EXPOSE 80
CMD ["sh", "setup.sh", "start_inner"]