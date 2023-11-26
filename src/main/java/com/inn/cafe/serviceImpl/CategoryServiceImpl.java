package com.inn.cafe.serviceImpl;

import com.inn.cafe.JWT.JwtFilter;
import com.inn.cafe.POJO.Category;
import com.inn.cafe.constents.CafeConstants;
import com.inn.cafe.dao.CategoryDao;
import com.inn.cafe.service.CategoryService;
import com.inn.cafe.utils.CafeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryDao categoryDao;


    @Autowired
    private JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
        log.info("Inside addNewCategory {}", requestMap);

        try {
            if (jwtFilter.isAdmin()) { // check if current user is Admin
                if (validateCategoryMap(requestMap, false)) {
                    Category category = categoryDao.findByName(requestMap.get("name"));
                    if (Objects.isNull(category)) {
                        categoryDao.save(getCategoryFromMap(requestMap, false));
                        return CafeUtils.getResponseEntity("Category added successfully!!", HttpStatus.OK);
                    } else {
                        return CafeUtils.getResponseEntity("Email already exist. ", HttpStatus.CONFLICT);
                    }
                }
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }


    private boolean validateCategoryMap(Map<String, String> requestMap, boolean validateId) {
        if (requestMap.containsKey("name") && !requestMap.get("name").isBlank()) {
            if (requestMap.containsKey("id") && validateId && !requestMap.get("id").isBlank()) {
                return true;
            } else if (!validateId) {
                return true;
            }
        }
        return false;
    }

    private Category getCategoryFromMap(Map<String, String> requestMap, Boolean isAdd) {
        Category category = new Category();
        if (isAdd) {
            category.setId(Integer.parseInt(requestMap.get("id")));
        }
        category.setName(requestMap.get("name"));
        return category;
    }


    @Override
    public ResponseEntity<List<Category>> getAllCategory(String filter) {
        try {

            if (filter != null) {
                if (!Strings.isEmpty(filter) && filter.equalsIgnoreCase("true")) {
                    return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
                } else return new ResponseEntity<>(categoryDao.findAllByNameOrderByNameAsc(filter), HttpStatus.OK);
            } else return new ResponseEntity<>(categoryDao.findAll(Sort.by(Sort.Direction.ASC, "name")), HttpStatus.OK);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<List<Category>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
        try {

            if (jwtFilter.isAdmin()) {
                if (validateCategoryMap(requestMap, true)) {
                    Optional optional = categoryDao.findById(Integer.parseInt(requestMap.get("id")));
                    if (!optional.isEmpty()) {
                        Category category = categoryDao.findByName(requestMap.get("name"));
                        if (Objects.isNull(category)) {
                            categoryDao.save(getCategoryFromMap(requestMap, false));
                            return CafeUtils.getResponseEntity("Category updated successfully !!", HttpStatus.OK);
                        } else {
                            return CafeUtils.getResponseEntity("Email already exist. ", HttpStatus.CONFLICT);
                        }
                    } else {
                        return CafeUtils.getResponseEntity("Category id " + requestMap.get("id") + " doesn't exist", HttpStatus.NOT_FOUND);
                    }
                }
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
