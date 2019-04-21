FROM anapsix/alpine-java:8_server-jre_unlimited
MAINTAINER tangyi(1633736729@qq.com)
ADD ["target/exam-service.jar", "app.jar"]
EXPOSE 8098
ENV JAVA_OPTS="-Xdebug -Dspring.profiles.active=native -jar -Xmn128m -Xms128m"
RUN sh -c 'touch /app.jar'
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /app.jar" ]