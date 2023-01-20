FROM openjdk:14.0.1
MAINTAINER tangyi(1633736729@qq.com)

ADD ./build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -Duser.timezone=Asia/Shanghai -Dfile.encoding=UTF-8 -jar /app.jar
