package com.peswoc.hookclient.service;

import com.peswoc.hookclient.dto.request.openid.connect.ConnectRequestDto;
import com.peswoc.hookclient.dto.response.openid.connect.ServerInfoDto;

public interface IOpenIdService {
  ServerInfoDto createConnect(ConnectRequestDto request);
}
