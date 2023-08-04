

日志门面         日志实现

JCL             Log4j
SLF4j           Jul
jboss-logging   Log4j2
                Logback

# 简介
1. Spring使用commons-logging作为内部日志，但底层日志实现是开放的。可对接其他日志框架。
   a. spring5及以后 commons-logging被spring直接自己写了。
2. 支持 jul，log4j2,logback。SpringBoot 提供了默认的控制台输出配置，也可以配置输出为文件。
3. logback是默认使用的。
4. 虽然日志框架很多，但是我们不用担心，使用 SpringBoot 的默认配置就能工作的很好

# SpringBoot怎么把日志默认配置好的
1. 每个starter场景，都会导入一个核心场景spring-boot-starter 
2. 核心场景引入了日志的所用功能spring-boot-starter-logging 
3. 默认使用了logback + slf4j 组合作为默认底层日志
4. 日志是系统一启动就要用，xxxAutoConfiguration是系统启动好了以后放好的组件，后来用的。
5. 日志是利用监听器机制配置好的。ApplicationListener。
6. 日志所有的配置都可以通过修改配置文件实现。以logging开始的所有配置。

# 日志格式

默认输出格式：时间毫秒级 日志级别 进程ID  ----分隔符  [线程名] Logger名(产生日志的文类名) 日志内容

#  记录日志

```java
Logger logger = LoggerFactory.getLogger(getClass());
//或者使用Lombok的@Slf4j注解
```
# 日志级别
    由低到高：ALL,TRACE, DEBUG, INFO, WARN, ERROR,FATAL,OFF；   只会打印指定级别及以上级别的日志

- ALL：打印所有日志
- TRACE：追踪框架详细流程日志，一般不使用
- DEBUG：开发调试细节日志
- INFO：关键、感兴趣信息日志
- WARN：警告但不是错误的信息日志，比如：版本过时
- ERROR：业务错误日志，比如出现各种异常
- FATAL：致命错误日志，比如jvm系统崩溃
- OFF：关闭所有日志记录
- SpringBoot日志默认级别是 INFO

1. 在application.properties/yaml中配置logging.level.<logger-name>=<level>指定日志级别
2. level可取值范围：TRACE, DEBUG, INFO, WARN, ERROR, FATAL, or OFF，定义在 LogLevel类中
3. root 的logger-name叫root，可以配置logging.level.root=warn，代表所有未指定日志级别都使用 root 的 warn 级别

# 日志分组

```properties
logging.group.组名=全类名,全类名
logging.level.组名=级别
```

# 文件输出
    pringBoot 默认只把日志写在控制台，如果想额外记录到文件，可以在application.properties中添加logging.file.name or logging.file.path配置项。

logging.file.name	logging.file.path 	示例	效果
未指定	未指定		仅控制台输出
指定	未指定	my.log	写入指定文件。可以加路径
未指定	指定	/var/log	写入指定目录，文件名为spring.log
指定	指定		以logging.file.name为准

# 文件归档与滚动切割
> 归档：每天的日志单独存到一个文档中。 切割：每个文件10MB，超过大小切割成另外一个文件。
> 


1. 每天的日志应该独立分割出来存档。如果使用logback（SpringBoot 默认整合），可以通过application.properties/yaml文件指定日志滚动规则。
2. 如果是其他日志系统，需要自行配置（添加log4j2.xml或log4j2-spring.xml）
3. 支持的滚动规则设置如下

```properties
# 日志存档的文件名格式（默认值：${LOG_FILE}.%d{yyyy-MM-dd}.%i.gz）
logging.logback.rollingpolicy.file-name-pattern	=1
# 应用启动时是否清除以前存档（默认值：false）
logging.logback.rollingpolicy.clean-history-on-start=false
# 存档前，每个日志文件的最大大小（默认值：10MB）	
logging.logback.rollingpolicy.max-file-size	=1
# 日志文件被删除之前，可以容纳的最大大小（默认值：0B）。设置1GB则磁盘存储超过 1GB 日志后就会删除旧日志文件
logging.logback.rollingpolicy.total-size-cap=1
# 日志文件保存的最大天数(默认值：7).
logging.logback.rollingpolicy.max-history=1
```

# 自定义配置
日志系统	自定义
Logback	logback-spring.xml, logback-spring.groovy,
logback.xml, or logback.groovy
Log4j2	log4j2-spring.xml or log4j2.xml
JDK (Java Util Logging)	logging.properties

自己要写配置，配置文件名加上 xx-spring.xml

#  最佳实战
1. 导入任何第三方框架，先排除它的日志包，因为Boot底层控制好了日志
2. 修改 application.properties 配置文件，就可以调整日志的所有行为。如果不够，可以编写日志框架自己的配置文件放在类路径下就行，比如logback-spring.xml，log4j2-spring.xml
3. 如需对接专业日志系统，也只需要把 logback 记录的日志灌倒 kafka之类的中间件，这和SpringBoot没关系，都是日志框架自己的配置，修改配置文件即可
4. 业务中使用slf4j-api记录日志。不要再 sout 了