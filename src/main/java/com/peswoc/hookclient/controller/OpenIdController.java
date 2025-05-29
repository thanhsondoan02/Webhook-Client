package com.peswoc.hookclient.controller;

import com.peswoc.hookclient.constant.ConnectionAction;
import com.peswoc.hookclient.constant.ConnectionStatus;
import com.peswoc.hookclient.constant.MessageConst;
import com.peswoc.hookclient.constant.SyncScope;
import com.peswoc.hookclient.dto.request.openid.auth.OpenIdLoginRequestDto;
import com.peswoc.hookclient.dto.request.openid.connect.ConnectRequestDto;
import com.peswoc.hookclient.dto.request.openid.connect.UpdateConnectionRequestDto;
import com.peswoc.hookclient.dto.request.openid.sync.SyncRequestDto;
import com.peswoc.hookclient.model.openid.PendingConnection;
import com.peswoc.hookclient.service.*;
import com.peswoc.hookclient.util.ResponseBuilder;
import com.peswoc.hookclient.util.ValidationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class OpenIdController {

  private final IOpenIdService openIdService;
  private final IApiService apiService;
  private final IUserService userService;
  private final IPostService postService;
  private final IGroupService groupService;

  public OpenIdController(
    IOpenIdService openIdService,
    IApiService apiService,
    IUserService userService,
    IPostService postService,
    IGroupService groupService) {
    this.openIdService = openIdService;
    this.apiService = apiService;
    this.userService = userService;
    this.postService = postService;
    this.groupService = groupService;
  }

  @PostMapping("/connections")
  public ResponseEntity<?> requestConnect(@RequestBody ConnectRequestDto request) {
    var name = request.getName();
    if (name == null || name.isBlank()
      || !ValidationUtils.isValidDomain(request.getDomain())
      || !ValidationUtils.isValidDomain(request.getTargetDomain())) {
      return ResponseBuilder.error(HttpStatus.BAD_REQUEST.value(), MessageConst.BAD_REQUEST);
    }

    // Set callback URL base on the id of the connection
    var id = UUID.randomUUID().toString().replace("-", "");
    request.setCallbackUrl(request.getDomain() + "/api/connections/" + id);

    // Call to server B to register new connection
    var response = apiService.registerConnection(request.getTargetDomain() + "/api/connections", request);
    if (!response.isSuccess()) {
      return ResponseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getMessage());
    }

    // Save the new connection to the database
    var newConnection = new PendingConnection();
    newConnection.setId(id);
    newConnection.setName(request.getName());
    newConnection.setDomain(request.getDomain());
    newConnection.setCallbackUrl(request.getCallbackUrl());
    newConnection.setTargetDomain(request.getTargetDomain());
    newConnection.setTargetId(response.getData().getId());

    var connectionDto = openIdService.savePendingConnection(newConnection);

    return ResponseBuilder.success(connectionDto);
  }

  @GetMapping("/connections")
  public ResponseEntity<?> getConnections(@RequestParam(required = false) String status) {
    ConnectionStatus connectionStatus;
    if (status != null) {
      try {
        connectionStatus = ConnectionStatus.fromString(status);
      } catch (IllegalArgumentException e) {
        return ResponseBuilder.error(HttpStatus.BAD_REQUEST.value(), MessageConst.BAD_REQUEST);
      }
    } else {
      connectionStatus = null;
    }

    return ResponseBuilder.success(openIdService.getConnections(connectionStatus));
  }

  @PostMapping("/connections/{id}")
  public ResponseEntity<?> updateConnection(@PathVariable("id") String id, @RequestBody UpdateConnectionRequestDto request) {
    ConnectionAction action;
    try {
      action = ConnectionAction.fromString(request.getAction());
    } catch (IllegalArgumentException e) {
      return ResponseBuilder.error(HttpStatus.BAD_REQUEST.value(), MessageConst.BAD_REQUEST);
    }

    if (action == ConnectionAction.ACCEPT && (request.getClientId() == null || request.getClientSecret() == null)) {
      return ResponseBuilder.error(HttpStatus.BAD_REQUEST.value(), MessageConst.BAD_REQUEST);
    }

    if (!openIdService.isConnectionExistAndPending(id)) {
      return ResponseBuilder.error(HttpStatus.NOT_FOUND.value(), MessageConst.CONNECTION_NOT_FOUND);
    }

    if (action == ConnectionAction.REJECT) {
      openIdService.rejectConnection(id);
      return ResponseBuilder.success();
    } else {
      return ResponseBuilder.success(openIdService.acceptConnection(id, request.getClientId(), request.getClientSecret()));
    }
  }

  @PostMapping("/sync")
  public ResponseEntity<?> syncData(@RequestBody SyncRequestDto request) {
    if (request.getConnectionId() == null || request.getScopes() == null || request.getScopes().isEmpty()) {
      return ResponseBuilder.error(HttpStatus.BAD_REQUEST.value(), MessageConst.MISSING_REQUIRED_FIELD);
    }

    List<SyncScope> scopes = new ArrayList<>();
    for (var v: request.getScopes()) {
      try {
        scopes.add(SyncScope.fromString(v));
      } catch (IllegalArgumentException e) {
        return ResponseBuilder.error(HttpStatus.BAD_REQUEST.value(), MessageConst.BAD_REQUEST);
      }
    }

    var connection = openIdService.getAcceptedConnection(request.getConnectionId());
    if (connection == null) {
      return ResponseBuilder.error(HttpStatus.NOT_FOUND.value(), MessageConst.CONNECTION_NOT_FOUND);
    }

    var domainB = connection.getTargetDomain();
    var accessToken = apiService.getAccessToken(
      domainB + "/api/auth/login-openid",
      new OpenIdLoginRequestDto(connection.getClientId(), connection.getClientSecret())
    ).getData().getToken();

    for (var scope : scopes) {
      switch (scope) {
        case USERS -> {
          var res = apiService.getUsers(domainB + "/api/users", accessToken);
          userService.syncUsers(res.getData());
        }
        case POSTS -> {
          var res = apiService.getPosts(domainB + "/api/posts", accessToken);
          postService.syncPosts(res.getData());
        }
        case GROUPS -> {
          var res = apiService.getGroups(domainB + "/api/groups", accessToken);
          groupService.syncGroups(res.getData());
        }
      }
    }
    return ResponseBuilder.success();
  }
}
