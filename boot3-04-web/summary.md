# 静态资源

## 默认规则

### 静态资源映射

静态资源映射规则在 WebMvcAutoConfiguration 中进行了定义：

1. /webjars/** 的所有路径 资源都在 classpath:/META-INF/resources/webjars/
2. /** 的所有路径 资源都在 classpath:/META-INF/resources/、classpath:/resources/、classpath:/static/、classpath:/public/
3. 所有静态资源都定义了缓存规则。【浏览器访问过一次，就会缓存一段时间】，但此功能参数无默认值
   a. period： 缓存间隔。 默认 0S；
   b. cacheControl：缓存控制。 默认无；
   c. useLastModified：是否使用lastModified头。 默认 false；

### 静态资源缓存

如前面所述

1. 所有静态资源都定义了缓存规则。【浏览器访问过一次，就会缓存一段时间】，但此功能参数无默认值
   a. period： 缓存间隔。 默认 0S；
   b. cacheControl：缓存控制。 默认无；
   c. useLastModified：是否使用lastModified头。 默认 false；

### 欢迎页

欢迎页规则在 WebMvcAutoConfiguration 中进行了定义：

1. 在静态资源目录下找 index.html
2. 没有就在 templates下找index模板页

### Favicon

1. 在静态资源目录下找 favicon.ico

### 缓存实验

```properties
server.port=9000
#1、spring.web：
# 1.配置国际化的区域信息
# 2.静态资源策略(开启、处理链、缓存)
#开启静态资源映射规则
spring.web.resources.add-mappings=true
#设置缓存
#spring.web.resources.cache.period=3600
##缓存详细合并项控制，覆盖period配置：
## 浏览器第一次请求服务器，服务器告诉浏览器此资源缓存7200秒，7200秒以内的所有此资源访问不用发给服务器请求，7200秒以后发请求给服务器
spring.web.resources.cache.cachecontrol.max-age=7200
#使用资源 last-modified 时间，来对比服务器和浏览器的资源是否相同没有变化。相同返回 304
spring.web.resources.cache.use-last-modified=true
```

## 自定义静态资源规则

> spring.mvc： 静态资源访问前缀路径
> spring.web
>

### 配置方式

```properties
#1、spring.web：
# 1.配置国际化的区域信息
# 2.静态资源策略(开启、处理链、缓存)
#开启静态资源映射规则
spring.web.resources.add-mappings=true
#设置缓存
spring.web.resources.cache.period=3600
##缓存详细合并项控制，覆盖period配置：
## 浏览器第一次请求服务器，服务器告诉浏览器此资源缓存7200秒，7200秒以内的所有此资源访问不用发给服务器请求，7200秒以后发请求给服务器
spring.web.resources.cache.cachecontrol.max-age=7200
## 共享缓存
spring.web.resources.cache.cachecontrol.cache-public=true
#使用资源 last-modified 时间，来对比服务器和浏览器的资源是否相同没有变化。相同返回 304
spring.web.resources.cache.use-last-modified=true
#自定义静态资源文件夹位置
spring.web.resources.static-locations=classpath:/a/,classpath:/b/,classpath:/static/
#2、 spring.mvc
## 2.1. 自定义webjars路径前缀
spring.mvc.webjars-path-pattern=/wj/**
## 2.2. 静态资源访问路径前缀
spring.mvc.static-path-pattern=/static/**
```

### 代码方式

```java

@Configuration //这是一个配置类
//容器中只要有一个 WebMvcConfigurer 组件。配置的底层行为都会生效
//@EnableWebMvc //禁用boot的默认配置
public class MyConfig implements WebMvcConfigurer {


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //保留以前规则
        //自己写新的规则。
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/a/", "classpath:/b/")
                .setCacheControl(CacheControl.maxAge(1180, TimeUnit.SECONDS));
    }
}

@Configuration //这是一个配置类,给容器中放一个 WebMvcConfigurer 组件，就能自定义底层
public class MyConfig  /*implements WebMvcConfigurer*/ {


    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/static/**")
                        .addResourceLocations("classpath:/a/", "classpath:/b/")
                        .setCacheControl(CacheControl.maxAge(1180, TimeUnit.SECONDS));
            }
        };
    }

}
```

# 路径匹配

Spring5.3 之后加入了更多的请求路径匹配的实现策略；以前只支持 AntPathMatcher 策略, 现在提供了 PathPatternParser
策略。并且可以让我们指定到底使用那种策略。

#### Ant风格路径用法

Ant 风格的路径模式语法具有以下规则：

- *：表示任意数量的字符。
- ?：表示任意一个字符。
- **：表示任意数量的目录。
- {}：表示一个命名的模式占位符。
- []：表示字符集合，例如[a-z]表示小写字母。
  例如：
- *.html 匹配任意名称，扩展名为.html的文件。
- /folder1/*/*.java 匹配在folder1目录下的任意两级目录下的.java文件。
- /folder2/**/*.jsp 匹配在folder2目录下任意目录深度的.jsp文件。
- /{type}/{id}.html 匹配任意文件名为{id}.html，在任意命名的{type}目录下的文件。
  注意：Ant 风格的路径模式语法中的特殊字符需要转义，如：
- 要匹配文件路径中的星号，则需要转义为\\*。
- 要匹配文件路径中的问号，则需要转义为\\?。

#### 模式切换

> AntPathMatcher 与 PathPatternParser
> PathPatternParser 在 jmh 基准测试下，有 6~8 倍吞吐量提升，降低 30%~40%空间分配率
> PathPatternParser 兼容 AntPathMatcher语法，并支持更多类型的路径模式
> PathPatternParser  "**" 多段匹配的支持仅允许在模式末尾使用

```java
    @GetMapping("/a*/b?/{p1:[a-f]+}")
public String hello(HttpServletRequest request,
@PathVariable("p1") String path){

        log.info("路径变量p1： {}",path);
        //获取请求路径
        String uri=request.getRequestURI();
        return uri;
        }
```

```properties
# 总结：
# 使用默认的路径匹配规则，是由 PathPatternParser  提供的
# 如果路径中间需要有 **，替换成ant风格路径
# 改变路径匹配策略：
# ant_path_matcher 老版策略；
# path_pattern_parser 新版策略；
spring.mvc.pathmatch.matching-strategy=ant_path_matcher
```

# 内容协商

## 多段内容适配

### 默认规则

#### SpringBoot 多端内容适配

##### 基于请求头内容协商

> 携带Http标准的Accept请求头
> 服务端根据请求头期望的数据类型进行动态返回

##### 基于请求参数内容协商

> 例如url?format=json

## 实战

- 引入Jackson依赖
- 实体类新增注解
- 传递参数或者Accept值

## 自定义内容返回

## 内容协商返回原理  HttpMessageConverter

> 定制HttpMessageConverter来实现内容协商

### ResponseBody 由HttpMessageConverter处理

> 标注了@ResponseBody的返回值将会由支持它的HttpMessageConverter写给浏览器

1. 如果controller方法的返回值标注了 @ResponseBody 注解
   1.1. 请求进来先来到DispatcherServlet的doDispatch()进行处理
   1.2. 找到一个 HandlerAdapter 适配器。利用适配器执行目标方法
   1.3. RequestMappingHandlerAdapter来执行，调用invokeHandlerMethod（）来执行目标方法
   1.4. 目标方法执行之前，准备好两个东西
   1.4.1. HandlerMethodArgumentResolver：参数解析器，确定目标方法每个参数值
   1.4.2. HandlerMethodReturnValueHandler：返回值处理器，确定目标方法的返回值改怎么处理
   1.5. RequestMappingHandlerAdapter 里面的invokeAndHandle()真正执行目标方法
   1.6. 目标方法执行完成，会返回返回值对象
   1.7. 找到一个合适的返回值处理器 HandlerMethodReturnValueHandler
   1.8. 最终找到 RequestResponseBodyMethodProcessor能处理 标注了 @ResponseBody注解的方法
   1.9. RequestResponseBodyMethodProcessor 调用writeWithMessageConverters ,利用MessageConverter把返回值写出去

上面解释：@ResponseBody由HttpMessageConverter处理

2. HttpMessageConverter 会先进行内容协商
   2.1. 遍历所有的MessageConverter看谁支持这种内容类型的数据
   2.2. 默认MessageConverter有以下
   2.3.
   2.4. 最终因为要json所以MappingJackson2HttpMessageConverter支持写出json
   2.5. jackson用ObjectMapper把对象写出去

### 增加yaml返回支持

1. 引入处理包
2. 增加配置的媒体类型
3. 创建自己的处理类 继承于AbstractHttpMessageConverter (调用第三方方法处理,调用父类构造器来决定处理什么类型)
4. 创建自己的配置类 重写configureMessageConverters方法 添加自己的处理类

# WenMvcAutoConfiguration原理

## 生效条件

## 效果

## WebMvcConfigure接口

## 静态资源规则源码

## EnableWebMvcConfiguration源码

## WebMvcConfigurationSupport

# 模板引擎

获取路径记得@{} 不然的话会被content-path影响 动态路径可以 @{${变量}} 写死的可以@{路径}

## Thymeleaf整合

- maven引入
- 默认页面放在classpath:/templates下
- 找后缀为html的文件

## 基础语法

### 核心用法

th:xxx：动态渲染指定的 html 标签属性值、或者th指令（遍历、判断等）

- th:text：标签体内文本值渲染
- th:utext：不会转义，显示为html原本的样子。
- th:属性：标签指定属性渲染
- th:attr：标签任意属性渲染
- th:ifth:each...：其他th指令

表达式：用来动态取值

- ${}：变量取值；使用model共享给页面的值都直接用${}
- @{}：url路径；
- #{}：国际化消息
- ~{}：片段引用
- *{}：变量选择：需要配合th:object绑定对象

系统工具&内置对象：详细文档 https://www.thymeleaf.org/doc/tutorials/3.1/usingthymeleaf.html#appendix-a-expression-basic-objects

- param：请求参数对象
- session：session对象
- application：application对象
- #execInfo：模板执行信息
- #messages：国际化消息
- #uris：uri/url工具
- #conversions：类型转换工具
- #dates：日期工具，是java.util.Date对象的工具类
- #calendars：类似#dates，只不过是java.util.Calendar对象的工具类
- #temporals： JDK8+ java.time API 工具类
- #numbers：数字操作工具
- #strings：字符串操作
- #objects：对象操作
- #bools：bool操作
- #arrays：array工具
- #lists：list工具
- #sets：set工具
- #maps：map工具
- #aggregates：集合聚合工具（sum、avg）
- #ids：id生成工具

### 语法示例

表达式：

- 变量取值：${...}
- url 取值：@{...}
- 国际化消息：#{...}
- 变量选择：*{...}
- 片段引用: ~{...}
  常见：
- 文本： 'one text'，'another one!',...
- 数字： 0,34,3.0,12.3,...
- 布尔：true、false
- null: null
- 变量名： one,sometext,main...
  文本操作：
- 拼串： +
- 文本替换：| The name is ${name} |
  布尔操作：
- 二进制运算： and,or
- 取反：!,not
  比较运算：
- 比较：>，<，<=，>=（gt，lt，ge,le）
- 等值运算：==,!=（eq，ne）
  条件运算：
- if-then： (if)?(then)
- if-then-else: (if)?(then):(else)
- default: (value)?:(defaultValue)
  特殊语法：
- 无操作：_
  所有以上都可以嵌套组合

## 属性设置

1. th:href="@{/product/list}"
2. th:attr="class=${active}"
3. th:attr="src=@{/images/gtvglogo.png},title=${logo},alt=#{logo}"
4. th:checked="${user.active}"

## 遍历

th:each="元素名,迭代状态 : ${集合}"

iterStat 有以下属性：

- index：当前遍历元素的索引，从0开始
- count：当前遍历元素的索引，从1开始
- size：需要遍历元素的总数量
- current：当前正在遍历的元素对象
- even/odd：是否偶数/奇数行
- first：是否第一个元素
- last：是否最后一个元素

## 判断

- th:if
- th:switch th:case

## 属性优先级

- 片段
- 遍历
- 判断
  Order Feature Attributes
  1 片段包含 th:insert th:replace
  2 遍历 th:each
  3 判断 th:if th:unless th:switch th:case
  4 定义本地变量 th:object th:with
  5 通用方式属性修改 th:attr th:attrprepend th:attrappend
  6 指定属性修改 th:value th:href th:src ...
  7 文本值 th:text th:utext
  8 片段指定 th:fragment
  9 片段移除 th:remove

## 行内写法

[[...]] or [(...)]

```html
<p>Hello, [[${session.user.name}]]!</p>
```

## 变量选择

```html

<div th:object="${session.user}">
    <p>Name: <span th:text="*{firstName}">Sebastian</span>.</p>
    <p>Surname: <span th:text="*{lastName}">Pepper</span>.</p>
    <p>Nationality: <span th:text="*{nationality}">Saturn</span>.</p>
</div>
<!--等同于-->
<div>
    <p>Name: <span th:text="${session.user.firstName}">Sebastian</span>.</p>
    <p>Surname: <span th:text="${session.user.lastName}">Pepper</span>.</p>
    <p>Nationality: <span th:text="${session.user.nationality}">Saturn</span>.</p>
</div
```

## 模板布局

- 定义模板： th:fragment
- 引用模板：~{templatename::selector}
- 插入模板：th:insert、th:replace

```html

<footer th:fragment="copy">&copy; 2011 The Good Thymes Virtual Grocery</footer>

<body>
<div th:insert="~{footer :: copy}"></div>
<div th:replace="~{footer :: copy}"></div>
</body>
<body>
结果：
<body>
<div>
    <footer>&copy; 2011 The Good Thymes Virtual Grocery</footer>
</div>

<footer>&copy; 2011 The Good Thymes Virtual Grocery</footer>
</body>
</body>
```

## devtools

```xml
<!--
修改页面后；ctrl+F9刷新效果；
java代码的修改，如果devtools热启动了，可能会引起一些bug，难以排查
-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
</dependency>
```

# 国际化

国际化的自动配置参照MessageSourceAutoConfiguration
实现步骤：

1. Spring Boot 在类路径根下查找messages资源绑定文件。文件名为：messages.properties
2. 多语言可以定义多个消息文件，命名为messages_区域代码.properties。如：
   a. messages.properties：默认
   b. messages_zh_CN.properties：中文环境
   c. messages_en_US.properties：英语环境
3. 在程序中可以自动注入 MessageSource组件，获取国际化的配置项值
4. 在页面中可以使用表达式 #{}获取国际化的配置项值

```java
    @Autowired  //国际化取消息用的组件
    MessageSource messageSource;
@GetMapping("/haha")
public String haha(HttpServletRequest request){

        Locale locale=request.getLocale();
        //利用代码的方式获取国际化配置文件中指定的配置项的值
        String login=messageSource.getMessage("login",null,locale);
        return login;
        }
```

# 错误处理

## 默认机制

> 错误处理的自动配置都在ErrorMvcAutoConfiguration中，两大核心机制：
> 1. SpringBoot 会自适应处理错误，响应页面或JSON数据
> 2. SpringMVC的错误处理机制依然保留，MVC处理不了，才会交给boot进行处理
>

发生错误以后，转发给/error路径，SpringBoot在底层写好一个 BasicErrorController的组件，专门处理这个请求

```java
    @RequestMapping(produces = MediaType.TEXT_HTML_VALUE) //返回HTML
public ModelAndView errorHtml(HttpServletRequest request,HttpServletResponse response){
        HttpStatus status=getStatus(request);
        Map<String, Object> model=Collections
        .unmodifiableMap(getErrorAttributes(request,getErrorAttributeOptions(request,MediaType.TEXT_HTML)));
        response.setStatus(status.value());
        ModelAndView modelAndView=resolveErrorView(request,response,status,model);
        return(modelAndView!=null)?modelAndView:new ModelAndView("error",model);
        }

@RequestMapping  //返回 ResponseEntity, JSON
public ResponseEntity<Map<String, Object>>error(HttpServletRequest request){
        HttpStatus status=getStatus(request);
        if(status==HttpStatus.NO_CONTENT){
        return new ResponseEntity<>(status);
        }
        Map<String, Object> body=getErrorAttributes(request,getErrorAttributeOptions(request,MediaType.ALL));
        return new ResponseEntity<>(body,status);
        }
```

错误页面是这么解析到的

```java
        //1、解析错误的自定义视图地址
        ModelAndView modelAndView=resolveErrorView(request,response,status,model);
                //2、如果解析不到错误页面的地址，默认的错误页就是 error
                return(modelAndView!=null)?modelAndView:new ModelAndView("error",model);
```

容器中专门有一个错误视图解析器

```java
@Bean
@ConditionalOnBean(DispatcherServlet.class)
@ConditionalOnMissingBean(ErrorViewResolver.class)
DefaultErrorViewResolver conventionErrorViewResolver(){
        return new DefaultErrorViewResolver(this.applicationContext,this.resources);
        }
```

SpringBoot解析自定义错误页的默认规则

```java
    @Override
public ModelAndView resolveErrorView(HttpServletRequest request,HttpStatus status,Map<String, Object> model){
        ModelAndView modelAndView=resolve(String.valueOf(status.value()),model);
        if(modelAndView==null&&SERIES_VIEWS.containsKey(status.series())){
        modelAndView=resolve(SERIES_VIEWS.get(status.series()),model);
        }
        return modelAndView;
        }

private ModelAndView resolve(String viewName,Map<String, Object> model){
        String errorViewName="error/"+viewName;
        TemplateAvailabilityProvider provider=this.templateAvailabilityProviders.getProvider(errorViewName,
        this.applicationContext);
        if(provider!=null){
        return new ModelAndView(errorViewName,model);
        }
        return resolveResource(errorViewName,model);
        }

private ModelAndView resolveResource(String viewName,Map<String, Object> model){
        for(String location:this.resources.getStaticLocations()){
        try{
        Resource resource=this.applicationContext.getResource(location);
        resource=resource.createRelative(viewName+".html");
        if(resource.exists()){
        return new ModelAndView(new HtmlResourceView(resource),model);
        }
        }
        catch(Exception ex){
        }
        }
        return null;
        }
```

容器中有一个默认的名为 error 的 view； 提供了默认白页功能

```java
@Bean(name = "error")
@ConditionalOnMissingBean(name = "error")
public View defaultErrorView(){
        return this.defaultErrorView;
        }
```

封装了JSON格式的错误信息

```java
    @Bean
@ConditionalOnMissingBean(value = ErrorAttributes.class, search = SearchStrategy.CURRENT)
public DefaultErrorAttributes errorAttributes(){
        return new DefaultErrorAttributes();
        }
```

规则：

1. 解析一个错误页
    1. 如果发生了500、404、503、403 这些错误
        - 如果有模板引擎，默认在 classpath:/templates/error/精确码.html
        - 如果没有模板引擎，在静态资源文件夹下找 精确码.html
    2. 如果匹配不到精确码.html这些精确的错误页，就去找5xx.html，4xx.html模糊匹配
        - 如果有模板引擎，默认在 classpath:/templates/error/5xx.html
        - 如果没有模板引擎，在静态资源文件夹下找 5xx.html
2. 如果模板引擎路径templates下有 error.html页面，就直接渲染

## 自定义错误响应

### 自定义Json

> 使用@ControllerAdvice + @ExceptionHandler 进行统一异常处理

### 自定义页面相应

> 根据boot的错误页面规则，自定义页面模板

## 最佳实战

- 前后分离
    - 后台发生的所有错误，@ControllerAdvice + @ExceptionHandler进行统一异常处理
- 服务段页面渲染
    - 不可预知的一些 Http码表示的服务器或客户端错误
        - 给classpath:/templates/error/下面，放常用精确的错误码页面。500.html，404.html
        - 给classpath:/templates/error/下面，放通用模糊匹配的错误码页面。 5xx.html，4xx.html
    - 发生业务错误
        - 核心业务，每一种错误，都应该代码控制，跳转到自己定制的错误页。
        - 通用业务，classpath:/templates/error.html页面，显示错误信息。

页面/Json可用的Model数据

- timestamp
- status
- error
- trace
- message
- path

# 嵌入式容器

> Servlet容器：管理、运行Servlet组件（Servlet、Filter、Listener）的环境，一般指服务器

## 自动配置原理

> - SpringBoot 默认嵌入Tomcat作为Servlet容器。
> - 自动配置类是ServletWebServerFactoryAutoConfiguration，EmbeddedWebServerFactoryCustomizerAutoConfiguration
> - 自动配置类开始分析功能。`xxxxAutoConfiguration`

``` java 
@AutoConfiguration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnClass(ServletRequest.class)
@ConditionalOnWebApplication(type = Type.SERVLET)
@EnableConfigurationProperties(ServerProperties.class)
@Import({ ServletWebServerFactoryAutoConfiguration.BeanPostProcessorsRegistrar.class,
		ServletWebServerFactoryConfiguration.EmbeddedTomcat.class,
		ServletWebServerFactoryConfiguration.EmbeddedJetty.class,
		ServletWebServerFactoryConfiguration.EmbeddedUndertow.class })
public class ServletWebServerFactoryAutoConfiguration {
    
}
```

1. ServletWebServerFactoryAutoConfiguration 自动配置了嵌入式容器场景
2. 绑定了ServerProperties配置类，所有和服务器有关的配置 server
3. ServletWebServerFactoryAutoConfiguration 导入了 嵌入式的三大服务器 Tomcat、Jetty、Undertow
    1. 导入 Tomcat、Jetty、Undertow 都有条件注解。系统中有这个类才行（也就是导了包）
    2. 默认 Tomcat配置生效。给容器中放 TomcatServletWebServerFactory
    3. 都给容器中 ServletWebServerFactory放了一个 web服务器工厂（造web服务器的）
    4. web服务器工厂 都有一个功能，getWebServer获取web服务器
    5. TomcatServletWebServerFactory 创建了 tomcat。
4. ServletWebServerFactory 什么时候会创建 webServer出来。
5. ServletWebServerApplicationContextioc容器，启动的时候会调用创建web服务器
6. Spring容器刷新（启动）的时候，会预留一个时机，刷新子容器。onRefresh()
7. refresh() 容器刷新 十二大步的刷新子容器会调用 onRefresh()；

```java
    @Override
protected void onRefresh(){
        super.onRefresh();
        try{
        createWebServer();
        }
        catch(Throwable ex){
        throw new ApplicationContextException("Unable to start web server",ex);
        }
        }
```

> Web场景的Spring容器启动，在onRefresh的时候，会调用创建web服务器的方法。
> Web服务器的创建是通过WebServerFactory搞定的。容器中又会根据导了什么包条件注解，启动相关的 服务器配置，默认EmbeddedTomcat会给容器中放一个
> TomcatServletWebServerFactory，导致项目启动，自动创建出Tomcat。

## 自定义

- 默认支持三个Tomcat Jetty Undertow
- 将web自带的tomcat移除
- 引入新的starter

## 最佳实践

- 修改server下的相关配置就可以修改服务器参数
- 通过给容器中放一个ServletWebServerFactory，来禁用掉SpringBoot默认放的服务器工厂，实现自定义嵌入任意服务器

# 全面接管SpringMVC

> SpringBoot 默认配置好了 SpringMVC 的所有常用特性。
> 如果我们需要全面接管SpringMVC的所有配置并禁用默认配置，仅需要编写一个WebMvcConfigurer配置类，并标注 @EnableWebMvc 即可
> 全手动模式
> @EnableWebMvc : 禁用默认配置
> WebMvcConfigurer组件：定义MVC的底层行为

## WebMvcAutoConfiguration到底自动配置了那些规则

1. WebMvcAutoConfigurationweb场景的自动配置类
    1. 支持RESTful的filter：HiddenHttpMethodFilter
    2. 支持非POST请求，请求体携带数据：FormContentFilter
    3. 导入EnableWebMvcConfiguration：
        1. RequestMappingHandlerAdapter
        2. WelcomePageHandlerMapping： 欢迎页功能支持（模板引擎目录、静态资源目录放index.html），项目访问/ 就默认展示这个页面.
        3. RequestMappingHandlerMapping：找每个请求由谁处理的映射关系
        4. ExceptionHandlerExceptionResolver：默认的异常解析器
        5. LocaleResolver：国际化解析器
        6. ThemeResolver：主题解析器
        7. FlashMapManager：临时数据共享
        8. FormattingConversionService： 数据格式化 、类型转化
        9. Validator： 数据校验JSR303提供的数据校验功能
        10. WebBindingInitializer：请求参数的封装与绑定
        11. ContentNegotiationManager：内容协商管理器
    4. WebMvcAutoConfigurationAdapter配置生效，它是一个WebMvcConfigurer，定义mvc底层组件
        1. 定义好 WebMvcConfigurer 底层组件默认功能；所有功能详见列表
        2. 视图解析器：InternalResourceViewResolver
        3. 视图解析器：BeanNameViewResolver,视图名（controller方法的返回值字符串）就是组件名
        4. 内容协商解析器：ContentNegotiatingViewResolver
        5. 请求上下文过滤器：RequestContextFilter: 任意位置直接获取当前请求
        6. 静态资源链规则
        7. ProblemDetailsExceptionHandler：错误详情 SpringMVC内部场景异常被它捕获：新特性的
    5. 定义了MVC默认的底层行为: WebMvcConfigurer

## @EnableWebMvc 禁用默认行为

1. @EnableWebMvc给容器中导入 DelegatingWebMvcConfiguration组件，
   他是 WebMvcConfigurationSupport
2. WebMvcAutoConfiguration有一个核心的条件注解, @ConditionalOnMissingBean(WebMvcConfigurationSupport.class)
   ，容器中没有WebMvcConfigurationSupport，WebMvcAutoConfiguration才生效.
3. @EnableWebMvc 导入 WebMvcConfigurationSupport 导致 WebMvcAutoConfiguration 失效。导致禁用了默认行为

> @EnableWebMVC 禁用了 Mvc的自动配置
> WebMvcConfigurer 定义SpringMVC底层组件的功能类

## WebMvcConfigurer 功能

> 定义扩展SpringMVC底层功能

addFormatters FormatterRegistry 格式化器：支持属性上@NumberFormat和@DatetimeFormat的数据类型转换
GenericConversionService  
getValidator 无 数据校验：校验 Controller 上使用@Valid标注的参数合法性。需要导入starter-validator 无
addInterceptors InterceptorRegistry 拦截器：拦截收到的所有请求 无
configureContentNegotiation ContentNegotiationConfigurer 内容协商：支持多种数据格式返回。需要配合支持这种类型的HttpMessageConverter
支持 json
configureMessageConverters List<HttpMessageConverter<?>>    消息转换器：标注@ResponseBody的返回值会利用MessageConverter直接写出去
8 个，支持byte，string,multipart,resource，json
addViewControllers ViewControllerRegistry 视图映射：直接将请求路径与物理视图映射。用于无 java 业务逻辑的直接视图页渲染 无
<mvc:view-controller>
configureViewResolvers ViewResolverRegistry 视图解析器：逻辑视图转为物理视图 ViewResolverComposite
addResourceHandlers ResourceHandlerRegistry 静态资源处理：静态资源路径映射、缓存控制 ResourceHandlerRegistry
configureDefaultServletHandling DefaultServletHandlerConfigurer 默认 Servlet：可以覆盖 Tomcat
的DefaultServlet。让DispatcherServlet拦截/ 无
configurePathMatch PathMatchConfigurer 路径匹配：自定义 URL 路径匹配。可以自动为所有路径加上指定前缀，比如 /api 无
configureAsyncSupport AsyncSupportConfigurer 异步支持： TaskExecutionAutoConfiguration
addCorsMappings CorsRegistry 跨域： 无
addArgumentResolvers List<HandlerMethodArgumentResolver>    参数解析器： mvc 默认提供
addReturnValueHandlers List<HandlerMethodReturnValueHandler>    返回值解析器： mvc 默认提供
configureHandlerExceptionResolvers List<HandlerExceptionResolver>    异常处理器： 默认 3 个
ExceptionHandlerExceptionResolver
ResponseStatusExceptionResolver
DefaultHandlerExceptionResolver
getMessageCodesResolver 无 消息码解析器：国际化使用 无

# 最佳实践

> SpringBoot 已经默认配置好了Web开发场景常用功能。我们直接使用即可。

## 三种方式

| 方式   | 用法                                                         |                    | 效果                          |
|------|------------------------------------------------------------|--------------------|-----------------------------|
| 全自动  | 直接编写控制器逻辑                                                  |                    | 全部使用自动配置默认效果                |
| 手自一体 | @Configuration +配置WebMvcConfigurer+ 配置 WebMvcRegistrations | 不要标注 @EnableWebMvc | 保留自动配置效果 手动设置部分功能 定义MVC底层组件 |
| 全手动  | @Configuration +      配置WebMvcConfigurer                   | 标注 @EnableWebMvc   | 禁用自动配置效果 全手动设置              |

总结： 给容器中写一个配置类@Configuration实现 WebMvcConfigurer但是不要标注 @EnableWebMvc注解，实现手自一体的效果。

## 两种模式
1. 前后分离模式： @RestController 响应JSON数据 
2. 前后不分离模式：@Controller + Thymeleaf模板引擎

# Web新特性

##  ProblemDetails
RFC 7807: https://www.rfc-editor.org/rfc/rfc7807
错误信息返回新格式

### 原理
```java
@Configuration(proxyBeanMethods = false)
//配置过一个属性 spring.mvc.problemdetails.enabled=true
@ConditionalOnProperty(prefix = "spring.mvc.problemdetails", name = "enabled", havingValue = "true")
static class ProblemDetailsErrorHandlingConfiguration {

    @Bean
    @ConditionalOnMissingBean(ResponseEntityExceptionHandler.class)
    ProblemDetailsExceptionHandler problemDetailsExceptionHandler() {
        return new ProblemDetailsExceptionHandler();
    }

}
```

1. ProblemDetailsExceptionHandler 是一个 @ControllerAdvice集中处理系统异常
2. 处理以下异常。如果系统出现以下异常，会被SpringBoot支持以 RFC 7807规范方式返回错误数据

```java
	@ExceptionHandler({
			HttpRequestMethodNotSupportedException.class, //请求方式不支持
			HttpMediaTypeNotSupportedException.class,
			HttpMediaTypeNotAcceptableException.class,
			MissingPathVariableException.class,
			MissingServletRequestParameterException.class,
			MissingServletRequestPartException.class,
			ServletRequestBindingException.class,
			MethodArgumentNotValidException.class,
			NoHandlerFoundException.class,
			AsyncRequestTimeoutException.class,
			ErrorResponseException.class,
			ConversionNotSupportedException.class,
			TypeMismatchException.class,
			HttpMessageNotReadableException.class,
			HttpMessageNotWritableException.class,
			BindException.class
		})
```
默认响应错误的json。状态码 405
```json
{
    "timestamp": "2023-04-18T11:13:05.515+00:00",
    "status": 405,
    "error": "Method Not Allowed",
    "trace": "org.springframework.web.HttpRequestMethodNotSupportedException: Request method 'POST' is not supported\r\n\tat org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping.handleNoMatch(RequestMappingInfoHandlerMapping.java:265)\r\n\tat org.springframework.web.servlet.handler.AbstractHandlerMethodMapping.lookupHandlerMethod(AbstractHandlerMethodMapping.java:441)\r\n\tat org.springframework.web.servlet.handler.AbstractHandlerMethodMapping.getHandlerInternal(AbstractHandlerMethodMapping.java:382)\r\n\tat org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping.getHandlerInternal(RequestMappingInfoHandlerMapping.java:126)\r\n\tat org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping.getHandlerInternal(RequestMappingInfoHandlerMapping.java:68)\r\n\tat org.springframework.web.servlet.handler.AbstractHandlerMapping.getHandler(AbstractHandlerMapping.java:505)\r\n\tat org.springframework.web.servlet.DispatcherServlet.getHandler(DispatcherServlet.java:1275)\r\n\tat org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1057)\r\n\tat org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:974)\r\n\tat org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1011)\r\n\tat org.springframework.web.servlet.FrameworkServlet.doPost(FrameworkServlet.java:914)\r\n\tat jakarta.servlet.http.HttpServlet.service(HttpServlet.java:563)\r\n\tat org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:885)\r\n\tat jakarta.servlet.http.HttpServlet.service(HttpServlet.java:631)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:205)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149)\r\n\tat org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:53)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:174)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149)\r\n\tat org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100)\r\n\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:174)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149)\r\n\tat org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93)\r\n\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:174)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149)\r\n\tat org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201)\r\n\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:174)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149)\r\n\tat org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:166)\r\n\tat org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:90)\r\n\tat org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:493)\r\n\tat org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:115)\r\n\tat org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:93)\r\n\tat org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:74)\r\n\tat org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:341)\r\n\tat org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:390)\r\n\tat org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:63)\r\n\tat org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:894)\r\n\tat org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1741)\r\n\tat org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:52)\r\n\tat org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1191)\r\n\tat org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:659)\r\n\tat org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)\r\n\tat java.base/java.lang.Thread.run(Thread.java:833)\r\n",
    "message": "Method 'POST' is not supported.",
    "path": "/list"
}
```
开启ProblemDetails返回, 使用新的MediaType
Content-Type: application/problem+json+ 额外扩展返回

```json
{
    "type": "about:blank",
    "title": "Method Not Allowed",
    "status": 405,
    "detail": "Method 'POST' is not supported.",
    "instance": "/list"
}
```
## 函数式Web
> SpringMVC 5.2 以后 允许我们使用函数式的方式，定义Web的请求处理流程。 
> 函数式接口 
> Web请求处理的方式：
> 1. @Controller + @RequestMapping：耦合式 （路由、业务耦合）
> 2. 函数式Web：分离式（路由、业务分离）


### 场景 RestFul CRUD
### 核心类
- RouterFunction
- RequestPredicate
- ServerRequest
- ServerResponse
### 示例

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.RequestPredicate;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.web.servlet.function.RequestPredicates.accept;
import static org.springframework.web.servlet.function.RouterFunctions.route;

@Configuration(proxyBeanMethods = false)
public class MyRoutingConfiguration {

    private static final RequestPredicate ACCEPT_JSON = accept(MediaType.APPLICATION_JSON);

    @Bean
    public RouterFunction<ServerResponse> routerFunction(MyUserHandler userHandler) {
        return route()
                .GET("/{user}", ACCEPT_JSON, userHandler::getUser)
                .GET("/{user}/customers", ACCEPT_JSON, userHandler::getUserCustomers)
                .DELETE("/{user}", ACCEPT_JSON, userHandler::deleteUser)
                .build();
    }

}


```
```java
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

@Component
public class MyUserHandler {

    public ServerResponse getUser(ServerRequest request) {
        //body携带参数返回
        return ServerResponse.ok().body().build();
    }

    public ServerResponse getUserCustomers(ServerRequest request) {
        return ServerResponse.ok().build();
    }

    public ServerResponse deleteUser(ServerRequest request) {
        return ServerResponse.ok().build();
    }

}
```