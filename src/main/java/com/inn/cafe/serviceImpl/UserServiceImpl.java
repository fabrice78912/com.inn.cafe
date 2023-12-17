package com.inn.cafe.serviceImpl;

import com.google.common.base.Strings;
import com.inn.cafe.JWT.CustomerUsersDetailsService;
import com.inn.cafe.JWT.JWTUtils;
import com.inn.cafe.JWT.JwtFilter;
import com.inn.cafe.POJO.User;
import com.inn.cafe.constents.CafeConstants;
import com.inn.cafe.dao.UserDao;
import com.inn.cafe.mapper.UserMapper;
import com.inn.cafe.service.UserService;
import com.inn.cafe.utils.CafeUtils;
import com.inn.cafe.utils.EmailUtils;
import com.inn.cafe.wrapper.UserWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserDao userDao;

    private final AuthenticationManager authenticationManager;

    private final CustomerUsersDetailsService customerUsersDetailsService;

    private final JWTUtils jwtUtils;

    private final JwtFilter jwtFilter;

    private final EmailUtils emailUtils;

    private final UserMapper userMapper;


    @Override
    public ResponseEntity<String> singUp(Map<String, String> requestMap) {

        log.info("Inside singUp {}", requestMap);

        try {
            if (validateSingUpMap(requestMap)) {
                User user = userDao.findByEmail(requestMap.get("email"));
                if (Objects.isNull(user)) {
                    userDao.save(getUserFromMap(requestMap));
                    return CafeUtils.getResponseEntity("Successfully registered", HttpStatus.OK);
                } else {
                    return CafeUtils.getResponseEntity("Email already exist. ", HttpStatus.CONFLICT);
                }
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateSingUpMap(Map<String, String> requestMap) {
        if (requestMap.containsKey("name") && !requestMap.get("name").isBlank() &&
                requestMap.containsKey("contactNumber") && !requestMap.get("contactNumber").isBlank()
                && requestMap.containsKey("email") && !requestMap.get("email").isBlank() &&
                requestMap.containsKey("password") && !requestMap.get("password").isBlank()) {
            return true;
        }
        return false;
    }

    private User getUserFromMap(Map<String, String> requestMap) {
        User user = new User();
        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus("false");
        user.setRole("user");
        return user;
    }


    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Inside login");

        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email")
                            , requestMap.get("password")));

            log.info("Authenticated {}", auth);
            if (auth.isAuthenticated()) {
                if (customerUsersDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")) {
                    return new ResponseEntity<String>("{\"token\":\"" +
                            jwtUtils.generatedToken(customerUsersDetailsService.getUserDetail().getEmail(),
                                    customerUsersDetailsService.getUserDetail().getRole()) + "\"}",
                            HttpStatus.OK);
                } else {
                    return new ResponseEntity<String>("{\"message\":\"" + "Wait for admin  approval. " + "\"}", HttpStatus.BAD_REQUEST);
                }
            }

        } catch (Exception ex) {
            log.error("{}", ex);
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try {
            if (jwtFilter.isAdmin()) {
                return new ResponseEntity<>(userMapper.listEntityToListDto(userDao.findByRole("user")), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                Optional<User> optional = userDao.findById(Integer.parseInt(requestMap.get("id")));
                if (!optional.isEmpty()) {
                    userDao.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
                    sendEmailToAllAdmin(requestMap.get("status"), optional.get().getEmail(), userDao.getAllAdmin("admin"));
                    return CafeUtils.getResponseEntity("user updated successfully", HttpStatus.OK);
                } else {
                    return CafeUtils.getResponseEntity("User Id doesn't exist ", HttpStatus.OK);
                }
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private void sendEmailToAllAdmin(String status, String user, List<String> allAdmin) {
        allAdmin.remove(jwtFilter.getCurrentUser());
        if (status != null && status.equalsIgnoreCase("true")) {
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Account Approved", "USER: - " + user + " \n is approved by \nADMIN: - " + jwtFilter.getCurrentUser(), allAdmin);
        } else {
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Account disabled", "USER: - " + user + " \n is disabled by \nADMIN: - " + jwtFilter.getCurrentUser(), allAdmin);
        }
    }

    @Override
    public ResponseEntity<String> checkToken() {
        return CafeUtils.getResponseEntity("true", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
            //find user by username
            User user = userDao.findByEmail(jwtFilter.getCurrentUser());
            if (!user.equals(null)) {
                if (user.getPassword().equals(requestMap.get("oldPassword"))) {
                    user.setPassword(requestMap.get("newPassword"));
                    userDao.save(user);
                    return CafeUtils.getResponseEntity("Password updated successfully", HttpStatus.OK);
                }
                return CafeUtils.getResponseEntity("Incorrect old password", HttpStatus.BAD_REQUEST);
            }
            return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try {
            User user = userDao.findByEmail(requestMap.get("email"));
            if (!Objects.isNull(user) && !Strings.isNullOrEmpty(user.getEmail())) {
                emailUtils.forgotMail(user.getEmail(), "Credentials by cafe Management", user.getPassword());
            }
            else if (Objects.isNull(user)){
            return CafeUtils.getResponseEntity("Check your mail for credentials.", HttpStatus.OK);}
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
