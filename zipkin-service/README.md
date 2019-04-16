`spring cloud finchley`后，官方不建议自定义`zipkin-server`,已经提供官方打包好的jar包，直接启动即可,默认端口为`9411`

下载：`https://search.maven.org/remote_content?g=io.zipkin.java&a=zipkin-server&v=LATEST&c=exec`

运行：`java -jar zipkin-server-2.12.5-exec.jar`

或者

集成rabbitMq：`java -jar zipkin-server-2.12.5-exec.jar --zipkin.collector.rabbitmq.addresses=127.0.0.1`