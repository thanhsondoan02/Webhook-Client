package com.peswoc.hookclient.service;

import com.peswoc.hookclient.constant.ConnectionStatus;
import com.peswoc.hookclient.dto.response.openid.connect.ConnectionDto;
import com.peswoc.hookclient.dto.response.openid.connect.ConnectionListResponseDto;
import com.peswoc.hookclient.model.openid.AcceptedConnection;
import com.peswoc.hookclient.model.openid.PendingConnection;

import java.util.Optional;

public interface IOpenIdService {
  ConnectionDto savePendingConnection(PendingConnection connection);

  boolean isConnectionExistAndPending(String id);

  AcceptedConnection getAcceptedConnection(String id);

  void rejectConnection(String id);

  ConnectionDto acceptConnection(String id, String clientId, String clientSecret);

  ConnectionListResponseDto getConnections(ConnectionStatus status);
}
