package com.inn.cafe.restImpl;

import com.inn.cafe.POJO.Category;
import com.inn.cafe.constents.CafeConstants;
import com.inn.cafe.dto.Response;
import com.inn.cafe.rest.CategoryRest;
import com.inn.cafe.service.CategoryService;
import com.inn.cafe.utils.CafeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.OK;


@RestController
public class CategoryRestImpl implements CategoryRest {

    @Autowired
    private CategoryService categoryService;


    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
        try {
            return categoryService.addNewCategory(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<List<Category>> getAllCategory(String filter) {
        try {
            return categoryService.getAllCategory(filter);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
        try {
            return categoryService.updateCategory(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<Response> getCategory(Optional<String> name, Optional<Integer> page, Optional<Integer> size) {
            // TimeUnit.SECONDS.sleep(3);
            //throw new RuntimeException("Forced exception for testing");
            //return new ResponseEntity<>(categoryService.getCategory(name.orElse(""), page.orElse(0), size.orElse(10) ), OK) ;

        return ResponseEntity.ok().body(
                Response.builder()
                        .timeStamp(LocalDateTime.parse(now().toString()))
                        .data(of("page", categoryService.getCategory(name.orElse(""), page.orElse(0), size.orElse(10))))
                        .message("categories Retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }



}
