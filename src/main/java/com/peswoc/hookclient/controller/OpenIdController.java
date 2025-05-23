package com.peswoc.hookclient.controller;

import com.peswoc.hookclient.constant.ConnectionStatus;
import com.peswoc.hookclient.constant.MessageConst;
import com.peswoc.hookclient.dto.request.openid.connect.ConnectRequestDto;
import com.peswoc.hookclient.service.IAuthService;
import com.peswoc.hookclient.service.IOpenIdService;
import com.peswoc.hookclient.util.ResponseBuilder;
import com.peswoc.hookclient.util.ValidationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OpenIdController {

  IAuthService authService;
  IOpenIdService openIdService;

  public OpenIdController(IAuthService authService, IOpenIdService openIdService) {
    this.authService = authService;
    this.openIdService = openIdService;
  }

  @PostMapping("/connections")
  public ResponseEntity<?> requestConnect(@RequestBody ConnectRequestDto request) {
    var name = request.getName();
    if (name == null || name.isBlank()
      || !ValidationUtils.isValidDomain(request.getDomain())
      || !ValidationUtils.isValidDomain(request.getTargetDomain())) {
      return ResponseBuilder.error(HttpStatus.BAD_REQUEST.value(), MessageConst.BAD_REQUEST);
    }

    var connectionDto = openIdService.addPendingConnections(request);
    // Call to server B

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

//  @PostMapping("/connections/{id}")
//  public ResponseEntity<?> updateConnection(@PathVariable("id") String id, @RequestBody UpdateConnectionRequestDto request) {
//    if (!authService.isAdmin()) {
//      return ResponseBuilder.error(HttpStatus.FORBIDDEN.value(), MessageConst.ACCESS_DENIED);
//    }
//
//    ConnectionAction action;
//    try {
//      action = ConnectionAction.fromString(request.getAction());
//    } catch (IllegalArgumentException e) {
//      return ResponseBuilder.error(HttpStatus.BAD_REQUEST.value(), MessageConst.BAD_REQUEST);
//    }
//
//    if (!openIdService.isConnectionExistAndPending(id)) {
//      return ResponseBuilder.error(HttpStatus.NOT_FOUND.value(), MessageConst.CONNECTION_NOT_FOUND);
//    }
//
//    // call to server A
//
//    return ResponseBuilder.success(openIdService.updateConnection(id, action));
//  }
}
