package com.peswoc.hookclient.service;

import com.peswoc.hookclient.constant.ConnectionAction;
import com.peswoc.hookclient.constant.ConnectionStatus;
import com.peswoc.hookclient.dto.request.openid.connect.ConnectRequestDto;
import com.peswoc.hookclient.dto.response.openid.connect.ConnectionDto;
import com.peswoc.hookclient.dto.response.openid.connect.ConnectionListResponseDto;

public interface IOpenIdService {
  ConnectionDto addPendingConnections(ConnectRequestDto request);
  boolean isConnectionExistAndPending(String id);
  ConnectionDto updateConnection(String id, ConnectionAction action);
  ConnectionListResponseDto getConnections(ConnectionStatus status);
}
