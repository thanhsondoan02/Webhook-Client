package com.peswoc.hookclient.exception;

import com.peswoc.hookclient.util.ResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleGlobalException(Exception e) {
//    return ResponseBuilder.internalServerError();
    return ResponseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
  }
}
