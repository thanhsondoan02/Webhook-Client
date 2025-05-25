package com.peswoc.hookclient.dto.request.openid.connect;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateConnectionRequestDto {
  private String action;
  private String clientId;
  private String clientSecret;
}
