package com.peswoc.hookclient.dto.request.openid.connect;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConnectRequestDto {
  private String id;
  private String name;
  private String domain;
  private String callbackUrl;
  private String targetDomain;
}
