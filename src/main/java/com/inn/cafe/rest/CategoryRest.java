package com.inn.cafe.rest;


import com.inn.cafe.POJO.Category;
import com.inn.cafe.dto.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequestMapping(path = "/category")
public interface CategoryRest {

    @PostMapping(path = "/add")
    public ResponseEntity<String> addNewCategory(@RequestBody(required = true) Map<String, String> requestMap);

    @GetMapping(path = "/get")
    public ResponseEntity<List<Category>> getAllCategory(@RequestParam(required = false)  String filter);

    @PostMapping(path = "/update")
    public ResponseEntity<String> updateCategory(@RequestBody(required = true) Map<String, String> requestMap);


    @GetMapping(path = "/getCategory")
    public ResponseEntity<Response> getCategory(@RequestParam Optional<String> name,
                                                @RequestParam Optional<Integer> page,
                                                @RequestParam Optional<Integer> size) ;



}
