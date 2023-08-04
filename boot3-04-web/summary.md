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
###  Favicon
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
  spring.web
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
                .addResourceLocations("classpath:/a/","classpath:/b/")
                .setCacheControl(CacheControl.maxAge(1180, TimeUnit.SECONDS));
    }
}

@Configuration //这是一个配置类,给容器中放一个 WebMvcConfigurer 组件，就能自定义底层
public class MyConfig  /*implements WebMvcConfigurer*/ {


   @Bean
   public WebMvcConfigurer webMvcConfigurer(){
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
   Spring5.3 之后加入了更多的请求路径匹配的实现策略；以前只支持 AntPathMatcher 策略, 现在提供了 PathPatternParser 策略。并且可以让我们指定到底使用那种策略。
   
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
####  模式切换

> AntPathMatcher 与 PathPatternParser 
> PathPatternParser 在 jmh 基准测试下，有 6~8 倍吞吐量提升，降低 30%~40%空间分配率 
> PathPatternParser 兼容 AntPathMatcher语法，并支持更多类型的路径模式 
> PathPatternParser  "**" 多段匹配的支持仅允许在模式末尾使用

```java
    @GetMapping("/a*/b?/{p1:[a-f]+}")
    public String hello(HttpServletRequest request, 
                        @PathVariable("p1") String path) {

        log.info("路径变量p1： {}", path);
        //获取请求路径
        String uri = request.getRequestURI();
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