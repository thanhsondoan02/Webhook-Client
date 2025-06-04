package com.peswoc.hookclient.service.impl;

import com.peswoc.hookclient.constant.ConnectionStatus;
import com.peswoc.hookclient.dto.response.openid.connect.ConnectionDto;
import com.peswoc.hookclient.dto.response.openid.connect.ConnectionListResponseDto;
import com.peswoc.hookclient.model.openid.Connection;
import com.peswoc.hookclient.model.openid.Server;
import com.peswoc.hookclient.repository.ConnectionRepository;
import com.peswoc.hookclient.repository.ServerRepository;
import com.peswoc.hookclient.service.IOpenIdService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class OpenIdService implements IOpenIdService {

  private final ConnectionRepository connectionRepository;
  private final ServerRepository serverRepository;

  public OpenIdService(ConnectionRepository connectionRepository, ServerRepository serverRepository) {
    this.connectionRepository = connectionRepository;
    this.serverRepository = serverRepository;
  }

  @Override
  public String getOwnerDomain() {
    return serverRepository
      .findOwnerServer()
      .orElseThrow(() -> new RuntimeException("Owner server not found"))
      .getDomain();
  }

  @Override
  public ConnectionDto addPendingConnection(Server server, Connection connection) {
    var savedServer = serverRepository
      .findByDomain(server.getDomain())
      .orElseGet(() -> serverRepository.save(server));

    connection.setTargetServer(savedServer);
    return new ConnectionDto(connectionRepository.save(connection));
  }

  @Override
  public boolean isConnectionExistAndPending(String id) {
    var optional = connectionRepository.findActiveByIdAndStatus(id, ConnectionStatus.PENDING);
    return optional.isPresent();
  }

  @Override
  public Connection getAcceptedConnection(String id) {
    return connectionRepository.findActiveByIdAndStatus(id, ConnectionStatus.ACCEPTED).orElse(null);
  }

  @Override
  public void rejectConnection(String id) {
    var connection = connectionRepository.findActiveByIdAndStatus(id, ConnectionStatus.PENDING)
      .orElseThrow(() -> new RuntimeException("Connection not found or already processed"));
    connection.setStatus(ConnectionStatus.REJECTED);
    connectionRepository.save(connection);
  }

  @Override
  public ConnectionDto acceptConnection(String id, String clientId, String clientSecret) {
    var connection = connectionRepository.findActiveByIdAndStatus(id, ConnectionStatus.PENDING)
      .orElseThrow(() -> new RuntimeException("Group not found"));

    connection.setClientId(clientId);
    connection.setClientSecret(clientSecret);
    connection.setStatus(ConnectionStatus.ACCEPTED);

    var saved = connectionRepository.save(connection);

    return new ConnectionDto(saved);
  }

  @Override
  public ConnectionListResponseDto getFilteredConnections(ConnectionStatus status) {
    return new ConnectionListResponseDto(connectionRepository.findActiveByStatus(status));
  }

  @Override
  public ConnectionListResponseDto getAllConnections() {
    var connectionList = new ArrayList<Connection>();
    connectionList.addAll(connectionRepository.findActiveByStatus(ConnectionStatus.PENDING));
    connectionList.addAll(connectionRepository.findActiveByStatus(ConnectionStatus.ACCEPTED));
    connectionList.addAll(connectionRepository.findActiveByStatus(ConnectionStatus.REJECTED));
    return new ConnectionListResponseDto(connectionList);
  }
}
