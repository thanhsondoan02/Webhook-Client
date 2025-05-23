package com.peswoc.hookclient.dto.request.openid.connect;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ConnectRequestDto {
  private String name;
  private String domain;
  private String targetDomain;
}
