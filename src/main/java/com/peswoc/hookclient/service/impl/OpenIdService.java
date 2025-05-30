package com.peswoc.hookclient.service.impl;

import com.peswoc.hookclient.constant.ConnectionStatus;
import com.peswoc.hookclient.constant.State;
import com.peswoc.hookclient.dto.response.openid.connect.ConnectionDto;
import com.peswoc.hookclient.dto.response.openid.connect.ConnectionListResponseDto;
import com.peswoc.hookclient.model.openid.AcceptedConnection;
import com.peswoc.hookclient.model.openid.BaseConnection;
import com.peswoc.hookclient.model.openid.PendingConnection;
import com.peswoc.hookclient.repository.AcceptedConnectionRepository;
import com.peswoc.hookclient.repository.PendingConnectionRepository;
import com.peswoc.hookclient.service.IOpenIdService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OpenIdService implements IOpenIdService {

  private final AcceptedConnectionRepository acceptedConnectionRepo;
  private final PendingConnectionRepository pendingConnectionRepo;

  public OpenIdService(
    AcceptedConnectionRepository acceptedConnectionRepo,
    PendingConnectionRepository pendingConnectionRepo
  ) {
    this.acceptedConnectionRepo = acceptedConnectionRepo;
    this.pendingConnectionRepo = pendingConnectionRepo;
  }

  @Override
  public ConnectionDto savePendingConnection(PendingConnection connection) {
    var savedConnection = pendingConnectionRepo.save(connection);
    return new ConnectionDto(savedConnection);
  }

  @Override
  public boolean isConnectionExistAndPending(String id) {
    return pendingConnectionRepo.existsAndActiveById(id);
  }

  @Override
  public AcceptedConnection getAcceptedConnection(String id) {
    return acceptedConnectionRepo.getActiveById(id).orElse(null);
  }

  @Override
  public AcceptedConnection getConnectionByTargetId(String targetId) {
    return acceptedConnectionRepo.getActiveByTargetId(targetId).orElse(null);
  }

  @Override
  public void rejectConnection(String id) {
    pendingConnectionRepo.updateState(id, State.INACTIVE);
  }

  @Override
  public ConnectionDto acceptConnection(String id, String clientId, String clientSecret) {
    var pendingConnection = pendingConnectionRepo.findByIdAndState(id, State.ACTIVE)
      .orElseThrow(() -> new RuntimeException("Group not found"));

    var newConnection = new AcceptedConnection(pendingConnection);
    newConnection.setClientId(clientId);
    newConnection.setClientSecret(clientSecret);

    // Save new connection and delete pending connection
    var saved = acceptedConnectionRepo.save(newConnection);
    pendingConnectionRepo.deleteById(pendingConnection.getId());

    var res = new ConnectionDto(saved);
    res.setClientSecret(clientSecret);
    return res;
  }

  @Override
  public ConnectionListResponseDto getConnections(ConnectionStatus status) {
    if (status != null) {
      switch (status) {
        case PENDING -> {
          var connectionList = pendingConnectionRepo.findAllByState(State.ACTIVE);
          return new ConnectionListResponseDto(new ArrayList<>(connectionList));
        }
        case ACCEPTED -> {
          var connectionList = acceptedConnectionRepo.findAllByState(State.ACTIVE);
          return new ConnectionListResponseDto(new ArrayList<>(connectionList));
        }
        case REJECTED -> {
          var connectionList = pendingConnectionRepo.findAllByState(State.INACTIVE);
          return new ConnectionListResponseDto(new ArrayList<>(connectionList));
        }
      }
    }
    List<BaseConnection> connectionList = new ArrayList<>();
    connectionList.addAll(acceptedConnectionRepo.findAllByState(State.ACTIVE));
    connectionList.addAll(pendingConnectionRepo.findAllByState(State.ACTIVE));
    connectionList.addAll(pendingConnectionRepo.findAllByState(State.INACTIVE));
    return new ConnectionListResponseDto(connectionList);
  }
}
