package com.jxc.exampleDemo.springData_JPA;

import org.springframework.data.repository.Repository;

import java.util.List;


/**
 * 可以通过 @RepositoryDefinition 代替继承 Repository 接口
 */
//@RepositoryDefinition(domainClass = User.class, idClass = Long.class)
public interface UserRepository extends Repository<User, Long> {

    /** select * from user where userName = ? */
    User getByUserName(String username);

    /** select * from user where studentId = ? */
    User getByStudentId(Long id);

    /** select * from user where id < ? */
    List<User> getByIdLessThan(Long id);

    /** select * from user where id > ? */
    List<User> getByIdGreaterThan(Long id);

    /** select * from user where userName like ?% */
    List<User> getByUserNameStartingWith(String userName);

    /** select * from user where userName like %? */
    List<User> getByUserNameEndingWith(String userName);

    /** select * from user where id in (?, ?, ?)*/
    List<User> getByIdIn(List<Long> ids);

    List<User> getById_StudentId(Long id);
}
