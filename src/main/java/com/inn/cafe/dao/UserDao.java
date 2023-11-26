package com.inn.cafe.dao;

import com.inn.cafe.POJO.User;
import com.inn.cafe.wrapper.UserWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface UserDao extends JpaRepository<User, Integer> {

//    User findByEmailId(@Param("email") String email);

    User findByEmail(String email);

    List<User> findByRole(String role);

    List<UserWrapper> getAllUser();

    @Query("select u.email from User u where UPPER(u.role) = UPPER (:role )")
    List<String> getAllAdmin(@Param("role") String role);

    @Modifying
    @Transactional
    @Query("update User u set u.status= :status where u.id= :id")
    Integer updateStatus(@Param("status") String status, @Param("id") Integer id);
}
