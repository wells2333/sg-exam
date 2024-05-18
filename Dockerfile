# Use Alibaba Cloud Linux
FROM alibaba-cloud-linux-3-registry.cn-hangzhou.cr.aliyuncs.com/alinux3/alinux3:latest

LABEL description="A online examination application with Nginx and Java 17, using Alibaba Cloud Linux as the base OS." \
      version="0.0.14" \
      maintainer="tangyi(1633736729@qq.com)" \
      repository_url="https://gitee.com/wells2333/sg-exam" \
      license="Apache-2.0"

# Update and install packages, then clean up
RUN yum update -y && \
    yum install -y yum-utils nginx java-17-openjdk && \
    yum clean all

# Install stable nginx、jdk17
RUN yum install -y nginx java-17-openjdk

# Set environment variables and timezone
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
ENV TZ=Asia/Shanghai

# Copy necessary files and directories
WORKDIR /apps/data/web/working
COPY frontend/sg-exam-web/dist /usr/share/nginx/html
COPY frontend/sg-exam-admin/dist /usr/share/nginx/html/admin
COPY frontend/sg-exam-mobile/dist /usr/share/nginx/html/mobile
COPY config-repo/nginx /etc/nginx/
COPY sg-user-service/build/libs/*.jar app.jar
COPY config-repo config-repo
COPY setup.sh setup.sh

# Expose port
EXPOSE 80

# Define startup command
CMD ["sh", "setup.sh", "start_inner"]