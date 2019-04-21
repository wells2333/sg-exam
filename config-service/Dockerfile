FROM anapsix/alpine-java:8_server-jre_unlimited
MAINTAINER tangyi(1633736729@qq.com)
ADD ["target/config-service.jar", "app.jar"]
EXPOSE 8769
ENV JAVA_OPTS="-Xdebug -Dspring.profiles.active=native -jar -Xmn128m -Xms128m"
RUN sh -c 'touch /app.jar'
HEALTHCHECK CMD curl -f http://localhost:8769/actuator/health || exit 1
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /app.jar" ]