FROM anapsix/alpine-java:8_server-jre_unlimited
MAINTAINER tangyi(1633736729@qq.com)
ADD ["target/config-service.jar", "app.jar"]
EXPOSE 8769
ENV JAVA_OPTS="-Xmx512m -Xms256m"
RUN sh -c 'touch /app.jar'
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /app.jar" ]