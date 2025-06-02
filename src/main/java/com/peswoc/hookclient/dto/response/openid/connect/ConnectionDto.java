package com.peswoc.hookclient.dto.response.openid.connect;

import com.peswoc.hookclient.model.openid.Connection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionDto {
  private String id;
  private String name;
  private String callbackUrl;
  private String targetDomain;
  private String targetId;
  private String clientId;
  private String clientSecret;
  private Long createdAt;
  private String status;

  public ConnectionDto(Connection connection) {
    this.id = connection.getId();
    this.name = connection.getTargetServer().getName();
    this.targetDomain = connection.getTargetServer().getDomain();
    this.callbackUrl = connection.getCallbackUrl();
    this.createdAt = connection.getCreatedAt();
    this.status = connection.getStatus().toString();
    this.clientId = connection.getClientId();
    this.clientSecret = connection.getClientSecret();
  }
}

