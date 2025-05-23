package com.peswoc.hookclient.service.impl;

import com.peswoc.hookclient.constant.ConnectionAction;
import com.peswoc.hookclient.constant.ConnectionStatus;
import com.peswoc.hookclient.constant.State;
import com.peswoc.hookclient.dto.request.openid.connect.ConnectRequestDto;
import com.peswoc.hookclient.dto.response.openid.connect.ConnectionDto;
import com.peswoc.hookclient.dto.response.openid.connect.ConnectionListResponseDto;
import com.peswoc.hookclient.model.openid.AcceptedConnection;
import com.peswoc.hookclient.model.openid.BaseConnection;
import com.peswoc.hookclient.model.openid.PendingConnection;
import com.peswoc.hookclient.repository.AcceptedConnectionRepository;
import com.peswoc.hookclient.repository.PendingConnectionRepository;
import com.peswoc.hookclient.security.JwtUtils;
import com.peswoc.hookclient.service.IOpenIdService;
import com.peswoc.hookclient.util.PasswordUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OpenIdService implements IOpenIdService {

  private final AcceptedConnectionRepository acceptedConnectionRepo;
  private final PendingConnectionRepository pendingConnectionRepo;
  private final AuthenticationManager authenticationManager;
  private final JwtUtils jwtUtils;
  private final PasswordEncoder passwordEncoder;

  public OpenIdService(
    AcceptedConnectionRepository acceptedConnectionRepo,
    PendingConnectionRepository pendingConnectionRepo,
    AuthenticationManager authenticationManager,
    JwtUtils jwtUtils,
    PasswordEncoder passwordEncoder
  ) {
    this.acceptedConnectionRepo = acceptedConnectionRepo;
    this.pendingConnectionRepo = pendingConnectionRepo;
    this.authenticationManager = authenticationManager;
    this.jwtUtils = jwtUtils;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public ConnectionDto addPendingConnections(ConnectRequestDto request) {
    var newConnection = new PendingConnection();
    newConnection.setName(request.getName());
    newConnection.setDomain(request.getDomain());
    newConnection.setCallbackUrl(request.getDomain() + "/api/connections/" + newConnection.getId());
    newConnection.setTargetDomain(request.getTargetDomain());

    var savedConnection = pendingConnectionRepo.save(newConnection);
    return new ConnectionDto(savedConnection);
  }

  @Override
  public boolean isConnectionExistAndPending(String id) {
    return pendingConnectionRepo.existsAndActiveById(id);
  }

  @Override
  public ConnectionDto updateConnection(String id, ConnectionAction action) {
    switch (action) {
      case ACCEPT -> {
        var pendingConnection = pendingConnectionRepo.findByIdAndState(id, State.ACTIVE)
          .orElseThrow(() -> new RuntimeException("Group not found"));

        var newConnection = new AcceptedConnection(pendingConnection);

        //  Generate clientId and clientSecret
        var clientId = UUID.randomUUID().toString().replace("-", "");
        newConnection.setClientId(clientId);

        var clientSecret = PasswordUtils.generateSecurePassword();
        newConnection.setClientSecret(passwordEncoder.encode(clientSecret));

        // Save new connection and delete pending connection
        var saved = acceptedConnectionRepo.save(newConnection);
        pendingConnectionRepo.deleteById(pendingConnection.getId());

        var res = new ConnectionDto(saved);
        res.setClientSecret(clientSecret);
        return res;
      }
      case REJECT -> {
        pendingConnectionRepo.updateState(id, State.INACTIVE);
        return null;
      }
      default -> throw new IllegalArgumentException("Invalid action: " + action);
    }
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
