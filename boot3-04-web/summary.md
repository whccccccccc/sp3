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