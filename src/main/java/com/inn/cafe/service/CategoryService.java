package com.inn.cafe.service;

import com.inn.cafe.POJO.Category;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface CategoryService {

    ResponseEntity<String> addNewCategory(Map<String , String> requestMap);

    ResponseEntity<List<Category>> getAllCategory(String filter);

    ResponseEntity<String> updateCategory(Map<String, String> requestMap);

    Page<Category> getCategory(String orElse, Integer orElse1, Integer orElse2);
}
