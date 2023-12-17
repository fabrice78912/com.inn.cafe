package com.inn.cafe.advise;

import lombok.extern.slf4j.Slf4j;
import org.example.common.advice.RestaurantServiceGlobalExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ServiceGlobalExceptionHandler extends RestaurantServiceGlobalExceptionHandler {

}
