package com.peswoc.hookclient.controller;

import com.peswoc.hookclient.constant.MessageConst;
import com.peswoc.hookclient.dto.request.openid.connect.ConnectRequestDto;
import com.peswoc.hookclient.service.IAuthService;
import com.peswoc.hookclient.service.IOpenIdService;
import com.peswoc.hookclient.util.ResponseBuilder;
import com.peswoc.hookclient.util.ValidationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/openid")
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
    var domain = request.getDomain();
    var callbackUrl = request.getCallbackUrl();

    if (name == null || name.isBlank()
      || !ValidationUtils.isValidDomain(domain)
      || !ValidationUtils.isValidCallbackUrl(domain, callbackUrl)) {
      return ResponseBuilder.error(HttpStatus.BAD_REQUEST.value(), MessageConst.BAD_REQUEST);
    }

    return ResponseBuilder.success(openIdService.createConnect(request));
  }

//  @PostMapping("/auth")
//  public ResponseEntity<?> getAccessToken(@RequestBody RegisterRequestDto request) {
//
//  }
}
