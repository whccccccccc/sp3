整合总结:
> 1. 导入 mybatis-spring-boot-starter
> 2. 配置数据源信息
> 3. 配置mybatis的mapper接口扫描与xml映射文件扫描
> 4. 编写bean，mapper，生成xml，编写sql 进行crud。事务等操作依然和Spring中用法一样
> 5. 效果：
     a. 所有sql写在xml中
     b. 所有mybatis配置写在application.properties下面
# 自动配置原理
- jdbc场景的自动配置： 
   - mybatis-spring-boot-starter导入 spring-boot-starter-jdbc，jdbc是操作数据库的场景 
   - Jdbc场景的几个自动配置 
     - org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration 
       - 数据源的自动配置 
       - 所有和数据源有关的配置都绑定在DataSourceProperties 
       - 默认使用 HikariDataSource
     - org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration 
       - 给容器中放了JdbcTemplate操作数据库
     - org.springframework.boot.autoconfigure.jdbc.JndiDataSourceAutoConfiguration
     - org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration 
       - 基于XA二阶提交协议的分布式事务数据源
     - org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration
       - 支持事务 
       - 具有的底层能力：数据源、JdbcTemplate、事务

- MyBatisAutoConfiguration：配置了MyBatis的整合流程 
  - mybatis-spring-boot-starter导入 mybatis-spring-boot-autoconfigure（mybatis的自动配置包）， 
  - 默认加载两个自动配置类：
    - org.mybatis.spring.boot.autoconfigure.MybatisLanguageDriverAutoConfiguration
    - org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration
      - 必须在数据源配置好之后才配置
      - 给容器中SqlSessionFactory组件。创建和数据库的一次会话
      - 给容器中SqlSessionTemplate组件。操作数据库
  - MyBatis的所有配置绑定在MybatisProperties
    - 每个Mapper接口的代理对象是怎么创建放到容器中。详见@MapperScan原理：
    - 利用@Import(MapperScannerRegistrar.class)批量给容器中注册组件。解析指定的包路径里面的每一个类，为每一个Mapper接口类，创建Bean定义信息，注册到容器中。

> 如何分析哪个场景导入以后，开启了哪些自动配置类。 
> 找：classpath:/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports文件中配置的所有值，
> 就是要开启的自动配置类，
> 但是每个类可能有条件注解，基于条件注解判断哪个自动配置类生效了。

# 快速定位生效的配置
```properties
#开启调试模式，详细打印开启了哪些自动配置
debug=true
# Positive（生效的自动配置）  Negative（不生效的自动配置）
```
