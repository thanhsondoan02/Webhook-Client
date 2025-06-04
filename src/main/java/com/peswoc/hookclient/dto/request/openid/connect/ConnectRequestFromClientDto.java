package com.peswoc.hookclient.dto.request.openid.connect;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConnectRequestFromClientDto {
  private String name;
  private String domain;
}
