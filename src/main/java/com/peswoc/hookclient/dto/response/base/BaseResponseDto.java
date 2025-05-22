package com.peswoc.hookclient.dto.response.base;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BaseResponseDto<T> {
  private int status;
  private String message;
  private T data;
}
