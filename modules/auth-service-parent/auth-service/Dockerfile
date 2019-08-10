FROM anapsix/alpine-java:8_server-jre_unlimited
MAINTAINER tangyi(1633736729@qq.com)
ARG JAR_FILE
ENV PROFILE native
ADD target/${JAR_FILE} /opt/app.jar
EXPOSE 8090
ENTRYPOINT java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -Duser.timezone=Asia/Shanghai -Dfile.encoding=UTF-8 -Dspring.profiles.active=${PROFILE} -jar /opt/app.jar

# JAVA_OPS配置，如：JAVA_OPS='-Xmx512m -Xms256m'