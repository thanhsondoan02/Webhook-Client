package com.peswoc.hookclient.dto.request.openid.connect;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConnectRequestToServerDto {
  private String name;
  private String id;
  private String domain;
  private String callbackUrl;
}
