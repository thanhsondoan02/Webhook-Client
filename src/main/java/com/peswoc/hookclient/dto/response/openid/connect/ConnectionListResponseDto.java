package com.peswoc.hookclient.dto.response.openid.connect;

import com.peswoc.hookclient.model.openid.BaseConnection;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ConnectionListResponseDto {
  private List<ConnectionDto> connections;

  public ConnectionListResponseDto(List<BaseConnection> connections) {
    this.connections = connections.stream()
      .map(ConnectionDto::new)
      .toList();
  }
}

