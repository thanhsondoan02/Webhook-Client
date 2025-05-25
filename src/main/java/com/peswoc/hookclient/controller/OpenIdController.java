package com.peswoc.hookclient.controller;

import com.peswoc.hookclient.constant.ConnectionAction;
import com.peswoc.hookclient.constant.ConnectionStatus;
import com.peswoc.hookclient.constant.MessageConst;
import com.peswoc.hookclient.dto.request.openid.connect.ConnectRequestDto;
import com.peswoc.hookclient.dto.request.openid.connect.UpdateConnectionRequestDto;
import com.peswoc.hookclient.model.openid.PendingConnection;
import com.peswoc.hookclient.service.IApiService;
import com.peswoc.hookclient.service.IAuthService;
import com.peswoc.hookclient.service.IOpenIdService;
import com.peswoc.hookclient.util.ResponseBuilder;
import com.peswoc.hookclient.util.ValidationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class OpenIdController {

  private final IAuthService authService;
  private final IOpenIdService openIdService;
  private final IApiService apiService;

  public OpenIdController(IAuthService authService, IOpenIdService openIdService, IApiService apiService) {
    this.authService = authService;
    this.openIdService = openIdService;
    this.apiService = apiService;
  }

  @PostMapping("/connections")
  public ResponseEntity<?> requestConnect(@RequestBody ConnectRequestDto request) {
    var name = request.getName();
    if (name == null || name.isBlank()
      || !ValidationUtils.isValidDomain(request.getDomain())
      || !ValidationUtils.isValidDomain(request.getTargetDomain())) {
      return ResponseBuilder.error(HttpStatus.BAD_REQUEST.value(), MessageConst.BAD_REQUEST);
    }

    apiService.setServerUrl(request.getTargetDomain());

    // Set callback URL base on the id of the connection
    var id = UUID.randomUUID().toString().replace("-", "");
    request.setCallbackUrl(request.getDomain() + "/api/connections/" + id);

    // Call to server B to register new connection
    var response = apiService.registerConnection(request);
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

    if (!openIdService.isConnectionExistAndPending(id)) {
      return ResponseBuilder.error(HttpStatus.NOT_FOUND.value(), MessageConst.CONNECTION_NOT_FOUND);
    }

    if (action == ConnectionAction.REJECT) {
      return ResponseBuilder.success(openIdService.acceptConnection(id, request.getClientId(), request.getClientSecret()));
    } else {
      openIdService.rejectConnection(id);
      return ResponseBuilder.success();
    }
  }
}
