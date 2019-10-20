FROM anapsix/alpine-java:8_server-jre_unlimited
MAINTAINER tangyi(1633736729@qq.com)
ARG JAR_FILE
ADD target/${JAR_FILE} /opt/app.jar
EXPOSE 9186
ENTRYPOINT java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -Duser.timezone=Asia/Shanghai -Dfile.encoding=UTF-8 -jar /opt/app.jar

# JAVA_OPS配置，如：JAVA_OPS='-Xmx512m -Xms256m'