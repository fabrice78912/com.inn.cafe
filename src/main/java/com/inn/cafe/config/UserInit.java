package com.inn.cafe.config;

import com.inn.cafe.POJO.User;
import com.inn.cafe.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class UserInit {


    @Autowired
    private UserDao userDao;

    @PostConstruct
    private void postConstruct() {

        String email = "admin@gmail.com";
        User user = userDao.findByEmail(email);
        if (user == null) {
            userDao.save(User.builder()
                    .email(email)
                    .role("admin")
                    .password("admin")
                    .status("true")
                    .build());
        }


    }
}
