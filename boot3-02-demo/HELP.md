# maven 依赖管理机制
- 依赖传递原则
- 就近原则

# 自动配置原理

## 以前
```
DispatchServlet ViewResolver CharacterEncodingFilter
```
## 现在 

```
自动配置原理

```
## 默认的包扫描规则
- 扫描启动主程序所在包及其子包

## 默认配置
- 自动加载的配置类 与那些配置类的属性一一对应



# 常用注解
## 组件注册
- Configuration
- Bean
- Scope
- SpringBootConfiguration
- Controller
- Service
- Repository
- Component
- Import
- ComponentScan
## 条件注解
- @ConditionalOnClass：如果类路径中存在这个类，则触发指定行为 
- @ConditionalOnMissingClass：如果类路径中不存在这个类，则触发指定行为 
- @ConditionalOnBean：如果容器中存在这个Bean（组件），则触发指定行为 
- @ConditionalOnMissingBean：如果容器中不存在这个Bean（组件），则触发指定行为
## 属性绑定
- ConfigurationProperties
- EnableConfigurationProperties

# 配置文件
## 细节
- birthDay 推荐写为 birth-day 
- 文本： 单引号不会转义【\n 则为普通字符串显示】 双引号会转义【\n会显示为换行符】 
- 大文本 |开头，大文本写在下层，保留文本格式，换行符正确显示 >开头，大文本写在下层，折叠换行符 
- 多文档合并  使用---可以把多个yaml文档合并在一个文档中，每个文档区依然认为内容独立

# 自动配置原理

## 流程
- 导入场景启动器 web
 - 场景启动器都引入了 spring-boot-starter核心场景启动器
 - 核心场景启动器引入了 spring-boot-autoconfigure包
 - spring-boot-autoconfigure 里面囊括了所有场景的所有配置 
 - 只要这个包下的所有类都能生效，那么相当于SpringBoot官方写好的整合功能就生效了。 
 - SpringBoot默认却扫描不到 spring-boot-autoconfigure下写好的所有配置类。（这些配置类给我们做了整合操作），默认只扫描主程序所在的包。
- 主程序
 - @SpringBootApplication由三个注解组成 @SpringBootConfiguration @EnableAutoConfiguration @ComponentScan
 - @EnableAutoConfiguration：SpringBoot 开启自动配置的核心。
    1. 是由@Import(AutoConfigurationImportSelector.class)提供功能：批量给容器中导入组件。
    2.  SpringBoot启动会默认加载 142个配置类。
    3. 这142个配置类来自于spring-boot-autoconfigure下 META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports文件指定的
    4.    每一个自动配置类，都有条件注解@ConditionalOnxxx，只有条件成立，才能生效
- 写业务

# yaml 配置我呢见
## 基本语法
   ● 大小写敏感
   ● 使用缩进表示层级关系，k: v，使用空格分割k,v
   ● 缩进时不允许使用Tab键，只允许使用空格。换行
   ● 缩进的空格数目不重要，只要相同层级的元素左侧对齐即可
   ● # 表示注释，从这个字符一直到行尾，都会被解析器忽略。
### 支持的写法：
● 对象：键值对的集合，如：映射（map）/ 哈希（hash） / 字典（dictionary）
● 数组：一组按次序排列的值，如：序列（sequence） / 列表（list）
● 纯量：单个的、不可再分的值，如：字符串、数字、bool、日期 

## 细节
● birthDay 推荐写为 birth-day
● 文本：
○ 单引号不会转义【\n 则为普通字符串显示】
○ 双引号会转义【\n会显示为换行符】
● 大文本
○ |开头，大文本写在下层，保留文本格式，换行符正确显示
○ >开头，大文本写在下层，折叠换行符
● 多文档合并
○ 使用---可以把多个yaml文档合并在一个文档中，每个文档区依然认为内容独立



# 日志配置

