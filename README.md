# Notes
* <font size=14>`spring security`</font>项目demo移步[https://github.com/dk980241/spring-boot-security-demo](https://github.com/dk980241/spring-boot-security-demo)


----

> 文档很久木有更新了。。。。

> 项目集成了很多框架的配置，有些框架存在冲突，会导致项目无法运行。

## dependency
* druid
* mybatis
* pagehelper
* redis
* shiro
* log4j
* mybatis-generator
* fastjson
* lombok

## init
* [shiro初始化sql](/assert/sql/shiro.sql)

## tip
* [redis模版配置](/src/main/java/site/yuyanjia/template/common/config/RedisConfig.java)
* [mybatis redis 二级缓存](/src/main/java/site/yuyanjia/template/common/config/MybatisRedisCache.java)
* [自定义shiro filter，前后端分离权限校验](/src/main/java/site/yuyanjia/template/common/config/ShiroConfig.java)
* [项目启动后执行](/src/main/java/site/yuyanjia/template/common/config/SpringApplicationRunner.java)
* [tomcat 启用apr模式，性能优化](/src/main/java/site/yuyanjia/template/common/config/TomcatConfig.java)
* [跨域过滤器](/src/main/java/site/yuyanjia/template/common/config/CorsFilterRegistrationConfig.java)
* [消息转换器，编码，json](/src/main/java/site/yuyanjia/template/common/config/DefinedWebMvcConfigurer.java)
* [配置文件读取](/src/main/java/site/yuyanjia/template/common/config/ConfigProperties.java)

## tool
* [base64转图片](/src/main/java/site/yuyanjia/template/common/util/ImageUtil.java)

 