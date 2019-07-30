# SSO-JWT 单点登录
## 依赖
~~~
 <dependency>
     <groupId>io.jsonwebtoken</groupId>
     <artifactId>jjwt</artifactId>
     <version>0.9.0</version>
 </dependency>
~~~

## JwtUtil
    JWT 加密解密工具类
## CookieUtil
    cookie 工具类
## AuthInterceptor
    自定义的 springMVC JWT拦截器
## WebMvcConfiguration
    springboot 拦截器配置类
## LoginRequire
    自定义权限注解
