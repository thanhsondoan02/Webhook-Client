package com.peswoc.hookclient.service;

import com.peswoc.hookclient.constant.ConnectionStatus;
import com.peswoc.hookclient.dto.response.openid.connect.ConnectionDto;
import com.peswoc.hookclient.dto.response.openid.connect.ConnectionListResponseDto;
import com.peswoc.hookclient.model.openid.Connection;

public interface IOpenIdService {
  ConnectionDto savePendingConnection(Connection connection);

  boolean isConnectionExistAndPending(String id);

  Connection getAcceptedConnection(String id);

  void rejectConnection(String id);

  ConnectionDto acceptConnection(String id, String clientId, String clientSecret);

  ConnectionListResponseDto getFilteredConnections(ConnectionStatus status);

  ConnectionListResponseDto getAllConnections();
}
