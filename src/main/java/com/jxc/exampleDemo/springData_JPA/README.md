#依赖
~~~
 <dependency>
            <groupId>org.springframework.data</groupId>
            <artifactId>spring-data-jpa</artifactId>
            <version>2.1.6.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>repo.org.hibernate</groupId>
            <artifactId>hibernate-core</artifactId>
            <version>5.2.5.Final</version>
        </dependency>
        <dependency>
            <groupId>org.hibernate.javax.persistence</groupId>
            <artifactId>hibernate-jpa-2.1-api</artifactId>
            <version>1.0.0.Final</version>
        </dependency>
~~~
##
* repository 是一个空接口，即 是一个标记接口
* 若我们定义的接口继承了 Repository，则该接口会被 IOC 容器识别为一个 Repository Bean 加入到ioc容器中，进而可以在该接口中定义满足一定规范的方法
* 也以通过 @RepositoryDefinition 代替继承 Repository 接口

# 查询方法
* 必须以 find | get | read 开头
* 涉及条件查询时，条件的属性用条件关键字链接
* 条件属性以首字母大写
* 支持属性的级联查询，若当前类有符合条件的属性，则优先使用，而不使用级联属性，如需要使用级联属性，则属性之间用_分割

