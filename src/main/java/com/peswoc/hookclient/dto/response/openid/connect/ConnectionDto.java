package com.peswoc.hookclient.dto.response.openid.connect;

import com.peswoc.hookclient.constant.ConnectionStatus;
import com.peswoc.hookclient.model.openid.AcceptedConnection;
import com.peswoc.hookclient.model.openid.BaseConnection;
import com.peswoc.hookclient.model.openid.PendingConnection;
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
  private String domain;
  private String callbackUrl;
  private String targetDomain;
  private String targetId;
  private String clientId;
  private String clientSecret;
  private Long createdAt;
  private String status;

  public ConnectionDto(BaseConnection connection) {
    this.id = connection.getId();
    this.name = connection.getName();
    this.domain = connection.getDomain();
    this.callbackUrl = connection.getCallbackUrl();
    this.targetDomain = connection.getTargetDomain();
    this.targetId = connection.getTargetId();
    this.createdAt = connection.getCreatedAt();

    if (connection instanceof AcceptedConnection accepted) {
      this.clientId = accepted.getClientId();
      this.clientSecret = accepted.getClientSecret();
      this.status = ConnectionStatus.ACCEPTED.toString();
    }

    if (connection instanceof PendingConnection pending) {
      switch (pending.getState()) {
        case ACTIVE -> {
          this.status = ConnectionStatus.PENDING.toString();
        }
        case INACTIVE -> {
          this.status = ConnectionStatus.REJECTED.toString();
        }
      }
    }
  }
}

