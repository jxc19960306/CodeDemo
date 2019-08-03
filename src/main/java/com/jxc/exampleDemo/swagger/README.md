# Swagger 接口测试 生成文档 工具
## 依赖
~~~
<dependency>
	<groupId>io.springfox</groupId>
	<artifactId>springfox-swagger2</artifactId>
	<version>2.6.1</version>
</dependency>

<dependency>
	<groupId>io.springfox</groupId>
	<artifactId>springfox-swagger-ui</artifactId>
	<version>2.6.1</version>
</dependency>
~~~

## 页面国际化
将 META-INF 下的所有文件 拷贝到 swagger 模块中 
~~~
  <script src='webjars/springfox-swagger-ui/lang/translator.js' type='text/javascript'></script>
    <script src='webjars/springfox-swagger-ui/lang/zh-cn.js' type='text/javascript'></script>
~~~

## 注意
springboot 主程序上需要加 @EnableSwagger2 注解

### Controller
~~~
@ApiOperation(value="获取用户详细信息", notes="根据url的id来获取用户详细信息")
@ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Integer", paramType = "path")
~~~

### entity
