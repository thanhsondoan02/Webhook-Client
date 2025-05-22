package com.peswoc.hookclient.util;

import com.peswoc.hookclient.constant.MessageConst;
import com.peswoc.hookclient.dto.response.base.BaseResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseBuilder {
  public static <T> ResponseEntity<BaseResponseDto<T>> success() {
    return success(null);
  }

  public static <T> ResponseEntity<BaseResponseDto<T>> success(T data) {
    return build(HttpStatus.OK.value(), MessageConst.SUCCESS, data);
  }

  public static <T> ResponseEntity<BaseResponseDto<T>> error(int code, String message) {
    return build(code, message, null);
  }

  public static <T> ResponseEntity<BaseResponseDto<T>> internalServerError() {
    return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), MessageConst.INTERNAL_SERVER_ERROR);
  }

  public static <T> ResponseEntity<BaseResponseDto<T>> build(int code, String message, T data) {
    return ResponseEntity
      .status(code)
      .body(new BaseResponseDto<>(code, message, data));
  }
}
