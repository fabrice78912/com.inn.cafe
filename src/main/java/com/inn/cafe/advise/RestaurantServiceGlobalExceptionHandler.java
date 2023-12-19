package com.inn.cafe.advise;


import com.inn.cafe.dto.CustomErrorResponse;
import com.inn.cafe.dto.GlobalErrorCode;
import com.inn.cafe.exception.InternalServerErrorException;
import com.inn.cafe.exception.OrderNotFoundException;
import com.inn.cafe.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
//@ComponentScan(basePackages="org.example.common.advice")
@Slf4j
public class RestaurantServiceGlobalExceptionHandler {

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<?> handleOrderNotFoundException(OrderNotFoundException ex){
        CustomErrorResponse errorResponse= CustomErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .errorCode(GlobalErrorCode.ERROR_ORDER_NOT_FOUND)
                .errorMessage(ex.getMessage())
                .build()  ;
        log.error("RestaurantServiceGlobalExceptionHandler::handleOrderNotFoundException exception caught {}",ex.getMessage());
        return ResponseEntity.internalServerError().body(errorResponse);
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleOrderNotFoundException(UserNotFoundException ex){
        CustomErrorResponse errorResponse= CustomErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND)
                .errorMessage(ex.getMessage())
                .build()  ;
        log.error("RestaurantServiceGlobalExceptionHandler::handleOrderNotFoundException exception caught {}",ex.getMessage());
        return ResponseEntity.ok(errorResponse);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<?> handleGenericException(Exception ex){
        CustomErrorResponse errorResponse= CustomErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .errorCode(GlobalErrorCode.GENERIC_ERROR)
                .errorMessage(ex.getMessage())
                .build()  ;
        log.error("RestaurantServiceGlobalExceptionHandler::handleGenericException exception caught {}",ex.getMessage());
        return ResponseEntity.internalServerError().body(errorResponse);
    }
}
