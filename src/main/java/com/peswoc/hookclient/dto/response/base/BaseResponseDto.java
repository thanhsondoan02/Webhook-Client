package com.peswoc.hookclient.dto.response.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.peswoc.hookclient.dto.response.openid.connect.ConnectionDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.core.ParameterizedTypeReference;

@Getter
@AllArgsConstructor
public class BaseResponseDto<T> {
  private int status;
  private String message;
  private T data;

  @JsonIgnore
  public boolean isSuccess() {
    return status == 200;
  }
}
