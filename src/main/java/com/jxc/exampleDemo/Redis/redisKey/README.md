# redis key 过期监控

## 步骤

1. 修改redis服务器配置文件

   ```properties
   notify-keyspace-events "Ex"
   # 注意 引号 不能省略
   ```

2. 订阅 key 过期事件信息

   ```properties
   subscribe __keyevent@0__:expired 
   # 注意下划线是两个连在一起的
   ```

# 依赖
## jedis_key过期监控
~~~
 <dependency>
            <groupId>redis.clients</groupId>
            <artifactId>jedis</artifactId>
            <version>2.9.0</version>
 </dependency>
~~~

## springData_Redis过期监控
~~~
 <dependency>
            <groupId>redis.clients</groupId>
            <artifactId>jedis</artifactId>
            <version>2.9.0</version>
 </dependency>
  <dependency>
             <groupId>org.springframework.data</groupId>
             <artifactId>spring-data-redis</artifactId>
             <version>2.1.6.RELEASE</version>
  </dependency>
~~~